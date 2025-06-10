package paintapp;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import paintapp.view.PaintView;
import paintapp.logging.LoggingManager;

public class Main extends Application {

    private LoggingManager logger;

    @Override
    public void start(Stage primaryStage) {
        // Initialize logging system
        logger = LoggingManager.getInstance();
        logger.info("JavaFX Paint Application starting...");

        try {
            PaintView view = new PaintView(primaryStage);
            Scene scene = new Scene(view.getRoot(), 1200, 900);
            primaryStage.setTitle("JavaFX Paint App - MySQL Database Integration");
            primaryStage.setScene(scene);
            primaryStage.show();

            logger.info("Application started successfully");

            // Set up application close handler
            primaryStage.setOnCloseRequest(e -> {
                logger.info("Application closing...");
            });

        } catch (Exception e) {
            logger.error("Failed to start application: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        launch(args);  // This is correct
    }
}
