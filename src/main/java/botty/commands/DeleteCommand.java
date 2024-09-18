package botty.commands;

import botty.exceptions.BottyException;
import botty.tasks.Task;
import botty.tasks.TaskData;
import botty.tasks.TaskManager;

/**
 * Defines the behaviour of the delete command
 */
public class DeleteCommand extends Command {
    /**
     * Executes the delete command, deleting the task at the given index
     * @param taskManager
     * @param parsedInput
     * @return success message
     * @throws BottyException if given input is invalid
     */
    @Override
    public String execute(TaskManager taskManager, ParsedInput parsedInput) throws BottyException {
        try {
            String argument = parsedInput.getArgument("main");
            int taskIndex = Integer.parseInt(argument) - 1;

            Task<? extends TaskData> task = taskManager.deleteTask(taskIndex);
            return "Got it! I have removed the following task:\n"
                    + task + "\n"
                    + "You have " + taskManager.size() + " tasks left!";
        } catch (NumberFormatException ex) {
            throw new BottyException("I don't quite know what you want me to do. "
                    + "Do indicate which task to delete with its number!");
        }
    }
    /**
     * Returns true as the command will edit the task list
     */
    @Override
    public boolean changesTaskList() {
        return true;
    }
}
