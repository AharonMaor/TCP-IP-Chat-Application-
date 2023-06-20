package il.ac.hit.chat;

import java.net.Socket;
import java.io.*;

/**
 * The ConnectionProxy class represents a connection proxy for a chat client.
 * It extends Thread and implements both the StringConsumer and StringProducer interfaces.
 */
public class ConnectionProxy extends Thread implements StringConsumer, StringProducer {

    private StringConsumer consumer = null;
    private Socket socket = null;
    private InputStream is = null;
    private OutputStream os = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private String name;

    /**
     * Constructs a new ConnectionProxy with the specified socket.
     *
     * @param socket The socket for the connection.
     * @throws IOException If an I/O error occurs while initializing the streams.
     */
    public ConnectionProxy(Socket socket) throws IOException {
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);
        setNameHost(dis.readUTF());
    }

    /**
     * Constructs a new ConnectionProxy with the specified socket and name.
     *
     * @param socket The socket for the connection.
     * @param name   The name of the host associated with the connection.
     * @throws IOException If an I/O error occurs while initializing the streams.
     */
    public ConnectionProxy(Socket socket, String name) throws IOException {
        this.name = name;
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);
        dos.writeUTF(getNameHost());
    }

    /**
     * Sets the name of the host associated with the connection.
     *
     * @param name The name of the host.
     */
    public void setNameHost(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the host associated with the connection.
     *
     * @return The name of the host.
     */
    public String getNameHost() {
        return this.name;
    }

    @Override
    public void consume(String str) {
        try {
            dos.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addConsumer(StringConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void removeConsumer(StringConsumer consumer) {
        this.consumer = null;
    }

    /**
     * Runs the connection proxy thread, continuously reading from the input stream
     * and notifying the registered consumer.
     */
    public void run() {
        while (true) {
            try {
                consumer.consume(dis.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
