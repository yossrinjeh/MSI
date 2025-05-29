package paintapp.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import paintapp.model.CommandManager;

public class ShapeFactory {
    public static void drawShape(String type, double x1, double y1, double x2, double y2, GraphicsContext gc) {
        gc.setStroke(Color.BLACK);
        switch (type) {
            case "Line":
                gc.strokeLine(x1, y1, x2, y2);
                break;
            case "Rectangle":
                gc.strokeRect(Math.min(x1,x2), Math.min(y1,y2), Math.abs(x2-x1), Math.abs(y2-y1));
                break;
            case "Ellipse":
                gc.strokeOval(Math.min(x1,x2), Math.min(y1,y2), Math.abs(x2-x1), Math.abs(y2-y1));
                break;
        }
    }

    public static class DrawCommand implements Command {
        private String shape;
        private double x1, y1, x2, y2;
        private GraphicsContext gc;

        public DrawCommand(String shape, double x1, double y1, double x2, double y2, GraphicsContext gc) {
            this.shape = shape; this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2; this.gc = gc;
        }

        @Override
        public void execute() {
            drawShape(shape, x1, y1, x2, y2, gc);
        }

        @Override
        public void undo() {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
            CommandManager.getInstance().replayAllExcept(this);
        }
    }
}
