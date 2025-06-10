package paintapp.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import paintapp.logging.LoggingManager;

/**
 * DatabaseManager implements the Singleton pattern to manage MySQL database connections
 * and operations for the paint application. Handles drawing persistence and retrieval.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;
    private LoggingManager logger;
    private boolean isInitializing = false; // Flag to prevent circular dependency

    /**
     * Private constructor for Singleton pattern.
     * Initializes database connection and creates tables if needed.
     */
    private DatabaseManager() {
        this.logger = LoggingManager.getInstance();
        initializeDatabase();
    }

    /**
     * Safe logging method that avoids circular dependency with DatabaseLogger.
     * Falls back to console logging when database logging is active.
     */
    private void safeLog(String level, String message) {
        if (isInitializing) {
            // During initialization, use console logging to avoid circular dependency
            System.out.println("[DatabaseManager] [" + level + "] " + message);
        } else {
            // Check if current logger is DatabaseLogger to avoid circular dependency
            if (logger.getCurrentLogger() instanceof paintapp.logging.DatabaseLogger) {
                // Use console logging to avoid circular dependency
                System.out.println("[DatabaseManager] [" + level + "] " + message);
            } else {
                // Safe to use LoggingManager
                logger.log(level, message);
            }
        }
    }

    /**
     * Gets the singleton instance of DatabaseManager.
     *
     * @return The DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Initializes the database connection and creates necessary tables.
     */
    private void initializeDatabase() {
        isInitializing = true; // Set flag to prevent circular dependency
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection
            connection = DriverManager.getConnection(
                    DatabaseConfig.JDBC_URL,
                    DatabaseConfig.USERNAME,
                    DatabaseConfig.PASSWORD
            );

            safeLog("INFO", "Connected to MySQL database: " + DatabaseConfig.DATABASE);

            // Create tables if they don't exist
            createTables();

        } catch (ClassNotFoundException e) {
            safeLog("ERROR", "MySQL JDBC driver not found: " + e.getMessage());
            throw new RuntimeException("MySQL JDBC driver not found", e);
        } catch (SQLException e) {
            safeLog("ERROR", "Failed to connect to database: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        } finally {
            isInitializing = false; // Reset flag
        }
    }

    /**
     * Creates necessary database tables if they don't exist.
     */
    private void createTables() {
        try (Statement stmt = connection.createStatement()) {

            // Create drawings table
            stmt.execute(DatabaseConfig.CREATE_DRAWINGS_TABLE);
            safeLog("INFO", "Drawings table ready");

            // Create shapes table
            stmt.execute(DatabaseConfig.CREATE_SHAPES_TABLE);
            safeLog("INFO", "Shapes table ready");

            // Create logs table
            stmt.execute(DatabaseConfig.CREATE_LOGS_TABLE);
            safeLog("INFO", "Logs table ready");

        } catch (SQLException e) {
            safeLog("ERROR", "Failed to create database tables: " + e.getMessage());
            throw new RuntimeException("Table creation failed", e);
        }
    }

    /**
     * Gets the current database connection.
     *
     * @return The database connection
     * @throws SQLException if connection is not available
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            initializeDatabase();
        }
        return connection;
    }

    /**
     * Tests the database connection.
     *
     * @return true if connection is valid, false otherwise
     */
    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed() && connection.isValid(5);
        } catch (SQLException e) {
            safeLog("ERROR", "Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets a list of all saved drawing names.
     *
     * @return List of drawing names
     */
    public List<String> getDrawingNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT name FROM " + DatabaseConfig.DRAWINGS_TABLE + " ORDER BY modified_date DESC";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                names.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            safeLog("ERROR", "Failed to retrieve drawing names: " + e.getMessage());
        }

        return names;
    }

    /**
     * Checks if a drawing with the given name exists.
     *
     * @param name The drawing name to check
     * @return true if drawing exists, false otherwise
     */
    public boolean drawingExists(String name) {
        String sql = "SELECT COUNT(*) FROM " + DatabaseConfig.DRAWINGS_TABLE + " WHERE name = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            logger.error("Failed to check if drawing exists: " + e.getMessage());
            return false;
        }
    }

    /**
     * Saves a drawing to the database.
     *
     * @param drawing The drawing to save
     * @return true if saved successfully, false otherwise
     */
    public boolean saveDrawing(paintapp.model.Drawing drawing) {
        if (drawing == null || drawing.getName() == null || drawing.getName().trim().isEmpty()) {
            logger.error("Cannot save drawing: invalid drawing or name");
            return false;
        }

        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Check if drawing already exists
            int drawingId = getDrawingId(drawing.getName());

            if (drawingId > 0) {
                // Update existing drawing
                updateDrawing(conn, drawingId, drawing);
            } else {
                // Insert new drawing
                drawingId = insertDrawing(conn, drawing);
                drawing.setId(drawingId);
            }

            // Delete existing shapes for this drawing
            deleteShapesForDrawing(conn, drawingId);

            // Insert shapes
            insertShapes(conn, drawingId, drawing.getShapes());

            conn.commit(); // Commit transaction
            logger.info("Drawing saved successfully: " + drawing.getName());
            return true;

        } catch (SQLException e) {
            logger.error("Failed to save drawing: " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                logger.error("Failed to rollback transaction: " + rollbackEx.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Failed to reset auto-commit: " + e.getMessage());
            }
        }
    }

    /**
     * Loads a drawing from the database by name.
     *
     * @param drawingName The name of the drawing to load
     * @return The loaded drawing, or null if not found
     */
    public paintapp.model.Drawing loadDrawing(String drawingName) {
        if (drawingName == null || drawingName.trim().isEmpty()) {
            logger.error("Cannot load drawing: invalid name");
            return null;
        }

        String sql = "SELECT * FROM " + DatabaseConfig.DRAWINGS_TABLE + " WHERE name = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, drawingName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    paintapp.model.Drawing drawing = new paintapp.model.Drawing(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getTimestamp("created_date").toLocalDateTime(),
                            rs.getTimestamp("modified_date").toLocalDateTime(),
                            rs.getString("drawing_data"),
                            rs.getDouble("canvas_width"),
                            rs.getDouble("canvas_height")
                    );

                    // Load shapes
                    List<paintapp.model.Shape> shapes = loadShapesForDrawing(drawing.getId());
                    drawing.setShapes(shapes);

                    logger.info("Drawing loaded successfully: " + drawingName);
                    return drawing;
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to load drawing: " + e.getMessage());
        }

        logger.warning("Drawing not found: " + drawingName);
        return null;
    }

    /**
     * Deletes a drawing from the database.
     *
     * @param drawingName The name of the drawing to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteDrawing(String drawingName) {
        if (drawingName == null || drawingName.trim().isEmpty()) {
            logger.error("Cannot delete drawing: invalid name");
            return false;
        }

        String sql = "DELETE FROM " + DatabaseConfig.DRAWINGS_TABLE + " WHERE name = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, drawingName);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("Drawing deleted successfully: " + drawingName);
                return true;
            } else {
                logger.warning("Drawing not found for deletion: " + drawingName);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Failed to delete drawing: " + e.getMessage());
            return false;
        }
    }

    // Helper methods for database operations

    /**
     * Gets the ID of a drawing by name.
     *
     * @param drawingName The drawing name
     * @return The drawing ID, or -1 if not found
     */
    private int getDrawingId(String drawingName) {
        String sql = "SELECT id FROM " + DatabaseConfig.DRAWINGS_TABLE + " WHERE name = ?";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setString(1, drawingName);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() ? rs.getInt("id") : -1;
            }

        } catch (SQLException e) {
            logger.error("Failed to get drawing ID: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Updates an existing drawing in the database.
     *
     * @param conn      The database connection
     * @param drawingId The drawing ID
     * @param drawing   The drawing data
     * @throws SQLException if update fails
     */
    private void updateDrawing(Connection conn, int drawingId, paintapp.model.Drawing drawing) throws SQLException {
        String sql = "UPDATE " + DatabaseConfig.DRAWINGS_TABLE +
                " SET modified_date = CURRENT_TIMESTAMP, drawing_data = ?, canvas_width = ?, canvas_height = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, drawing.getDrawingData());
            pstmt.setDouble(2, drawing.getCanvasWidth());
            pstmt.setDouble(3, drawing.getCanvasHeight());
            pstmt.setInt(4, drawingId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Inserts a new drawing into the database.
     *
     * @param conn    The database connection
     * @param drawing The drawing data
     * @return The generated drawing ID
     * @throws SQLException if insert fails
     */
    private int insertDrawing(Connection conn, paintapp.model.Drawing drawing) throws SQLException {
        String sql = "INSERT INTO " + DatabaseConfig.DRAWINGS_TABLE +
                " (name, drawing_data, canvas_width, canvas_height) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, drawing.getName());
            pstmt.setString(2, drawing.getDrawingData());
            pstmt.setDouble(3, drawing.getCanvasWidth());
            pstmt.setDouble(4, drawing.getCanvasHeight());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    throw new SQLException("Failed to get generated drawing ID");
                }
            }
        }
    }

    /**
     * Deletes all shapes for a drawing.
     *
     * @param conn      The database connection
     * @param drawingId The drawing ID
     * @throws SQLException if delete fails
     */
    private void deleteShapesForDrawing(Connection conn, int drawingId) throws SQLException {
        String sql = "DELETE FROM " + DatabaseConfig.SHAPES_TABLE + " WHERE drawing_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, drawingId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Inserts shapes for a drawing.
     *
     * @param conn      The database connection
     * @param drawingId The drawing ID
     * @param shapes    The list of shapes
     * @throws SQLException if insert fails
     */
    private void insertShapes(Connection conn, int drawingId, List<paintapp.model.Shape> shapes) throws SQLException {
        if (shapes == null || shapes.isEmpty()) {
            return;
        }

        String sql = "INSERT INTO " + DatabaseConfig.SHAPES_TABLE +
                " (drawing_id, shape_type, x1, y1, x2, y2, color, is_filled, shape_order) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (paintapp.model.Shape shape : shapes) {
                pstmt.setInt(1, drawingId);
                pstmt.setString(2, shape.getShapeType());
                pstmt.setDouble(3, shape.getX1());
                pstmt.setDouble(4, shape.getY1());
                pstmt.setDouble(5, shape.getX2());
                pstmt.setDouble(6, shape.getY2());
                pstmt.setString(7, shape.getColor());
                pstmt.setBoolean(8, shape.isFilled());
                pstmt.setInt(9, shape.getShapeOrder());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
    }

    /**
     * Loads shapes for a drawing.
     *
     * @param drawingId The drawing ID
     * @return List of shapes
     */
    private List<paintapp.model.Shape> loadShapesForDrawing(int drawingId) {
        List<paintapp.model.Shape> shapes = new ArrayList<>();
        String sql = "SELECT * FROM " + DatabaseConfig.SHAPES_TABLE + " WHERE drawing_id = ? ORDER BY shape_order";

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, drawingId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    paintapp.model.Shape shape = new paintapp.model.Shape(
                            rs.getInt("id"),
                            rs.getInt("drawing_id"),
                            rs.getString("shape_type"),
                            rs.getDouble("x1"),
                            rs.getDouble("y1"),
                            rs.getDouble("x2"),
                            rs.getDouble("y2"),
                            rs.getString("color"),
                            rs.getBoolean("is_filled"),
                            rs.getInt("shape_order")
                    );
                    shapes.add(shape);
                }
            }

        } catch (SQLException e) {
            logger.error("Failed to load shapes for drawing: " + e.getMessage());
        }

        return shapes;
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed");
            }
        } catch (SQLException e) {
            logger.error("Failed to close database connection: " + e.getMessage());
        }
    }
}
