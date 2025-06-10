package paintapp.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DrawCommand implements Command {
    private String shape;
    private double x1, y1, x2, y2;
    private Color color;
    private boolean fillMode;
    private GraphicsContext gc;

    public DrawCommand(String shape, double x1, double y1, double x2, double y2,
                      Color color, boolean fillMode, GraphicsContext gc) {
        this.shape = shape;
        this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
        this.color = color;
        this.fillMode = fillMode;
        this.gc = gc;
    }

    @Override
    public void execute() {
        ShapeFactory.drawShape(shape, x1, y1, x2, y2, color, fillMode, gc);
    }

    @Override
    public void undo() {
        // For simplicity, clear and redraw all commands (advanced: use layers)
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        CommandManager.getInstance().replayAllExcept(this);
    }
}
