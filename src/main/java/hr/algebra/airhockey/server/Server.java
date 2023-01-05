package hr.algebra.airhockey.server;

import hr.algebra.airhockey.jndi.helper.JndiHelper;
import hr.algebra.airhockey.jndi.helper.ServerConfigurationKey;
import hr.algebra.airhockey.message.LoginMessage;
import hr.algebra.airhockey.models.GameStateDto;

import javax.naming.NamingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Server {
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
            for (Socket socket : playersData.values()) {
                try( ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())){
                    oos.writeObject("CONTINUE_GAME");
                }catch (IOException ex){
                    System.out.println("NOCANDO FRENDE");
                }

            }
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



            /*
            if(playersData.size() == NUMBER_OF_PLAYERS) {
                for(Integer port : playersData.keySet()) {
                    if(clientSocket.getPort() == port) {
                        oos.writeObject("ENABLE_GAME");
                    }
                    else {
                        ObjectOutputStream oosPlayerOne
                                = new ObjectOutputStream(playersData.get(port).getOutputStream());
                        oosPlayerOne.writeObject("ENABLE_GAME");
                        ServerSocket serverSocket = new ServerSocket(PORT);
                        Socket clientPlayerOne = serverSocket.accept();
                    }
                }
            }
            */
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Integer counter = 0;

        while(true) {

            try {

                Socket serverClientSocket = new Socket(Server.HOST, 2022);

                ObjectOutputStream serverClientObjectOutputStream
                        = new ObjectOutputStream(serverClientSocket.getOutputStream());

                System.out.println("Server sent message at: " + LocalDateTime.now());
                //oos.writeObject("Test");
                GameStateDto gameStateDto = new GameStateDto();
                gameStateDto.setName(String.valueOf(counter++));
                serverClientObjectOutputStream.writeObject(gameStateDto);


                Thread.sleep(2000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
