package hr.algebra.airhockey.models;

import hr.algebra.airhockey.models.SerializableActor.SerializablePlayer;
import hr.algebra.airhockey.models.SerializableActor.SerializablePuck;

import java.io.Serializable;

public class GameStateDto implements Serializable {
    private SerializablePlayer redPlayer;
    private SerializablePlayer bluePlayer;
    private SerializablePuck puck;

    private String name;

    public GameStateDto() {
    }

    public SerializablePlayer getRedPlayer() {
        return redPlayer;
    }

    public void setRedPlayer(SerializablePlayer redPlayer) {
        this.redPlayer = redPlayer;
    }

    public SerializablePlayer getBluePlayer() {
        return bluePlayer;
    }

    public void setBluePlayer(SerializablePlayer bluePlayer) {
        this.bluePlayer = bluePlayer;
    }

    public SerializablePuck getPuck() {
        return puck;
    }

    public void setPuck(SerializablePuck puck) {
        this.puck = puck;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
