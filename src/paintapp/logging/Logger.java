package paintapp.logging;

/**
 * Logger interface defining the contract for all logging implementations.
 * Uses Strategy pattern to allow switching between different logging methods.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public interface Logger {
    
    /**
     * Logs a message with default INFO level.
     * 
     * @param message The message to log
     */
    void log(String message);
    
    /**
     * Logs a message with specified level.
     * 
     * @param level The log level (INFO, WARNING, ERROR, DEBUG)
     * @param message The message to log
     */
    void log(String level, String message);
    
    /**
     * Logs an info message.
     * 
     * @param message The info message to log
     */
    default void info(String message) {
        log("INFO", message);
    }
    
    /**
     * Logs a warning message.
     * 
     * @param message The warning message to log
     */
    default void warning(String message) {
        log("WARNING", message);
    }
    
    /**
     * Logs an error message.
     * 
     * @param message The error message to log
     */
    default void error(String message) {
        log("ERROR", message);
    }
    
    /**
     * Logs a debug message.
     * 
     * @param message The debug message to log
     */
    default void debug(String message) {
        log("DEBUG", message);
    }
}
