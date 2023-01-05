package hr.algebra.airhockey.Threads;

import hr.algebra.airhockey.rmiserver.ChatService;
import javafx.scene.control.TextArea;

import java.rmi.RemoteException;

public class ClientChatThread implements Runnable{
    private ChatService chatService;
    private TextArea chatHistory;

    public ClientChatThread(ChatService chatService, TextArea chatHistory) {
        this.chatHistory = chatHistory;
        this.chatService = chatService;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1500);
                chatHistory.clear();
                StringBuilder chatHistoryBuilder = new StringBuilder();
                chatService.getChatHistory().forEach(a -> chatHistoryBuilder.append(a + "\n"));
                chatHistory.setText(chatHistoryBuilder.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
