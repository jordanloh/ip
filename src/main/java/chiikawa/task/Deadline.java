package chiikawa.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import chiikawa.Parser;

/**
 * Represents a Deadline task. Includes a LocalDateTime
 * representing the deadline of the task.
 */
public class Deadline extends Task {

    protected LocalDateTime by;

    /**
     * Initialises a deadline task.
     *
     * @param description String of the description of the task.
     * @param by The deadline of the task.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String saveFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return "D | " + getIsDone() + " | " + description + " | " + by.format(formatter);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
        return "[D]" + super.toString() + " (by: " + by.format(formatter) + ")";
    }

    public void setBy(String by) {
        this.by = Parser.parseDateTime(by);
    }

    @Override
    public void updateField(String key, String value) {
        switch (key) {
        case "/description" -> setDescription(value);
        case "/by" -> setBy(value);
        default -> throw new UnsupportedOperationException("Deadline cannot have field " + key);
        }
    }
}
