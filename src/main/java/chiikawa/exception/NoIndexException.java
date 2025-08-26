package chiikawa.exception;

public class NoIndexException extends ChiikawaException {

    public NoIndexException() {
        super("You have to provide an index for me to know which task to refer to! D:");
    }
}
