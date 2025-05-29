package paintapp;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import paintapp.view.PaintView;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        PaintView view = new PaintView();
        Scene scene = new Scene(view.getRoot(), 800, 600);
        primaryStage.setTitle("JavaFX Paint App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  // This is correct
    }
}
