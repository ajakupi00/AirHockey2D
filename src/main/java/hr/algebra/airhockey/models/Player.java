package hr.algebra.airhockey.models;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Objects;

public abstract class Player  {
    private final Scene scene;
    private final PlayerType type;
    private final String name;

    private Circle circle ;
    private int goals = 0;
    private int goalsConceived = 0;
    private int score = 0;
    public int boostGoals = 0;


    //TODO: Make GameUtils
    public static double SCENE_HEIGHT = 700;
    public static double SCENE_WIDTH = 480;


    public Player(final String name, final Scene scene, final PlayerType type) {
        this.scene = scene;
        this.type = type;
        this.name = name;
        circle = createPlayer();
    }




    public abstract void movementSetup();

    public abstract void init();

    private Circle createPlayer() {
        Circle circle = new Circle();
        circle.setRadius(25);
        switch (type){
            case RED -> {
                circle.setFill(Color.RED);

                circle.setLayoutX(SCENE_WIDTH / 2);
                circle.setLayoutY((SCENE_HEIGHT / 2) + (SCENE_HEIGHT / 4));
            }
            case BLUE -> {
                circle.setFill(Color.BLUE);

                circle.setLayoutX(SCENE_WIDTH / 2);
                circle.setLayoutY((SCENE_HEIGHT / 2) - (SCENE_HEIGHT / 4));
            }
        }


        return circle;
    }

    public PlayerType getType() {
        return type;
    }

    public Circle getCircle() {
        return circle;
    }

    public void scoredGoal(final Boolean boostGoal) {
        if(boostGoal){
            boostGoals++;
        }
        this.goals++;
    }

    public void goalConceived() {
        this.goalsConceived++;
    }

    public int getGoals() {
        return goals;
    }

    public int getGoalsConceived() {
        return goalsConceived;
    }

    public String getName() {
        return name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getBoostGoals() {
        return boostGoals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
