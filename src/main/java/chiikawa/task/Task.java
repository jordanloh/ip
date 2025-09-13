package chiikawa.task;

/**
 * Represents a Task object. A Task object consist
 * of a description, and a boolean marking whether
 * the task was done or not.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Initialises the task object.
     *
     * @param description String of the description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public String getIsDone() {
        return (isDone ? "1" : "0");
    }

    public void setAsDone() {
        this.isDone = true;
    }

    public void setAsUndone() {
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    public String saveFormat() {
        return "T | " + getIsDone() + " | " + description;
    }

    @Override
    public String toString() {
        return "[" + this.getStatusIcon() + "] " + this.description;
    }

    /**
     * Updates a single field. Throws UnsupportedOperationException if the field
     * cannot be applied to this task type.
     */
    public abstract void updateField(String key, String value);

    public void setDescription(String description) {
        this.description = description;
    }
}
