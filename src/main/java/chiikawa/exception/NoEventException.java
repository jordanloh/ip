package chiikawa.exception;

/**
 * Represents an exception thrown when trying to create an event task without
 * a start and/or end time to the task.
 */
public class NoEventException extends ChiikawaException {

    public NoEventException() {
        super("You need to provide the start and end time of the event!");
    }
}
