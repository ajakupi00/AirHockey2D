package hr.algebra.airhockey.models;

import hr.algebra.airhockey.GameScreenController;
import hr.algebra.airhockey.hr.algebra.airhockey.utils.GameUtils;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Puck extends Circle {
    private byte xDirection = 0;
    private byte yDirection = 0;
    private double speed = 5;
    private Boolean firstTouch = false;



    public Puck(final double radius, final Paint paint, final int positionX, final int positionY) {
        super(radius, paint);
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);
    }

    public void updatePosition(){
        double currentPositionY = this.getLayoutY();
        this.setLayoutY(currentPositionY + (speed * yDirection));

        double currentPositionX = this.getLayoutX();
        this.setLayoutX(currentPositionX + (speed * xDirection));
    }

    public int getxDirection() {
        return xDirection;
    }

    public int getyDirection() {
        return yDirection;
    }

    public double getSpeed() {
        return speed;
    }

    public Boolean getFirstTouch(){ return firstTouch;}

    public void setFirstTouch(Boolean isFirstTouch){
        this.firstTouch = isFirstTouch;
        if(this.firstTouch){

        }
    }

    public void setxDirection(byte xDirection) {
        this.xDirection = xDirection;
    }

    public void setyDirection(byte yDirection) {
        this.yDirection = yDirection;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void speedUp(){
        this.speed += 0.5;
    }
    public void slowDown(){
        if(this.speed <= 1){
            return;
        }
        this.speed -= 0.25;
    }


    public void reset() {
        this.xDirection = 0;
        this.yDirection = 0;
        this.speed = 5;
        this.setLayoutY(GameUtils.SCENE_HEIGHT / 2);
        this.setLayoutX(GameUtils.SCENE_WIDTH / 2);
    }
}
