package paintapp.observer;

import paintapp.model.DrawCommand;
import javafx.scene.paint.Color;

/**
 * Observer interface for drawing events.
 * Implements the Observer pattern to allow multiple components to react to drawing changes.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public interface DrawingObserver {
    
    /**
     * Called when a new shape is added to the canvas.
     * 
     * @param command The draw command that was executed
     */
    void onShapeAdded(DrawCommand command);
    
    /**
     * Called when a shape is removed from the canvas (undo operation).
     * 
     * @param command The draw command that was undone
     */
    void onShapeRemoved(DrawCommand command);
    
    /**
     * Called when the canvas is cleared.
     */
    void onCanvasCleared();
    
    /**
     * Called when a drawing is loaded from the database.
     * 
     * @param drawingName The name of the loaded drawing
     * @param shapeCount The number of shapes in the drawing
     */
    void onDrawingLoaded(String drawingName, int shapeCount);
    
    /**
     * Called when a drawing is saved to the database.
     * 
     * @param drawingName The name of the saved drawing
     * @param shapeCount The number of shapes in the drawing
     */
    void onDrawingSaved(String drawingName, int shapeCount);
    
    /**
     * Called when the current drawing tool is changed.
     * 
     * @param previousTool The previous tool name
     * @param newTool The new tool name
     */
    void onToolChanged(String previousTool, String newTool);
    
    /**
     * Called when the current drawing color is changed.
     * 
     * @param previousColor The previous color
     * @param newColor The new color
     */
    void onColorChanged(Color previousColor, Color newColor);
    
    /**
     * Called when the fill mode is changed.
     * 
     * @param previousFillMode The previous fill mode
     * @param newFillMode The new fill mode
     */
    void onFillModeChanged(boolean previousFillMode, boolean newFillMode);
    
    /**
     * Called when an undo operation is performed.
     * 
     * @param undoStackSize The current size of the undo stack
     * @param redoStackSize The current size of the redo stack
     */
    void onUndoPerformed(int undoStackSize, int redoStackSize);
    
    /**
     * Called when a redo operation is performed.
     * 
     * @param undoStackSize The current size of the undo stack
     * @param redoStackSize The current size of the redo stack
     */
    void onRedoPerformed(int undoStackSize, int redoStackSize);
}
