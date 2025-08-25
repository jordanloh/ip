import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

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

    public static String getCommandArgs(String input) {
        String[] parts = input.split(" ", 2);
        return (parts.length > 1) ? parts[1].trim() : "";
    }

    public static LocalDateTime parseDateTime(String input) throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return LocalDateTime.parse(input, formatter);
    }

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
