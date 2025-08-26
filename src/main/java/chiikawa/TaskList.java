package chiikawa;

import chiikawa.task.Task;

import java.util.ArrayList;

/**
 * Contains the task list and the necessary operations to deal with the task list.
 */
public class TaskList {

    private ArrayList<Task> tasks;

    /**
     * Initialises the task list.
     *
     * @param tasks ArrayList containing the tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the array list.
     *
     * @param task Task to be added to the list.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes the task list from the array with the given index.
     *
     * @param index Index of the task to be deleted from the task list.
     * @return The task that was deleted.
     */
    public Task deleteTask(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task with the given index.
     *
     * @param index Index of the task to be returned.
     * @return The task with the given index.
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns an ArrayList containing the list of all tasks.
     *
     * @return ArrayList of the list of all tasks.
     */
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }
}
