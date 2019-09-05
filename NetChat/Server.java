package NetChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mihail
 * Created on 03.09.2019
 */
public class Server {
    private static final int PORT = 8888;
    private List<ClientHandler> clients = new ArrayList<>();

    public Server() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is launched");

            while (true) {
                final ClientHandler clientHandler = getNewClientHandler(serverSocket);
                new Thread(clientHandler).start();
                clients.add(clientHandler);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                serverSocket.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private ClientHandler getNewClientHandler(ServerSocket serverSocket) throws IOException {
        final Socket newClientSocket = getNewClientSocket(serverSocket);
        return new ClientHandler(this,newClientSocket);
    }

    private void getNewClientHandler(Socket newClientSocket) {
        ClientHandler clientHandler = new ClientHandler(this, newClientSocket);
    }

    private Socket getNewClientSocket(ServerSocket serverSocket) throws IOException {
        return serverSocket.accept();
    }

    public void sendMessageToAll(String message) {
        for (ClientHandler client : clients) {
            try {
                client.sendMessage(message);
            } catch (Exception e) {
                System.out.println("Can't send message to client.It skipped");
            }
        }
    }

    public void sendMessageToNick(String message, String nick) {
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(nick)) {        // Добавил поле nickname в классе ClientHandler
                try {                                       //Нужно только как-то его инициализировать.
                    client.sendMessage(message);
                } catch (Exception e) {
                    System.out.println("Can't send message to client.It skipped");
                }
            }
        }
    }

    public void disconnectClient(ClientHandler client) {
        sendMessageToAll("Client exit from chat. Clients count = " + ClientHandler.clientCount);
        clients.remove(client);
    }
}
