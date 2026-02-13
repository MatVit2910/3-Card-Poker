import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//a controller for the clients screen
public class ClientsController implements Initializable {

    //data members
    @FXML
    private Label clientsLabel;
    public static Label clientsLabelRef;
    @FXML
    private GridPane clientsGrid;
    public static GridPane clientsGridRef;
    @FXML
    private ListView<String> clientLog;
    public static ListView<String> clientLogRef;
    private ScreenChanger manager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientsLabelRef = clientsLabel;
        clientsGridRef = clientsGrid;
        clientLogRef = clientLog;
    }

    //sets screen change manager of this controller
    public void setScreenChanger(ScreenChanger manager) {
        this.manager = manager;
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
}