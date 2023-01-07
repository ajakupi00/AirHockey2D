package hr.algebra.airhockey.server;

import hr.algebra.airhockey.models.Game;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {

    public static void main(String[] args) {

        try(DatagramSocket serverSocket = new DatagramSocket(5001)) {
            while (true){
                Game game = new Game();

                byte [] receivingGameByteBuffer = SerializationUtils.serialize(game);
                byte[] sendingGameByteBuffer;

                DatagramPacket inputPacket = new DatagramPacket(receivingGameByteBuffer, receivingGameByteBuffer.length);

                System.err.println("Waiting for a client to connect... - UDP Server");
                serverSocket.receive(inputPacket);

                Game receivedGame = Game.bytesToGame(inputPacket.getData());
                System.err.println("Received game object from client. :)");

                sendingGameByteBuffer = getBytes(receivedGame);

                InetAddress senderAddress = inputPacket.getAddress();
                int senderPort = inputPacket.getPort();

                DatagramPacket outputPacket = new DatagramPacket(
                        sendingGameByteBuffer, sendingGameByteBuffer.length, senderAddress, senderPort
                );

                serverSocket.send(outputPacket);
                Thread.sleep(2000);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static byte[] getBytes(Game game){
        try( ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos);)
        {
            oos.writeObject(game);
            oos.flush();
            byte [] data = baos.toByteArray();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
