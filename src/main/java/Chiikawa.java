import java.util.Scanner;

public class Chiikawa {
    private static final String name = "Chiikawa";
    private static final String divider = "----------------------------------";
    private Task[] arr = new Task[100];
    private int counter = 0;

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
            String command = parts[0];

            System.out.println(divider);

            try {
                if (command.equalsIgnoreCase("bye")) {
                    System.out.println("Byebye!! See you again soon nya~!");
                    System.out.println(divider);
                    break;
                }

                switch (command) {
                    case "list":
                        chiikawa.listTasks();
                        break;
                    case "mark":
                        chiikawa.markTask(Integer.parseInt(parts[1]) - 1);
                        break;
                    case "unmark":
                        chiikawa.unmarkTask(Integer.parseInt(parts[1]) - 1);
                        break;
                    case "todo":
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new EmptyDescriptionException();
                        }
                        chiikawa.addTodo(parts[1].trim());
                        break;
                    case "deadline":
                        if (parts.length < 2 || parts[1].isBlank()) {
                            throw new EmptyDescriptionException();
                        }
                        chiikawa.addDeadline(parts[1].trim());
                        break;
                    case "event":
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
        if (this.counter == 0) {
            throw new ListEmptyException();
        }
        for (int i = 1; i <= counter; i++) {
            System.out.println(i + ". " + arr[i - 1]);
        }
    }

    public void markTask(int index) throws ChiikawaException {
        if (index < 0 || index >= counter) {
            throw new IndexOutOfBoundException();
        }
        arr[index].markAsDone();
        System.out.println("I've marked this task as done ~nya! : ");
        System.out.println(arr[index]);
    }

    public void unmarkTask(int index) throws ChiikawaException {
        if (index < 0 || index >= counter) {
            throw new IndexOutOfBoundException();
        }
        arr[index].markAsUndone();
        System.out.println("I've marked this task as not done yet ~nya! : ");
        System.out.println(arr[index]);
    }

    public void addTodo(String description) {
        arr[counter++] = new Todo(description);
        System.out.println("I've added in this task ~nya! : ");
        System.out.println(arr[counter - 1]);
        System.out.println("Now you have " + counter + " tasks in the list.");
    }

    public void addDeadline(String input) throws ChiikawaException {
        String[] parts = input.split(" /by ", 2);
        if (parts.length < 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new NoDeadlineException();
        }
        arr[counter++] = new Deadline(parts[0], parts[1]);
        System.out.println("I've added in this task ~nya! : ");
        System.out.println(arr[counter - 1]);
        System.out.println("Now you have " + counter + " tasks in the list.");
    }

    public void addEvent(String input) throws ChiikawaException {
        String[] parts = input.split(" /from | /to ", 3);
        if (parts.length < 3 || parts[0].isBlank() || parts[1].isBlank() || parts[2].isBlank()) {
            throw new NoEventException();
        }
        arr[counter++] = new Event(parts[0], parts[1], parts[2]);
        System.out.println("I've added in this task ~nya! : ");
        System.out.println(arr[counter - 1]);
        System.out.println("Now you have " + counter + " tasks in the list.");
    }
}