package cs131.pa1.filter.exceptions;

/**
 * Exception thrown when a given subcommand has the wrong number of parameters
 */
public class InvalidParameterException extends Exception {
    public InvalidParameterException(String msg) {
        super(msg);
    }
}
