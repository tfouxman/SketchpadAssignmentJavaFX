package SketchpadAssignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Sketchpad extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("canvas.fxml"))));
        stage.setTitle("Sketchpad Application");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
