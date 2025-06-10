package paintapp.observer;

import paintapp.model.DrawCommand;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Observer implementation for collecting drawing statistics.
 * Tracks various metrics about the drawing session.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class StatisticsObserver implements DrawingObserver {
    
    private int totalShapesDrawn = 0;
    private int totalShapesRemoved = 0;
    private int canvasClearedCount = 0;
    private int drawingsSaved = 0;
    private int drawingsLoaded = 0;
    private int undoOperations = 0;
    private int redoOperations = 0;
    private int toolChanges = 0;
    private int colorChanges = 0;
    private int fillModeChanges = 0;
    
    private Map<String, Integer> shapeTypeCount = new HashMap<>();
    private Map<String, Integer> colorUsageCount = new HashMap<>();
    
    /**
     * Constructor that initializes the statistics tracking.
     */
    public StatisticsObserver() {
        System.out.println("StatisticsObserver initialized - tracking drawing statistics");
    }
    
    @Override
    public void onShapeAdded(DrawCommand command) {
        totalShapesDrawn++;
        
        // Track shape types
        String shapeType = command.getShape();
        shapeTypeCount.put(shapeType, shapeTypeCount.getOrDefault(shapeType, 0) + 1);
        
        // Track color usage
        String colorName = colorToString(command.getColor());
        colorUsageCount.put(colorName, colorUsageCount.getOrDefault(colorName, 0) + 1);
    }
    
    @Override
    public void onShapeRemoved(DrawCommand command) {
        totalShapesRemoved++;
    }
    
    @Override
    public void onCanvasCleared() {
        canvasClearedCount++;
    }
    
    @Override
    public void onDrawingLoaded(String drawingName, int shapeCount) {
        drawingsLoaded++;
    }
    
    @Override
    public void onDrawingSaved(String drawingName, int shapeCount) {
        drawingsSaved++;
    }
    
    @Override
    public void onToolChanged(String previousTool, String newTool) {
        toolChanges++;
    }
    
    @Override
    public void onColorChanged(Color previousColor, Color newColor) {
        colorChanges++;
    }
    
    @Override
    public void onFillModeChanged(boolean previousFillMode, boolean newFillMode) {
        fillModeChanges++;
    }
    
    @Override
    public void onUndoPerformed(int undoStackSize, int redoStackSize) {
        undoOperations++;
    }
    
    @Override
    public void onRedoPerformed(int undoStackSize, int redoStackSize) {
        redoOperations++;
    }
    
    /**
     * Prints a comprehensive statistics report.
     */
    public void printStatistics() {
        System.out.println("\n=== DRAWING SESSION STATISTICS ===");
        System.out.println("Total shapes drawn: " + totalShapesDrawn);
        System.out.println("Total shapes removed: " + totalShapesRemoved);
        System.out.println("Current shapes on canvas: " + (totalShapesDrawn - totalShapesRemoved));
        System.out.println("Canvas cleared count: " + canvasClearedCount);
        System.out.println("Drawings saved: " + drawingsSaved);
        System.out.println("Drawings loaded: " + drawingsLoaded);
        System.out.println("Undo operations: " + undoOperations);
        System.out.println("Redo operations: " + redoOperations);
        System.out.println("Tool changes: " + toolChanges);
        System.out.println("Color changes: " + colorChanges);
        System.out.println("Fill mode changes: " + fillModeChanges);
        
        System.out.println("\nShape Type Distribution:");
        for (Map.Entry<String, Integer> entry : shapeTypeCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println("\nColor Usage Distribution:");
        for (Map.Entry<String, Integer> entry : colorUsageCount.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("=====================================\n");
    }
    
    /**
     * Resets all statistics to zero.
     */
    public void resetStatistics() {
        totalShapesDrawn = 0;
        totalShapesRemoved = 0;
        canvasClearedCount = 0;
        drawingsSaved = 0;
        drawingsLoaded = 0;
        undoOperations = 0;
        redoOperations = 0;
        toolChanges = 0;
        colorChanges = 0;
        fillModeChanges = 0;
        shapeTypeCount.clear();
        colorUsageCount.clear();
        System.out.println("Statistics reset to zero");
    }
    
    /**
     * Gets the total number of shapes currently on canvas.
     * 
     * @return The current shape count
     */
    public int getCurrentShapeCount() {
        return totalShapesDrawn - totalShapesRemoved;
    }
    
    /**
     * Gets the most used shape type.
     * 
     * @return The most used shape type, or "None" if no shapes drawn
     */
    public String getMostUsedShapeType() {
        return shapeTypeCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");
    }
    
    /**
     * Gets the most used color.
     * 
     * @return The most used color, or "None" if no shapes drawn
     */
    public String getMostUsedColor() {
        return colorUsageCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("None");
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
}
