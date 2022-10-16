package hr.algebra.airhockey;

import hr.algebra.airhockey.models.*;
import javafx.animation.Animation;
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
    public static final int SCENE_WIDTH = 480;
    public static final int SCENE_HEIGHT = 700;
    private final int GAME_DURATIONS_SECONDS = 10;
    private int secondsLeft = GAME_DURATIONS_SECONDS;

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

    private RedPlayer redPlayer;
    private Puck puck;


    private AnimationTimer collisionTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            checkPuckPlayerCollision(redPlayer.getCircle(), puck);
        }
    };

    private AnimationTimer puckMovementTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if(puck.getLayoutY() <= 0 + puck.getRadius() || puck.getLayoutY() >= SCENE_HEIGHT - puck.getRadius()){
                puck.setyDirection(puck.getyDirection() * -1);
                puck.speedUp();
                if(redGoalRectangle.getBoundsInParent().intersects(puck.getBoundsInParent())){
                    //TODO: check time

                    puck.reset();
                    puckMovementTimer.stop();
                    puck.setFirstTouch(false);
                }else if(blueGoalRectangle.getBoundsInParent().intersects(puck.getBoundsInParent())){
                    //TODO: add goal
                    if(redPlayer.isSpacePressed()){
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

            if(puck.getLayoutX() <= 0 + puck.getRadius() || puck.getLayoutX() >= SCENE_WIDTH - puck.getRadius()){
                //puck.slowDown();
                puck.setxDirection(puck.getxDirection() * -1);
            }

            puck.setLayoutX(puck.getLayoutX() + (puck.getSpeed() * puck.getxDirection()));
        }
    };


    private void loadLeaderboard() {
        collisionTimer.stop();
        puckMovementTimer.stop();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("leaderboard.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LeaderboardController leaderboardController = fxmlLoader.getController();
        leaderboardController.setPlayers(redPlayer);        //TODO: set BLUE PLAYER
        leaderboardController.initStats();
        MainApplication.getStage().setTitle("AirHockey");
        MainApplication.getStage().setScene(scene);
        MainApplication.getStage().show();
        LeaderboardController.currentScene = MainApplication.getStage().getScene();

    }



    private void checkPuckPlayerCollision(Circle player, Puck puck) {
        if(puck.getBoundsInParent().intersects(player.getBoundsInParent())){
            puckDefineDirection();
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
                    loadLeaderboard();
                }
            }
        });
        gameTime.getKeyFrames().add(perSecondKeyFrame);
        gameTime.play();
    }

    private void puckDefineDirection() {
        if(this.redPlayer.iswPressed()){
            this.puck.setyDirection(-1);
        }
        if(this.redPlayer.issPressed()){
            this.puck.setyDirection(1);
        }
        if(this.redPlayer.isaPressed()){
            this.puck.setxDirection(-1);
        }
        if(this.redPlayer.isdPressed()){
            this.puck.setxDirection(1);
        }
        this.puck.updatePosition();
    }

    private void initializePlayer(final String redPlayerName, final String bluePlayerName){
        Player redPlayer = new RedPlayer(redPlayerName, MainApplication.getMainScene(), PlayerType.RED, puck);
        anchorPane.getChildren().add(redPlayer.getCircle());
        redPlayer.movementSetup();
        redPlayer.init();
        this.redPlayer = (RedPlayer) redPlayer;
        collisionTimer.start();



    }

    public void initGame(final String redPlayerName, final String bluePlayerName) {
        redGoalsLabel.setText("0");
        blueGoalsLabel.setText("0");

        Puck puck = new Puck(15, Color.BLACK, SCENE_WIDTH / 2, SCENE_HEIGHT - 300);
        this.puck = puck;
        anchorPane.getChildren().add(puck);
        initializePlayer(redPlayerName, bluePlayerName);


    }



}



