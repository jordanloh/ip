package chiikawa;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import chiikawa.task.Task;

public class Storage {
    private final Path filePath;

    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    private final Ui ui = new Ui();

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
