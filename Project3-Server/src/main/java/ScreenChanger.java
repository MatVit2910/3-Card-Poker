import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.io.IOException;

//a class that saves and manages all screens
public class ScreenChanger {

    //data members
    private final BorderPane root = new BorderPane();
    private final BorderPane controlScreen;
    private final BorderPane clientsScreen;
    private final BorderPane logScreen;

    //constructor
    public ScreenChanger() throws IOException {
        // load all screens (controls, clients, log)
        FXMLLoader f1 = new FXMLLoader(getClass().getResource("/FXML/Control Screen.fxml"));
        controlScreen = f1.load();
        IntroController c1 = f1.getController();
        c1.setScreenChanger(this);

        FXMLLoader f2 = new FXMLLoader(getClass().getResource("/FXML/Clients Screen.fxml"));
        clientsScreen = f2.load();
        ClientsController c2 = f2.getController();
        c2.setScreenChanger(this);

        FXMLLoader f3 = new FXMLLoader(getClass().getResource("/FXML/Log Screen.fxml"));
        logScreen = f3.load();
        LogController c3 = f3.getController();
        c3.setScreenChanger(this);

        Font.loadFont(getClass().getResourceAsStream("/fonts/IrishGrover-Regular.ttf"), 10);
    }

    public BorderPane getRoot() {
        return root;
    }

    //functions for nav buttons
    public void controlScreen() {
        root.setCenter(controlScreen);
        root.getStylesheets().add("/styles/control.css");
    }
    public void clientsScreen() {
        root.setCenter(clientsScreen);
        root.getStylesheets().add("/styles/clients.css");
    }
    public void logScreen() {
        root.setCenter(logScreen);
        root.getStylesheets().add("/styles/log.css");
    }
}
