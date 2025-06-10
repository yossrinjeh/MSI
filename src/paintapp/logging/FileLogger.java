package paintapp.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * File implementation of Logger interface.
 * Writes log messages to a text file with automatic file creation and append mode.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class FileLogger implements Logger {
    
    private static final String DEFAULT_LOG_FILE = "paintapp.log";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private final String logFileName;
    
    /**
     * Creates a FileLogger with default log file name.
     */
    public FileLogger() {
        this(DEFAULT_LOG_FILE);
    }
    
    /**
     * Creates a FileLogger with specified log file name.
     * 
     * @param logFileName The name of the log file
     */
    public FileLogger(String logFileName) {
        this.logFileName = logFileName;
    }
    
    /**
     * Logs a message to file with INFO level.
     * 
     * @param message The message to log
     */
    @Override
    public void log(String message) {
        log("INFO", message);
    }
    
    /**
     * Logs a message to file with specified level.
     * Format: [TIMESTAMP] [LEVEL] MESSAGE
     * 
     * @param level The log level
     * @param message The message to log
     */
    @Override
    public void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String formattedMessage = String.format("[%s] [%s] %s%n", 
            timestamp, level.toUpperCase(), message);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFileName, true))) {
            writer.print(formattedMessage);
        } catch (IOException e) {
            // Fallback to console if file writing fails
            System.err.println("Failed to write to log file: " + e.getMessage());
            System.err.println("Log message: " + formattedMessage.trim());
        }
    }
    
    /**
     * Gets the current log file name.
     * 
     * @return The log file name
     */
    public String getLogFileName() {
        return logFileName;
    }
}
