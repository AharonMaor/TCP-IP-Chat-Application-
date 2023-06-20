package il.ac.hit.chat.client;

import il.ac.hit.chat.ConnectionProxy;
import il.ac.hit.chat.StringConsumer;
import il.ac.hit.chat.StringProducer;
import il.ac.hit.chat.server.MessageBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.List;

/**
 * A simple client GUI for a chat application.
 */
public class SimpleClientGUI implements StringConsumer, StringProducer {
    private JFrame frame;
    private JPanel panelTop, panelBottom, panelCenter;
    private JTextArea textArea;
    private JTextField nickNameField, server, port, writingArea;
    private JButton connect, disconnect, send;
    private JScrollPane scroll;
    private JLabel toLabel, message;
    private JComboBox participants;
    private boolean isConnected;
    private String nickName;
    private ConnectionProxy con;
    private Socket socket;
    Vector<StringConsumer> consumers;

    /**
     * Creates an instance of the SimpleClientGUI class and UI for the user. Implements Composite DP.
     */
    public SimpleClientGUI() {
        isConnected = false;
        nickNameField = new JTextField(10);
        server = new JTextField(10);
        port = new JTextField(10);
        writingArea = new JTextField(50);
        textArea = new JTextArea(25, 75);
        connect = new JButton("Connect");
        disconnect = new JButton("Disconnect");
        send = new JButton("Send");
        frame = new JFrame("Chat");
        scroll = new JScrollPane(textArea);
        participants = new JComboBox<>();
        toLabel = new JLabel();
        message = new JLabel();

        panelTop = new JPanel();
        panelBottom = new JPanel();
        panelCenter = new JPanel();
        panelCenter.setBackground(new Color(234, 237, 244));
        panelTop.setBackground(new Color(101, 138, 206));
        panelBottom.setBackground(new Color(101, 138, 206));
        textArea.setEnabled(false);

        frame.setLayout(new BorderLayout());
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        panelTop.add(nickNameField);
        panelTop.add(server);
        panelTop.add(port);
        panelTop.add(connect);
        panelTop.add(disconnect);

        panelBottom.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelBottom.add(message);
        panelBottom.add(writingArea);
        panelBottom.add(toLabel);
        panelBottom.add(participants);
        //panelBottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelBottom.add(send);

        panelCenter.add(scroll);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.add(panelTop, BorderLayout.NORTH);
        frame.add(panelBottom, BorderLayout.SOUTH);
        frame.add(panelCenter, BorderLayout.CENTER);

        frame.setSize(900, 550);
        frame.setVisible(true);
        frame.requestFocusInWindow(); // Remove focus from nickname field
        participants.addItem("Everyone");
        toLabel.setText(" To: ");
        toLabel.setForeground(Color.white);
        message.setText("   Message: ");
        message.setForeground(Color.white);

        // Apply custom styles
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCenter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        writingArea.setPreferredSize(new Dimension(300, 30));
        participants.setPreferredSize(new Dimension(100, 30));
    }

    /**
     * Sets the connection status of the client.
     *
     * @param isConnected set true for isConnected
     */
    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
        this.connect.setEnabled(!isConnected);
        this.send.setEnabled(isConnected);
        this.disconnect.setEnabled(isConnected);
    }

    /**
     * Sets the disconnected status of the client.
     *
     * @param sc the StringConsumer
     */
    public void setDisconnected(StringConsumer sc) {
        if (this.isConnected) {
            this.isConnected = false;
            this.disconnect.setEnabled(false);
            this.connect.setEnabled(true);
            this.send.setEnabled(false);
        }
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        consumers.addElement(sc);
    }

    @Override
    public void removeConsumer(StringConsumer sc) {
        consumers.remove(sc);
    }

    // The Consume from the MessageBoard class can be 3 things :
    //1. The first if : message that came for user / all users
    //2. The second else if : Message from the server that the name the user choose is taken.
    //3. The last else : List of the connected users with separator "!@#end#@!". We updating the JComboBox of participants with it
    @Override
    public void consume(String str) {
        if (!(str.contains("!@#end#@!"))) {
            textArea.append(str + "\n");
        } else if (str.equals("This NickNmae is taken. Please try a diffrent Nickname")) {
            textArea.append(str + "\n");
        } else {
            String[] names = str.split("!@#end#@!");

            this.participants.removeAllItems();
            this.participants.addItem("Everyone");
            for (String name : names) {
                if (!(name.equals(this.nickName))) {
                    this.participants.addItem(name);
                }
            }
            setConnected(true);
        }
    }

    /**
     * Changes the nickname field text behavior.
     */
    public void changeNicknameText() {
        this.nickNameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nickNameField.getText().equals("Nickname")) {
                    nickNameField.setText(""); // Clear the default text when focused
                    nickNameField.setForeground(Color.BLACK); // Change the text color
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nickNameField.getText().isEmpty()) {
                    nickNameField.setText("Nickname"); // Restore the default text if empty
                    nickNameField.setForeground(Color.GRAY); // Change the text color back to gray
                }
            }
        });
    }

    /**
     * Changes the server field text behavior.
     */
    public void changeServerText() {
        this.server.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (server.getText().equals("127.0.0.1")) {
                    server.setText(""); // Clear the default text when focused
                    server.setForeground(Color.BLACK); // Change the text color
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (server.getText().isEmpty()) {
                    server.setText("127.0.0.1"); // Restore the default text if empty
                    server.setForeground(Color.GRAY); // Change the text color back to gray
                }
            }
        });
    }

    /**
     * Changes the port field text behavior.
     */
    public void changePortText() {
        this.port.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (port.getText().equals("1300")) {
                    port.setText(""); // Clear the default text when focused
                    port.setForeground(Color.BLACK); // Change the text color
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (port.getText().isEmpty()) {
                    port.setText("1300"); // Restore the default text if empty
                    port.setForeground(Color.GRAY); // Change the text color back to gray
                }
            }
        });
    }

    /**
     * Checks if the given IP address is valid : "127.x.x.x"
     *
     * @param ipAddress the IP address to validate
     * @return true if the IP address is valid, false otherwise
     */
    public boolean isValidIPAddress(String ipAddress) {
        String[] octets = ipAddress.split("\\.");

        if (octets.length != 4) {
            return false;
        }

        if (!octets[0].equals("127")) {
            return false;
        }

        for (int i = 1; i < 4; i++) {
            int octetValue = Integer.parseInt(octets[i]);

            if (octetValue < 0 || octetValue > 255) {
                return false;
            }
        }

        return true;
    }

    /**
     * The entry point of the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimpleClientGUI gui = new SimpleClientGUI();
        gui.nickNameField.setText("Nickname");
        gui.server.setText("127.0.0.1");
        gui.port.setText("1300");

        gui.changeNicknameText();
        gui.changeServerText();
        gui.changePortText();

        gui.disconnect.setEnabled(false);
        gui.send.setEnabled(false);

        // Handling with the "connect" button. Implements State and Observer DP.
        gui.connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    //Chceking the port input from user
                    if (Integer.parseInt(gui.port.getText()) != 1300) {
                        throw new IOException("Incorrect port");
                    }
                    // Cheking the Ip adress input from user
                    if (!(gui.isValidIPAddress(gui.server.getText()))) {
                        throw new IOException("Incorrect server");
                    }
                    //Creating new socket and ConnectionProxy. Implments Proxy DP
                    gui.socket = new Socket(gui.server.getText(), Integer.parseInt(gui.port.getText()));
                    gui.nickName = new String(gui.nickNameField.getText());
                    gui.con = new ConnectionProxy(gui.socket, gui.nickName);
                    gui.con.addConsumer(gui);
                    gui.con.start();
                    gui.con.consume(gui.nickName + " connected");
                } catch (IOException er) {
                    er.printStackTrace();
                    gui.consume(er.getMessage());
                    gui.setConnected(false);
                    return;
                }
            }

        });

        // Handling with the "Disconnect" button. Implements State and Observer DP.
        gui.disconnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.setDisconnected(gui);
                gui.con.removeConsumer(gui);
                gui.con.consume(gui.nickName + " disconnected");

            }
        });

        // Handling with the "send" button. Implements State and Observer DP.
        gui.send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.con.consume(gui.nickName + ": " + gui.writingArea.getText() + "!@#end#@!" + gui.participants.getSelectedItem());
                gui.writingArea.setText("");
            }
        });
    }
}
