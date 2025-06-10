package paintapp.observer;

import paintapp.model.DrawCommand;
import paintapp.logging.LoggingManager;
import javafx.scene.paint.Color;

/**
 * Observer implementation for logging drawing events.
 * Automatically logs all drawing operations and state changes.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class LoggingObserver implements DrawingObserver {
    
    private final LoggingManager logger;
    
    /**
     * Constructor that initializes the logging manager.
     */
    public LoggingObserver() {
        this.logger = LoggingManager.getInstance();
        logger.info("LoggingObserver initialized - will log all drawing events");
    }
    
    @Override
    public void onShapeAdded(DrawCommand command) {
        logger.info(String.format("SHAPE_ADDED: %s at (%.1f,%.1f)-(%.1f,%.1f), color=%s, filled=%s",
                command.getShape(),
                command.getX1(), command.getY1(),
                command.getX2(), command.getY2(),
                colorToString(command.getColor()),
                command.isFillMode()));
    }
    
    @Override
    public void onShapeRemoved(DrawCommand command) {
        logger.info(String.format("SHAPE_REMOVED: %s at (%.1f,%.1f)-(%.1f,%.1f), color=%s, filled=%s",
                command.getShape(),
                command.getX1(), command.getY1(),
                command.getX2(), command.getY2(),
                colorToString(command.getColor()),
                command.isFillMode()));
    }
    
    @Override
    public void onCanvasCleared() {
        logger.info("CANVAS_CLEARED: All shapes removed from canvas");
    }
    
    @Override
    public void onDrawingLoaded(String drawingName, int shapeCount) {
        logger.info(String.format("DRAWING_LOADED: '%s' with %d shapes", drawingName, shapeCount));
    }
    
    @Override
    public void onDrawingSaved(String drawingName, int shapeCount) {
        logger.info(String.format("DRAWING_SAVED: '%s' with %d shapes", drawingName, shapeCount));
    }
    
    @Override
    public void onToolChanged(String previousTool, String newTool) {
        logger.info(String.format("TOOL_CHANGED: %s -> %s", previousTool, newTool));
    }
    
    @Override
    public void onColorChanged(Color previousColor, Color newColor) {
        logger.info(String.format("COLOR_CHANGED: %s -> %s", 
                colorToString(previousColor), colorToString(newColor)));
    }
    
    @Override
    public void onFillModeChanged(boolean previousFillMode, boolean newFillMode) {
        String previousMode = previousFillMode ? "Fill" : "Stroke";
        String newMode = newFillMode ? "Fill" : "Stroke";
        logger.info(String.format("FILL_MODE_CHANGED: %s -> %s", previousMode, newMode));
    }
    
    @Override
    public void onUndoPerformed(int undoStackSize, int redoStackSize) {
        logger.info(String.format("UNDO_PERFORMED: Undo stack size=%d, Redo stack size=%d", 
                undoStackSize, redoStackSize));
    }
    
    @Override
    public void onRedoPerformed(int undoStackSize, int redoStackSize) {
        logger.info(String.format("REDO_PERFORMED: Undo stack size=%d, Redo stack size=%d", 
                undoStackSize, redoStackSize));
    }
    
    /**
     * Converts a Color object to a readable string for logging.
     * 
     * @param color The color to convert
     * @return A string representation of the color
     */
    private String colorToString(Color color) {
        if (color == null) return "null";
        
        // Check for common colors first
        if (color.equals(Color.BLACK)) return "BLACK";
        if (color.equals(Color.WHITE)) return "WHITE";
        if (color.equals(Color.RED)) return "RED";
        if (color.equals(Color.GREEN)) return "GREEN";
        if (color.equals(Color.BLUE)) return "BLUE";
        if (color.equals(Color.YELLOW)) return "YELLOW";
        if (color.equals(Color.ORANGE)) return "ORANGE";
        if (color.equals(Color.PURPLE)) return "PURPLE";
        if (color.equals(Color.PINK)) return "PINK";
        if (color.equals(Color.CYAN)) return "CYAN";
        if (color.equals(Color.MAGENTA)) return "MAGENTA";
        if (color.equals(Color.GRAY)) return "GRAY";
        if (color.equals(Color.LIGHTGRAY)) return "LIGHTGRAY";
        if (color.equals(Color.DARKGRAY)) return "DARKGRAY";
        
        // For custom colors, show hex representation
        return String.format("#%02X%02X%02X", 
                           (int)(color.getRed() * 255), 
                           (int)(color.getGreen() * 255), 
                           (int)(color.getBlue() * 255));
    }
}
