package hr.algebra.airhockey.Threads;

import hr.algebra.airhockey.jndi.helper.JndiHelper;
import hr.algebra.airhockey.jndi.helper.ServerConfigurationKey;
import hr.algebra.airhockey.models.GameStateDto;
import hr.algebra.airhockey.server.Server;

import javax.naming.NamingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class ClientThread implements Runnable {
    private GameStateDto gameStateDto;
    private int serverSocketPort;

    public ClientThread(GameStateDto gameStateDto) {
        this.gameStateDto = gameStateDto;
        configurePort();

    }

    private void configurePort() {
        String serverSocketPortString = null;
        try {
            serverSocketPortString = JndiHelper.getConfigurationParameter(
                    ServerConfigurationKey.SERVER_SOCKET_PORT);
            serverSocketPort = Integer.parseInt(serverSocketPortString);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(serverSocketPort)) {
            System.err.println("Client #1 listening on port: " + serverSocket.getLocalPort());

            while (true) {

                System.out.println("Client thread started and waiting for server response!");

                Socket clientSocket = serverSocket.accept();

                System.out.println("Server message accepted!");

                System.out.println("Server message accepted at " + LocalDateTime.now());

                try (ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                     ObjectOutputStream oos = new ObjectOutputStream((clientSocket.getOutputStream()))) {
                    GameStateDto newGameStateDto = (GameStateDto) ois.readObject();
                    System.out.println("Received new game state DTO with the name: " + newGameStateDto.getName());
                    System.out.println("New game state loaded!");
                }
                catch(IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
