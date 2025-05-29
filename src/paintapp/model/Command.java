package paintapp.model;

public interface Command {
    void execute();
    void undo();
}
