package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;
import cs131.pa1.filter.exceptions.InvalidCommandException;
import cs131.pa1.filter.exceptions.InvalidParameterException;
import cs131.pa1.filter.filters.*;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentCommandBuilder {

	public static List<ConcurrentFilter> createFiltersFromCommand(String command) throws InvalidCommandException,
            InvalidParameterException {
        CommandParser parser = new CommandParser(command);
        List<String> subCommands = parser.splitCommands();
	    List<ConcurrentFilter> filters = new ArrayList<>();
	    for (String subCommand : subCommands) {
	        parser.split(subCommand);
            if (!parser.isValidCommand()) {
	            throw new InvalidCommandException(Message.COMMAND_NOT_FOUND.with_parameter(subCommand));
            } else if (!parser.hasValidParameters()) {
                throw new InvalidParameterException(Message.REQUIRES_PARAMETER.with_parameter(parser.getCommandKeyword()));
            }
            filters.add(constructFilterFromSubCommand(parser));
        }
        linkFilters(filters);
        return filters;
	}
	
	private static ConcurrentFilter constructFilterFromSubCommand(CommandParser checker) {
		String args = checker.getCommandParams();
        switch (checker.getCommandKeyword()) {
            case "ls":
                return new LsFilter();
            case "cd":
                return new CdFilter(args);
            case "pwd":
                return new PwdFilter();
            case "grep":
                return new GrepFilter(args);
            case "cat":
                return new CatFilter(args);
            case "wc":
                return new WcFilter();
            case "uniq":
                return new UniqFilter();
            case ">":
                return new StreamFilter(args);
            case "":
                return new StreamFilter();
        }
        return null; //should never happen
	}

	private static void linkFilters(List<ConcurrentFilter> filters){
	     for (int i = 0; i < filters.size() - 1; i++) {
            ConcurrentFilter current = filters.get(i);
            current.setNextFilter(filters.get(i + 1));
        }
	}
}
