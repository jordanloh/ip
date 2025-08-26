package chiikawa;

import chiikawa.exception.ChiikawaException;
import chiikawa.task.Deadline;
import chiikawa.task.Event;
import chiikawa.task.Task;
import chiikawa.task.Todo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Deals with formatting inputs properly.
 */
public class Parser {

    /**
     * Parses the input into a valid command.
     *
     * @param input Command that needs to be parsed into an appropriate format.
     * @return Command contained in the input.
     * @throws ChiikawaException If the input is empty or is an invalid command.
     */
    public static Command parseCommand(String input) throws ChiikawaException {
        if (input.isBlank()) {
            throw new ChiikawaException("Input cannot be empty!");
        }

        String[] parts = input.split(" ", 2);
        try {
            return Command.valueOf(parts[0].toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new ChiikawaException("Oh no! I don't recognise that command :(!");
        }
    }

    /**
     * Returns the arguments provided with the command by the user.
     *
     * @param input Input that was passed in by the user containing additional arguments.
     * @return The additional arguments provided by the user.
     */
    public static String getCommandArgs(String input) {
        String[] parts = input.split(" ", 2);
        return (parts.length > 1) ? parts[1].trim() : "";
    }

    /**
     * Returns a LocalDateTime object formatted by yyyy-MM-dd HHmm.
     *
     * @param input String containing the datetime to be formatted.
     * @return LocalDateTime object formatted properly.
     * @throws DateTimeParseException If there is an error parsing the input.
     */
    public static LocalDateTime parseDateTime(String input) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return LocalDateTime.parse(input, formatter);
    }

    /**
     * Returns the task object after parsing the input by the user.
     *
     * @param line String containing the description / deadline / event datetime of the task.
     * @return The newly created Task object.
     */
    public static Task parseTask(String line) {
        Ui ui = new Ui();
        try {
            String[] parts = line.split(" \\| ");
            String type = parts[0];
            boolean isDone = parts[1].equals("1");
            String description = parts[2];

            switch (type) {
            case "T":
                Task t = new Todo(description);
                if (isDone) {
                    t.markAsDone();
                }
                return t;
            case "D":
                Task d = new Deadline(description, Parser.parseDateTime(parts[3]));
                if (isDone) {
                    d.markAsDone();
                }
                return d;
            case "E":
                Task e = new Event(description, Parser.parseDateTime(parts[3]), Parser.parseDateTime(parts[4]));
                if (isDone) {
                    e.markAsDone();
                }
                return e;
            default:
                return null;
            }
        } catch (Exception e) {
            ui.showMessage("Something went wrong! >_<");
            return null;
        }
    }
}
