package chiikawa.exception;

public class EmptyDescriptionException extends ChiikawaException {

    public EmptyDescriptionException() {
        super("The description of a task cannot be empty! Add something!");
    }
}
