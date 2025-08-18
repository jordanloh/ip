import java.util.Scanner;

public class Chiikawa {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String name = "Chiikawa";
        String divider = "----------------------------------";
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
            System.out.println("Awww you said: " + input);
            System.out.println(divider);
        }
        sc.close();
    }
}
