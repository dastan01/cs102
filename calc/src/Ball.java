import javafx.scene.shape.Circle;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;

public class Ball {

   Circle ball;  
   
   /**
    * Whether the ball is moving  
    */ 
   boolean movingRight = true;
   boolean movingDown = true;
   
   public Ball(double positionX, double positionY, double radius) {
     ball = new Circle(positionX, positionY, radius, Color.WHITE);
   }
   public Circle getBallShape() {
     return ball;
   }
   public void move(double speed) {
     if(speed < 0) {
       return;  
     }
     
     if(isMovingRight()) {
       moveRight(speed);
     } else {
       moveLeft(speed);
     }
     
     if(isMovingDown()) {
      
       moveDown(speed);
     } else {
       moveUp(speed);
     }
   }
   public void moveRight(double speed) {
     movingRight = true;
     ball.setCenterX(ball.getCenterX() + speed);
   }
   public void moveLeft(double speed) {
     movingRight = false;
     ball.setCenterX(ball.getCenterX() - speed);
   }
   public void moveUp(double speed) {
     movingDown = false;
     ball.setCenterY(ball.getCenterY() - speed);
   } 
   public void moveDown(double speed) {
     movingDown = true;
     ball.setCenterY(ball.getCenterY() + speed);
   }
   public boolean isMovingUp() {
     return !movingDown;
   }
   public boolean isMovingDown() {
     return movingDown;
   }
   public boolean isMovingRight() {
     return movingRight;
   }
   public boolean isMovingLeft() {
     return !movingRight;
   }
   
  public double getCenterX() {
    return ball.getCenterX();
  }
  public double getCenterY() {
    return ball.getCenterY();
  }
  public void setCenterX(double x) {
    ball.setCenterX(x);
  }
  public void setCenterY(double y) {
    ball.setCenterY(y);
  }
}