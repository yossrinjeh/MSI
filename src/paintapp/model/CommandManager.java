package paintapp.model;

import java.util.Stack;
import paintapp.logging.LoggingManager;
import paintapp.observer.DrawingSubject;

public class CommandManager {
    private static CommandManager instance = null;
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();
    private LoggingManager logger;
    private DrawingSubject drawingSubject;

    private CommandManager() {
        this.logger = LoggingManager.getInstance();
        this.drawingSubject = new DrawingSubject();
        logger.info("CommandManager initialized with Observable pattern support");
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

        // Notify observers if it's a DrawCommand
        if (cmd instanceof DrawCommand) {
            drawingSubject.notifyShapeAdded((DrawCommand) cmd);
        }
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
            logger.debug("Command undone. Undo stack size: " + undoStack.size() +
                        ", Redo stack size: " + redoStack.size());

            // Notify observers
            if (cmd instanceof DrawCommand) {
                drawingSubject.notifyShapeRemoved((DrawCommand) cmd);
            }
            drawingSubject.notifyUndoPerformed(undoStack.size(), redoStack.size());
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

            // Notify observers
            if (cmd instanceof DrawCommand) {
                drawingSubject.notifyShapeAdded((DrawCommand) cmd);
            }
            drawingSubject.notifyRedoPerformed(undoStack.size(), redoStack.size());
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
     * Clears the command history and notifies observers.
     */
    public void clearHistory() {
        undoStack.clear();
        redoStack.clear();
        drawingSubject.notifyCanvasCleared();
        logger.info("Command history cleared");
    }

    /**
     * Gets the drawing subject for observer management.
     *
     * @return The drawing subject
     */
    public DrawingSubject getDrawingSubject() {
        return drawingSubject;
    }

    /**
     * Gets the current size of the undo stack.
     *
     * @return The undo stack size
     */
    public int getUndoStackSize() {
        return undoStack.size();
    }

    /**
     * Gets the current size of the redo stack.
     *
     * @return The redo stack size
     */
    public int getRedoStackSize() {
        return redoStack.size();
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




}
