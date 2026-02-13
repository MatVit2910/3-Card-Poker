import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//controller for the intro screen
public class IntroController implements Initializable {

    //data members
    @FXML
    private TextField portInput;
    @FXML
    private Button toggleButton;
    private ScreenChanger manager;
    private boolean isServerRunning = false;
    private static Integer portNum;
    public static Server serverConnection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //sets screen change manager of this controller
    public void setScreenChanger(ScreenChanger manager) {
        this.manager = manager;
    }

    public void handleToggleButton() throws IOException {
        //into screen state when server is stopped
        if (isServerRunning) {
            isServerRunning = false;
            toggleButton.setText("START");
            toggleButton.getStyleClass().remove("stop-button");
            toggleButton.getStyleClass().add("start-button");
            portInput.setDisable(false);
            portInput.clear();
            serverConnection.stop();

        }
        //intro screen when server is running
        else {
            //check for valid port
            String portStr = portInput.getText();
            if (portStr == null || portStr.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing port number");
                alert.setHeaderText("Please add a port number (e.g. 88888)");
                alert.setContentText("Port number is missing, please add a port number");
                alert.showAndWait();
            }
            else{
                try{
                    //change screen and create server
                    portNum = Integer.parseInt(portStr);
                    if (portNum < 1){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Port must be a valid number");
                        alert.setHeaderText("Please add a valid port number (e.g. 88888)");
                        alert.setContentText("Port number is invalid, please add a port number greater than 0");
                        alert.showAndWait();
                        return;
                    }
                    isServerRunning = true;
                    toggleButton.setText("STOP");
                    toggleButton.getStyleClass().remove("start-button");
                    toggleButton.getStyleClass().add("stop-button");
                    portInput.setDisable(true);
                    portInput.setText("Running on port: " + portStr);
                    serverConnection = new Server(data -> Platform.runLater(() -> {
                        LogController.logRef.getItems().add(data.toString());
                        LogController.logRef.scrollTo(LogController.logRef.getItems().size() - 1);
                        ClientsController.clientsLabelRef.setText("Clients: " + serverConnection.clients.size());
                        updateClientsGrid(serverConnection.clients);

                    }),data -> Platform.runLater(() -> {
                        ClientsController.clientLogRef.getItems().add(data.toString());
                        ClientsController.clientLogRef.scrollTo(ClientsController.clientLogRef.getItems().size() - 1);
                    })
                    );
                    System.out.println("SERVER: Server started on port " + portStr);
                }
                //popup for when port is not a num
                catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Port must be a valid number");
                    alert.setHeaderText("Please add a valid port number (e.g. 88888)");
                    alert.setContentText("Port number is invalid, please add a valid port number");
                    alert.showAndWait();
                }
            }
        }
    }


    //handle nav buttons
    public void handleControl(ActionEvent e) throws IOException {
        manager.controlScreen();
    }
    public void handleClients(ActionEvent e) throws IOException {
        manager.clientsScreen();
    }
    public void handleLog() {
        manager.logScreen();
    }

    //getters
    public static Integer getPortNum() {
        return portNum;
    }

    //function to update the clients grid
    private void updateClientsGrid(ArrayList<Server.ClientThread> activeClients) {
        ClientsController.clientsGridRef.getChildren().clear();
        int index = 0;
        for (Server.ClientThread client : activeClients) {
            int row = index /4;
            int col = index % 4;

            //the grids have to be created dynamically, that's why I am using setStyle :c
            Label clientLabel = new Label("Client #" + client.count);
            clientLabel.setStyle("-fx-background-color: white; -fx-padding: 10; -fx-pref-width: 100; -fx-alignment: CENTER; -fx-background-radius: 30; -fx-font-weight: bold; -fx-border-radius: 30");
            ClientsController.clientsGridRef.add(clientLabel, col, row);
            index++;
        }
    }
}