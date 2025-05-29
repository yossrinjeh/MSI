package paintapp.model;

import java.util.Stack;

public class CommandManager {
    private static CommandManager instance = null;
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    private CommandManager() {}

    public static CommandManager getInstance() {
        if (instance == null)
            instance = new CommandManager();
        return instance;
    }

    public void addCommand(Command cmd) {
        undoStack.push(cmd);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            undoStack.push(cmd);
        }
    }

    public void replayAllExcept(Command exclude) {
        for (Command cmd : undoStack) {
            if (cmd != exclude) cmd.execute();
        }
    }
}
