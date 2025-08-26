package chiikawa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static java.nio.file.Files.readString;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import chiikawa.task.Task;

public class StorageTest {

    @TempDir
    Path tempDir;

    static class TaskStub extends Task {
        public final String description;
        public final boolean isDone;

        public TaskStub(String description, boolean done) {
            super(description);
            this.description = description;
            this.isDone = done;
        }

        @Override
        public String saveFormat() { return "T | " + (isDone ? "1" : "0") + " | " + description; }
    }

    static class TodoStub extends TaskStub {
        public TodoStub(String description, boolean done) {
            super(description, done);
        }
    }

    static class DeadlineStub extends TaskStub {
        private final String by;
        public DeadlineStub(String description, boolean done, String by) {
            super(description, done);
            this.by = by;
        }
        @Override
        public String saveFormat() {
            return "D | " + (isDone ? "1" : "0") + " | " + this.description + " | " + by;
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
