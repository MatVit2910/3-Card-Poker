import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;

//main app
public class MainClient extends Application {

    public static PokerInfo globalPokerInfo;
    public static int globalWinnings;

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            ScreenChanger mainScreen = new ScreenChanger();
            Scene scene = new Scene(mainScreen.getRoot());
            primaryStage.setScene(scene);
            primaryStage.setTitle("3 Card Poker - Server");

            primaryStage.setMinHeight(830);
            primaryStage.setMinWidth(1320);
            primaryStage.setMaximized(true);
            primaryStage.show();
            globalWinnings = 0;
            mainScreen.mainMenuScreen();

        } catch(Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (Controller.myClient != null && Controller.myClient.socketClient != null) {
            Controller.myClient.socketClient.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
