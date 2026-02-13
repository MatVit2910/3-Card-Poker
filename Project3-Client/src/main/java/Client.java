import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

public class Client extends Thread{

    Socket socketClient;

    ObjectOutputStream out;
    ObjectInputStream in;

    private Consumer<Serializable> callback;

    String ipAddress;
    int port;

    // constructor, socket and callback set up
    Client(Consumer<Serializable> call, String ipAddress, int port) throws IOException {
        callback = call;
        this.ipAddress = ipAddress;
        this.port = port;

        socketClient= new Socket(ipAddress,port);
        out = new ObjectOutputStream(socketClient.getOutputStream());
        in = new ObjectInputStream(socketClient.getInputStream());
        socketClient.setTcpNoDelay(true);
    }

    // reading info
    public void run(){

        while(true) {
            try {
                PokerInfo initialData = (PokerInfo) in.readObject();
                callback.accept(initialData);
            }
            catch(Exception e) {
                Platform.exit();
            }
        }

    }

    // sending info
    public void send(PokerInfo data) {

        try {
            out.writeObject(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // updates callback
    public void setCallback(Consumer<Serializable> callback) {
        this.callback = callback;
    }


}