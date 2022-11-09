package hr.algebra.airhockey.models;

/*
*  This will be the game object that will implement Serializable
*  Here are going to be saved game specifics
*       Players, SecondsLeft, Score
*   Let's ask ourselves some questions?
*       When is this object created?
*           This object is created when user clicks "save game" (this will pause the timers, therefore the game).
*           When saved, it will be obligatory to construct the object with the players playing (and theirs general stats),
*           current SCORE and seconds left in the game.
*       Will this screw me in the future?
*           So in the future we will have to be sending game objects that will have to load the players
*           current position, pucks current position, score, seconds left.
*       Is this the right approach?
*           We will use UDP protocol for sending packages, therefore idk? hahaha
*/

import hr.algebra.airhockey.models.SerializableActor.SerializablePlayer;
import hr.algebra.airhockey.models.SerializableActor.SerializablePuck;

import java.io.Serializable;

public class Game implements Serializable {

    private byte redPlayerScore;
    private byte bluePlayerScore;
    private short secondsLeft;
    private SerializablePlayer redPlayer;
    private SerializablePlayer bluePlayer;
    private SerializablePuck puck;

    public Game(short secondsLeft, SerializablePlayer redPlayer, SerializablePlayer bluePlayer, SerializablePuck puck, byte redPlayerScore, byte bluePlayerScore){
        this.secondsLeft = secondsLeft;
        this.redPlayer = redPlayer;
        this.bluePlayer = bluePlayer;
        this.puck = puck;
        this.redPlayerScore = redPlayerScore;
        this.bluePlayerScore = bluePlayerScore;
    }

    public short getSecondsLeft() {
        return secondsLeft;
    }

    public SerializablePlayer getRedPlayer() {
        return redPlayer;
    }

    public SerializablePlayer getBluePlayer() {
        return bluePlayer;
    }

    public SerializablePuck getPuck() {
        return puck;
    }

    public void setSecondsLeft(short secondsLeft) {
        this.secondsLeft = secondsLeft;
    }

    public void setRedPlayer(SerializablePlayer redPlayer) {
        this.redPlayer = redPlayer;
    }

    public void setBluePlayer(SerializablePlayer bluePlayer) {
        this.bluePlayer = bluePlayer;
    }

    public void setPuck(SerializablePuck puck) {
        this.puck = puck;
    }

    public byte getRedPlayerScore() {
        return redPlayerScore;
    }

    public byte getBluePlayerScore() {
        return bluePlayerScore;
    }

    public void setRedPlayerScore(byte redPlayerScore) {
        this.redPlayerScore = redPlayerScore;
    }

    public void setBluePlayerScore(byte bluePlayerScore) {
        this.bluePlayerScore = bluePlayerScore;
    }
}


