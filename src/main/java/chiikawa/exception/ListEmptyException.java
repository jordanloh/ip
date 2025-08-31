package chiikawa.exception;

/**
 * Represents an exception thrown when trying to list tasks
 * when there is nothing in the list.
 */
public class ListEmptyException extends ChiikawaException {

    public ListEmptyException() {
        super("Oh no! You have no tasks to list now!");
    }
}
