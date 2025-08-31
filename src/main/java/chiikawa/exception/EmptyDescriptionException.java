package chiikawa.exception;

/**
 * Represents an exception when trying to create a task object with an empty descrption.
 */
public class EmptyDescriptionException extends ChiikawaException {

    public EmptyDescriptionException() {
        super("The description of a task cannot be empty! Add something!");
    }
}
