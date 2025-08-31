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
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;

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
     * Initiliases the Chiikawa object with the default directory.
     */
    public Chiikawa() {
        Path path = java.nio.file.Paths.get("data", "chiikawa.Chiikawa.txt");
        storage = new Storage(path);
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
                case find -> findTask(args);
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

    /**
     * Finds all tasks with the given keywords from the task list.
     *
     * @param args String containing the keywords to find from task list.
     */
    public void findTask(String args) {
        ui.showFindMatchingTaskMessage();
        int count = 1;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.getTask(i).getDescription().contains(args)) {
                ui.showMessage(count + "." + tasks.getTask(i));
                count++;
            }
        }
        if (count == 1) {
            ui.showNoMatchFoundMessage();
        }
    }

    public String getResponse(String input) {
        StringBuilder response = new StringBuilder();

        try {
            Command command = Parser.parseCommand(input);
            String args = Parser.getCommandArgs(input);

            switch (command) {
            case bye -> {
                response.append("Goodbye! Hope to see you again soon!");
                PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
                delay.setOnFinished(event -> Platform.exit());
                delay.play();
            }
            case list -> response.append(listTasksAsString());
            case mark -> {
                if (args.isBlank()) {
                    throw new NoIndexException();
                }
                response.append(markTaskAsString(Integer.parseInt(args) - 1));
            }
            case unmark -> {
                if (args.isBlank()) {
                    throw new NoIndexException();
                }
                response.append(unmarkTaskAsString(Integer.parseInt(args) - 1));
            }
            case delete -> {
                if (args.isBlank()) {
                    throw new NoIndexException();
                }
                response.append(deleteTaskAsString(Integer.parseInt(args) - 1));
            }
            case todo -> {
                if (args.isBlank()) {
                    throw new EmptyDescriptionException();
                }
                response.append(addTodoAsString(args));
            }
            case deadline -> {
                if (args.isBlank()) {
                    throw new EmptyDescriptionException();
                }
                response.append(addDeadlineAsString(args));
            }
            case event -> {
                if (args.isBlank()) {
                    throw new EmptyDescriptionException();
                }
                response.append(addEventAsString(args));
            }
            case find -> response.append(findTaskAsString(args));
            default -> throw new ChiikawaException("Oh no! I don't recognise that command :(!");
            }
        } catch (ChiikawaException e) {
            response.append(e.getMessage());
        }

        return response.toString();
    }

    public String getWelcomeMessage() {
        return "Hello! I'm Chiikawa :3\nWhat can I do for you today?";
    }

    private String listTasksAsString() throws ChiikawaException {
        if (tasks.size() == 0) {
            throw new ListEmptyException();
        }
        StringBuilder sb = new StringBuilder("Here are your tasks:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(". ").append(tasks.getTask(i)).append("\n");
        }
        return sb.toString();
    }

    private String markTaskAsString(int index) throws ChiikawaException {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundException();
        }
        tasks.getTask(index).markAsDone();
        storage.save(tasks.getAllTasks());
        return "Nice! I've marked this task as done:\n  " + tasks.getTask(index);
    }

    private String unmarkTaskAsString(int index) throws ChiikawaException {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundException();
        }
        tasks.getTask(index).markAsUndone();
        storage.save(tasks.getAllTasks());
        return "OK, I've marked this task as not done yet:\n  " + tasks.getTask(index);
    }

    private String addTodoAsString(String description) {
        tasks.addTask(new Todo(description));
        storage.save(tasks.getAllTasks());
        return "Got it. I've added this task:\n  "
                + tasks.getTask(tasks.size() - 1)
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String addDeadlineAsString(String input) throws ChiikawaException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new NoDeadlineException();
        }
        tasks.addTask(new Deadline(parts[0], Parser.parseDateTime(parts[1])));
        storage.save(tasks.getAllTasks());
        return "Got it. I've added this task:\n  "
                + tasks.getTask(tasks.size() - 1)
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String addEventAsString(String input) throws ChiikawaException {
        String[] parts = input.split(" /from | /to ", 3);
        if (parts.length < 3 || parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
            throw new NoEventException();
        }
        tasks.addTask(new Event(parts[0],
                Parser.parseDateTime(parts[1]),
                Parser.parseDateTime(parts[2])));
        storage.save(tasks.getAllTasks());
        return "Got it. I've added this task:\n  "
                + tasks.getTask(tasks.size() - 1)
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String deleteTaskAsString(int index) throws ChiikawaException {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundException();
        }
        Task task = tasks.getTask(index);
        tasks.deleteTask(index);
        storage.save(tasks.getAllTasks());
        return "Noted. I've removed this task:\n  "
                + task
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String findTaskAsString(String args) {
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        int count = 1;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.getTask(i).getDescription().contains(args)) {
                sb.append(count).append(". ").append(tasks.getTask(i)).append("\n");
                count++;
            }
        }
        if (count == 1) {
            return "No matching tasks found!";
        }
        return sb.toString();
    }


}
