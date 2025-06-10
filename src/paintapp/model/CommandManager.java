package paintapp.model;

import java.util.Stack;
import paintapp.logging.LoggingManager;

public class CommandManager {
    private static CommandManager instance = null;
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();
    private LoggingManager logger;

    private CommandManager() {
        this.logger = LoggingManager.getInstance();
        logger.info("CommandManager initialized");
    }

    public static CommandManager getInstance() {
        if (instance == null)
            instance = new CommandManager();
        return instance;
    }

    public void addCommand(Command cmd) {
        undoStack.push(cmd);
        redoStack.clear();
        logger.debug("Command added to history. Undo stack size: " + undoStack.size());
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
            logger.debug("Command undone. Undo stack size: " + undoStack.size() +
                        ", Redo stack size: " + redoStack.size());
        } else {
            logger.warning("Undo requested but no commands available to undo");
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            undoStack.push(cmd);
            logger.debug("Command redone. Undo stack size: " + undoStack.size() +
                        ", Redo stack size: " + redoStack.size());
        } else {
            logger.warning("Redo requested but no commands available to redo");
        }
    }

    public void replayAllExcept(Command exclude) {
        for (Command cmd : undoStack) {
            if (cmd != exclude) cmd.execute();
        }
    }
}
