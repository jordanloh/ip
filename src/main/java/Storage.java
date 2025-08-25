import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Path;

public class Storage {
    private final Path filePath;

    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    private Ui ui = new Ui();

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
                Task task = parseTask(line);
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
        try (FileWriter fw = new FileWriter(filePath.toFile())){
            for (Task task : tasks) {
                fw.write(task.saveFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            ui.showMessage("I cannot save to the file! >_<");
        }
    }

    private Task parseTask(String line) {
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
