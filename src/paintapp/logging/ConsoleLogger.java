package paintapp.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Console implementation of Logger interface.
 * Prints log messages to System.out with timestamp and level formatting.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class ConsoleLogger implements Logger {
    
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Logs a message to console with INFO level.
     * 
     * @param message The message to log
     */
    @Override
    public void log(String message) {
        log("INFO", message);
    }
    
    /**
     * Logs a message to console with specified level.
     * Format: [TIMESTAMP] [LEVEL] MESSAGE
     * 
     * @param level The log level
     * @param message The message to log
     */
    @Override
    public void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String formattedMessage = String.format("[%s] [%s] %s", 
            timestamp, level.toUpperCase(), message);
        
        // Use different output streams based on level
        if ("ERROR".equalsIgnoreCase(level) || "WARNING".equalsIgnoreCase(level)) {
            System.err.println(formattedMessage);
        } else {
            System.out.println(formattedMessage);
        }
    }
}
