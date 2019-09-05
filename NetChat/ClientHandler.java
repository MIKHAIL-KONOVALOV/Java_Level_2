package NetChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Mihail
 * Created on 03.09.2019
 */
public class ClientHandler implements Runnable {
    public static int clientCount = 0;
    private final static  String EXIT_MSG_TEMPLATE = "EXIT";
    private Server server;
    private DataOutputStream outputChannel;
    private DataInputStream inputChannel;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ClientHandler(Server server, Socket socket) {

        this.server = server;
        try {
        this.outputChannel = getOutputChannel(socket);
        this.inputChannel = getInputChannel(socket);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DataInputStream getInputChannel(Socket socket) throws IOException {
        return new DataInputStream(socket.getInputStream());
    }

    private DataOutputStream getOutputChannel(Socket socket) throws IOException{
        return new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        System.out.println("We have new client");
        incrementClientCount();

        try {
            while (true) {
                String clientMessage = getClientMessage();
                char[] arrayOfChars = clientMessage.toCharArray();

                if (clientExit(clientMessage)){
                    break;
                }

                if ((arrayOfChars[0] == '/') && (arrayOfChars[1] == 'w')) { //Проверка что это личное сообщение
                    char[] nick = new char[20];

                    for (int i = 3; i < arrayOfChars.length ; i++) { // Читается до первого пробела ник идущий за /w
                        if (!Character.isWhitespace(arrayOfChars[i])){
                            nick[i-3] = arrayOfChars[i];
                        } else break;
                    }
                    server.sendMessageToNick(clientMessage, new String(nick));// В классе Server создан новый метод

                } else {                                                           // Иначе все как обычно
                    System.out.println("Message from client = " + clientMessage);
                    server.sendMessageToAll(clientMessage);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            exit();
        }
    }

    private void exit() {
        decreaseClientCount();
        server.disconnectClient(this);

        try {
        outputChannel.close();
        inputChannel.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void decreaseClientCount() {
        clientCount--;
    }

    private String getClientMessage() throws IOException {
        return inputChannel.readUTF();
    }

    private boolean clientExit(String clientMessage) {
        return clientMessage.equalsIgnoreCase(EXIT_MSG_TEMPLATE);
    }

    private void incrementClientCount() {
        clientCount++;
    }


    public void sendMessage(String message) throws IOException{
            outputChannel.writeUTF(message);
            outputChannel.flush();
    }
}
