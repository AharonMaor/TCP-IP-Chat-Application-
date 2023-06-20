package il.ac.hit.chat;

/**
 * The ChatException class represents an exception specific to the chat application.
 * It is a subclass of the general Exception class.
 */
public class ChatException extends Exception {

    /**
     * Constructs a new ChatException with the specified error message.
     *
     * @param message The error message associated with the exception.
     */
    public ChatException(String message) {
        super(message);
    }

    /**
     * Constructs a new ChatException with the specified error message and cause.
     *
     * @param message The error message associated with the exception.
     * @param cause   The cause of the exception.
     */
    public ChatException(String message, Throwable cause) {
        super(message, cause);
    }
}
