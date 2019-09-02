package Chat;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Mihail
 * Created on 03.09.2019
 */
public class Client {
    private final static int SERVER_PORT = 8888;

    public static void main(String[] args) {
        try {
            InetAddress serverHost = InetAddress.getLocalHost();
            Socket socket = new Socket(serverHost,SERVER_PORT);

            DataOutputStream outputChanel = getOutcomeChanel(socket);
            DataInputStream inputChanel = getIncomeChanel(socket);

            BufferedReader terminalReader = getTerminalReader();

            Thread serverListen = new Thread() {    //Добаил параллельный поток прослушивания сервера
                @Override
                public void run() {
                    while (true) {
                        try {
                            String msgFromServer = inputChanel.readUTF();
                            System.out.println("We have new message from server: " + msgFromServer);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            serverListen.start();

            while (true) {
                String msg = terminalReader.readLine();
                System.out.println("We are sending to server message: " + msg);
                outputChanel.writeUTF(msg);
                outputChanel.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static BufferedReader getTerminalReader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }

    private static DataOutputStream getOutcomeChanel(Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        return new DataOutputStream(outputStream);
    }

    private static DataInputStream getIncomeChanel(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        return new DataInputStream(inputStream);
    }
}
