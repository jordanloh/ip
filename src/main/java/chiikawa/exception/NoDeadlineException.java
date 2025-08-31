package chiikawa.exception;

/**
 * Represents an exception thrown when a deadline is not provided when trying to
 * create a deadline task.
 */
public class NoDeadlineException extends ChiikawaException {

    public NoDeadlineException() {
        super("How can a deadline task not have a deadline?! Add a deadline!");
    }
}
