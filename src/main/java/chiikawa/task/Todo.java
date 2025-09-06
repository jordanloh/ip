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

    @Override
    public void updateField(String key, String value) {
        if ("/description".equals(key)) {
            setDescription(value);
        } else {
            throw new UnsupportedOperationException("Todo cannot have field " + key);
        }
    }
}
