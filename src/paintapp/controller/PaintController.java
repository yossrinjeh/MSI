package paintapp.controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.stage.Stage;
import paintapp.model.*;
import paintapp.logging.LoggingManager;
import paintapp.database.DatabaseManager;
import paintapp.view.DrawingSelectionDialog;
import paintapp.observer.DrawingSubject;

import java.util.List;
import java.util.Optional;

public class PaintController {
    private Canvas canvas;
    private String currentShape = "Line";
    private Color currentColor = Color.BLACK;
    private boolean fillMode = false;
    private CommandManager commandManager;
    private LoggingManager logger;
    private Label statusLabel;
    private DatabaseManager databaseManager;
    private String currentDrawingName;
    private Stage parentStage;
    private DrawingSubject drawingSubject;

    public PaintController(Canvas canvas) {
        this.canvas = canvas;
        this.commandManager = CommandManager.getInstance();
        this.logger = LoggingManager.getInstance();
        this.databaseManager = DatabaseManager.getInstance();
        this.currentDrawingName = null;
        this.drawingSubject = commandManager.getDrawingSubject();

        logger.info("PaintController initialized with canvas size: " +
                   canvas.getWidth() + "x" + canvas.getHeight());
    }

    public void initEvents() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        canvas.setOnMouseMoved(this::handleMouseMoved);

        // Add keyboard shortcuts for logging methods
        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(this::handleKeyPressed);
    }

    private double startX, startY;

    private void handleMousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        logger.debug("Mouse pressed at coordinates: (" + startX + ", " + startY + ")");
        updateStatus("Drawing " + currentShape + " - drag to set end point");
    }

    private void handleMouseReleased(MouseEvent e) {
        double endX = e.getX();
        double endY = e.getY();

        logger.debug("Mouse released at coordinates: (" + endX + ", " + endY + ")");
        logger.info("Drawing " + currentShape + " from (" + startX + ", " + startY +
                   ") to (" + endX + ", " + endY + ") with color " + currentColor +
                   " and fill mode: " + fillMode);

        DrawCommand command = new DrawCommand(currentShape, startX, startY, endX, endY,
                                            currentColor, fillMode, canvas.getGraphicsContext2D());
        command.execute();
        commandManager.addCommand(command);

        logger.info("Shape drawn successfully and added to command history");
        updateStatus("Ready - " + currentShape + " tool selected");
    }

    private void handleMouseMoved(MouseEvent e) {
        updateStatus("Position: (" + String.format("%.0f", e.getX()) + ", " +
                    String.format("%.0f", e.getY()) + ") - " + currentShape + " tool selected");
    }

    private void handleKeyPressed(KeyEvent e) {
        // Keyboard shortcuts for logging methods
        if (e.isControlDown()) {
            switch (e.getCode()) {
                case L:
                    if (e.isShiftDown()) {
                        // Ctrl+Shift+L = Database Logging
                        switchToDatabaseLogging();
                    } else if (e.isAltDown()) {
                        // Ctrl+Alt+L = File Logging
                        switchToFileLogging();
                    } else {
                        // Ctrl+L = Console Logging
                        switchToConsoleLogging();
                    }
                    e.consume();
                    break;
                default:
                    break;
            }
        }
    }

    public void setCurrentShape(String shape) {
        String previousShape = this.currentShape;
        this.currentShape = shape;
        logger.info("Shape tool changed from " + previousShape + " to " + shape);
        updateStatus("Ready - " + shape + " tool selected");

        // Notify observers of tool change
        drawingSubject.notifyToolChanged(previousShape, shape);
    }

    public void setCurrentColor(Color color) {
        Color previousColor = this.currentColor;
        this.currentColor = color;
        logger.info("Color changed from " + previousColor + " to " + color);
        updateStatus("Color changed to " + color);

        // Notify observers of color change
        drawingSubject.notifyColorChanged(previousColor, color);
    }

    public void setFillMode(boolean fillMode) {
        boolean previousFillMode = this.fillMode;
        this.fillMode = fillMode;
        String mode = fillMode ? "fill" : "stroke";
        logger.info("Drawing mode changed from " + (previousFillMode ? "fill" : "stroke") + " to " + mode);
        updateStatus("Drawing mode: " + mode);

        // Notify observers of fill mode change
        drawingSubject.notifyFillModeChanged(previousFillMode, fillMode);
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
        updateStatus("Ready - " + currentShape + " tool selected");
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            Platform.runLater(() -> statusLabel.setText(message));
        }
    }

    public void undo() {
        logger.info("Undo operation requested");
        commandManager.undo();
        logger.info("Undo operation completed");
        updateStatus("Undo completed");
    }

    public void redo() {
        logger.info("Redo operation requested");
        commandManager.redo();
        logger.info("Redo operation completed");
        updateStatus("Redo completed");
    }

    // File menu operations
    public void newDrawing() {
        logger.info("New drawing requested");
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        commandManager.clearHistory();
        currentDrawingName = null;
        updateStatus("New drawing created");

        // Note: clearHistory() in CommandManager already notifies observers of canvas cleared
    }

    public void openDrawing() {
        logger.info("Open drawing requested");

        try {
            // Show drawing selection dialog
            DrawingSelectionDialog dialog = new DrawingSelectionDialog(parentStage);
            String selectedDrawing = dialog.showAndWait();

            if (selectedDrawing != null) {
                // Load the selected drawing
                Drawing drawing = databaseManager.loadDrawing(selectedDrawing);

                if (drawing != null) {
                    loadDrawingToCanvas(drawing);
                    currentDrawingName = selectedDrawing;
                    updateStatus("Drawing loaded: " + selectedDrawing);
                    logger.info("Drawing loaded successfully: " + selectedDrawing);
                } else {
                    updateStatus("Failed to load drawing: " + selectedDrawing);
                    logger.error("Failed to load drawing: " + selectedDrawing);
                }
            } else {
                updateStatus("Open drawing cancelled");
                logger.info("Open drawing cancelled by user");
            }

        } catch (Exception e) {
            logger.error("Error opening drawing: " + e.getMessage());
            updateStatus("Error opening drawing");
        }
    }

    public void saveDrawing() {
        logger.info("Save drawing requested");

        if (currentDrawingName != null) {
            // Save to existing name
            saveDrawingWithName(currentDrawingName);
        } else {
            // No current name, prompt for name (same as Save As)
            saveAsDrawing();
        }
    }

    public void saveAsDrawing() {
        logger.info("Save As drawing requested");

        // Prompt for drawing name
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Drawing");
        dialog.setHeaderText("Save Drawing As");
        dialog.setContentText("Enter a name for your drawing:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String drawingName = result.get().trim();

            // Check if drawing already exists
            if (databaseManager.drawingExists(drawingName)) {
                // Show confirmation dialog
                javafx.scene.control.Alert confirmAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Drawing Exists");
                confirmAlert.setHeaderText("Overwrite Drawing");
                confirmAlert.setContentText("A drawing with the name '" + drawingName + "' already exists.\nDo you want to overwrite it?");

                Optional<javafx.scene.control.ButtonType> confirmResult = confirmAlert.showAndWait();
                if (confirmResult.isPresent() && confirmResult.get() == javafx.scene.control.ButtonType.OK) {
                    saveDrawingWithName(drawingName);
                } else {
                    updateStatus("Save cancelled");
                    logger.info("Save cancelled - user chose not to overwrite");
                }
            } else {
                saveDrawingWithName(drawingName);
            }
        } else {
            updateStatus("Save cancelled");
            logger.info("Save cancelled - no name provided");
        }
    }

    public void exitApplication() {
        logger.info("Exit application requested");
        Platform.exit();
    }

    // Helper methods for database operations

    /**
     * Saves the current drawing with the specified name.
     *
     * @param drawingName The name to save the drawing as
     */
    private void saveDrawingWithName(String drawingName) {
        try {
            // Create drawing state from current command manager
            DrawingState drawingState = DrawingState.fromCommandManager(
                drawingName,
                canvas.getWidth(),
                canvas.getHeight(),
                commandManager
            );

            // Convert to Drawing object
            Drawing drawing = new Drawing(drawingName);
            drawing.setCanvasWidth(canvas.getWidth());
            drawing.setCanvasHeight(canvas.getHeight());
            drawing.setShapes(drawingState.toShapeList());

            // Save to database
            boolean saved = databaseManager.saveDrawing(drawing);

            if (saved) {
                currentDrawingName = drawingName;
                updateStatus("Drawing saved: " + drawingName);
                logger.info("Drawing saved successfully: " + drawingName);

                // Notify observers of drawing saved
                drawingSubject.notifyDrawingSaved(drawingName, drawing.getShapes().size());
            } else {
                updateStatus("Failed to save drawing");
                logger.error("Failed to save drawing: " + drawingName);
            }

        } catch (Exception e) {
            logger.error("Error saving drawing: " + e.getMessage());
            updateStatus("Error saving drawing");
        }
    }

    /**
     * Loads a drawing to the canvas.
     *
     * @param drawing The drawing to load
     */
    private void loadDrawingToCanvas(Drawing drawing) {
        try {
            // Clear current canvas and command history
            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            commandManager.clearHistory();

            // Create drawing state from shapes
            DrawingState drawingState = DrawingState.fromShapeList(
                drawing.getShapes(),
                drawing.getName(),
                drawing.getCanvasWidth(),
                drawing.getCanvasHeight()
            );

            // Recreate and execute commands
            for (DrawingState.SerializableCommand serCmd : drawingState.getCommands()) {
                Color color = DrawingState.stringToColor(serCmd.getColor());

                DrawCommand command = new DrawCommand(
                    serCmd.getShapeType(),
                    serCmd.getX1(),
                    serCmd.getY1(),
                    serCmd.getX2(),
                    serCmd.getY2(),
                    color,
                    serCmd.isFilled(),
                    canvas.getGraphicsContext2D()
                );

                command.execute();
                commandManager.addCommand(command);
            }

            logger.info("Drawing loaded to canvas: " + drawing.getName() + " with " + drawing.getShapes().size() + " shapes");

            // Notify observers of drawing loaded
            drawingSubject.notifyDrawingLoaded(drawing.getName(), drawing.getShapes().size());

        } catch (Exception e) {
            logger.error("Error loading drawing to canvas: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Sets the parent stage for dialogs.
     *
     * @param parentStage The parent stage
     */
    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    /**
     * Gets the current drawing name.
     *
     * @return The current drawing name, or null if no drawing is loaded
     */
    public String getCurrentDrawingName() {
        return currentDrawingName;
    }

    /**
     * Gets the drawing subject for observer management.
     *
     * @return The drawing subject
     */
    public DrawingSubject getDrawingSubject() {
        return drawingSubject;
    }

    // Logging method switching methods

    /**
     * Switches logging to console output.
     */
    public void switchToConsoleLogging() {
        logger.useConsoleLogging();
        logger.info("Switched to Console Logging");
        updateStatus("Logging method changed to: Console");
    }

    /**
     * Switches logging to file output.
     */
    public void switchToFileLogging() {
        logger.useFileLogging();
        logger.info("Switched to File Logging");
        updateStatus("Logging method changed to: File");
    }

    /**
     * Switches logging to database output.
     */
    public void switchToDatabaseLogging() {
        logger.useDatabaseLogging();
        logger.info("Switched to Database Logging");
        updateStatus("Logging method changed to: Database");
    }
}
