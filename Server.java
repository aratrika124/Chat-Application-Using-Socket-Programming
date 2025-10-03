import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket server;
    private Socket socket;
    private BufferedReader br;
    private PrintWriter out;

    private JFrame frame;
    private JTextArea chatArea;
    private JTextField messageField;
    private JButton sendButton;
    private String username;

    public Server() {
        initializeUI();

        try {
            server = new ServerSocket(2802);
            appendToChatArea("[Server]: Waiting for client connection...");
        } catch (IOException e) {
            appendToChatArea("[Error]: Could not start server.");
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        frame = new JFrame("Server");
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
        JLabel usernameLabel = new JLabel("Your Username:");
        JTextField usernameField = new JTextField(10);
        JButton startButton = new JButton("Start Server");
        JButton quitButton = new JButton("Quit");

        topPanel.add(usernameLabel);
        topPanel.add(usernameField);
        topPanel.add(startButton);
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
        startButton.addActionListener(e -> {
            String usernameInput = usernameField.getText().trim();
            if (!usernameInput.isEmpty()) {
                username = usernameInput;
                startButton.setEnabled(false);
                usernameField.setEditable(false);

                new Thread(this::acceptClient).start();
            }
        });

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());
        quitButton.addActionListener(e -> shutdown());

        frame.setVisible(true);
    }

    private void acceptClient() {
        try {
            socket = server.accept();

            // Read client username first
            BufferedReader tempReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String clientUsername = tempReader.readLine();

            appendToChatArea("[Server]: Client \"" + clientUsername + "\" wants to connect.");

            int result = JOptionPane.showConfirmDialog(frame,
                    "Accept connection from " + clientUsername + "?",
                    "Client Connection",
                    JOptionPane.YES_NO_OPTION);

            if (result != JOptionPane.YES_OPTION) {
                appendToChatArea("[Server]: Connection rejected.");
                socket.close();
                return;
            }

            appendToChatArea("[Server]: Connection accepted.");
            br = tempReader;
            out = new PrintWriter(socket.getOutputStream(), true);

            messageField.setEnabled(true);
            sendButton.setEnabled(true);
            messageField.requestFocus();

            startReading();

        } catch (IOException e) {
            appendToChatArea("[Error]: Unable to accept client.");
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
            if (server != null) server.close();
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
                appendToChatArea("[Server]: Client disconnected.");
                shutdown();
            } catch (Exception e) {
                appendToChatArea("[Error]: Connection lost.");
                e.printStackTrace();
            }
        };
        new Thread(reader).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Server::new);
    }
}
