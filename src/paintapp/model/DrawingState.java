package paintapp.model;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * DrawingState class manages the serialization and deserialization of drawing commands.
 * Provides functionality to convert between command history and database-storable format.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class DrawingState {
    
    private String drawingName;
    private double canvasWidth;
    private double canvasHeight;
    private List<SerializableCommand> commands;
    
    /**
     * Default constructor.
     */
    public DrawingState() {
        this.commands = new ArrayList<>();
        this.canvasWidth = 800;
        this.canvasHeight = 600;
    }
    
    /**
     * Constructor with drawing name and canvas dimensions.
     * 
     * @param drawingName The name of the drawing
     * @param canvasWidth The canvas width
     * @param canvasHeight The canvas height
     */
    public DrawingState(String drawingName, double canvasWidth, double canvasHeight) {
        this();
        this.drawingName = drawingName;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }
    
    /**
     * Creates a DrawingState from the current command manager state.
     * 
     * @param drawingName The name for the drawing
     * @param canvasWidth The canvas width
     * @param canvasHeight The canvas height
     * @param commandManager The command manager containing the drawing history
     * @return A DrawingState representing the current drawing
     */
    public static DrawingState fromCommandManager(String drawingName, double canvasWidth, 
                                                  double canvasHeight, CommandManager commandManager) {
        DrawingState state = new DrawingState(drawingName, canvasWidth, canvasHeight);
        
        // Extract commands from the undo stack
        Stack<Command> undoStack = commandManager.getUndoStack();
        int order = 0;
        
        for (Command cmd : undoStack) {
            if (cmd instanceof DrawCommand) {
                DrawCommand drawCmd = (DrawCommand) cmd;
                SerializableCommand serCmd = new SerializableCommand(
                    drawCmd.getShape(),
                    drawCmd.getX1(),
                    drawCmd.getY1(),
                    drawCmd.getX2(),
                    drawCmd.getY2(),
                    colorToString(drawCmd.getColor()),
                    drawCmd.isFillMode(),
                    order++
                );
                state.addCommand(serCmd);
            }
        }
        
        return state;
    }
    
    /**
     * Converts a Color object to a string representation.
     * 
     * @param color The color to convert
     * @return String representation of the color
     */
    private static String colorToString(Color color) {
        if (color == null) return "BLACK";
        
        // Convert to hex format
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
    
    /**
     * Converts a string representation to a Color object.
     * 
     * @param colorString The color string (hex format or color name)
     * @return The Color object
     */
    public static Color stringToColor(String colorString) {
        if (colorString == null || colorString.isEmpty()) {
            return Color.BLACK;
        }
        
        try {
            if (colorString.startsWith("#")) {
                return Color.web(colorString);
            } else {
                // Try to parse as a named color
                return Color.valueOf(colorString.toUpperCase());
            }
        } catch (Exception e) {
            return Color.BLACK; // Default fallback
        }
    }
    
    /**
     * Converts this DrawingState to a list of Shape objects for database storage.
     * 
     * @return List of Shape objects
     */
    public List<Shape> toShapeList() {
        List<Shape> shapes = new ArrayList<>();
        
        for (SerializableCommand cmd : commands) {
            Shape shape = new Shape(
                cmd.getShapeType(),
                cmd.getX1(),
                cmd.getY1(),
                cmd.getX2(),
                cmd.getY2(),
                cmd.getColor(),
                cmd.isFilled(),
                cmd.getOrder()
            );
            shapes.add(shape);
        }
        
        return shapes;
    }
    
    /**
     * Creates a DrawingState from a list of Shape objects.
     * 
     * @param shapes The list of shapes
     * @param drawingName The drawing name
     * @param canvasWidth The canvas width
     * @param canvasHeight The canvas height
     * @return A DrawingState object
     */
    public static DrawingState fromShapeList(List<Shape> shapes, String drawingName, 
                                           double canvasWidth, double canvasHeight) {
        DrawingState state = new DrawingState(drawingName, canvasWidth, canvasHeight);
        
        // Sort shapes by order
        shapes.sort((s1, s2) -> Integer.compare(s1.getShapeOrder(), s2.getShapeOrder()));
        
        for (Shape shape : shapes) {
            SerializableCommand cmd = new SerializableCommand(
                shape.getShapeType(),
                shape.getX1(),
                shape.getY1(),
                shape.getX2(),
                shape.getY2(),
                shape.getColor(),
                shape.isFilled(),
                shape.getShapeOrder()
            );
            state.addCommand(cmd);
        }
        
        return state;
    }
    
    // Getters and Setters
    
    public String getDrawingName() {
        return drawingName;
    }
    
    public void setDrawingName(String drawingName) {
        this.drawingName = drawingName;
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
    
    public List<SerializableCommand> getCommands() {
        return commands;
    }
    
    public void setCommands(List<SerializableCommand> commands) {
        this.commands = commands != null ? commands : new ArrayList<>();
    }
    
    public void addCommand(SerializableCommand command) {
        if (command != null) {
            commands.add(command);
        }
    }
    
    public void clearCommands() {
        commands.clear();
    }
    
    public int getCommandCount() {
        return commands.size();
    }
    
    /**
     * Inner class representing a serializable drawing command.
     */
    public static class SerializableCommand {
        private String shapeType;
        private double x1, y1, x2, y2;
        private String color;
        private boolean filled;
        private int order;
        
        public SerializableCommand() {}
        
        public SerializableCommand(String shapeType, double x1, double y1, double x2, double y2,
                                 String color, boolean filled, int order) {
            this.shapeType = shapeType;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.filled = filled;
            this.order = order;
        }
        
        // Getters and setters for SerializableCommand
        public String getShapeType() { return shapeType; }
        public void setShapeType(String shapeType) { this.shapeType = shapeType; }
        
        public double getX1() { return x1; }
        public void setX1(double x1) { this.x1 = x1; }
        
        public double getY1() { return y1; }
        public void setY1(double y1) { this.y1 = y1; }
        
        public double getX2() { return x2; }
        public void setX2(double x2) { this.x2 = x2; }
        
        public double getY2() { return y2; }
        public void setY2(double y2) { this.y2 = y2; }
        
        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }
        
        public boolean isFilled() { return filled; }
        public void setFilled(boolean filled) { this.filled = filled; }
        
        public int getOrder() { return order; }
        public void setOrder(int order) { this.order = order; }
    }
}
