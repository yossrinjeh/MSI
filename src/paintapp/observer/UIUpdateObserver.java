package paintapp.observer;

import paintapp.model.DrawCommand;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.application.Platform;

/**
 * Observer implementation for updating UI components.
 * Automatically updates status labels and other UI elements when drawing events occur.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class UIUpdateObserver implements DrawingObserver {
    
    private Label statusLabel;
    private Label shapeCountLabel;
    private int currentShapeCount = 0;
    
    /**
     * Constructor with status label.
     * 
     * @param statusLabel The status label to update
     */
    public UIUpdateObserver(Label statusLabel) {
        this.statusLabel = statusLabel;
    }
    
    /**
     * Constructor with status and shape count labels.
     * 
     * @param statusLabel The status label to update
     * @param shapeCountLabel The shape count label to update
     */
    public UIUpdateObserver(Label statusLabel, Label shapeCountLabel) {
        this.statusLabel = statusLabel;
        this.shapeCountLabel = shapeCountLabel;
    }
    
    /**
     * Sets the shape count label.
     * 
     * @param shapeCountLabel The shape count label
     */
    public void setShapeCountLabel(Label shapeCountLabel) {
        this.shapeCountLabel = shapeCountLabel;
    }
    
    @Override
    public void onShapeAdded(DrawCommand command) {
        currentShapeCount++;
        updateStatus("Shape added: " + command.getShape() + " (Total: " + currentShapeCount + ")");
        updateShapeCount();
    }
    
    @Override
    public void onShapeRemoved(DrawCommand command) {
        currentShapeCount = Math.max(0, currentShapeCount - 1);
        updateStatus("Shape removed: " + command.getShape() + " (Total: " + currentShapeCount + ")");
        updateShapeCount();
    }
    
    @Override
    public void onCanvasCleared() {
        currentShapeCount = 0;
        updateStatus("Canvas cleared - Ready for new drawing");
        updateShapeCount();
    }
    
    @Override
    public void onDrawingLoaded(String drawingName, int shapeCount) {
        currentShapeCount = shapeCount;
        updateStatus("Drawing loaded: " + drawingName + " (" + shapeCount + " shapes)");
        updateShapeCount();
    }
    
    @Override
    public void onDrawingSaved(String drawingName, int shapeCount) {
        updateStatus("Drawing saved: " + drawingName + " (" + shapeCount + " shapes)");
    }
    
    @Override
    public void onToolChanged(String previousTool, String newTool) {
        updateStatus("Tool changed to: " + newTool);
    }
    
    @Override
    public void onColorChanged(Color previousColor, Color newColor) {
        updateStatus("Color changed to: " + colorToString(newColor));
    }
    
    @Override
    public void onFillModeChanged(boolean previousFillMode, boolean newFillMode) {
        String mode = newFillMode ? "Fill" : "Stroke";
        updateStatus("Drawing mode: " + mode);
    }
    
    @Override
    public void onUndoPerformed(int undoStackSize, int redoStackSize) {
        updateStatus("Undo performed (Undo: " + undoStackSize + ", Redo: " + redoStackSize + ")");
    }
    
    @Override
    public void onRedoPerformed(int undoStackSize, int redoStackSize) {
        updateStatus("Redo performed (Undo: " + undoStackSize + ", Redo: " + redoStackSize + ")");
    }
    
    /**
     * Updates the status label with the given message.
     * 
     * @param message The status message
     */
    private void updateStatus(String message) {
        if (statusLabel != null) {
            Platform.runLater(() -> statusLabel.setText(message));
        }
    }
    
    /**
     * Updates the shape count label.
     */
    private void updateShapeCount() {
        if (shapeCountLabel != null) {
            Platform.runLater(() -> shapeCountLabel.setText("Shapes: " + currentShapeCount));
        }
    }
    
    /**
     * Converts a Color object to a readable string.
     * 
     * @param color The color to convert
     * @return A string representation of the color
     */
    private String colorToString(Color color) {
        if (color == null) return "Unknown";
        
        // Check for common colors
        if (color.equals(Color.BLACK)) return "Black";
        if (color.equals(Color.WHITE)) return "White";
        if (color.equals(Color.RED)) return "Red";
        if (color.equals(Color.GREEN)) return "Green";
        if (color.equals(Color.BLUE)) return "Blue";
        if (color.equals(Color.YELLOW)) return "Yellow";
        if (color.equals(Color.ORANGE)) return "Orange";
        if (color.equals(Color.PURPLE)) return "Purple";
        if (color.equals(Color.PINK)) return "Pink";
        if (color.equals(Color.CYAN)) return "Cyan";
        if (color.equals(Color.MAGENTA)) return "Magenta";
        if (color.equals(Color.GRAY)) return "Gray";
        
        // For custom colors, show RGB values
        return String.format("RGB(%.0f,%.0f,%.0f)", 
                           color.getRed() * 255, 
                           color.getGreen() * 255, 
                           color.getBlue() * 255);
    }
    
    /**
     * Resets the shape count to zero.
     */
    public void resetShapeCount() {
        currentShapeCount = 0;
        updateShapeCount();
    }
    
    /**
     * Sets the current shape count.
     * 
     * @param count The shape count
     */
    public void setShapeCount(int count) {
        currentShapeCount = count;
        updateShapeCount();
    }
}
