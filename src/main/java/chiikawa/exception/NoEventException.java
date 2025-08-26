package chiikawa.exception;

public class NoEventException extends ChiikawaException {

    public NoEventException() {
        super("You need to provide the start and end time of the event!");
    }
}
