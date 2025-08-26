package chiikawa.exception;

public class NoDeadlineException extends ChiikawaException {

    public NoDeadlineException() {
        super("How can a deadline task not have a deadline?! Add a deadline!");
    }
}
