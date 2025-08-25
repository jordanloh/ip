import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                return tasks;
            }

            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Task task = parseTask(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("I cannot read the file! >_<");
        }
        return tasks;
    }

    public void save(ArrayList<Task> tasks) {
        try {
            FileWriter fw = new FileWriter(filePath);
            for (Task task : tasks) {
                fw.write(task.saveFormat() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("I cannot save to the file! >_<");
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
                Task d = new Deadline(description, parts[3]);
                if (isDone) {
                    d.markAsDone();;
                }
                return d;
            case "E":
                Task e = new Event(description, parts[3], parts[4]);
                if (isDone) {
                    e.markAsDone();
                }
                return e;
            default:
                return null;
            }
        } catch (Exception e) {
            System.out.println("Something went wrong! >_<");
            return null;
        }
    }
}
