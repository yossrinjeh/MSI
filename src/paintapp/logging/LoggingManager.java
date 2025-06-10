package paintapp.logging;

/**
 * LoggingManager implements the Strategy pattern to manage different logging strategies.
 * Provides a centralized way to switch between Console, File, and Database logging.
 * Uses Singleton pattern to ensure consistent logging throughout the application.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class LoggingManager {
    
    private static LoggingManager instance;
    private Logger currentLogger;
    
    // Available logging strategies
    private final Logger consoleLogger;
    private final Logger fileLogger;
    private final Logger databaseLogger;
    
    /**
     * Private constructor for Singleton pattern.
     * Initializes all available loggers.
     */
    private LoggingManager() {
        this.consoleLogger = new ConsoleLogger();
        this.fileLogger = new FileLogger();
        this.databaseLogger = new DatabaseLogger();
        
        // Default to console logging
        this.currentLogger = consoleLogger;
    }
    
    /**
     * Gets the singleton instance of LoggingManager.
     * 
     * @return The LoggingManager instance
     */
    public static LoggingManager getInstance() {
        if (instance == null) {
            instance = new LoggingManager();
        }
        return instance;
    }
    
    /**
     * Sets the current logging strategy.
     * 
     * @param logger The logger to use
     */
    public void setLogger(Logger logger) {
        if (logger != null) {
            this.currentLogger = logger;
            currentLogger.info("Logging strategy changed to: " + logger.getClass().getSimpleName());
        }
    }
    
    /**
     * Sets logging strategy to console.
     */
    public void useConsoleLogging() {
        setLogger(consoleLogger);
    }
    
    /**
     * Sets logging strategy to file.
     */
    public void useFileLogging() {
        setLogger(fileLogger);
    }
    
    /**
     * Sets logging strategy to database.
     */
    public void useDatabaseLogging() {
        setLogger(databaseLogger);
    }
    
    /**
     * Logs a message using the current strategy.
     * 
     * @param message The message to log
     */
    public void log(String message) {
        currentLogger.log(message);
    }
    
    /**
     * Logs a message with specified level using the current strategy.
     * 
     * @param level The log level
     * @param message The message to log
     */
    public void log(String level, String message) {
        currentLogger.log(level, message);
    }
    
    /**
     * Logs an info message.
     * 
     * @param message The info message
     */
    public void info(String message) {
        currentLogger.info(message);
    }
    
    /**
     * Logs a warning message.
     * 
     * @param message The warning message
     */
    public void warning(String message) {
        currentLogger.warning(message);
    }
    
    /**
     * Logs an error message.
     * 
     * @param message The error message
     */
    public void error(String message) {
        currentLogger.error(message);
    }
    
    /**
     * Logs a debug message.
     * 
     * @param message The debug message
     */
    public void debug(String message) {
        currentLogger.debug(message);
    }
    
    /**
     * Gets the current logger being used.
     * 
     * @return The current logger
     */
    public Logger getCurrentLogger() {
        return currentLogger;
    }
}
