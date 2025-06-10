package paintapp.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import paintapp.model.CommandManager;

public class ShapeFactory {
    public static void drawShape(String type, double x1, double y1, double x2, double y2,
                               Color color, boolean fillMode, GraphicsContext gc) {
        // Set color for both stroke and fill
        gc.setStroke(color);
        gc.setFill(color);

        // Calculate rectangle bounds for shapes
        double minX = Math.min(x1, x2);
        double minY = Math.min(y1, y2);
        double width = Math.abs(x2 - x1);
        double height = Math.abs(y2 - y1);

        switch (type) {
            case "Line":
                // Lines are always stroked, fill mode doesn't apply
                gc.strokeLine(x1, y1, x2, y2);
                break;
            case "Rectangle":
                if (fillMode) {
                    gc.fillRect(minX, minY, width, height);
                } else {
                    gc.strokeRect(minX, minY, width, height);
                }
                break;
            case "Ellipse":
                if (fillMode) {
                    gc.fillOval(minX, minY, width, height);
                } else {
                    gc.strokeOval(minX, minY, width, height);
                }
                break;
        }
    }
}
