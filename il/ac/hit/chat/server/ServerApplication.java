package il.ac.hit.chat.server;

import il.ac.hit.chat.ConnectionProxy;
import java.io.*;
import java.net.*;

/**
 * The ServerApplication class represents the main server application for the chat server.
 */
public class ServerApplication {
    public static void main(String args[]) {
        ServerSocket server = null;
        MessageBoard mb = new MessageBoard();

        try {
            server = new ServerSocket(1300, 5);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Socket socket = null;
        ClientDescriptor client = null;
        ConnectionProxy connection = null;

        // Continuously accept client connections
        while (true) {
            try {
                socket = server.accept();
                connection = new ConnectionProxy(socket);
                client = new ClientDescriptor();
                connection.addConsumer(client);
                client.addConsumer(mb);
                mb.addConsumer(connection);
                connection.start();

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
