package paintapp.controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import paintapp.model.*;
import paintapp.logging.LoggingManager;

public class PaintController {
    private Canvas canvas;
    private String currentShape = "Line";
    private Color currentColor = Color.BLACK;
    private boolean fillMode = false;
    private CommandManager commandManager;
    private LoggingManager logger;
    private Label statusLabel;

    public PaintController(Canvas canvas) {
        this.canvas = canvas;
        this.commandManager = CommandManager.getInstance();
        this.logger = LoggingManager.getInstance();

        logger.info("PaintController initialized with canvas size: " +
                   canvas.getWidth() + "x" + canvas.getHeight());
    }

    public void initEvents() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        canvas.setOnMouseMoved(this::handleMouseMoved);
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

    public void setCurrentShape(String shape) {
        String previousShape = this.currentShape;
        this.currentShape = shape;
        logger.info("Shape tool changed from " + previousShape + " to " + shape);
        updateStatus("Ready - " + shape + " tool selected");
    }

    public void setCurrentColor(Color color) {
        Color previousColor = this.currentColor;
        this.currentColor = color;
        logger.info("Color changed from " + previousColor + " to " + color);
        updateStatus("Color changed to " + color);
    }

    public void setFillMode(boolean fillMode) {
        boolean previousFillMode = this.fillMode;
        this.fillMode = fillMode;
        String mode = fillMode ? "fill" : "stroke";
        logger.info("Drawing mode changed from " + (previousFillMode ? "fill" : "stroke") + " to " + mode);
        updateStatus("Drawing mode: " + mode);
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

    // File menu operations (placeholder implementations)
    public void newDrawing() {
        logger.info("New drawing requested");
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        commandManager = CommandManager.getInstance(); // Reset command history
        updateStatus("New drawing created");
    }

    public void openDrawing() {
        logger.info("Open drawing requested");
        updateStatus("Open drawing - feature coming soon");
        // TODO: Implement in Phase 3 with database integration
    }

    public void saveDrawing() {
        logger.info("Save drawing requested");
        updateStatus("Save drawing - feature coming soon");
        // TODO: Implement in Phase 3 with database integration
    }

    public void saveAsDrawing() {
        logger.info("Save As drawing requested");
        updateStatus("Save As drawing - feature coming soon");
        // TODO: Implement in Phase 3 with database integration
    }

    public void exitApplication() {
        logger.info("Exit application requested");
        Platform.exit();
    }
}
