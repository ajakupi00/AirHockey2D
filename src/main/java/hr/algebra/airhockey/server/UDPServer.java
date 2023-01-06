package hr.algebra.airhockey.server;

import hr.algebra.airhockey.models.Game;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {

    public static void main(String[] args) {

        try(DatagramSocket serverSocket = new DatagramSocket(5001)) {
            while (true){
                byte[] receivingGameByteBuffer = new byte[1024];
                byte[] sendingGameByteBuffer = new byte[1024];

                DatagramPacket inputPacket = new DatagramPacket(receivingGameByteBuffer, receivingGameByteBuffer.length);

                System.err.println("Waiting for a client to connect... - UDP Server");
                serverSocket.receive(inputPacket);

                String receivedData = new String(inputPacket.getData());
                System.out.println("Sent from the client: " + receivedData);

                sendingGameByteBuffer = receivedData.toUpperCase().getBytes();

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
}
