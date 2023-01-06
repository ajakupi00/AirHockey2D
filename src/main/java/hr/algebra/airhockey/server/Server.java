package hr.algebra.airhockey.server;

import hr.algebra.airhockey.jndi.helper.JndiHelper;
import hr.algebra.airhockey.jndi.helper.ServerConfigurationKey;
import hr.algebra.airhockey.message.LoginMessage;

import javax.naming.NamingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int NUMBER_OF_PLAYERS = 2;
    public static String HOST;
    public static int PORT;

    private static Map<Integer, Socket> playersData;

    public static void main(String[] args) throws SocketException {
        playersData = new HashMap<>();
        configureProperties();
        listenToRequestsOnPort();
    }

    public static void configureProperties(){
        try {
            HOST = JndiHelper.getConfigurationParameter(
                    ServerConfigurationKey.GAME_SERVER_IP);
            PORT = Integer.parseInt(JndiHelper.getConfigurationParameter(
                    ServerConfigurationKey.GAME_SERVER_PORT));
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listenToRequestsOnPort() {
        try(ServerSocket serverSocket = new ServerSocket(PORT)){
            System.out.println("Server is listening on port: " + serverSocket.getLocalPort());

            while (playersData.size() < 2){
                Socket clientSocket = serverSocket.accept();
                playersData.put(clientSocket.getPort(), clientSocket);
                System.out.println("Client connected from port: " + clientSocket.getPort());
                new Thread(() -> processSerializableClient(clientSocket)).start();
            }
            System.err.println("Server has 2 players connected.\nServer will shut down, his work is done.");

        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    private static void processSerializableClient(Socket clientSocket) {
        try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream oos = new ObjectOutputStream((clientSocket.getOutputStream())))
        {

            LoginMessage loginMessage = (LoginMessage) ois.readObject();

            System.out.println("Received login message: " + loginMessage.getPlayerID() + " "
                    + loginMessage.getUsername());

            oos.writeObject("Login success");
            System.out.println("Server sent login success message to " + loginMessage.getUsername()
                    + " !");
            
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
