package il.ac.hit.chat.server;

import il.ac.hit.chat.ConnectionProxy;
import il.ac.hit.chat.StringConsumer;
import il.ac.hit.chat.StringProducer;
import java.io.IOException;
import java.util.*;

import javax.lang.model.util.ElementScanner6;

/**
 * The MessageBoard class represents a message board that handles communication between clients.
 * It implements the StringConsumer and StringProducer interfaces.
 */
public class MessageBoard implements StringConsumer, StringProducer {

    private Vector<ConnectionProxy> connections = new Vector<ConnectionProxy>();
    private List<String> names = new ArrayList<>();
    private String connectedUsers;

    /**
     * Consumes the incoming text message and performs the necessary actions based on the content.
     * Implements the consume method from the StringConsumer interface.
     *
     * @param text the text message to consume
     */
    @Override
    public void consume(String text) {
        String temp = text;
        // Hanling with the text : "name disconnected". Removing the name from the list.
        if (temp.endsWith("disconnected")) {
            int indexDisonnected = temp.lastIndexOf("disconnected");
            if (indexDisonnected != -1) {
                // Extract the name before "disconnected"
                String name = temp.substring(0, indexDisonnected).trim();
                removeName(name);
            }
            for (ConnectionProxy connection : connections) {
                connection.consume(text);
                connection.consume(connectedUsers);
            }

            // Handling the text : "name connected". Inserting to the list of names. 
            //Also checks if the name the user has chosen exists in the system
        } else if (temp.endsWith("connected")) {
            int counterNames = 0;
            int connectedIndex = temp.lastIndexOf("connected");
            if (connectedIndex != -1) {
                // Extract the name before "connected"
                String name = temp.substring(0, connectedIndex).trim();
                for (ConnectionProxy connection : connections) {
                    if (connection.getNameHost().equals(name)) {
                        counterNames++;
                        if (counterNames > 1) {
                            connection.consume("This NickName is taken. Please try a different Nickname");
                            removeName(name);
                            return;
                        }
                    }
                }
                addName(name);
            }
            for (ConnectionProxy connection : connections) {
                connection.consume(text);
                connection.consume(connectedUsers);
            }

            // Handling with the user choise to send message for Everyone by using Iterator (DP as needed).
        } else {
            Iterator<ConnectionProxy> iterator = connections.iterator();
            if (temp.endsWith("Everyone")) {
                String endSequence = "!@#end#@!";
                int endIndex = temp.indexOf(endSequence);
                String result = temp.substring(0, endIndex);

                while (iterator.hasNext()) {
                    ConnectionProxy connection = iterator.next();
                    connection.consume(result);
                }

                // handling with the user choise to send message for spesific user by using command (DS as needed)
            } else {
                // Substring the reciver name
                String endSequence = "!@#end#@!";
                int endIndex = temp.indexOf(endSequence);
                String reciver = temp.substring(endIndex + endSequence.length());

                //Substring the sender name
                String tempText = text;
                String separator = ":";
                int separatorIndex = tempText.indexOf(separator);
                String sender = tempText.substring(0, separatorIndex);

                //Substring the message
                String anotherTempText = text;
                int startIndex = anotherTempText.indexOf(separator);
                String message = anotherTempText.substring(startIndex + separator.length(), endIndex);

                // Send the message to the sender and the reciver
                for (ConnectionProxy connection : connections) {
                    if (connection.getNameHost().equals(reciver)) {
                        connection.consume(sender + " : " + message);
                    }
                    if (connection.getNameHost().equals(sender)) {
                        connection.consume(sender + " : " + message);
                    }
                }
            }
        }
    }

    /**
     * Adds a StringConsumer to the message board's connections.
     * Implements the addConsumer method from the StringProducer interface.
     *
     * @param consumer the StringConsumer to add
     */
    @Override
    public void addConsumer(StringConsumer consumer) {
        connections.add((ConnectionProxy) consumer);
    }

    /**
     * Adds a name to the list of connected users.
     *
     * @param name the name to add
     */
    public void addName(String name) {
        names.add(name);
        this.connectedUsers = String.join("!@#end#@!", names);
        this.connectedUsers += "!@#end#@!";
    }

    /**
     * Removes a StringConsumer from the message board's connections.
     * Implements the removeConsumer method from the StringProducer interface.
     *
     * @param consumer the StringConsumer to remove
     */
    @Override
    public void removeConsumer(StringConsumer consumer) {
        connections.remove((ConnectionProxy) consumer);
    }

    /**
     * Removes a name from the list of connected users.
     *
     * @param name the name to remove
     */
    public void removeName(String name) {
        names.remove(name);
        this.connectedUsers = String.join("!@#end#@!", names);
        this.connectedUsers += "!@#end#@!";
    }
}
