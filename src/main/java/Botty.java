import java.util.Scanner;

public class Botty {
    private static final String logo = " ________  ________  _________  _________    ___    ___                 \n" +
            "|\\   __  \\|\\   __  \\|\\___   ___\\\\___   ___\\ |\\  \\  /  /|                \n" +
            "\\ \\  \\|\\ /\\ \\  \\|\\  \\|___ \\  \\_\\|___ \\  \\_| \\ \\  \\/  / /                \n" +
            " \\ \\   __  \\ \\  \\\\\\  \\   \\ \\  \\     \\ \\  \\   \\ \\    / /                 \n" +
            "  \\ \\  \\|\\  \\ \\  \\\\\\  \\   \\ \\  \\     \\ \\  \\   \\/  /  /                  \n" +
            "   \\ \\_______\\ \\_______\\   \\ \\__\\     \\ \\__\\__/  / /                    \n" +
            "    \\|_______|\\|_______|    \\|__|      \\|__|\\___/ /                     \n" +
            "                                           \\|___|/                      \n" +
            "                                                                        \n" +
            " _________  ___  ___  _______           ________  ________  _________   \n" +
            "|\\___   ___\\\\  \\|\\  \\|\\  ___ \\         |\\   __  \\|\\   __  \\|\\___   ___\\ \n" +
            "\\|___ \\  \\_\\ \\  \\\\\\  \\ \\   __/|        \\ \\  \\|\\ /\\ \\  \\|\\  \\|___ \\  \\_| \n" +
            "     \\ \\  \\ \\ \\   __  \\ \\  \\_|/__       \\ \\   __  \\ \\  \\\\\\  \\   \\ \\  \\  \n" +
            "      \\ \\  \\ \\ \\  \\ \\  \\ \\  \\_|\\ \\       \\ \\  \\|\\  \\ \\  \\\\\\  \\   \\ \\  \\ \n" +
            "       \\ \\__\\ \\ \\__\\ \\__\\ \\_______\\       \\ \\_______\\ \\_______\\   \\ \\__\\\n" +
            "        \\|__|  \\|__|\\|__|\\|_______|        \\|_______|\\|_______|    \\|__|";

    private static final String bottySymbol = "Botty: ";
    private static final String bottyIndentation = "       ";

    private static int currentIndex = 0;
    private static Task[] taskList;

    public static void main(String[] args) {
        System.out.println(logo);
        System.out.println();

        reply("Hello, I am Botty the Bot, how may I be of service today?");

        Scanner inputScanner = new Scanner(System.in);

        taskList = new Task[100];

        boolean exitFlag = false;

        while (!exitFlag) {
            System.out.println();

            String userInput = inputScanner.nextLine();

            String[] splitInput = userInput.trim().split(" ", 2);
            String command = splitInput[0].toLowerCase();
            String argument = splitInput.length > 1 ? splitInput[1] : null;

            switch (command) {
                case "bye":
                    exitFlag = true;
                    break;
                case "list":
                    handleList();
                    break;
                case "mark":
                    handleMark(argument);
                    break;
                case "unmark":
                    handleUnmark(argument);
                    break;
                case "todo":
                    handleTodo(argument);
                    break;
                case "deadline":
                    handleDeadline(argument);
                    break;
                case "event":
                    handleEvent(argument);
                    break;
                default:
                    reply("I'm sorry, that is not a command I am familiar with.");
            }
        }

        inputScanner.close();
        reply("Thank you for your continued patronage. Goodbye!");
    }
    private static void addToTaskList(Task task) {
        if (currentIndex < taskList.length) {
            taskList[currentIndex] = task;
            currentIndex++;

            reply("I have added the following task to the list!",
                    task.toString(),
                    "You now have " + currentIndex + " tasks.");
        } else {
            reply("I have run out of space, sorry!");
        }
    }

    // Adapted from https://stackoverflow.com/a/39402538
    private static boolean isNumber(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    private static void setTaskCompletion(boolean completion, int taskIndex) {
        if (currentIndex == 0) {
            reply("Your list is empty! Add a task with the todo, deadline or event command.");
        } else if (taskIndex >= 0 && taskIndex <= currentIndex - 1) {
            taskList[taskIndex].setCompleted(completion);
            reply(completion
                    ? "Congrats on completing that! Let me just mark that as done for you."
                    : "It's okay, we can get that done later. I'll mark that as undone for you.",
                    taskList[taskIndex].toString());
        } else {
            reply("I don't see a task with that number! Try a number from 1 to " +
                    currentIndex);
        }
    }
    private static void reply(String... strings) {
        System.out.println(bottySymbol + strings[0]);
        for (int i = 1; i < strings.length; i++) {
            System.out.println(bottyIndentation + strings[i]);
        }
    }

    private static void handleList() {
        if (currentIndex == 0) {
            reply("Your list is empty! Add a task with the todo, deadline or event command.");
        } else {
            String[] content = new String[currentIndex + 1];
            content[0] = "Here you go!";

            for (int i = 1; i < currentIndex + 1; i++) {
                content[i] = i + ". " + taskList[i - 1];
            }

            reply(content);
        }
    }
    private static void handleMark(String argument) {
        if (!isNumber(argument)) {
            reply("I don't quite know what you want me to do. " +
                    "Do indicate which task to mark with its number!");
            return;
        }

        setTaskCompletion(true, Integer.parseInt(argument) - 1);
    }
    private static void handleUnmark(String argument) {
        if (!isNumber(argument)) {
            reply("I don't quite know what you want me to do. " +
                    "Do indicate which task to unmark with its number!");
            return;
        }

        setTaskCompletion(false, Integer.parseInt(argument) - 1);
    }
    private static void handleTodo(String argument) {
        Todo todo = Todo.generateFromString(argument);
        if (todo == null) {
            reply("I am unable to add that todo! Please ensure that the description is not blank");
        } else {
            addToTaskList(todo);
        }
    }
    private static void handleDeadline(String argument) {
        Deadline deadline = Deadline.generateFromString(argument);
        if (deadline == null) {
            reply("I am unable to add that deadline! Please provide details " +
                    "in the following format: [description] /by [deadline]");
        } else {
            addToTaskList(deadline);
        }
    }
    private static void handleEvent(String argument) {
        Event event = Event.generateFromString(argument);
        if (event == null) {
            reply("I am unable to add that event! Please provide details in " +
                    "the following format: [description] /from [start] /to [end]");
        } else {
            addToTaskList(event);
        }
    }
}
