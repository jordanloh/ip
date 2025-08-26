package chiikawa;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import chiikawa.task.Task;

/**
 * Deals with loading tasks from the file and saving tasks in the file.
 */
public class Storage {
    private final Path filePath;

    /**
     * Initialises the Storage object with the given filePath.
     *
     * @param filePath Directory of the file where tasks are saved and loaded from.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    private final Ui ui = new Ui();

    /**
     * Loads the task list from the file in the computer.
     *
     * @return An ArrayList containing the list of tasks from the file.
     */
    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            Files.createDirectories(filePath.getParent());
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                return tasks;
            }

            Scanner sc = new Scanner(filePath.toFile());
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Task task = Parser.parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            ui.showMessage("I cannot read the file! >_<");
        }
        return tasks;
    }

    /**
     * Saves the updated task list to the file.
     *
     * @param tasks An ArrayList containing the updated tasks to be saved to the file.
     */
    public void save(ArrayList<Task> tasks) {
        try (FileWriter fw = new FileWriter(filePath.toFile())) {
            for (Task task : tasks) {
                fw.write(task.saveFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            ui.showMessage("I cannot save to the file! >_<");
        }
    }

}
