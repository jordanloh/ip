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
            else if (command.equalsIgnoreCase("list")) {
                for (int i = 1; i <= counter; i++) {
                    System.out.println(i + ". " + arr[i-1].toString());
                }
            }
            else if (command.equalsIgnoreCase("mark")) {
                int index = Integer.parseInt(parts[1]) - 1;
                arr[index].markAsDone();
                System.out.println("I've marked this task as done ~nya! : ");
                System.out.println(arr[index].toString());
            }
            else if (command.equalsIgnoreCase("unmark")) {
                int index = Integer.parseInt(parts[1]) - 1;
                arr[index].markAsUndone();
                System.out.println("I've marked this task as not done yet ~nya! : ");
                System.out.println(arr[index].toString());
            }
            else {
                System.out.println("I've added in: " + input);
                arr[counter++] = new Task(input);
            }
            System.out.println(divider);
        }
        sc.close();
    }
}
