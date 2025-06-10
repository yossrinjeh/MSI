package paintapp.logging;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Database implementation of Logger interface.
 * Stores log messages in a MySQL database table.
 * Uses lazy initialization to avoid circular dependencies.
 *
 * @author JavaFX Paint App
 * @version 1.0
 */
public class DatabaseLogger implements Logger {

    private paintapp.database.DatabaseManager databaseManager;

    /**
     * Creates a DatabaseLogger with lazy initialization.
     */
    public DatabaseLogger() {
        // Lazy initialization to avoid circular dependency
        this.databaseManager = null;
    }

    /**
     * Gets the database manager, initializing it if necessary.
     *
     * @return The database manager
     */
    private paintapp.database.DatabaseManager getDatabaseManager() {
        if (databaseManager == null) {
            try {
                databaseManager = paintapp.database.DatabaseManager.getInstance();
            } catch (Exception e) {
                // If database manager fails to initialize, we can't log to database
                System.err.println("DatabaseLogger: Failed to initialize database manager: " + e.getMessage());
                return null;
            }
        }
        return databaseManager;
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
        paintapp.database.DatabaseManager dbManager = getDatabaseManager();
        if (dbManager == null) {
            // Fallback to console if database manager is not available
            System.err.println("DatabaseLogger: Database not available, falling back to console");
            System.err.println("Log message: [" + level.toUpperCase() + "] " + message);
            return;
        }

        String sql = "INSERT INTO " + paintapp.database.DatabaseConfig.LOGS_TABLE + " (level, message) VALUES (?, ?)";

        try (Connection conn = dbManager.getConnection();
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
     * Gets the database manager being used.
     *
     * @return The database manager
     */

}
