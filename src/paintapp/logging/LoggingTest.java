package paintapp.logging;

/**
 * Test class to demonstrate the logging system functionality.
 * This class shows how to use different logging strategies.
 * 
 * @author JavaFX Paint App
 * @version 1.0
 */
public class LoggingTest {
    
    public static void main(String[] args) {
        LoggingManager loggingManager = LoggingManager.getInstance();
        
        System.out.println("=== Testing Console Logging ===");
        loggingManager.useConsoleLogging();
        testLogging(loggingManager);
        
        System.out.println("\n=== Testing File Logging ===");
        loggingManager.useFileLogging();
        testLogging(loggingManager);
        System.out.println("Check 'paintapp.log' file for logged messages");
        
        System.out.println("\n=== Testing Database Logging ===");
        loggingManager.useDatabaseLogging();
        testLogging(loggingManager);
        System.out.println("Check 'paintapp.db' database for logged messages");
        
        System.out.println("\n=== Testing Individual Loggers ===");
        testIndividualLoggers();
    }
    
    private static void testLogging(LoggingManager logger) {
        logger.info("This is an info message");
        logger.warning("This is a warning message");
        logger.error("This is an error message");
        logger.debug("This is a debug message");
        logger.log("This is a default log message");
        logger.log("CUSTOM", "This is a custom level message");
    }
    
    private static void testIndividualLoggers() {
        System.out.println("\n--- Console Logger ---");
        Logger consoleLogger = new ConsoleLogger();
        consoleLogger.info("Direct console logging test");
        
        System.out.println("\n--- File Logger ---");
        Logger fileLogger = new FileLogger("test.log");
        fileLogger.info("Direct file logging test");
        System.out.println("Check 'test.log' file");
        
        System.out.println("\n--- Database Logger ---");
        Logger dbLogger = new DatabaseLogger("test.db");
        dbLogger.info("Direct database logging test");
        System.out.println("Check 'test.db' database");
    }
}
