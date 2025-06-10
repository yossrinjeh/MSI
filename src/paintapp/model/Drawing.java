package paintapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Drawing class represents a saved drawing with its metadata and shapes.
 * Used for database persistence and retrieval operations.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class Drawing {
    
    private int id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String drawingData;
    private double canvasWidth;
    private double canvasHeight;
    private List<Shape> shapes;
    
    /**
     * Default constructor.
     */
    public Drawing() {
        this.shapes = new ArrayList<>();
        this.canvasWidth = 800;
        this.canvasHeight = 600;
    }
    
    /**
     * Constructor with name.
     * 
     * @param name The drawing name
     */
    public Drawing(String name) {
        this();
        this.name = name;
    }
    
    /**
     * Full constructor.
     * 
     * @param id The drawing ID
     * @param name The drawing name
     * @param createdDate The creation date
     * @param modifiedDate The modification date
     * @param drawingData The serialized drawing data
     * @param canvasWidth The canvas width
     * @param canvasHeight The canvas height
     */
    public Drawing(int id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate,
                   String drawingData, double canvasWidth, double canvasHeight) {
        this();
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.drawingData = drawingData;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }
    
    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    public String getDrawingData() {
        return drawingData;
    }
    
    public void setDrawingData(String drawingData) {
        this.drawingData = drawingData;
    }
    
    public double getCanvasWidth() {
        return canvasWidth;
    }
    
    public void setCanvasWidth(double canvasWidth) {
        this.canvasWidth = canvasWidth;
    }
    
    public double getCanvasHeight() {
        return canvasHeight;
    }
    
    public void setCanvasHeight(double canvasHeight) {
        this.canvasHeight = canvasHeight;
    }
    
    public List<Shape> getShapes() {
        return shapes;
    }
    
    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes != null ? shapes : new ArrayList<>();
    }
    
    /**
     * Adds a shape to the drawing.
     * 
     * @param shape The shape to add
     */
    public void addShape(Shape shape) {
        if (shape != null) {
            shapes.add(shape);
        }
    }
    
    /**
     * Removes a shape from the drawing.
     * 
     * @param shape The shape to remove
     */
    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }
    
    /**
     * Clears all shapes from the drawing.
     */
    public void clearShapes() {
        shapes.clear();
    }
    
    /**
     * Gets the number of shapes in the drawing.
     * 
     * @return The number of shapes
     */
    public int getShapeCount() {
        return shapes.size();
    }
    
    @Override
    public String toString() {
        return String.format("Drawing{id=%d, name='%s', shapes=%d, size=%.0fx%.0f}",
                id, name, shapes.size(), canvasWidth, canvasHeight);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Drawing drawing = (Drawing) obj;
        return id == drawing.id && name != null && name.equals(drawing.name);
    }
    
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
