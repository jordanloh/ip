package chiikawa.exception;

/**
 * Represents an exception when the index selected does not correspond to
 * an element within the current list.
 */
public class IndexOutOfBoundException extends ChiikawaException {

    public IndexOutOfBoundException() {
        super("No such task exist in the list!");
    }
}
