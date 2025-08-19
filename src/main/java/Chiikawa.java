import java.util.Scanner;

public class Chiikawa {
    private static final String name = "Chiikawa";
    private static final String divider = "----------------------------------";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] arr = new String[100];
        int counter = 0;

        System.out.println(divider);
        System.out.println("Hewwo! I'm " + name + "!!");
        System.out.println("What can I do for you nya~?");
        System.out.println(divider);

        while (true) {
            String input = sc.nextLine();

            System.out.println(divider);
            if (input.equalsIgnoreCase("bye")) {
                System.out.println("Byebye!! See you again soon nya~!");
                System.out.println(divider);
                break;
            }
            else if (input.equalsIgnoreCase("list")) {
                for (int i = 1; i <= counter; i++) {
                    System.out.println(i + ". " + arr[i-1]);
                }
            }
            else {
                System.out.println("I've added in: " + input);
                arr[counter++] = input;
            }
            System.out.println(divider);
        }
        sc.close();
    }
}
