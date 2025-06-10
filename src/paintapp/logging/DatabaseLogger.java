package paintapp.logging;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Database implementation of Logger interface.
 * Stores log messages in a SQLite database table.
 * Creates the logs table automatically if it doesn't exist.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class DatabaseLogger implements Logger {
    
    private static final String DEFAULT_DB_NAME = "paintapp.db";
    private static final String LOGS_TABLE_SQL = """
        CREATE TABLE IF NOT EXISTS logs (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
            level VARCHAR(10) NOT NULL,
            message TEXT NOT NULL
        )
        """;
    
    private final String databaseUrl;
    
    /**
     * Creates a DatabaseLogger with default database name.
     */
    public DatabaseLogger() {
        this(DEFAULT_DB_NAME);
    }
    
    /**
     * Creates a DatabaseLogger with specified database name.
     * 
     * @param databaseName The name of the SQLite database file
     */
    public DatabaseLogger(String databaseName) {
        this.databaseUrl = "jdbc:sqlite:" + databaseName;
        initializeDatabase();
    }
    
    /**
     * Initializes the database and creates the logs table if needed.
     */
    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(LOGS_TABLE_SQL);
            
        } catch (SQLException e) {
            System.err.println("Failed to initialize database for logging: " + e.getMessage());
        }
    }
    
    /**
     * Logs a message to database with INFO level.
     * 
     * @param message The message to log
     */
    @Override
    public void log(String message) {
        log("INFO", message);
    }
    
    /**
     * Logs a message to database with specified level.
     * 
     * @param level The log level
     * @param message The message to log
     */
    @Override
    public void log(String level, String message) {
        String sql = "INSERT INTO logs (level, message) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, level.toUpperCase());
            pstmt.setString(2, message);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            // Fallback to console if database logging fails
            System.err.println("Failed to log to database: " + e.getMessage());
            System.err.println("Log message: [" + level.toUpperCase() + "] " + message);
        }
    }
    
    /**
     * Gets the database URL being used.
     * 
     * @return The database URL
     */
    public String getDatabaseUrl() {
        return databaseUrl;
    }
}
