package botty.commands;

import botty.exceptions.BottyException;
import botty.tasks.Task;
import botty.tasks.TaskData;
import botty.tasks.TaskManager;

/**
 * Defines the behaviour of the mark command
 */
public class MarkCommand extends Command {
    /**
     * Executes the mark command, marking the task at the given index as done
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
            Task<? extends TaskData> task = taskManager.markTask(taskIndex);

            return "Congrats on completing that! Let me just mark that as done for you.\n" + task;
        } catch (NumberFormatException ex) {
            throw new BottyException("I don't quite know what you want me to do. "
                    + "Do indicate which task to mark with its number!");
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
