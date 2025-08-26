package chiikawa.exception;

public class ListEmptyException extends ChiikawaException {

    public ListEmptyException() {
        super("Oh no! You have no tasks to list now!");
    }
}
