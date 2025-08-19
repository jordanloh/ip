public class ListEmptyException extends ChiikawaException {

    ListEmptyException() {
        super("Oh no! You have no tasks to list now!");
    }
}
