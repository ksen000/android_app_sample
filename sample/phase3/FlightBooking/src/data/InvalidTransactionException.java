package data;

/**
 * A class for catching invalid transaction.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class InvalidTransactionException extends Exception {

    /**
     * Initializes a new InvalidTransactionException with no arguments.
     */
    public InvalidTransactionException() {
    }

    /**
     * Initializes a new InvalidTransactionException with the given message.
     * @param message the message that reports what happened.
     */
    public InvalidTransactionException(String message) {
        super(message);
    }
}
