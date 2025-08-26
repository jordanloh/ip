package chiikawa;

import java.util.Scanner;

import chiikawa.task.Task;
/**
 * Deals with input and output operations from the user.
 */
public class Ui {
    private static final String name = "Chiikawa";
    private static final String divider = "------------------------------------------";
    private Scanner sc;

    public Ui() {
        sc = new Scanner(System.in);
    }

    /**
     * Prints a divider to the screen.
     */
    public void showDivider() {
        System.out.println(divider);
    }

    /**
     * Prints the welcome message upon starting the chatbot.
     */
    public void showWelcome() {
        showDivider();
        System.out.println("Hewwo! I'm " + name + "!!");
        System.out.println("What can I do for you nya~?");
        showDivider();
    }

    /**
     * Prints the goodbye message when stopping the chatbot.
     */
    public void showGoodbye() {
        System.out.println("Byebye!! See you again soon nya~!");
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
     * Prints the add task message to the screen, together with the task added.
     *
     * @param task Task that was added.
     * @param size Number of tasks currently in the list.
     */
    public void showTaskAdded(Task task, int size) {
        System.out.println("I've added in this task ~nya! : ");
        System.out.println(task.toString());
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    /**
     * Prints the remove task message to the screen, together with the task removed.
     *
     * @param task Task that was removed.
     * @param size Number of tasks left in the list.
     */
    public void showTaskRemoved(Task task, int size) {
        System.out.println("I've removed this task ~nya! : ");
        System.out.println(task.toString());
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    /**
     * Prints the task that was marked to the screen.
     *
     * @param task Task that was marked as done.
     */
    public void showTaskMarked(Task task) {
        System.out.println("I've marked this task as done ~nya! : ");
        System.out.println(task.toString());
    }

    /**
     * Prints the task that was unmarked to the screen.
     *
     * @param task Task that was unmarked as done.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("I've marked this task as not done yet ~nya! : ");
        System.out.println(task.toString());
    }

    /**
     * Prints the list of tasks currently stored.
     *
     * @param tasks TaskList that contains the list of tasks.
     */
    public void showListTasks(TaskList tasks) {
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println(i + ". " + tasks.getTask(i - 1));
        }
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
