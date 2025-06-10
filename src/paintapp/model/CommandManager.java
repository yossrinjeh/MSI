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

    /**
     * Gets a copy of the undo stack for serialization purposes.
     *
     * @return A copy of the undo stack
     */
    public Stack<Command> getUndoStack() {
        return new Stack<Command>() {{
            addAll(undoStack);
        }};
    }

    /**
     * Clears all command history.
     */
    public void clearHistory() {
        undoStack.clear();
        redoStack.clear();
        logger.info("Command history cleared");
    }

    /**
     * Gets the number of commands in the undo stack.
     *
     * @return The number of undoable commands
     */
    public int getUndoStackSize() {
        return undoStack.size();
    }

    /**
     * Gets the number of commands in the redo stack.
     *
     * @return The number of redoable commands
     */
    public int getRedoStackSize() {
        return redoStack.size();
    }
}
