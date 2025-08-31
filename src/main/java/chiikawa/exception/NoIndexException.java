package chiikawa.exception;

/**
 * Represents an exception thrown when no index is provided when using the delete,
 * mark or unmark commands.
 */
public class NoIndexException extends ChiikawaException {

    public NoIndexException() {
        super("You have to provide an index for me to know which task to refer to! D:");
    }
}
