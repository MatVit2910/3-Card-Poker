import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

//server class for 3 card poker server
public class Server{

    //data members
    int count = 1;
    public final ArrayList<ClientThread> clients = new ArrayList<>();
    TheServer server;
    private final Consumer<Serializable> logCallback;
    private final Consumer<Serializable> clientCallback;

    //constructor
    Server(Consumer<Serializable> call1, Consumer<Serializable> call2){

        logCallback = call1;
        logCallback.accept("Server started");
        clientCallback = call2;
        server = new TheServer();
        server.start();
    }

    public void stop() throws IOException {
        synchronized (clients) {
            for (ClientThread client : clients) {
                try {
                    client.connection.close();
                } catch (IOException e) {
                }
            }
            clients.clear();
        }
        server.socketRef.close();
    }

    //server socket thread
    public class TheServer extends Thread{
        ServerSocket socketRef;
        public void run() {

            try(ServerSocket mysocket = new ServerSocket(IntroController.getPortNum())){
                System.out.println("Server is waiting for a client!");
                socketRef = mysocket;

                while(clients.size() < 8) {

                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    synchronized (clients) {
                        clients.add(c);
                    }
                    c.start();
                    logCallback.accept("Client has connected to server: " + "Client #" + count);
                    count++;
                }
            }//end of try
            catch(Exception e) {
                logCallback.accept("Server stopped.");
            }
        }//end of while
    }


    //client socket thread
    class ClientThread extends Thread{


        Socket connection;
        int count;
        ObjectInputStream in;
        ObjectOutputStream out;

        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
        }

        public void run(){

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            while(true) {
                try {
                    //wait for client to start game
                    PokerInfo readInitialData = (PokerInfo) in.readObject();
                    if (readInitialData.getStartNewGame()) {
                        clientCallback.accept("Client #" +count+ " is playing a new hand.");
                        //create and start new game
                        Game game = new Game();
                        game.startGame();

                        //send client initial game data
                        PokerInfo initialData = new PokerInfo();
                        initialData.setDealerHand(game.getDealerHand());
                        initialData.setPlayerHand(game.getPlayerHand());
                        initialData.setDealerStr(game.evaluateHand(game.getDealerHand()));
                        initialData.setPlayerStr(game.evaluateHand(game.getPlayerHand()));
                        initialData.setDealerQualifies(game.dealerQualifies());
                        out.writeObject(initialData);

                        PokerInfo readData = (PokerInfo) in.readObject();
                        if (readData.getFreshStart()){
                            continue;
                        }
                        clientCallback.accept("Client #" + count + " bet: $" + readData.getAnteBet() + " for Ante Bet and $"
                                + readData.getPairPlus() + " for Pair Plus"
                        );

                        //read data from the client to get bets
                        PokerInfo responseData = new PokerInfo();
                        responseData.setDealerHand(game.getDealerHand());
                        responseData.setPlayerHand(game.getPlayerHand());
                        responseData. setDealerStr(game.evaluateHand(game.getDealerHand()));
                        responseData.setPlayerStr(game.evaluateHand(game.getPlayerHand()));

                        //this is for logging purposes
                        if (readData.getFold()){
                            clientCallback.accept("Client #" + count + " folded and lost $" + readData.getAnteBet());
                            responseData.setWinningsAmt(0);
                        }
                        else{
                            responseData.setWinningsAmt(game.compareHands(game.getDealerHand(),
                                    game.getPlayerHand(), readData.getAnteBet(), readData.getPlayBet()));
                            responseData.setDealerQualifies(game.dealerQualifies());
                        }
                        responseData.setPairPlusAmt(game.calculatePairPlus(game.getPlayerHand(), readData.getPairPlus()));

                        if (!readData.getFold()){
                            clientCallback.accept("Client #" + count +"'s Hand: " + responseData.getPlayerStr() + " vs. Dealer's Hand: " + responseData.getDealerStr()
                            );
                            if (responseData.getWinningsAmt() == 0){
                                clientCallback.accept("Client #" + count + " lost $" + (readData.getAnteBet() + readData.getPlayBet()));
                            }
                            else if(!game.dealerQualifies()){
                                clientCallback.accept("Client #" + count + " got their money back. Dealer didn't qualify");
                            }
                            else if (responseData.getWinningsAmt() > 0){
                                clientCallback.accept("Client #" + count + " won $" + (readData.getAnteBet() + readData.getPlayBet()));
                            }
                            else{
                                clientCallback.accept("Client #" + count + " and Dealer's hands are the same??!! They got their money back");
                            }
                        }
                        if (readData.getPairPlus() != 0){
                            clientCallback.accept("Client #" + count + " won $" + responseData.getPairPlusAmt() + " for Pair Plus");
                        }
                        responseData.setPlayBet(readData.getPlayBet());
                        responseData.setPairPlus(readData.getPairPlus());
                        responseData.setAnteBet(readData.getAnteBet());

                        //send results of game to client
                        out.writeObject(responseData);
                    }

                }
                catch(Exception e) {
                    synchronized (clients) {
                        clients.remove(this);
                    }
                    logCallback.accept("Client #" + count + " has left the server.");

                    break;
                }
            }
        }//end of run


    }//end of client thread
}
