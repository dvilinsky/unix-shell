package cs131.pa1.filter.exceptions;

/**
 * Exception thrown when a subcommand is not recognized
 * */
public class InvalidCommandException extends Exception{
    public InvalidCommandException(String msg) {
        super(msg);
    }
}
