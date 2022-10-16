package hr.algebra.airhockey;

import hr.algebra.airhockey.hr.algebra.airhockey.utils.GameUtils;
import hr.algebra.airhockey.models.*;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;


public class GameScreenController{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Rectangle redGoalRectangle;
    @FXML
    private Rectangle blueGoalRectangle;
    @FXML
    private Label redGoalsLabel;
    @FXML
    private Label blueGoalsLabel;
    @FXML
    private Label lblGameSeconds;

    private int secondsLeft = GameUtils.GAME_DURATIONS_SECONDS;
    private Player redPlayer;
    private Player bluePlayer;
    private Puck puck;

    //TIMERS
    private AnimationTimer collisionTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            checkPuckPlayerCollision(redPlayer.getCircle(), bluePlayer.getCircle(), puck);
        }
    };

    private AnimationTimer puckMovementTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if(puck.getLayoutY() <= 0 + puck.getRadius() || puck.getLayoutY() >= GameUtils.SCENE_HEIGHT - puck.getRadius()){
                puck.setyDirection(puck.getyDirection() * -1);
                puck.speedUp();
                if(redGoalRectangle.getBoundsInParent().intersects(puck.getBoundsInParent())){
                    if(bluePlayer.getBoostPressed()){
                        bluePlayer.scoredGoal(true);
                    }else{
                        bluePlayer.scoredGoal(false);
                    }
                    blueGoalsLabel.setText(Integer.toString(bluePlayer.getGoals()));
                    puck.reset();
                    puckMovementTimer.stop();
                    puck.setFirstTouch(false);
                }else if(blueGoalRectangle.getBoundsInParent().intersects(puck.getBoundsInParent())){
                    //TODO: add goal
                    if(redPlayer.getBoostPressed()){
                        redPlayer.scoredGoal(true);
                    }else{
                        redPlayer.scoredGoal(false);
                    }

                    redGoalsLabel.setText(Integer.toString(redPlayer.getGoals()));
                    puck.reset();
                    puckMovementTimer.stop();
                    puck.setFirstTouch(false);
                }
            }
            puck.setLayoutY(puck.getLayoutY() + (puck.getSpeed() * puck.getyDirection()));

            if(puck.getLayoutX() <= 0 + puck.getRadius() || puck.getLayoutX() >= GameUtils.SCENE_WIDTH - puck.getRadius()){
                //puck.slowDown();
                puck.setxDirection(puck.getxDirection() * -1);
            }
            puck.setLayoutX(puck.getLayoutX() + (puck.getSpeed() * puck.getxDirection()));
        }
    };

    //BEFORE GAMEPLAY METHODS
    public void initGame(final String redPlayerName, final String bluePlayerName) {
        redGoalsLabel.setText("0");
        blueGoalsLabel.setText("0");

        Puck puck = new Puck(15, Color.BLACK, GameUtils.SCENE_WIDTH / 2, GameUtils.SCENE_HEIGHT / 2);
        this.puck = puck;
        anchorPane.getChildren().add(puck);
        initializePlayer(redPlayerName, bluePlayerName);


    }
    private void initializePlayer(final String redPlayerName, final String bluePlayerName){
        //CREATE RED PLAYER
        Player redPlayer = new Player(redPlayerName, MainApplication.getMainScene(), PlayerType.RED, GameUtils.wasd);
        anchorPane.getChildren().add(redPlayer.getCircle());
        redPlayer.sceneKeyboardMovementSetup();
        redPlayer.initPlayerTimer();
        this.redPlayer = redPlayer;

        //CREATE BLUE PLAYER
        Player bluePlayer = new Player(bluePlayerName, MainApplication.getMainScene(), PlayerType.BLUE, GameUtils.arrows);
        anchorPane.getChildren().add(bluePlayer.getCircle());
        bluePlayer.sceneKeyboardMovementSetup();
        bluePlayer.initPlayerTimer();
        this.bluePlayer = bluePlayer;

        collisionTimer.start();



    }


    //DURING GAMEPLAY METHODS
    private void checkPuckPlayerCollision(Circle redPlayerCircle, Circle bluePlayerCircle, Puck puck) {
        if(puck.getBoundsInParent().intersects(redPlayerCircle.getBoundsInParent())){
            puck.slowDown();
            puckDefineDirection(redPlayer);
            if(!puck.getFirstTouch()){
                puckMovementTimer.start();
                startGameTimer();
                puck.setFirstTouch(true);
            }
        }else if(puck.getBoundsInParent().intersects(bluePlayerCircle.getBoundsInParent())){
            puck.slowDown();
            puckDefineDirection(bluePlayer);
            if(!puck.getFirstTouch()){
                puckMovementTimer.start();
                startGameTimer();
                puck.setFirstTouch(true);
            }
        }
    }
    private void startGameTimer() {
        Timeline gameTime = new Timeline();
        gameTime.setCycleCount(Timeline.INDEFINITE);
        KeyFrame perSecondKeyFrame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                secondsLeft--;
                lblGameSeconds.setText(String.valueOf(secondsLeft));
                if(secondsLeft <= 0){
                    gameTime.stop();
                    collisionTimer.stop();
                    puckMovementTimer.stop();
                    loadLeaderboard();
                }
            }
        });
        gameTime.getKeyFrames().add(perSecondKeyFrame);
        gameTime.play();
    }
    private void puckDefineDirection(final Player player) {
        if(player.getBoostPressed()){
            puck.speedUp();
        }
        if(player.getUpPressed()){
            this.puck.setyDirection(-1);
        }
        if(player.getDownPressed()){
            this.puck.setyDirection(1);
        }
        if(player.getLeftPressed()){
            this.puck.setxDirection(-1);
        }
        if(player.getRightPressed()){
            this.puck.setxDirection(1);
        }
        this.puck.updatePosition();
    }

    //AFTER GAMEPLAY METHODS
    private void loadLeaderboard() {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("leaderboard.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LeaderboardController leaderboardController = fxmlLoader.getController();
        leaderboardController.setPlayers(redPlayer, bluePlayer);        //TODO: set BLUE PLAYER
        leaderboardController.initStats();
        MainApplication.getStage().setTitle("AirHockey");
        MainApplication.getStage().setScene(scene);
        MainApplication.getStage().show();
        GameUtils.currentScene = MainApplication.getStage().getScene();

    }


}



