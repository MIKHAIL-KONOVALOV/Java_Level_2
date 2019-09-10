package NetChat.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Mihail
 * Created on 08.09.2019
 */
public class ClientWindow extends JFrame {
    private final static int SERVER_PORT = 8877;
    private final static String SERVER_HOST = "127.0.0.1";

    private DataInputStream incomeChannel;
    private DataOutputStream outputChannel;

    private JTextArea textArea;
    private JTextField messageInputField;
    private JTextField usernameInputField;
    private JLabel numberOfClients;

    public ClientWindow() {
        try {
            final Socket socket = connectToServer();
            incomeChannel = getIncomeChannel(socket);
            outputChannel = getOutputChannel(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }

        createView();
    }

    private void createView() {
        setBounds(600, 200, 600, 400);
        setTitle("Client Window");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);

        JScrollPane jsp = new JScrollPane(textArea);
        add(jsp,BorderLayout.CENTER);

        numberOfClients = new JLabel("Clients Count :");
        add(numberOfClients,BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.SOUTH);

        JButton sendButton = getSendButton();
        bottomPanel.add(sendButton, BorderLayout.EAST);

        initMessageInputField(bottomPanel);
        initUsernameInputField(bottomPanel);
        
        launchServerListner();
        windowCloseBehavor();


        setVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                long t1 = System.currentTimeMillis();
                while (true) {
                    if ( (System.currentTimeMillis() - t1) > 12000 ){
//                        && (usernameInputField.getText().isEmpty() || usernameInputField.getText().equalsIgnoreCase("Input your name") )) {
                        windowCloseBehavor();
                        break;
                    }
                }
            }
        }).start();
    }

    private void windowCloseBehavor() {

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    outputChannel.writeUTF(usernameInputField.getName() + " exit from chat");
                    outputChannel.flush();
                    outputChannel.writeUTF("EXIT");
                    outputChannel.flush();
                    outputChannel.close();
                    incomeChannel.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void launchServerListner() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String messageFromServer = incomeChannel.readUTF();
                        if (messageFromServer.indexOf("Clients Count :") == 0) {
                            numberOfClients.setText(messageFromServer);
                        } else {
                            textArea.append(messageFromServer);
                            textArea.append("\n");
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initUsernameInputField(JPanel bottomPanel) {
        usernameInputField = new JTextField();
        usernameInputField.setText("Input your name");
        usernameInputField.addFocusListener(new FocusAdapter() {
             @Override
             public void focusGained(FocusEvent e) {
                 if (usernameInputField.getText().equalsIgnoreCase("Input your name")) {
                     usernameInputField.setText("");
                 }
             }

             @Override
             public void focusLost(FocusEvent e) {
                 if (usernameInputField.getText().equalsIgnoreCase("")) {
                     usernameInputField.setText("Input your name");
                 }
             }
        });

                bottomPanel.add(usernameInputField, BorderLayout.WEST);
    }

    private void initMessageInputField(JPanel bottomPanel) {
        messageInputField = new JTextField();
        messageInputField.setText("Input your message:");
        messageInputField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (messageInputField.getText().equalsIgnoreCase("Input your message:")) {
                    messageInputField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (messageInputField.getText().equalsIgnoreCase("")) {
                    messageInputField.setText("Input your message:");
                }
            }
        });
        bottomPanel.add(messageInputField, BorderLayout.CENTER);
    }

    private JButton getSendButton() {
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!messageInputField.getText().trim().isEmpty() && (!messageInputField.getText().equals("Input your message:")) && !usernameInputField.getText().isEmpty()) {
                    sendMessage(usernameInputField.getText() + ": " + messageInputField.getText().trim());
                    messageInputField.grabFocus();
                }
            }
        });
        return sendButton;
    }

    private void sendMessage(String message) {
        try {
        outputChannel.writeUTF(message);
        outputChannel.flush();
        messageInputField.setText("");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DataOutputStream getOutputChannel(Socket socket) throws IOException {
        return new DataOutputStream(socket.getOutputStream());
    }

    private DataInputStream getIncomeChannel(Socket socket) throws IOException {
        return new DataInputStream(socket.getInputStream());
    }

    private Socket connectToServer() throws IOException {
        return new Socket(SERVER_HOST, SERVER_PORT);
    }
}
