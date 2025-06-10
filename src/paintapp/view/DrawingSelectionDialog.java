package paintapp.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import paintapp.database.DatabaseManager;
import paintapp.logging.LoggingManager;

import java.util.List;
import java.util.Optional;

/**
 * DrawingSelectionDialog provides a user interface for selecting, managing,
 * and loading saved drawings from the database.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class DrawingSelectionDialog {
    
    private Stage dialogStage;
    private ListView<String> drawingListView;
    private String selectedDrawing;
    private boolean confirmed;
    private DatabaseManager databaseManager;
    private LoggingManager logger;
    
    /**
     * Constructor for DrawingSelectionDialog.
     * 
     * @param parentStage The parent stage for modal dialog
     */
    public DrawingSelectionDialog(Stage parentStage) {
        this.databaseManager = DatabaseManager.getInstance();
        this.logger = LoggingManager.getInstance();
        this.confirmed = false;
        
        createDialog(parentStage);
    }
    
    /**
     * Creates and configures the dialog.
     * 
     * @param parentStage The parent stage
     */
    private void createDialog(Stage parentStage) {
        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setTitle("Select Drawing to Load");
        dialogStage.setResizable(false);
        
        // Create main layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Create title label
        Label titleLabel = new Label("Select a drawing to load:");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        // Create drawing list
        drawingListView = new ListView<>();
        drawingListView.setPrefHeight(200);
        drawingListView.setPrefWidth(300);
        
        // Load drawings from database
        refreshDrawingList();
        
        // Create buttons
        Button loadButton = new Button("Load");
        Button deleteButton = new Button("Delete");
        Button refreshButton = new Button("Refresh");
        Button cancelButton = new Button("Cancel");
        
        // Configure button actions
        loadButton.setOnAction(e -> loadSelectedDrawing());
        deleteButton.setOnAction(e -> deleteSelectedDrawing());
        refreshButton.setOnAction(e -> refreshDrawingList());
        cancelButton.setOnAction(e -> closeDialog());
        
        // Enable/disable buttons based on selection
        loadButton.setDisable(true);
        deleteButton.setDisable(true);
        
        drawingListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
            loadButton.setDisable(!hasSelection);
            deleteButton.setDisable(!hasSelection);
        });
        
        // Create button layout
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(loadButton, deleteButton, refreshButton, cancelButton);
        
        // Create content layout
        VBox contentBox = new VBox(10);
        contentBox.getChildren().addAll(titleLabel, drawingListView);
        
        // Set up main layout
        root.setCenter(contentBox);
        root.setBottom(buttonBox);
        
        // Create scene and show dialog
        Scene scene = new Scene(root);
        dialogStage.setScene(scene);
        
        // Handle window close
        dialogStage.setOnCloseRequest(e -> closeDialog());
    }
    
    /**
     * Refreshes the drawing list from the database.
     */
    private void refreshDrawingList() {
        try {
            List<String> drawingNames = databaseManager.getDrawingNames();
            
            Platform.runLater(() -> {
                drawingListView.getItems().clear();
                drawingListView.getItems().addAll(drawingNames);
                
                if (drawingNames.isEmpty()) {
                    drawingListView.setPlaceholder(new Label("No saved drawings found"));
                }
            });
            
            logger.info("Drawing list refreshed: " + drawingNames.size() + " drawings found");
            
        } catch (Exception e) {
            logger.error("Failed to refresh drawing list: " + e.getMessage());
            showErrorAlert("Database Error", "Failed to load drawing list from database.");
        }
    }
    
    /**
     * Loads the selected drawing.
     */
    private void loadSelectedDrawing() {
        String selected = drawingListView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedDrawing = selected;
            confirmed = true;
            logger.info("Drawing selected for loading: " + selected);
            dialogStage.close();
        }
    }
    
    /**
     * Deletes the selected drawing after confirmation.
     */
    private void deleteSelectedDrawing() {
        String selected = drawingListView.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        
        // Show confirmation dialog
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Drawing");
        confirmAlert.setContentText("Are you sure you want to delete the drawing '" + selected + "'?\nThis action cannot be undone.");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean deleted = databaseManager.deleteDrawing(selected);
                if (deleted) {
                    logger.info("Drawing deleted: " + selected);
                    refreshDrawingList(); // Refresh the list
                    showInfoAlert("Success", "Drawing '" + selected + "' has been deleted.");
                } else {
                    logger.warning("Failed to delete drawing: " + selected);
                    showErrorAlert("Delete Failed", "Failed to delete the drawing. It may not exist.");
                }
            } catch (Exception e) {
                logger.error("Error deleting drawing: " + e.getMessage());
                showErrorAlert("Database Error", "An error occurred while deleting the drawing.");
            }
        }
    }
    
    /**
     * Closes the dialog without selection.
     */
    private void closeDialog() {
        confirmed = false;
        selectedDrawing = null;
        dialogStage.close();
    }
    
    /**
     * Shows the dialog and waits for user action.
     * 
     * @return The selected drawing name, or null if cancelled
     */
    public String showAndWait() {
        dialogStage.showAndWait();
        return confirmed ? selectedDrawing : null;
    }
    
    /**
     * Shows an error alert dialog.
     * 
     * @param title The alert title
     * @param message The error message
     */
    private void showErrorAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    /**
     * Shows an information alert dialog.
     * 
     * @param title The alert title
     * @param message The information message
     */
    private void showInfoAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
    
    /**
     * Gets whether the dialog was confirmed.
     * 
     * @return true if a drawing was selected, false if cancelled
     */
    public boolean isConfirmed() {
        return confirmed;
    }
    
    /**
     * Gets the selected drawing name.
     * 
     * @return The selected drawing name, or null if none selected
     */
    public String getSelectedDrawing() {
        return selectedDrawing;
    }
}
