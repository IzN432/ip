package botty.commands;

import botty.exceptions.BottyException;
import botty.tasks.Task;
import botty.tasks.TaskData;
import botty.tasks.TaskManager;

/**
 * Defines the behaviour of the unmark command
 */
public class UnmarkCommand extends Command {
    /**
     * Executes the todo command, adding a todo to the given task manager with
     * the given arguments
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
            Task<? extends TaskData> task = taskManager.unmarkTask(taskIndex);

            return "It's okay, we can get that done later. I'll mark that as undone for you.\n" + task;
        } catch (NumberFormatException ex) {
            throw new BottyException("I don't quite know what you want me to do. "
                    + "Do indicate which task to unmark with its number!");
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
