package chiikawa;

import java.util.Scanner;

/**
 * Deals with input and output operations from the user.
 */
public class Ui {
    private static final String NAME = "Chiikawa";
    private static final String DIVIDER = "------------------------------------------";
    private final Scanner sc;

    public Ui() {
        sc = new Scanner(System.in);
    }

    /**
     * Prints a divider to the screen.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Prints the welcome message upon starting the chatbot.
     */
    public void showWelcome() {
        showDivider();
        showMessage("Hewwo! I'm " + NAME + "!!");
        showMessage("What can I do for you nya~?");
        showDivider();
    }

    /**
     * Prints the goodbye message when stopping the chatbot.
     */
    public void showGoodbye() {
        showMessage("Byebye!! See you again soon nya~!");
        showDivider();
    }

    /**
     * Prints a given message to the screen.
     *
     * @param message Message to be printed.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Reads the command inputted by the user.
     *
     * @return String containing the command that was inputted by the user.
     */
    public String readCommand() {
        String input;
        while (true) {
            input = sc.nextLine();
            if (!input.isBlank()) {
                showDivider();
                return input.trim();
            }
        }
    }

    /**
     * Closes the scanner object in Ui.
     */
    public void close() {
        sc.close();
    }
}
