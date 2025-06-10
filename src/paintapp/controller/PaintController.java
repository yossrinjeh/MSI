package paintapp.controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import paintapp.model.*;
import paintapp.logging.LoggingManager;

public class PaintController {
    private Canvas canvas;
    private String currentShape = "Line";
    private CommandManager commandManager;
    private LoggingManager logger;

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
    }

    private double startX, startY;

    private void handleMousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
        logger.debug("Mouse pressed at coordinates: (" + startX + ", " + startY + ")");
    }

    private void handleMouseReleased(MouseEvent e) {
        double endX = e.getX();
        double endY = e.getY();

        logger.debug("Mouse released at coordinates: (" + endX + ", " + endY + ")");
        logger.info("Drawing " + currentShape + " from (" + startX + ", " + startY +
                   ") to (" + endX + ", " + endY + ")");

        DrawCommand command = new DrawCommand(currentShape, startX, startY, endX, endY, canvas.getGraphicsContext2D());
        command.execute();
        commandManager.addCommand(command);

        logger.info("Shape drawn successfully and added to command history");
    }

    public void setCurrentShape(String shape) {
        String previousShape = this.currentShape;
        this.currentShape = shape;
        logger.info("Shape tool changed from " + previousShape + " to " + shape);
    }

    public void undo() {
        logger.info("Undo operation requested");
        commandManager.undo();
        logger.info("Undo operation completed");
    }

    public void redo() {
        logger.info("Redo operation requested");
        commandManager.redo();
        logger.info("Redo operation completed");
    }
}
