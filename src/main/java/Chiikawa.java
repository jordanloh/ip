import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.nio.file.Path;

public class Chiikawa {
    private Ui ui = new Ui();
    private ArrayList<Task> arr;
    private Storage storage;

    public Chiikawa(Path filePath) {
        storage = new Storage(filePath);
        arr = storage.load();
    }

    public void run() {
        Path path = java.nio.file.Paths.get( "data", "Chiikawa.txt");
        Chiikawa chiikawa = new Chiikawa(path);

        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            String[] parts = input.split(" ", 2);
            Command command;
            ui.showDivider();

            try {
                try {
                    command = Command.valueOf(parts[0].toLowerCase());
                } catch (IllegalArgumentException e) {
                    ui.showMessage("Oh no! I don't recognise that command :(!");
                    ui.showDivider();
                    continue;
                }
                if (command == Command.bye) {
                    ui.showMessage("Byebye!! See you again soon nya~!");
                    ui.showDivider();
                    break;
                }
                switch (command) {
                case list:
                    chiikawa.listTasks();
                    break;
                case mark:
                    if (parts.length < 2) {
                        throw new NoIndexException();
                    }
                    chiikawa.markTask(Integer.parseInt(parts[1]) - 1);
                    break;
                case unmark:
                    if (parts.length < 2) {
                        throw new NoIndexException();
                    }
                    chiikawa.unmarkTask(Integer.parseInt(parts[1]) - 1);
                    break;
                case delete:
                    if (parts.length < 2) {
                        throw new NoIndexException();
                    }
                    chiikawa.deleteTask(Integer.parseInt(parts[1]) - 1);
                    break;
                case todo:
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new EmptyDescriptionException();
                    }
                    chiikawa.addTodo(parts[1].trim());
                    break;
                case deadline:
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new EmptyDescriptionException();
                    }
                    chiikawa.addDeadline(parts[1].trim());
                    break;
                case event:
                    if (parts.length < 2 || parts[1].isBlank()) {
                        throw new EmptyDescriptionException();
                    }
                    chiikawa.addEvent(parts[1].trim());
                    break;
                default:
                    throw new ChiikawaException("Oh no! I don't recognise that command :(!");
                }
            } catch (ChiikawaException e) {
                ui.showMessage(e.getMessage());
            }
            ui.showDivider();
        }

        ui.close();
    }
    public static void main(String[] args) {
        Path path = java.nio.file.Paths.get( "data", "Chiikawa.txt");
        Chiikawa chiikawa = new Chiikawa(path);
        chiikawa.run();
    }

    public void listTasks() throws ChiikawaException {
        if (arr.isEmpty()) {
            throw new ListEmptyException();
        }
        ui.showListTasks(arr);
    }

    public void markTask(int index) throws ChiikawaException {
        if (index < 0 || index >= arr.size()) {
            throw new IndexOutOfBoundException();
        }
        arr.get(index).markAsDone();
        storage.save(arr);
        ui.showTaskMarked(arr.get(index));
    }

    public void unmarkTask(int index) throws ChiikawaException {
        if (index < 0 || index >= arr.size()) {
            throw new IndexOutOfBoundException();
        }
        arr.get(index).markAsUndone();
        storage.save(arr);
        ui.showTaskUnmarked(arr.get(index));
    }

    public void addTodo(String description) {
        arr.add(new Todo(description));
        storage.save(arr);
        ui.showTaskAdded(arr.get(arr.size() - 1), arr.size());
    }

    public void addDeadline(String input) throws ChiikawaException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new NoDeadlineException();
        }
        try {
            arr.add(new Deadline(parts[0], Parser.parseDateTime(parts[1])));
        } catch (DateTimeParseException e) {
            ui.showMessage("Invalid date/time format! Please use yyyy-MM-dd HHmm, e.g. 2019-12-25 1800");
            return;
        }
        storage.save(arr);
        ui.showTaskAdded(arr.get(arr.size() - 1), arr.size());
    }

    public void addEvent(String input) throws ChiikawaException {
        String[] parts = input.split(" /from | /to ", 3);
        if (parts.length < 3 || parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
            throw new NoEventException();
        }
        try {
            arr.add(new Event(parts[0], Parser.parseDateTime(parts[1]), Parser.parseDateTime(parts[2])));
        } catch (DateTimeParseException e) {
            ui.showMessage("Invalid date/time format! Please use yyyy-MM-dd HHmm, e.g. 2019-12-25 1800");
            return;
        }
        storage.save(arr);
        ui.showTaskAdded(arr.get(arr.size() - 1), arr.size());
    }

    public void deleteTask(int index) throws ChiikawaException {
        if (index < 0 || index >= arr.size()) {
            throw new IndexOutOfBoundException();
        }
        Task task = arr.get(index);
        arr.remove(index);
        storage.save(arr);
        ui.showTaskRemoved(task, arr.size());
    }
}