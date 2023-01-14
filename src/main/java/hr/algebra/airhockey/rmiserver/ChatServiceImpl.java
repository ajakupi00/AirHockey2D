package hr.algebra.airhockey.rmiserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ChatServiceImpl  implements ChatService {
    private List<String> chatHistory;

    public ChatServiceImpl(){
        chatHistory = new ArrayList<>();
    }

    @Override
    public void sendMessage(String message, String user) {
        String userMessage = user + ": " + message;
        chatHistory.add(userMessage);

    }

    @Override
    public List<String> getChatHistory() {
       return chatHistory;
    }



}
