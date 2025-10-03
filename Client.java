import java.awt.*;
import java.io.*;
import java.net.Socket;
import javax.swing.*;

public class Client {

    private Socket socket;
    private BufferedReader br;
    private PrintWriter out;

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private String username;

    public Client() {
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // ===== Chat Area =====
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // ===== Top Panel =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField(10);
        JButton connectButton = new JButton("Connect");
        JButton quitButton = new JButton("Quit");

        topPanel.add(usernameLabel);
        topPanel.add(usernameField);
        topPanel.add(connectButton);
        topPanel.add(quitButton);

        frame.add(topPanel, BorderLayout.NORTH);

        // ===== Bottom Panel =====
        JPanel bottomPanel = new JPanel(new BorderLayout());
        messageField = new JTextField();
        messageField.setEnabled(false);
        sendButton = new JButton("Send");
        sendButton.setEnabled(false);

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // ===== Actions =====
        connectButton.addActionListener(e -> {
            String usernameInput = usernameField.getText().trim();
            if (!usernameInput.isEmpty()) {
                username = usernameInput;
                connectButton.setEnabled(false);
                usernameField.setEditable(false);

                new Thread(this::connectToServer).start();
            }
        });

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        quitButton.addActionListener(e -> shutdown());

        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket("127.0.0.1", 2802); // change IP if needed
            out = new PrintWriter(socket.getOutputStream(), true);

            // Send username first
            out.println(username);

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            appendToChatArea("[Client]: Connected to server.");
            messageField.setEnabled(true);
            sendButton.setEnabled(true);
            messageField.requestFocus();

            startReading();

        } catch (IOException e) {
            appendToChatArea("[Error]: Unable to connect to server.");
            e.printStackTrace();
        }
    }

    private void appendToChatArea(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty() && out != null) {
            out.println(username + ": " + message);
            appendToChatArea(username + ": " + message);
            messageField.setText("");
        }
    }

    private void shutdown() {
        try {
            if (socket != null) socket.close();
            if (br != null) br.close();
            if (out != null) out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void startReading() {
        Runnable reader = () -> {
            try {
                String message;
                while ((message = br.readLine()) != null) {
                    appendToChatArea(message);
                }
                appendToChatArea("[Client]: Connection closed by server.");
                shutdown();
            } catch (Exception e) {
                appendToChatArea("[Error]: Connection lost.");
                e.printStackTrace();
            }
        };
        new Thread(reader).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }
}
