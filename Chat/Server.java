package Chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Mihail
 * Created on 02.09.2019
 */
public class Server extends Thread {
    private final static int PORT = 8888;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = getServerSocket();
            System.out.println("We are waiting our clients");
            Socket clientSocket = waitNewClient(serverSocket);
            System.out.println("We have new client");

            DataInputStream incomeChanel = getIncomeChanel(clientSocket);

            DataOutputStream outcomeChanel = getOutcomeChanel(clientSocket);

            Thread terminalRead = new Thread() { //Добаил параллельный поток чтения из консоли
                @Override                        //и отправка клиенту
                public void run() {
                    try {
                        while (true) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                            String msg = br.readLine();
                            System.out.println("We are sending to client message: " + msg);
                            outcomeChanel.writeUTF(msg);
                            outcomeChanel.flush();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            terminalRead.start();

            String incomeMesage = null;

            while(true) {
                incomeMesage = incomeChanel.readUTF();
                System.out.println("We have new message: " + incomeMesage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DataOutputStream getOutcomeChanel(Socket clientSocket) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        return new DataOutputStream(outputStream);
    }

    private static DataInputStream getIncomeChanel(Socket clientSocket) throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        return new DataInputStream(inputStream);
    }

    private static Socket waitNewClient(ServerSocket serverSocket) throws IOException {
        return serverSocket.accept();
    }

    private static ServerSocket getServerSocket() throws IOException {
        return new ServerSocket(PORT);
    }
}
