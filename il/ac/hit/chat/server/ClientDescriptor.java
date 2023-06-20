package il.ac.hit.chat.server;

import il.ac.hit.chat.StringConsumer;
import il.ac.hit.chat.StringProducer;
import java.io.IOException;

/**
 * The ClientDescriptor class represents a client connected to the chat server.
 * It implements both the StringProducer and StringConsumer interfaces.
 */
public class ClientDescriptor implements StringProducer, StringConsumer {

    private StringConsumer consumer;

    /**
     * Consumes the received text by forwarding it to the registered consumer.
     *
     * @param text The text to be consumed.
     * @throws IOException If an I/O error occurs during the consumption process.
     */
    @Override
    public void consume(String text) throws IOException {
        this.consumer.consume(text);
    }

    /**
     * Registers a consumer to receive the consumed strings.
     *
     * @param consumer The StringConsumer object to be added as a consumer.
     */
    @Override
    public void addConsumer(StringConsumer consumer) {
        this.consumer = consumer;
    }

    /**
     * Removes a consumer from receiving the consumed strings.
     *
     * @param consumer The StringConsumer object to be removed as a consumer.
     */
    @Override
    public void removeConsumer(StringConsumer consumer) {
        this.consumer = null;
    }
}
