package botty.tasks;

import java.util.ArrayList;
import java.util.stream.IntStream;

import botty.exceptions.BottyException;
import botty.exceptions.TaskListEmptyException;
import botty.exceptions.TaskNumberNotFoundException;
import botty.exceptions.TasksNotFoundException;

/**
 * Manages all interactions with the task list
 */
public class TaskManager {
    // The list of tasks
    private final ArrayList<Task<? extends TaskData>> taskList = new ArrayList<>(100);

    /**
     * Returns a string representation of the list of tasks
     * @throws TaskListEmptyException if the task list is empty
     */
    public String list() throws TaskListEmptyException {
        if (size() == 0) {
            throw new TaskListEmptyException();
        }
        StringBuilder content = new StringBuilder();

        for (int i = 0; i < taskList.size() - 1; i++) {
            content.append((i + 1)).append(". ").append(taskList.get(i)).append("\n");
        }
        content.append(taskList.size()).append(". ").append(taskList.get(taskList.size() - 1));
        return content.toString();
    }

    /**
     * Marks the task at the given index as done
     * @param index the index of the task
     * @return the marked task
     * @throws TaskListEmptyException if the task list is empty
     * @throws TaskNumberNotFoundException if the task number is out of range
     */
    public Task<? extends TaskData> markTask(int index) throws TaskListEmptyException, TaskNumberNotFoundException {
        if (size() == 0) {
            throw new TaskListEmptyException();
        }
        if (index < 0 || index > size() - 1) {
            throw new TaskNumberNotFoundException(index + 1, size());
        }
        Task<? extends TaskData> task = taskList.get(index);
        task.setCompleted(true);
        return task;
    }
    /**
     * Unmarks the task at the given index as done
     * @param index the index of the task
     * @return the unmarked task
     * @throws TaskListEmptyException if the task list is empty
     * @throws TaskNumberNotFoundException if the task number is out of range
     */
    public Task<? extends TaskData> unmarkTask(int index) throws TaskListEmptyException, TaskNumberNotFoundException {
        if (size() == 0) {
            throw new TaskListEmptyException();
        }
        if (index < 0 || index > size() - 1) {
            throw new TaskNumberNotFoundException(index + 1, size());
        }
        Task<? extends TaskData> task = taskList.get(index);
        task.setCompleted(false);
        return task;
    }
    /**
     * Deletes the task at the given index
     * @param index the index of the task
     * @return the deleted task
     * @throws TaskListEmptyException if the task list is empty
     * @throws TaskNumberNotFoundException if the index is out of range
     */
    public Task<? extends TaskData> deleteTask(int index) throws TaskListEmptyException, TaskNumberNotFoundException {
        if (size() == 0) {
            throw new TaskListEmptyException();
        }
        if (index < 0 || index > size() - 1) {
            throw new TaskNumberNotFoundException(index + 1, size());
        }
        Task<? extends TaskData> task = taskList.get(index);
        taskList.remove(index);
        return task;
    }
    /**
     * Adds the task to the task list
     * @param task the task to be added
     */
    public void addTask(Task<? extends TaskData> task) {
        assert task != null : "Cannot add a null task";
        taskList.add(task);
    }

    /**
     * Returns the current number of tasks in the task list
     */
    public int size() {
        return taskList.size();
    }

    /**
     * Returns the task at the given index
     * @param index the index of the task to retrieve
     * @return the retrieved task
     * @throws TaskListEmptyException if the task list is empty
     * @throws TaskNumberNotFoundException if the index is out of range
     */
    public Task<? extends TaskData> getTask(int index) throws TaskListEmptyException, TaskNumberNotFoundException {
        if (size() == 0) {
            throw new TaskListEmptyException();
        }
        if (index < 0 || index > size() - 1) {
            throw new TaskNumberNotFoundException(index + 1, size());
        }
        return taskList.get(index);
    }

    /**
     * Returns a list of tasks that matches the given keyword
     * @param keyword the keyword to be matched
     * @return the list of tasks matched as a string
     * @throws TaskListEmptyException when the task list is empty
     * @throws TasksNotFoundException when no tasks are found matching the keyword
     */
    public String findTasks(String keyword) throws TaskListEmptyException, TasksNotFoundException {
        if (taskList.isEmpty()) {
            throw new TaskListEmptyException();
        }

        String containsKeywordRegex = ".*\\b" + keyword.toLowerCase() + "\\b.*";

        StringBuilder result = new StringBuilder();
        IntStream.range(0, taskList.size())
                .filter(index -> taskList.get(index).toString().toLowerCase().matches(containsKeywordRegex))
                .mapToObj(index -> (index + 1) + ". " + taskList.get(index).toString())
                .forEach(str -> result.append(str).append("\n"));

        if (result.isEmpty()) {
            throw new TasksNotFoundException();
        }

        result.deleteCharAt(result.length() - 1); // remove the last new line for nicer formatting
        return result.toString();
    }

    /**
     * Updates the task at the given task index with the given data
     * @param taskIndex the index of the task to be updated
     * @param data the data of the updated task
     * @param <T> the type of data
     * @throws BottyException if an incorrect type is passed in
     */
    public <T extends TaskData> void updateTask(int taskIndex, T data) throws BottyException {
        try {
            // this cast is necessary to update the task with the appropriate data
            @SuppressWarnings("unchecked")
            Task<T> task = (Task<T>) getTask(taskIndex);
            task.update(data);
        } catch (ClassCastException e) {
            throw new BottyException("Incorrect data type was given");
        }
    }
}
