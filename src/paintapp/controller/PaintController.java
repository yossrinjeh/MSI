package paintapp.controller;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import paintapp.model.*;

public class PaintController {
    private Canvas canvas;
    private String currentShape = "Line";
    private CommandManager commandManager;

    public PaintController(Canvas canvas) {
        this.canvas = canvas;
        this.commandManager = CommandManager.getInstance();
    }

    public void initEvents() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseReleased(this::handleMouseReleased);
    }

    private double startX, startY;

    private void handleMousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
    }

    private void handleMouseReleased(MouseEvent e) {
        double endX = e.getX();
        double endY = e.getY();
        DrawCommand command = new DrawCommand(currentShape, startX, startY, endX, endY, canvas.getGraphicsContext2D());
        command.execute();
        commandManager.addCommand(command);
    }

    public void setCurrentShape(String shape) {
        this.currentShape = shape;
    }

    public void undo() {
        commandManager.undo();
    }

    public void redo() {
        commandManager.redo();
    }
}
