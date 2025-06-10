package paintapp.observer;

import paintapp.model.DrawCommand;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Subject class for the Observer pattern in drawing operations.
 * Manages a list of observers and notifies them of drawing events.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class DrawingSubject {
    
    // Using CopyOnWriteArrayList for thread-safe operations
    private final List<DrawingObserver> observers = new CopyOnWriteArrayList<>();
    
    /**
     * Adds an observer to the list.
     * 
     * @param observer The observer to add
     */
    public void addObserver(DrawingObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    /**
     * Removes an observer from the list.
     * 
     * @param observer The observer to remove
     */
    public void removeObserver(DrawingObserver observer) {
        observers.remove(observer);
    }
    
    /**
     * Removes all observers.
     */
    public void clearObservers() {
        observers.clear();
    }
    
    /**
     * Gets the number of registered observers.
     * 
     * @return The number of observers
     */
    public int getObserverCount() {
        return observers.size();
    }
    
    /**
     * Notifies all observers that a shape was added.
     * 
     * @param command The draw command that was executed
     */
    public void notifyShapeAdded(DrawCommand command) {
        for (DrawingObserver observer : observers) {
            try {
                observer.onShapeAdded(command);
            } catch (Exception e) {
                // Log error but continue notifying other observers
                System.err.println("Error notifying observer of shape added: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifies all observers that a shape was removed.
     * 
     * @param command The draw command that was undone
     */
    public void notifyShapeRemoved(DrawCommand command) {
        for (DrawingObserver observer : observers) {
            try {
                observer.onShapeRemoved(command);
            } catch (Exception e) {
                System.err.println("Error notifying observer of shape removed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifies all observers that the canvas was cleared.
     */
    public void notifyCanvasCleared() {
        for (DrawingObserver observer : observers) {
            try {
                observer.onCanvasCleared();
            } catch (Exception e) {
                System.err.println("Error notifying observer of canvas cleared: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifies all observers that a drawing was loaded.
     * 
     * @param drawingName The name of the loaded drawing
     * @param shapeCount The number of shapes in the drawing
     */
    public void notifyDrawingLoaded(String drawingName, int shapeCount) {
        for (DrawingObserver observer : observers) {
            try {
                observer.onDrawingLoaded(drawingName, shapeCount);
            } catch (Exception e) {
                System.err.println("Error notifying observer of drawing loaded: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifies all observers that a drawing was saved.
     * 
     * @param drawingName The name of the saved drawing
     * @param shapeCount The number of shapes in the drawing
     */
    public void notifyDrawingSaved(String drawingName, int shapeCount) {
        for (DrawingObserver observer : observers) {
            try {
                observer.onDrawingSaved(drawingName, shapeCount);
            } catch (Exception e) {
                System.err.println("Error notifying observer of drawing saved: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifies all observers that the drawing tool was changed.
     * 
     * @param previousTool The previous tool name
     * @param newTool The new tool name
     */
    public void notifyToolChanged(String previousTool, String newTool) {
        for (DrawingObserver observer : observers) {
            try {
                observer.onToolChanged(previousTool, newTool);
            } catch (Exception e) {
                System.err.println("Error notifying observer of tool changed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifies all observers that the drawing color was changed.
     * 
     * @param previousColor The previous color
     * @param newColor The new color
     */
    public void notifyColorChanged(Color previousColor, Color newColor) {
        for (DrawingObserver observer : observers) {
            try {
                observer.onColorChanged(previousColor, newColor);
            } catch (Exception e) {
                System.err.println("Error notifying observer of color changed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifies all observers that the fill mode was changed.
     * 
     * @param previousFillMode The previous fill mode
     * @param newFillMode The new fill mode
     */
    public void notifyFillModeChanged(boolean previousFillMode, boolean newFillMode) {
        for (DrawingObserver observer : observers) {
            try {
                observer.onFillModeChanged(previousFillMode, newFillMode);
            } catch (Exception e) {
                System.err.println("Error notifying observer of fill mode changed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifies all observers that an undo operation was performed.
     * 
     * @param undoStackSize The current size of the undo stack
     * @param redoStackSize The current size of the redo stack
     */
    public void notifyUndoPerformed(int undoStackSize, int redoStackSize) {
        for (DrawingObserver observer : observers) {
            try {
                observer.onUndoPerformed(undoStackSize, redoStackSize);
            } catch (Exception e) {
                System.err.println("Error notifying observer of undo performed: " + e.getMessage());
            }
        }
    }
    
    /**
     * Notifies all observers that a redo operation was performed.
     * 
     * @param undoStackSize The current size of the undo stack
     * @param redoStackSize The current size of the redo stack
     */
    public void notifyRedoPerformed(int undoStackSize, int redoStackSize) {
        for (DrawingObserver observer : observers) {
            try {
                observer.onRedoPerformed(undoStackSize, redoStackSize);
            } catch (Exception e) {
                System.err.println("Error notifying observer of redo performed: " + e.getMessage());
            }
        }
    }
}
