package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * This class does a few things:
 *      *Checks to see that a userCommand has been typed in
 *      *Checks that the userCommand is valid
 *      *Checks that the userCommand has the right number of parameters
 */
public class CommandParser {
    private Set<String> commandSet = putCommands();
    private String commandKeyword;
    private String commandParams;
    private String userCommand;

    public CommandParser(String command) {
        this.userCommand = command;
        this.commandKeyword = "";
        this.commandParams = "";
    }

    public boolean isValidCommand() {
         return commandSet.contains(this.commandKeyword);
    }

    //Pre: isValidCommand() has been called on this instance of syntax checker
    //and has returned true
    public boolean hasValidParameters() {
        Set<String> needParams = new HashSet<>(Arrays.asList("cat", "grep", "cd", ">"));
        Set<String> noParams = new HashSet<>(Arrays.asList("ls", "pwd", "uniq", "wc", ""));
        return (needParams.contains(commandKeyword) && !commandParams.equals("")) ||
                (noParams.contains(commandKeyword) && commandParams.equals(""));
    }

    public String getCommandParams() {
        return commandParams;
    }

    public String getCommandKeyword() {
        return commandKeyword;
    }

    private Set<String> putCommands() {
        return Stream.of("cat", "ls", "pwd", "grep", "uniq", "wc", "cd", "", ">")
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Takes in a userCommand, separated by pipes, and parses it into distinct
     * subcommands
     */
    public List<String> splitCommands() {
        //the following two lines could probably be combined into one regex,
        //but I don't know how
        userCommand = userCommand.replaceAll("\\s+[|]\\s+", "|");
        userCommand = userCommand.replaceAll("\\s+[>]\\s+", "> ");
        List<String> subCommands = Arrays.stream(userCommand.split("[|]|(?=>)"))
                .collect(Collectors.toList());
        if (!userCommand.contains(">")) {
            subCommands.add(""); //add empty string for StreamFilter
        }
        checkStreamFilter(subCommands);
        return subCommands;
    }

    private static void checkStreamFilter(List<String> subCommands) {
        if (subCommands.size() == 1) {
            throw new IllegalStateException(Message.REQUIRES_INPUT.with_parameter(subCommands.get(0)));
        }
        for (int i = 0; i < subCommands.size(); i++) {
            if (subCommands.get(i).startsWith(">") && i != subCommands.size() -1) {
                throw new IllegalStateException(Message.CANNOT_HAVE_OUTPUT.with_parameter(subCommands.get(i)));
            }
        }
    }


    public void split(String command) {
        if (command.contains(" ")) {
            this.commandKeyword = command.substring(0, command.indexOf(" "));
            this.commandParams = command.substring(command.indexOf(" ") + 1, command.length());
        } else {
            this.commandKeyword = command;
            this.commandParams = "";
        }
    }
}
