package paintapp.model;

/**
 * Shape class represents a geometric shape with its properties.
 * Used for database persistence of individual drawing commands.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class Shape {
    
    private int id;
    private int drawingId;
    private String shapeType;
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private String color;
    private boolean filled;
    private int shapeOrder;
    
    /**
     * Default constructor.
     */
    public Shape() {
    }
    
    /**
     * Constructor for creating a shape from drawing command.
     * 
     * @param shapeType The type of shape (Line, Rectangle, Ellipse)
     * @param x1 The starting X coordinate
     * @param y1 The starting Y coordinate
     * @param x2 The ending X coordinate
     * @param y2 The ending Y coordinate
     * @param color The color as string
     * @param filled Whether the shape is filled
     * @param shapeOrder The order of the shape in the drawing
     */
    public Shape(String shapeType, double x1, double y1, double x2, double y2,
                 String color, boolean filled, int shapeOrder) {
        this.shapeType = shapeType;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.filled = filled;
        this.shapeOrder = shapeOrder;
    }
    
    /**
     * Full constructor with all fields.
     * 
     * @param id The shape ID
     * @param drawingId The parent drawing ID
     * @param shapeType The type of shape
     * @param x1 The starting X coordinate
     * @param y1 The starting Y coordinate
     * @param x2 The ending X coordinate
     * @param y2 The ending Y coordinate
     * @param color The color as string
     * @param filled Whether the shape is filled
     * @param shapeOrder The order of the shape in the drawing
     */
    public Shape(int id, int drawingId, String shapeType, double x1, double y1, double x2, double y2,
                 String color, boolean filled, int shapeOrder) {
        this.id = id;
        this.drawingId = drawingId;
        this.shapeType = shapeType;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.filled = filled;
        this.shapeOrder = shapeOrder;
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getDrawingId() {
        return drawingId;
    }
    
    public void setDrawingId(int drawingId) {
        this.drawingId = drawingId;
    }
    
    public String getShapeType() {
        return shapeType;
    }
    
    public void setShapeType(String shapeType) {
        this.shapeType = shapeType;
    }
    
    public double getX1() {
        return x1;
    }
    
    public void setX1(double x1) {
        this.x1 = x1;
    }
    
    public double getY1() {
        return y1;
    }
    
    public void setY1(double y1) {
        this.y1 = y1;
    }
    
    public double getX2() {
        return x2;
    }
    
    public void setX2(double x2) {
        this.x2 = x2;
    }
    
    public double getY2() {
        return y2;
    }
    
    public void setY2(double y2) {
        this.y2 = y2;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public boolean isFilled() {
        return filled;
    }
    
    public void setFilled(boolean filled) {
        this.filled = filled;
    }
    
    public int getShapeOrder() {
        return shapeOrder;
    }
    
    public void setShapeOrder(int shapeOrder) {
        this.shapeOrder = shapeOrder;
    }
    
    @Override
    public String toString() {
        return String.format("Shape{id=%d, type='%s', coords=(%.1f,%.1f)-(%.1f,%.1f), color='%s', filled=%s, order=%d}",
                id, shapeType, x1, y1, x2, y2, color, filled, shapeOrder);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Shape shape = (Shape) obj;
        return id == shape.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
