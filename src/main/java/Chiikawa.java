import java.util.ArrayList;
import java.util.Scanner;

public class Chiikawa {
    private static final String name = "Chiikawa";
    private static final String divider = "------------------------------------------";
    private ArrayList<Task> arr = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Chiikawa chiikawa = new Chiikawa();

        System.out.println(divider);
        System.out.println("Hewwo! I'm " + name + "!!");
        System.out.println("What can I do for you nya~?");
        System.out.println(divider);

        while (true) {
            String input = sc.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split(" ", 2);
            Command command;
            System.out.println(divider);

            try {
                try {
                    command = Command.valueOf(parts[0].toLowerCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Oh no! I don't recognise that command :(!");
                    System.out.println(divider);
                    continue;
                }
                if (command == Command.bye) {
                    System.out.println("Byebye!! See you again soon nya~!");
                    System.out.println(divider);
                    break;
                }
                switch (command) {
                    case list:
                        chiikawa.listTasks();
                        break;
                    case mark:
                        if (parts.length < 2) {
                            throw new NoIndexException();
                        }
                        chiikawa.markTask(Integer.parseInt(parts[1]) - 1);
                        break;
                    case unmark:
                        if (parts.length < 2) {
                            throw new NoIndexException();
                        }
                        chiikawa.unmarkTask(Integer.parseInt(parts[1]) - 1);
                        break;
                    case delete:
                        if (parts.length < 2) {
                            throw new NoIndexException();
                        }
                        chiikawa.deleteTask(Integer.parseInt(parts[1]) - 1);
                        break;
                    case todo:
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new EmptyDescriptionException();
                        }
                        chiikawa.addTodo(parts[1].trim());
                        break;
                    case deadline:
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new EmptyDescriptionException();
                        }
                        chiikawa.addDeadline(parts[1].trim());
                        break;
                    case event:
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new EmptyDescriptionException();
                        }
                        chiikawa.addEvent(parts[1].trim());
                        break;
                    default:
                        throw new ChiikawaException("Oh no! I don't recognise that command :(!");
                }
            } catch (ChiikawaException e) {
                System.out.println(e.getMessage());
            }

            System.out.println(divider);
        }

        sc.close();
    }

    public void listTasks() throws ChiikawaException {
        if (arr.isEmpty()) {
            throw new ListEmptyException();
        }
        for (int i = 1; i <= arr.size(); i++) {
            System.out.println(i + ". " + arr.get(i - 1));
        }
    }

    public void markTask(int index) throws ChiikawaException {
        if (index < 0 || index >= arr.size()) {
            throw new IndexOutOfBoundException();
        }
        arr.get(index).markAsDone();
        System.out.println("I've marked this task as done ~nya! : ");
        System.out.println(arr.get(index));
    }

    public void unmarkTask(int index) throws ChiikawaException {
        if (index < 0 || index >= arr.size()) {
            throw new IndexOutOfBoundException();
        }
        arr.get(index).markAsUndone();
        System.out.println("I've marked this task as not done yet ~nya! : ");
        System.out.println(arr.get(index));
    }

    public void addTodo(String description) {
        arr.add(new Todo(description));
        System.out.println("I've added in this task ~nya! : ");
        System.out.println(arr.get(arr.size() - 1));
        System.out.println("Now you have " + arr.size() + " tasks in the list.");
    }

    public void addDeadline(String input) throws ChiikawaException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new NoDeadlineException();
        }
        arr.add(new Deadline(parts[0], parts[1]));
        System.out.println("I've added in this task ~nya! : ");
        System.out.println(arr.get(arr.size() - 1));
        System.out.println("Now you have " + arr.size() + " tasks in the list.");
    }

    public void addEvent(String input) throws ChiikawaException {
        String[] parts = input.split(" /from | /to ", 3);
        if (parts.length < 3 || parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
            throw new NoEventException();
        }
        arr.add(new Event(parts[0], parts[1], parts[2]));
        System.out.println("I've added in this task ~nya! : ");
        System.out.println(arr.get(arr.size() - 1));
        System.out.println("Now you have " + arr.size() + " tasks in the list.");
    }

    public void deleteTask(int index) throws ChiikawaException {
        if (index < 0 || index >= arr.size()) {
            throw new IndexOutOfBoundException();
        }
        Task task = arr.get(index);
        arr.remove(index);
        System.out.println("I've removed this task ~nya! : ");
        System.out.println(task.toString());
        System.out.println("Now you have " + arr.size() + " tasks in the list.");
    }
}