package il.ac.hit.chat;

import java.io.IOException;

/**
 * The StringConsumer interface defines the contract for a string consumer.
 * Objects implementing this interface can consume strings.
 */
public interface StringConsumer {
    /**
     * Consumes the given text.
     *
     * @param text The string to be consumed.
     * @throws IOException If an I/O error occurs during the consumption process.
     */
    public void consume(String text) throws IOException;
}
