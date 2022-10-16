package hr.algebra.airhockey.models;

import hr.algebra.airhockey.GameScreenController;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Puck extends Circle {
    private int xDirection = 0;
    private int yDirection = 0;
    private int speed = 5;
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

    public int getSpeed() {
        return speed;
    }

    public Boolean getFirstTouch(){ return firstTouch;}

    public void setFirstTouch(Boolean isFirstTouch){
        this.firstTouch = isFirstTouch;
        if(this.firstTouch){

        }
    }

    public void setxDirection(int xDirection) {
        this.xDirection = xDirection;
    }

    public void setyDirection(int yDirection) {
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
        this.speed -= 1;
    }


    public void reset() {
        this.xDirection = 0;
        this.yDirection = 0;
        this.speed = 5;
        this.setLayoutY(GameScreenController.SCENE_HEIGHT / 2);
        this.setLayoutX(GameScreenController.SCENE_WIDTH / 2);
    }
}
