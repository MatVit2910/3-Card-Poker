import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    // FXML Components
    @FXML private TextField anteBetField;
    @FXML private TextField pairPlusField;
    @FXML private Button dealBttn;
    @FXML private Button startBttn;
    @FXML private Button foldBttn;
    @FXML private Button summaryBttn;
    @FXML private ImageView userCard1;
    @FXML private ImageView userCard2;
    @FXML private ImageView userCard3;
    @FXML private ImageView dealerCard1;
    @FXML private ImageView dealerCard2;
    @FXML private ImageView dealerCard3;
    @FXML private Label titleSummary;
    @FXML private Label betsSummary;
    @FXML private Label pairPlusSummary;
    @FXML private Label totalSummary;
    @FXML private Label totalWinningsSummary;
    @FXML private Label totalWinningsGame;
    @FXML private ListView<String> playerLog;
    @FXML private TextField IPAddressInput;
    @FXML private TextField portInput;


    // data members
    private ScreenChanger manager;
    public static Client myClient;
    private final String backOfCard = "/cards/back_of_card.png";
    private Boolean isNewLook = false;
    private ArrayList<ImageView> userCards;
    private ArrayList<ImageView> dealerCards;

    // initilize player and dealer's hands
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.userCards = new ArrayList<>();
        this.userCards.add(userCard1); this.userCards.add(userCard2); this.userCards.add(userCard3);

        this.dealerCards = new ArrayList<>();
        this.dealerCards.add(dealerCard1); this.dealerCards.add(dealerCard2); this.dealerCards.add(dealerCard3);
    }

    // updates the current screen changer
    public void setManager(ScreenChanger manager) {
        this.manager = manager;
    }


    // from Main Menu Scree, tries to connect with the server
    public void handleTryConnection(){

        String ipAddress = IPAddressInput.getText().trim();
        String portString = portInput.getText().trim();
        int port;


        // checks for valid inputs
        if (portString.isEmpty() || ipAddress.isEmpty()) {
            showCustomAlert("Incorrect Input","Incorrect IP/Port Number","Please provide both an IP Address and a Port Number", 550,340);
            return;
        }

        //ipaddress has to be localhost, so we force it :)
        if (!ipAddress.equals("127.0.0.1") && !ipAddress.equals("localhost")) {
            showCustomAlert("Incorrect IP Address","Incorrect IP Address","you must use '127.0.0.1' or 'localhost' to connect our Server", 550,340);
            return;
        }

        // not negative
        try {
            port = Integer.parseInt(portString);
            if (port < 1) {
                showCustomAlert("Invalid Port", "Your Port in Negative", "Port number must be greater than 0",550,340);
                return;
            }
        } catch (NumberFormatException e) {
            showCustomAlert("Invalid Port","Your Port is not an Integer", "Port number must be a valid integer", 550,340);
            return;
        }

        System.out.println("Trying to connect to server..."); // debugging

        // creates and starts client thread
        try {
            Controller.myClient = new Client(data -> Platform.runLater(() -> {
                MainClient.globalPokerInfo = (PokerInfo) data;
            }), ipAddress, port);
            myClient.start();
            System.out.println("Connection to server established..."); // debugging
            manager.gameScreen();
        }
        catch (Exception e){

            // alert in case something went wrong
            showCustomAlert(
                    "Connection Failed",
                    "Could not connect to the server",
                    "Please check the IP address and port number\n and make sure the server is running\nError: " + e.getMessage(),
                    600,
                    450
            );

        }
    }

    // exits the app
    public void handleExit(){
        Platform.exit();
    }

    // from game screen, sends bets and the server and receives calculations
    public void handleDealButton(){

        // prepares info to send to the server
        PokerInfo currentPokerInfo = new PokerInfo();
        int anteBet = Integer.parseInt(anteBetField.getText());
        int pairPlus;
        if (pairPlusField.getText().isEmpty()){
            pairPlus = 0;
        }
        else{
            pairPlus = Integer.parseInt(pairPlusField.getText());
        }

        currentPokerInfo.setAnteBet(anteBet);
        currentPokerInfo.setPlayBet(anteBet);
        currentPokerInfo.setFold(false);
        currentPokerInfo.setPairPlus(pairPlus);

        // changes the callback for the next time we receive information from the server
        myClient.setCallback(data -> Platform.runLater(() -> {
            MainClient.globalPokerInfo = (PokerInfo) data; // info received

            // reveals the dealer's hand to the user and updates log
            ArrayList<Card> dealerHand = MainClient.globalPokerInfo.getDealerHand();
            for (int i = 0; i < 3; i++ ) {
                updateCardImage(dealerHand.get(i).getUrl(), dealerCards.get(i));
            }
            playerLog.getItems().add("Dealer's hand: " + MainClient.globalPokerInfo.getDealerStr());
            foldBttn.setVisible(false);

            // if dealer does not qualify, push the bet
            if (!MainClient.globalPokerInfo.getDealerQualifies()) {
                playerLog.getItems().add("Dealer does not qualify");
                showCustomAlert("Not Qualify", "The dealer does not qualify!", "Your ante and play bet will be pushed to the next round!", 600, 300);

                summaryBttn.setVisible(false);
                for (int i = 0; i < 3; i++) {
                    updateCardImage(backOfCard, dealerCards.get(i));
                }
                handleStartButton();
                return;
            }

            startBttn.setDisable(true);
            startBttn.setVisible(false);
            foldBttn.setDisable(true);
            foldBttn.setVisible(false);
            dealBttn.setVisible(false);
            dealBttn.setDisable(true);
            summaryBttn.setVisible(true);
            summaryBttn.setDisable(false);
        }));
        myClient.send(currentPokerInfo); // send updated info
    }


    // updates the final results and the player's winnings
    public void handleSummaryButton(){
        manager.endGameScreen();
        int total = 0;

        // checks wins/losses
        if(MainClient.globalPokerInfo.getWinningsAmt() > 0){
            playerLog.getItems().add("Player beats dealer");
            titleSummary.setText("YOU WIN!");

            total = MainClient.globalPokerInfo.getWinningsAmt() + MainClient.globalPokerInfo.getPairPlus();
        }
        else{
            playerLog.getItems().add("Player loses to dealer");
            titleSummary.setText("YOU LOSE!");

            total = MainClient.globalPokerInfo.getPlayBet() + MainClient.globalPokerInfo.getPlayBet() + MainClient.globalPokerInfo.getPairPlus() ;
            total = total * -1; // losses money, so negative
        }

        // updates labels and log
        String bets = String.valueOf(MainClient.globalPokerInfo.getAnteBet());
        String pairBet = String.valueOf(MainClient.globalPokerInfo.getPairPlus());

        if (MainClient.globalPokerInfo.getPairPlusAmt() > 0){
            playerLog.getItems().add("Player wins Pair Plus");
        }

        betsSummary.setText("ANTE / PLAY......$" + bets + " & $" + bets );
        pairPlusSummary.setText("PAIR PLUS......$" + pairBet );
        totalSummary.setText("TOTAL......$" + total );

        MainClient.globalWinnings += total;
        totalWinningsSummary.setText("Total Winnings: $" + MainClient.globalWinnings);
        totalWinningsGame.setText("Total Winnings: $" + MainClient.globalWinnings);

    }

    // sends user's bets to the server and updates log
    public void handleStartButton(){
        playerLog.getItems().add("Player started a new round");

        PokerInfo currentPokerInfo = new PokerInfo();

        // validation and bet setting
        String anteBetText = anteBetField.getText().trim();
        String pairPlusText = pairPlusField.getText().trim();

        if(anteBetText.isEmpty() || !isValidInt(anteBetText)){
            noValidBetPopUp();
            return;
        }

        int anteBetValue = Integer.parseInt(anteBetText);
        if (anteBetValue < 5 || anteBetValue > 25) {
            noValidBetPopUp();
            return;
        }

        if(!pairPlusText.isEmpty() && !isValidInt(pairPlusText)){
            noValidBetPopUp();
            return;
        }

        if (!pairPlusText.isEmpty()){
            int pairPlusValue = Integer.parseInt(pairPlusText);
            if (pairPlusValue < 5 || pairPlusValue > 25) {
                noValidBetPopUp();
                return;
            }
        }

        // updates signal to start a new game
        currentPokerInfo.setStartNewGame(true);

        // updates callback to show player's hand and updates log
        myClient.setCallback(data -> Platform.runLater(() -> {
            MainClient.globalPokerInfo = (PokerInfo) data;
            ArrayList<Card> playerHand = MainClient.globalPokerInfo.getPlayerHand();

            for (int i = 0; i < 3; i++ ) {
                updateCardImage(playerHand.get(i).getUrl(), userCards.get(i));
            }
            playerLog.getItems().add("Player's hand: " + MainClient.globalPokerInfo.getPlayerStr());
        }));
        myClient.send(currentPokerInfo); // sends info

        startBttn.setVisible(false);
        startBttn.setDisable(true);

        foldBttn.setVisible(true);
        foldBttn.setDisable(false);

        dealBttn.setVisible(true);
        dealBttn.setDisable(false);

        anteBetField.setDisable(true);
        pairPlusField.setDisable(true);
    }

    //function to handle fold
    public void handleFoldButton(){
        //add log
        playerLog.getItems().add("Player folded");

        //set data to send to server
        PokerInfo currentPokerInfo = new PokerInfo();
        int anteBet = Integer.parseInt(anteBetField.getText());
        int pairPlus;
        if (pairPlusField.getText().isEmpty()){
            pairPlus = 0;
        }
        else{
            pairPlus = Integer.parseInt(pairPlusField.getText());
        }
        currentPokerInfo.setAnteBet(anteBet);
        currentPokerInfo.setPlayBet(anteBet);
        currentPokerInfo.setFold(true);
        currentPokerInfo.setPairPlus(pairPlus);

        summaryBttn.setDisable(false);

        //change the callback to show dealer cards instead
        myClient.setCallback(data -> Platform.runLater(() -> {
            MainClient.globalPokerInfo = (PokerInfo) data;
            ArrayList<Card> dealerHand = MainClient.globalPokerInfo.getDealerHand();

            for (int i = 0; i < 3; i++ ) {
                updateCardImage(dealerHand.get(i).getUrl(), dealerCards.get(i));
            }
            foldBttn.setVisible(false);
            startBttn.setVisible(false);
            dealBttn.setVisible(false);
            summaryBttn.setVisible(true);
        }));

        //send data to server
        myClient.send(currentPokerInfo);
    }

    //function to handle play again
    public void handlePlayAgain(){

        //go to game screen and reset game to intial state
        manager.gameScreen();

        anteBetField.clear();
        pairPlusField.clear();
        anteBetField.setDisable(false);
        pairPlusField.setDisable(false);

        foldBttn.setVisible(false);
        foldBttn.setDisable(true);

        summaryBttn.setVisible(false);
        summaryBttn.setDisable(true);

        startBttn.setVisible(true);
        startBttn.setDisable(false);

        //flip cards
        for(int i = 0; i < 3; i++ ){
            updateCardImage(backOfCard, userCards.get(i));
            updateCardImage(backOfCard, dealerCards.get(i));
        }
    }


    //popup for invalid bets
    private void noValidBetPopUp(){
        showCustomAlert("Invalid Bet", "NOT A VALID BET", "Please enter a valid bet before starting",500,300);
    }

    //function to check if an int is valid
    private boolean isValidInt(String text) {
        try {
            Integer.parseInt(text.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //function to update the image of card
    public void updateCardImage(String newURL, ImageView cardToUpdate) {
        Image newImage = new Image(getClass().getResourceAsStream(newURL));
        cardToUpdate.setImage(newImage);
    }

    //function to show the different hands for the game
    public void handleHandsOption(){

        String content =
            "1 - STRAIGHT FLUSH: 10 9 8\n" +
            "2 - THREE OF A KIND: 10 9 8\n" +
            "3 - STRAIGHT: 8 7 6\n" +
            "4 - FLUSH: K 9 8\n" +
            "5 - PAIR: K K 9\n";

        showCustomAlert("HANDS", "POSSIBLE HANDS", content, 500,360 );
    }

    //function to show rules for the game
    public void handleRulesOption(){

        String content =
                        "1. DEALER QUALIFICATION:\n" +
                        "   - The dealer must have at least a Queen high or better to qualify\n" +
                        "   - If the dealer does NOT qualify, the Play bet is returned, and the Ante bet is pushed to the next hand\n" +
                        "\n" +
                        "2. PAIR PLUS PAYOUTS (Regardless of dealer):\n" +
                        "   (Only if hand is a Pair of 2's or better)\n" +
                        "   - Straight Flush: 40 to 1\n" +
                        "   - Three of a Kind: 30 to 1\n" +
                        "   - Straight: 6 to 1\n" +
                        "   - Flush: 3 to 1\n" +
                        "   - Pair: 1 to 1\n" +
                        "\n" +
                        "3. ANTE/PLAY PAYOUTS (Dealer qualifies):\n" +
                        "   - If Player Wins: Payout is 1 to 1 (gets back double the beted amount)\n" +
                        "   - If Dealer Wins: Player loses both the Ante and Play bets\n" +
                        "   - If Player Folds: Player loses both the Ante and Pair Plus bets";


        showCustomAlert("RULES", "GAME RULES", content, 790,660 );
    }

    //function to make popups easier
    //it creates a custom alert based on parameters
    private void showCustomAlert(String title, String header,
                                 String content, double width, double height) {

        //set data for alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();

        //set the stylesheet
        String cssPath = getClass().getResource("/styles/alerts.css").toExternalForm();
        dialogPane.getStylesheets().add(cssPath);
        dialogPane.getStyleClass().add("my-custom-alert");

        //set size
        if (width > 0) {
            dialogPane.setPrefWidth(width);
        }
        if (height > 0) {
            dialogPane.setPrefHeight(height);
        }

        alert.showAndWait();
    }

    //function to apply a different stylesheet to the game
    public void handleNewLookOption() {
        this.isNewLook = !this.isNewLook;
        String newStyleSheet;
        if (this.isNewLook) {
            newStyleSheet = "/styles/gameStylesALT.css";
            System.out.println("Switching to New Look (stylesALT.css)");
        } else {
            newStyleSheet = "/styles/gameStyles.css";
            System.out.println("Switching to Standard Look (styles.css)");
        }
        applyStylesheet(newStyleSheet);

    }

    //function to apply a stylesheet to a screen
    private void applyStylesheet(String newStyleSheetPath) {
        BorderPane root = manager.getRootGameScreen();
        if (root != null) {
            root.getStylesheets().clear();
            root.getStylesheets().add(newStyleSheetPath);
        }
    }

    //function to handle fresh start
    public void handleFreshStartOption(){
        totalWinningsGame.setText("Total Winnings: $0");
        playerLog.getItems().clear();
        MainClient.globalWinnings = 0;
        PokerInfo refreshSignal = new PokerInfo();
        refreshSignal.setFreshStart(true);
        myClient.send(refreshSignal);
        handlePlayAgain();
    }

}
