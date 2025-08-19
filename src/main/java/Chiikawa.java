import java.util.Scanner;

public class Chiikawa {
    private static final String name = "Chiikawa";
    private static final String divider = "----------------------------------";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Task[] arr = new Task[100];
        int counter = 0;

        System.out.println(divider);
        System.out.println("Hewwo! I'm " + name + "!!");
        System.out.println("What can I do for you nya~?");
        System.out.println(divider);

        while (true) {
            String input = sc.nextLine().trim();
            String[] parts = input.split(" ", 2);
            String command = parts[0];

            System.out.println(divider);

            if (command.equalsIgnoreCase("bye")) {
                System.out.println("Byebye!! See you again soon nya~!");
                System.out.println(divider);
                break;
            }
            switch (command) {
                case "list":
                    for (int i = 1; i <= counter; i++) {
                        System.out.println(i + ". " + arr[i-1].toString());
                    }
                    break;
                case "mark": {
                    int index = Integer.parseInt(parts[1]) - 1;
                    arr[index].markAsDone();
                    System.out.println("I've marked this task as done ~nya! :");
                    System.out.println(arr[index].toString());
                    break;
                }
                case "unmark": {
                    int index = Integer.parseInt(parts[1]) - 1;
                    arr[index].markAsUndone();
                    System.out.println("I've marked this task as not done yet ~nya! :");
                    System.out.println(arr[index].toString());
                    break;
                }
                case "todo":
                    System.out.println("I've added in this task ~nya! :");
                    arr[counter++] = new Todo(parts[1]);
                    System.out.println(arr[counter - 1].toString());
                    System.out.println("Now you have " + counter + " tasks in the list.");
                    break;
                case "deadline":
                    System.out.println("I've added in this task ~nya! :");
                    String[] deadlineParts = parts[1].split(" /by ", 2);
                    arr[counter++] = new Deadline(deadlineParts[0], deadlineParts[1]);
                    System.out.println(arr[counter - 1].toString());
                    System.out.println("Now you have " + counter + " tasks in the list.");
                    break;
                case "event":
                    System.out.println("I've added in this task ~nya! :");
                    String[] eventParts = parts[1].split(" /from | /to ", 3);
                    arr[counter++] = new Event(eventParts[0], eventParts[1], eventParts[2]);
                    System.out.println(arr[counter - 1].toString());
                    System.out.println("Now you have " + counter + " tasks in the list.");
                    break;
            }
            System.out.println(divider);
        }
        sc.close();
    }
}
