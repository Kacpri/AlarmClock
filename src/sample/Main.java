package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;



public class Main extends Application {
    private static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();

        root.setScaleX(1.2);
        root.setScaleY(1.2);
        primaryStage.setTitle("Alarm Clock");
        primaryStage.setScene(new Scene(root, 960, 600));
        primaryStage.setResizable(false);
        primaryStage.show();
        controller = loader.getController();

    }

    @Override
    public void stop() {
        controller.stopTimer();
        controller.stopHammerTimer();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
