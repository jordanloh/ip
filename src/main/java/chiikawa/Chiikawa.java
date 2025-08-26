package chiikawa;

import java.nio.file.Path;
import java.time.format.DateTimeParseException;

import chiikawa.exception.ChiikawaException;
import chiikawa.exception.EmptyDescriptionException;
import chiikawa.exception.IndexOutOfBoundException;
import chiikawa.exception.ListEmptyException;
import chiikawa.exception.NoDeadlineException;
import chiikawa.exception.NoEventException;
import chiikawa.exception.NoIndexException;
import chiikawa.task.Deadline;
import chiikawa.task.Event;
import chiikawa.task.Task;
import chiikawa.task.Todo;

/**
 * Represents the main Chiikawa chatbot.
 */
public class Chiikawa {
    private Ui ui = new Ui();
    private TaskList tasks;
    private Storage storage;

    /**
     * Initialises the Chiikawa object.
     *
     * @param filePath Directory to specify where to store the tasks.
     */
    public Chiikawa(Path filePath) {
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Runs the chatbot.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();

            try {
                Command command = Parser.parseCommand(input);
                String args = Parser.getCommandArgs(input);

                switch (command) {
                case bye -> {
                    ui.showGoodbye();
                    ui.close();
                    return;
                }
                case list -> listTasks();
                case mark -> {
                    if (args.isBlank()) {
                        throw new NoIndexException();
                    }
                    markTask(Integer.parseInt(args) - 1);
                }
                case unmark -> {
                    if (args.isBlank()) {
                        throw new NoIndexException();
                    }
                    unmarkTask(Integer.parseInt(args) - 1);
                }
                case delete -> {
                    if (args.isBlank()) {
                        throw new NoIndexException();
                    }
                    deleteTask(Integer.parseInt(args) - 1);
                }
                case todo -> {
                    if (args.isBlank()) {
                        throw new EmptyDescriptionException();
                    }
                    addTodo(args);
                }
                case deadline -> {
                    if (args.isBlank()) {
                        throw new EmptyDescriptionException();
                    }
                    addDeadline(args);
                }
                case event -> {
                    if (args.isBlank()) {
                        throw new EmptyDescriptionException();
                    }
                    addEvent(args);
                }
                default -> throw new ChiikawaException("Oh no! I don't recognise that command :(!");
                }
            } catch (ChiikawaException e) {
                ui.showMessage(e.getMessage());
            }
            ui.showDivider();
        }
    }

    /**
     * Main driver to start the program.
     *
     * @param args Necessary CLI arguments.
     */
    public static void main(String[] args) {
        Path path = java.nio.file.Paths.get("data", "chiikawa.Chiikawa.txt");
        Chiikawa chiikawa = new Chiikawa(path);
        chiikawa.run();
    }

    /**
     * Lists the tasks currently in the TaskList.
     *
     * @throws ChiikawaException If the list is empty.
     */
    public void listTasks() throws ChiikawaException {
        if (tasks.size() == 0) {
            throw new ListEmptyException();
        }
        ui.showListTasks(tasks);
    }

    /**
     * Marks the task with the given index in the list as done.
     *
     * @param index Index of the task to be marked as done.
     * @throws ChiikawaException If the index provided is out of bounds.
     */
    public void markTask(int index) throws ChiikawaException {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundException();
        }
        tasks.getTask(index).markAsDone();
        storage.save(tasks.getAllTasks());
        ui.showTaskMarked(tasks.getTask(index));
    }

    /**
     * Unmarks the task with the given index in the list as done.
     *
     * @param index Index of the task to be unmarked as done.
     * @throws ChiikawaException If the index provided is out of bounds.
     */
    public void unmarkTask(int index) throws ChiikawaException {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundException();
        }
        tasks.getTask(index).markAsUndone();
        storage.save(tasks.getAllTasks());
        ui.showTaskUnmarked(tasks.getTask(index));
    }

    /**
     * Adds a todo task to task list.
     *
     * @param description Description of the todo task.
     */
    public void addTodo(String description) {
        tasks.addTask(new Todo(description));
        storage.save(tasks.getAllTasks());
        ui.showTaskAdded(tasks.getTask(tasks.size() - 1), tasks.size());
    }

    /**
     * Adds a deadline task to the task list.
     *
     * @param input String containing the description and deadline of the task.
     * @throws ChiikawaException If the provided arguments are incorrect.
     */
    public void addDeadline(String input) throws ChiikawaException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new NoDeadlineException();
        }
        try {
            tasks.addTask(new Deadline(parts[0], Parser.parseDateTime(parts[1])));
        } catch (DateTimeParseException e) {
            ui.showMessage("Invalid date/time format! Please use yyyy-MM-dd HHmm, e.g. 2019-12-25 1800");
            return;
        }
        storage.save(tasks.getAllTasks());
        ui.showTaskAdded(tasks.getTask(tasks.size() - 1), tasks.size());
    }

    /**
     * Adds an event task to the task list.
     *
     * @param input String containing the description, start and end datetime of the task.
     * @throws ChiikawaException If the provided arguments are incorrect.
     */
    public void addEvent(String input) throws ChiikawaException {
        String[] parts = input.split(" /from | /to ", 3);
        if (parts.length < 3 || parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
            throw new NoEventException();
        }
        try {
            tasks.addTask(new Event(parts[0], Parser.parseDateTime(parts[1]), Parser.parseDateTime(parts[2])));
        } catch (DateTimeParseException e) {
            ui.showMessage("Invalid date/time format! Please use yyyy-MM-dd HHmm, e.g. 2019-12-25 1800");
            return;
        }
        storage.save(tasks.getAllTasks());
        ui.showTaskAdded(tasks.getTask(tasks.size() - 1), tasks.size());
    }

    /**
     * Deletes the task with the given index from the task list.
     *
     * @param index Index of the task to be deleted from the task list.
     * @throws ChiikawaException If the index provided is out of bounds.
     */
    public void deleteTask(int index) throws ChiikawaException {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundException();
        }
        Task task = tasks.getTask(index);
        tasks.deleteTask(index);
        storage.save(tasks.getAllTasks());
        ui.showTaskRemoved(task, tasks.size());
    }
}