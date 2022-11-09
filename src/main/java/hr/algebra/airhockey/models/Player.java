package hr.algebra.airhockey.models;

import hr.algebra.airhockey.MainApplication;
import hr.algebra.airhockey.models.SerializableActor.SerializablePlayer;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.NoRouteToHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static hr.algebra.airhockey.hr.algebra.airhockey.utils.GameUtils.SCENE_HEIGHT;
import static hr.algebra.airhockey.hr.algebra.airhockey.utils.GameUtils.SCENE_WIDTH;

public class Player  {
    private final Scene scene;
    private PlayerType type;
    private String name;

    //[0] = W, [1] = A, [2] = S, [3] = D, [4] = SPACE
    private final KeyCode[] movementInputsKeys;

    //MOVEMENTS PROPERTIES
    private BooleanProperty upPressed = new SimpleBooleanProperty();
    private BooleanProperty leftPressed = new SimpleBooleanProperty();
    private BooleanProperty downPressed = new SimpleBooleanProperty();
    private BooleanProperty rightPressed = new SimpleBooleanProperty();
    private BooleanProperty boostPressed = new SimpleBooleanProperty();

    private BooleanBinding keyPressed = upPressed.or(leftPressed).or(downPressed).or(rightPressed);

    //PLAYER PROPERTIES
    private AnimationTimer timer;
    private Circle circle ;
    private final int playerSpeed = 7;
    private final int boost = 3;
    private boolean lastGoalIsBoost = false;

    //PLAYER STATS
    private int wins = 0;
    private int lost = 0;
    private int goals = 0;
    private int goalsConceived = 0;
    private int score = 0;
    private int boostGoals = 0;

    
    
    public Player(final String name, final Scene scene, final PlayerType type, final KeyCode[] movementInputsKeys) {
        this.scene = scene;
        this.type = type;
        this.name = name;
        this.movementInputsKeys = movementInputsKeys;
        circle = createPlayerCircle();
        if(type == PlayerType.RED){
            timer = createRedPlayerTimer();
        }else{
            timer = createBluePlayerTimer();
        }

    }
    //ON-CREATE METHODS
    private AnimationTimer createRedPlayerTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(upPressed.get()){
                    if(getCircle().getLayoutY() > (SCENE_HEIGHT / 2) + circle.getRadius() / 2){
                        if(boostPressed.get()){
                            circle.setLayoutY(circle.getLayoutY() - (playerSpeed + boost));
                        }
                        circle.setLayoutY(circle.getLayoutY() - playerSpeed);
                    }
                }
                if(downPressed.get()){
                    if(circle.getLayoutY() < (SCENE_HEIGHT - circle.getRadius())){
                        if(boostPressed.get()){
                            circle.setLayoutY(circle.getLayoutY() + (playerSpeed + boost));
                        }
                        circle.setLayoutY(circle.getLayoutY() + playerSpeed);
                    }

                }
                if(leftPressed.get()){
                    if(circle.getLayoutX() > circle.getRadius()) {
                        if(boostPressed.get()){
                            circle.setLayoutX(circle.getLayoutX() - (playerSpeed + boost));
                        }
                        circle.setLayoutX(circle.getLayoutX() - playerSpeed);
                    }
                }
                if(rightPressed.get()){
                    if(circle.getLayoutX() < (SCENE_WIDTH - circle.getRadius())){
                        if(boostPressed.get()){
                            circle.setLayoutX(circle.getLayoutX() + (playerSpeed + boost));
                        }

                        circle.setLayoutX(circle.getLayoutX() + playerSpeed);
                    }
                }
            }
        };
    }
    private AnimationTimer createBluePlayerTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(upPressed.get()){
                    if(getCircle().getLayoutY() > circle.getRadius()){
                        if(boostPressed.get()){
                            circle.setLayoutY(circle.getLayoutY() - (playerSpeed + boost));
                        }
                        circle.setLayoutY(circle.getLayoutY() - playerSpeed);
                    }
                }
                if(downPressed.get()){
                    if(circle.getLayoutY() < ((SCENE_HEIGHT / 2) - (circle.getRadius() / 2))){
                        if(boostPressed.get()){
                            circle.setLayoutY(circle.getLayoutY() + (playerSpeed + boost));
                        }
                        circle.setLayoutY(circle.getLayoutY() + playerSpeed);
                    }

                }
                if(leftPressed.get()){
                    if(circle.getLayoutX() > circle.getRadius()) {
                        if(boostPressed.get()){
                            circle.setLayoutX(circle.getLayoutX() - (playerSpeed + boost));
                        }
                        circle.setLayoutX(circle.getLayoutX() - playerSpeed);
                    }
                }
                if(rightPressed.get()){
                    if(circle.getLayoutX() < (SCENE_WIDTH - circle.getRadius())){
                        if(boostPressed.get()){
                            circle.setLayoutX(circle.getLayoutX() + (playerSpeed + boost));
                        }

                        circle.setLayoutX(circle.getLayoutX() + playerSpeed);
                    }
                }
            }
        };
    }
    public void sceneKeyboardMovementSetup() {
        EventHandler redPlayerMovementPressed = (EventHandler<KeyEvent>) e -> {
            if (e.getCode() == movementInputsKeys[0]) {
                upPressed.set(true);
            }
            if (e.getCode()  == movementInputsKeys[1]) {
                leftPressed.set(true);
            }
            if (e.getCode() == movementInputsKeys[2]) {
                downPressed.set(true);
            }

            if (e.getCode() == movementInputsKeys[3]) {
                rightPressed.set(true);
            }
            if(e.getCode() == movementInputsKeys[4]) {
                boostPressed.set(true);
            }

        };
        MainApplication.getMainScene().addEventFilter(KeyEvent.KEY_PRESSED, redPlayerMovementPressed);

        EventHandler redPlayerMovementReleased = (EventHandler<KeyEvent>) e -> {
            if (e.getCode() == movementInputsKeys[0]) {
                upPressed.set(false);
            }
            if (e.getCode()== movementInputsKeys[1]) {
                leftPressed.set(false);
            }
            if (e.getCode() == movementInputsKeys[2]) {
                downPressed.set(false);
            }
            if (e.getCode() == movementInputsKeys[3]) {
                rightPressed.set(false);
            }
            if(e.getCode() == movementInputsKeys[4]) {
                boostPressed.set(false);
            }
        };
        MainApplication.getMainScene().addEventFilter(KeyEvent.KEY_RELEASED,redPlayerMovementReleased);
    }
    public void initPlayerTimer() {
        keyPressed.addListener((observableValue, aBoolean, t1) -> {
            if(!aBoolean){
                timer.start();
            }else{
                timer.stop();
            }
        });
    }
    private Circle createPlayerCircle() {
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

    //PLAYER METHODS
    public void scoredGoal(final Boolean boostGoal) {
        if(boostGoal){
            boostGoals++;
            lastGoalIsBoost = true;
        }else{
            lastGoalIsBoost = false;
        }
        this.goals++;

    }
    public void goalConceived() {
        this.goalsConceived++;
    }

    public void playerWon(){
        wins++;
    }
    public void playerLost(){
        lost++;
    }

    //SETTERS
    public void setScore(int score) {
        this.score = score;
    }

    //GETTERS
    public PlayerType getType() {
        return type;
    }
    public Circle getCircle() {
        return circle;
    }
    public int getScore() {
        return score;
    }
    public int getBoostGoals() {
        return boostGoals;
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
    public boolean getUpPressed() {
        return upPressed.get();
    }
    public boolean getLeftPressed() {
        return leftPressed.get();
    }
    public boolean getDownPressed() {
        return downPressed.get();
    }
    public boolean getRightPressed() {return rightPressed.get();}
    public boolean getBoostPressed() {return boostPressed.get();}
    public boolean isLastGoalIsBoost(){return lastGoalIsBoost;}
    public int getWins(){return wins;}
    public int getLost(){return lost;}

    public void deserializePlayer(SerializablePlayer serializablePlayer){
        this.name = serializablePlayer.getName().toString();
        this.wins = serializablePlayer.getWins();
        this.lost = serializablePlayer.getLost();
        this.goals = serializablePlayer.getGoals();
        this.goalsConceived = serializablePlayer.getGoalsConceived();
        this.boostGoals = serializablePlayer.getBoostGoals();
        this.circle.setLayoutX(serializablePlayer.getxPosition());
        this.circle.setLayoutY(serializablePlayer.getyPosition());

    }
}
