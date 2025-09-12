package chiikawa;

import static java.nio.file.Files.readString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import chiikawa.task.Task;

public class StorageTest {

    @TempDir
    Path tempDir;

    abstract static class TaskStub extends Task {
        private String description;
        private final boolean isDone;

        public TaskStub(String description, boolean done) {
            super(description);
            this.description = description;
            this.isDone = done;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String saveFormat() {
            return "T | " + getIsDone() + " | " + description;
        }
    }

    static class TodoStub extends TaskStub {
        public TodoStub(String description, boolean done) {
            super(description, done);
        }

        @Override
        public void updateField(String key, String value) {
            if ("/description".equals(key)) {
                setDescription(value);
            } else {
                throw new UnsupportedOperationException("Todo cannot have field " + key);
            }
        }
    }

    static class DeadlineStub extends TaskStub {
        private String by;

        public DeadlineStub(String description, boolean done, String by) {
            super(description, done);
            this.by = by;
        }

        @Override
        public String saveFormat() {
            return "D | " + getIsDone() + " | " + getDescription() + " | " + by;
        }

        @Override
        public void updateField(String key, String value) {
            switch (key) {
            case "/description" -> setDescription(value);
            case "/by" -> this.by = value;
            default -> throw new UnsupportedOperationException("Deadline cannot have field " + key);
            }
        }

    }

    @Test
    public void save_fileContainsSavedData() {
        Path filePath = tempDir.resolve("tasks.txt");
        Storage storage = new Storage(filePath);

        ArrayList<Task> tasksToSave = new ArrayList<>();
        tasksToSave.add(new TodoStub("Buy milk", true));
        tasksToSave.add(new DeadlineStub("Submit report", false, "2025-08-30 2359"));

        storage.save(tasksToSave);

        try {
            String fileContent = readString(filePath);
            assertTrue(fileContent.contains("T | 1 | Buy milk"));
            assertTrue(fileContent.contains("D | 0 | Submit report | 2025-08-30 2359"));
        } catch (IOException e) {
            fail();
        }
    }
}
