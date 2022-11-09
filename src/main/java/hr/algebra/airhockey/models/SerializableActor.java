package hr.algebra.airhockey.models;

import java.io.Serializable;

public abstract class SerializableActor implements Serializable {

    private byte xDirection;
    private byte yDirection;
    private double xPosition;
    private double yPosition;

    public SerializableActor(byte xDirection, byte yDirection, double xPosition, double yPosition){
        this.xPosition = xPosition;
        this.xDirection = xDirection;
        this.yPosition = yPosition;
        this.yDirection = yDirection;
    }

    public byte getxDirection() {
        return xDirection;
    }

    public byte getyDirection() {
        return yDirection;
    }

    public double getxPosition() {
        return xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public static class SerializablePlayer extends SerializableActor{
        private String name;
        private byte wins;
        private byte lost;
        private byte goals;
        private byte goalsConceived;
        private byte boostGoals;

        public SerializablePlayer(
                String name, byte wins, byte lost, byte goals,
                byte goalsConceived, byte boostGoals,
                byte xDirection, byte yDirection,
                double xPosition, double yPosition) {
            super(xDirection, yDirection, xPosition, yPosition);
            this.name = name;
            this.wins = wins;
            this.lost = lost;
            this.goals = goals;
            this.goalsConceived = goalsConceived;
            this.boostGoals = boostGoals;
        }

        public String getName() {
            return name;
        }

        public byte getWins() {
            return wins;
        }

        public byte getLost() {
            return lost;
        }

        public byte getGoals() {
            return goals;
        }

        public byte getGoalsConceived() {
            return goalsConceived;
        }

        public byte getBoostGoals() {
            return boostGoals;
        }
    }
    public static class SerializablePuck extends SerializableActor{

        public SerializablePuck(byte xDirection, byte yDirection, double xPosition, double yPosition) {
            super(xDirection, yDirection, xPosition, yPosition);
        }
    }



}
