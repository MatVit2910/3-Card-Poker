import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.io.IOException;

//class to manage all screens
public class ScreenChanger {

    //data members
    private final BorderPane root = new BorderPane();
    private final BorderPane menuScreen;
    private final BorderPane gameScreen;
    private final BorderPane endGameScreen;
    private Controller gameController;

    //constructor that loads all screens
    public ScreenChanger() throws IOException {
        //load all screens (main menu, game, end)
        FXMLLoader f1 = new FXMLLoader(getClass().getResource("/FXML/MainMenuScreen.fxml"));
        menuScreen = f1.load();
        gameController = f1.getController();
        gameController.setManager(this);

        FXMLLoader f2 = new FXMLLoader(getClass().getResource("/FXML/GameScreen.fxml"));
        f2.setController(gameController);
        gameScreen = f2.load();

        FXMLLoader f3 = new FXMLLoader(getClass().getResource("/FXML/EndGameScreen.fxml"));
        f3.setController(gameController);
        endGameScreen = f3.load();

        Font.loadFont(getClass().getResourceAsStream("/fonts/IrishGrover-Regular.ttf"), 10);
    }

    //to get root of main screen
    public BorderPane getRoot() {
        root.setCenter(menuScreen);
        root.getStylesheets().add("/styles/gameStyles.css");
        return root;
    }

    //to get root of game screen
    public BorderPane getRootGameScreen() {
        root.setCenter(gameScreen);
        root.getStylesheets().add("/styles/gameStyles.css");
        return root;
    }

    //functions to change screens
    public void mainMenuScreen() {
        root.setCenter(menuScreen);
        root.getStylesheets().add("/styles/gameStyles.css");
    }
    public void gameScreen() {
        root.setCenter(gameScreen);
        root.getStylesheets().add("/styles/gameStyles.css");
    }
    public void endGameScreen() {
        root.setCenter(endGameScreen);
        root.getStylesheets().add("/styles/gameStyles.css");
    }
}