import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

//controller class for log screen
public class LogController implements Initializable {

    //data members
    @FXML
    private ListView<String> log;
    public static ListView<String> logRef;
    private ScreenChanger manager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logRef = log;
    }

    //sets screen changer manger for this controller
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