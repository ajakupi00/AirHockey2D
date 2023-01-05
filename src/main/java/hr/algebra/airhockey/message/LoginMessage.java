package hr.algebra.airhockey.message;

import java.io.Serializable;

public class LoginMessage implements Serializable {
    public static final long serialVersionUID = 5L;

    private String playerID;
    private String username;

    public LoginMessage(){

    }

    public LoginMessage(String playerID, String username){
        this.playerID = playerID;
        this.username = username;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
