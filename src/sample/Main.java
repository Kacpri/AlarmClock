package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLOutput;


public class Main extends Application {
    private static Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Alarm Clock");
        primaryStage.setScene(new Scene(root, 1120, 700));
        primaryStage.show();
        controller = loader.getController();

        resize(root, primaryStage);
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            //primaryStage.setWidth(primaryStage.getHeight() * 1.6);
            resize(root, primaryStage);
        });
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            //primaryStage.setHeight(primaryStage.getWidth() / 1.6);
            resize(root, primaryStage);
        });
    }
    private void resize(Parent root, Stage primaryStage){
        root.setScaleX((primaryStage.getWidth() + primaryStage.getHeight()) / 1350);
        root.setScaleY((primaryStage.getWidth() + primaryStage.getHeight()) / 1350);
        controller.center = new Point2D(primaryStage.getWidth() / 2 - 8, primaryStage.getHeight() / 2 - 20);
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
