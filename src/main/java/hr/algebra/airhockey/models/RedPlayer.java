/*package hr.algebra.airhockey.models;

import hr.algebra.airhockey.MainApplication;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;

import java.security.Key;

public class RedPlayer extends Player{
    private final int playerSpeed = 7;
    private final int boost = 3;
        private AnimationTimer timer;
        private Circle circle;
        private Puck puck;



        private BooleanProperty wPressed = new SimpleBooleanProperty();
        private BooleanProperty aPressed = new SimpleBooleanProperty();
        private BooleanProperty sPressed = new SimpleBooleanProperty();
        private BooleanProperty dPressed = new SimpleBooleanProperty();
        private BooleanProperty spacePressed = new SimpleBooleanProperty();

        private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed);

        public RedPlayer(final String playerName, final Scene scene, final PlayerType type, final Puck puck) {
            super(playerName, scene, type);
            timer = createRedPlayerTimer();
            circle = super.getCircle();
            this.puck = puck;
        }


        private AnimationTimer createRedPlayerTimer() {
            return new AnimationTimer() {
                @Override
                public void handle(long l) {
                    if(wPressed.get()){
                        if(getCircle().getLayoutY() > (SCENE_HEIGHT / 2) + circle.getRadius() / 2){
                            if(spacePressed.get()){
                                circle.setLayoutY(circle.getLayoutY() - (playerSpeed + boost));
                            }
                            circle.setLayoutY(circle.getLayoutY() - playerSpeed);
                        }
                    }
                    if(sPressed.get()){
                        if(circle.getLayoutY() < (SCENE_HEIGHT - circle.getRadius())){
                            if(spacePressed.get()){
                                circle.setLayoutY(circle.getLayoutY() + (playerSpeed + boost));
                            }
                            circle.setLayoutY(circle.getLayoutY() + playerSpeed);
                        }

                    }
                    if(aPressed.get()){
                        if(circle.getLayoutX() > circle.getRadius()) {
                            if(spacePressed.get()){
                                circle.setLayoutX(circle.getLayoutX() - (playerSpeed + boost));
                            }
                            circle.setLayoutX(circle.getLayoutX() - playerSpeed);
                        }
                    }
                    if(dPressed.get()){
                        if(circle.getLayoutX() < (SCENE_WIDTH - circle.getRadius())){
                            if(spacePressed.get()){
                                circle.setLayoutX(circle.getLayoutX() + (playerSpeed + boost));
                            }

                            circle.setLayoutX(circle.getLayoutX() + playerSpeed);
                        }
                    }
                }
            };
        }

        @Override
        public void sceneKeyboardMovementSetup() {
            EventHandler redPlayerMovementPressed = new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent e) {
                    if (e.getCode() == KeyCode.W) {
                        wPressed.set(true);
                    }

                    if (e.getCode() == KeyCode.S) {
                        sPressed.set(true);
                    }

                    if (e.getCode() == KeyCode.A) {
                        aPressed.set(true);
                    }

                    if (e.getCode() == KeyCode.D) {
                        dPressed.set(true);
                    }

                    if(e.getCode() == KeyCode.SPACE) {
                        spacePressed.set(true);
                    }

                }
            };
            MainApplication.getMainScene().addEventFilter(KeyEvent.KEY_PRESSED, redPlayerMovementPressed);

            EventHandler redPlayerMovementReleased = new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent e) {
                    if (e.getCode() == KeyCode.W) {
                        wPressed.set(false);
                    }

                    if (e.getCode() == KeyCode.S) {
                        sPressed.set(false);
                    }

                    if (e.getCode() == KeyCode.A) {
                        aPressed.set(false);
                    }

                    if (e.getCode() == KeyCode.D) {
                        dPressed.set(false);
                    }
                    if(e.getCode() == KeyCode.SPACE) {
                        spacePressed.set(false);
                    }
                }
            };
            MainApplication.getMainScene().addEventFilter(KeyEvent.KEY_RELEASED,redPlayerMovementReleased);
        }

        @Override
        public void initPlayerTimer() {

            keyPressed.addListener((observableValue, aBoolean, t1) -> {
                if(!aBoolean){
                    timer.start();
                }else{
                    timer.stop();
                }
            });
        }

    public boolean getUpPressed() {
        return wPressed.get();
    }

    public boolean getLeftPressed() {
        return aPressed.get();
    }


    public boolean getDownPressed() {
        return sPressed.get();
    }


    public boolean getRightPressed() {
        return dPressed.get();
    }

    public boolean getBoostPressed() {
        return spacePressed.get();
    }


}*/
