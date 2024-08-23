public abstract class AddTaskCommand implements Command {
    private TaskManager taskManager;
    public AddTaskCommand(TaskManager taskManager) {
        this.taskManager = taskManager;
    }
    protected String addToTaskList(Task task) {
        taskManager.addTask(task);

        return "I have added the following task to the list!\n" +
                task + "\n" +
                "You now have " + taskManager.size() + " tasks.";

    }
}
