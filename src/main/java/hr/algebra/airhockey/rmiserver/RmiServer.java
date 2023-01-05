package hr.algebra.airhockey.rmiserver;

import hr.algebra.airhockey.jndi.helper.JndiHelper;
import hr.algebra.airhockey.jndi.helper.ServerConfigurationKey;

import javax.naming.NamingException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiServer {
    public static void main(String[] args) {
        try{
            String rmiPortString = JndiHelper.getConfigurationParameter(
                    ServerConfigurationKey.RMI_SERVER_PORT);
            Integer rmiPort = Integer.parseInt(rmiPortString);

            String rndPortString = JndiHelper.getConfigurationParameter(
                    ServerConfigurationKey.RANDOM_PORT);
            Integer rndPort = Integer.parseInt(rndPortString);

            Registry registry = LocateRegistry.createRegistry(rmiPort);
            ChatService chatService = new ChatServiceImpl();
            ChatService skeleton =(ChatService) UnicastRemoteObject.exportObject(chatService, rndPort);
            registry.rebind(ChatService.REMOTE_OBJECT_NAME, skeleton);
            System.err.println("Object registered in RMI registry!");

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
