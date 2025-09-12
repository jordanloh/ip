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
    private final Ui ui = new Ui();
    private final TaskList tasks;
    private final Storage storage;

    /**
     * Constructor for the Chiikawa object.
     *
     * @param filePath The path to where the data is stored.
     */
    public Chiikawa(Path filePath) {
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Constructor for the Chiikawa object without a file path provided.
     * The default file path would be data/chiikawa.txt.
     */
    public Chiikawa() {
        Path path = java.nio.file.Paths.get("data", "chiikawa.txt");
        storage = new Storage(path);
        tasks = new TaskList(storage.load());
    }

    /**
     * Main driver to start the program.
     *
     * @param args Necessary CLI arguments.
     */
    public static void main(String[] args) {
        Path path = java.nio.file.Paths.get("data", "chiikawa.txt");
        Chiikawa chiikawa = new Chiikawa(path);
        chiikawa.run();
    }

    /**
     * Runs the chatbot in CLI mode.
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            String response = getResponse(input);
            ui.showMessage(response);
            ui.showDivider();

            if (input.trim().equalsIgnoreCase("bye")) {
                ui.close();
                return;
            }
        }
    }

    /**
     * Returns a response string for GUI or CLI.
     */
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
            case update -> {
                if (args.isBlank()) {
                    throw new NoIndexException();
                }
                response.append(updateTaskAsString(args));
            }
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

    // ChatGPT recommends to extract out the checking of validity of index for TaskList.
    private void validateIndex(int index) throws IndexOutOfBoundException {
        if (index < 0 || index >= tasks.size()) {
            throw new IndexOutOfBoundException();
        }
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
        validateIndex(index);
        tasks.getTask(index).setAsDone();
        storage.save(tasks.getAllTasks());
        return "Nice! I've marked this task as done:\n  " + tasks.getTask(index);
    }

    private String unmarkTaskAsString(int index) throws ChiikawaException {
        validateIndex(index);
        tasks.getTask(index).setAsUndone();
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
        try {
            tasks.addTask(new Deadline(parts[0], Parser.parseDateTime(parts[1])));
        } catch (DateTimeParseException e) {
            return "Invalid date/time format! Please use yyyy-MM-dd HHmm, e.g. 2019-12-25 1800";
        }
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
        try {
            tasks.addTask(new Event(parts[0],
                    Parser.parseDateTime(parts[1]),
                    Parser.parseDateTime(parts[2])));
        } catch (DateTimeParseException e) {
            return "Invalid date/time format! Please use yyyy-MM-dd HHmm, e.g. 2019-12-25 1800";
        }
        storage.save(tasks.getAllTasks());
        return "Got it. I've added this task:\n  "
                + tasks.getTask(tasks.size() - 1)
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String deleteTaskAsString(int index) throws ChiikawaException {
        validateIndex(index);
        Task task = tasks.getTask(index);
        tasks.deleteTask(index);
        storage.save(tasks.getAllTasks());
        return "Noted. I've removed this task:\n  "
                + task
                + "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private String findTaskAsString(String args) {
        // ChatGPT recommends to compare both keywords and description with lower-case to eliminate case sensitivity.
        String keyword = args.trim().toLowerCase();
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        int count = 1;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.getTask(i).getDescription().toLowerCase().contains(keyword)) {
                sb.append(count).append(". ").append(tasks.getTask(i)).append("\n");
                count++;
            }
        }
        return (count == 1) ? "No matching tasks found!" : sb.toString();
    }

    private String updateTaskAsString(String args) throws ChiikawaException {
        String[] parts = args.split(" ", 3);
        if (parts.length < 3) {
            throw new ChiikawaException("Invalid format! Usage: update <index> <field> <value>");
        }

        int index;
        try {
            index = Integer.parseInt(parts[0]) - 1;
        } catch (NumberFormatException e) {
            throw new ChiikawaException("Invalid task index: " + parts[0]);
        }

        validateIndex(index);

        String key = parts[1];
        String value = parts[2];
        Task task = tasks.getTask(index);

        try {
            task.updateField(key, value);
        } catch (UnsupportedOperationException e) {
            return "Error: " + e.getMessage();
        } catch (DateTimeParseException e) {
            return "Invalid date/time format! Please use yyyy-MM-dd HHmm, e.g. 2019-12-25 1800";
        }

        storage.save(tasks.getAllTasks());
        return "Got it! I’ve updated the task:\n  " + task;
    }
}
