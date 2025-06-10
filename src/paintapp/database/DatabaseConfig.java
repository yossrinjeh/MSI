package paintapp.database;

/**
 * Database configuration class containing MySQL connection parameters.
 * Centralizes database configuration for easy maintenance.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class DatabaseConfig {
    
    // MySQL connection parameters
    public static final String HOST = "localhost";
    public static final int PORT = 3306;
    public static final String DATABASE = "msi";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    
    // JDBC URL construction
    public static final String JDBC_URL = String.format(
        "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
        HOST, PORT, DATABASE
    );
    
    // Database table names
    public static final String DRAWINGS_TABLE = "drawings";
    public static final String SHAPES_TABLE = "shapes";
    public static final String LOGS_TABLE = "logs";
    
    // SQL statements for table creation
    public static final String CREATE_DRAWINGS_TABLE =
        "CREATE TABLE IF NOT EXISTS drawings (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "name VARCHAR(255) NOT NULL UNIQUE," +
        "created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
        "drawing_data TEXT," +
        "canvas_width DOUBLE DEFAULT 800," +
        "canvas_height DOUBLE DEFAULT 600" +
        ")";

    public static final String CREATE_SHAPES_TABLE =
        "CREATE TABLE IF NOT EXISTS shapes (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "drawing_id INT NOT NULL," +
        "shape_type VARCHAR(50) NOT NULL," +
        "x1 DOUBLE NOT NULL," +
        "y1 DOUBLE NOT NULL," +
        "x2 DOUBLE NOT NULL," +
        "y2 DOUBLE NOT NULL," +
        "color VARCHAR(20) NOT NULL," +
        "is_filled BOOLEAN DEFAULT FALSE," +
        "shape_order INT NOT NULL," +
        "FOREIGN KEY (drawing_id) REFERENCES drawings(id) ON DELETE CASCADE" +
        ")";

    public static final String CREATE_LOGS_TABLE =
        "CREATE TABLE IF NOT EXISTS logs (" +
        "id INT AUTO_INCREMENT PRIMARY KEY," +
        "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
        "level VARCHAR(10) NOT NULL," +
        "message TEXT NOT NULL" +
        ")";
    
    /**
     * Private constructor to prevent instantiation.
     */
    private DatabaseConfig() {
        // Utility class - no instantiation
    }
}
