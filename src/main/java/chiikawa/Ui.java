package chiikawa;

import chiikawa.task.Task;

import java.util.Scanner;

public class Ui {
    private static final String name = "Chiikawa";
    private static final String divider = "------------------------------------------";
    private Scanner sc;

    public Ui() {
        sc = new Scanner(System.in);
    }

    public void showDivider() {
        System.out.println(divider);
    }

    public void showWelcome() {
        showDivider();
        System.out.println("Hewwo! I'm " + name + "!!");
        System.out.println("What can I do for you nya~?");
        showDivider();
    }

    public void showGoodbye() {
        System.out.println("Byebye!! See you again soon nya~!");
        showDivider();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showTaskAdded(Task task, int size) {
        System.out.println("I've added in this task ~nya! : ");
        System.out.println(task.toString());
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    public void showTaskRemoved(Task task, int size) {
        System.out.println("I've removed this task ~nya! : ");
        System.out.println(task.toString());
        System.out.println("Now you have " + size + " tasks in the list.");
    }

    public void showTaskMarked(Task task) {
        System.out.println("I've marked this task as done ~nya! : ");
        System.out.println(task.toString());
    }

    public void showTaskUnmarked(Task task) {
        System.out.println("I've marked this task as not done yet ~nya! : ");
        System.out.println(task.toString());
    }

    public void showListTasks(TaskList tasks) {
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println(i + ". " + tasks.getTask(i - 1));
        }
    }

    public void showTask(Task task) {
        System.out.println(task.toString());
    }

    public void showFindMatchingTaskMessage() {
        System.out.println("Hey!! Here are some matching tasks in your list:");
    }

    public void showNoMatchFoundMessage() {
        System.out.println("Aww... no matching tasks found.");
    }

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

    public void close() {
        sc.close();
    }
}
