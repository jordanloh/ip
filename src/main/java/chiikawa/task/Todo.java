package chiikawa.task;

/**
 * Represents a Todo task. Consist of a description
 * of the todo task.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
