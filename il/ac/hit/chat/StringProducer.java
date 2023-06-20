package il.ac.hit.chat;

/**
 * The StringProducer interface defines the contract for a string producer.
 * Objects implementing this interface can produce strings and notify registered consumers.
 */
public interface StringProducer {
    /**
     * Adds a string consumer to the producer.
     *
     * @param consumer The StringConsumer object to be added.
     */
    public void addConsumer(StringConsumer consumer);

    /**
     * Removes a string consumer from the producer.
     *
     * @param consumer The StringConsumer object to be removed.
     */
    public void removeConsumer(StringConsumer consumer);
}
