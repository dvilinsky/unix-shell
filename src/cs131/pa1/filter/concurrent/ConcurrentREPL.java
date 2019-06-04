package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;
import cs131.pa1.filter.ProcessManager;
import cs131.pa1.filter.exceptions.InvalidCommandException;
import cs131.pa1.filter.exceptions.InvalidParameterException;

import java.util.List;
import java.util.Scanner;

/**
 * Client-facing REPL
 */
public class ConcurrentREPL {

	public static String currentWorkingDirectory;

    public static void main(String[] args) {
	    currentWorkingDirectory = System.getProperty("user.dir");
        Scanner console = new Scanner(System.in);
        String command;
        System.out.print(Message.WELCOME);
        List<ConcurrentFilter> filters;
        while (true) {
            System.out.print(Message.NEWCOMMAND);
            command = console.nextLine().trim();
            if (command.equals("exit")) {
                break;
            }
            ProcessManager manager = new ProcessManager(command);
            try {
                manager.executeCommand();
            } catch (InvalidCommandException |InvalidParameterException |RuntimeException e) {
                System.out.print(e.getMessage());
            }
        }
        System.out.print(Message.GOODBYE);
    }

}
