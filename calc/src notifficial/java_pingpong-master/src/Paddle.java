import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

public class Paddle {
	private Rectangle paddle;
	private double paddleDragAnchorX;
	private double paddleDragAnchorY;

	public Paddle(double positionX, double positionY, double width, double height) {
		paddle = new Rectangle(positionX, positionY, width, height);
		//setDefaultMouseEvent();
	}

	private void setDefaultMouseEvent() {

		setOnMousePressed(me -> {
			paddleDragAnchorX = me.getSceneX() - paddle.getX();
			paddleDragAnchorY = me.getSceneY() - paddle.getY();
		});
		setOnMouseDragged(me -> {
			double dragX = me.getSceneX() - paddleDragAnchorX;
			double dragY = me.getSceneY() - paddleDragAnchorY;
			// move(dragX, dragY);
			moveUpOrDown(dragY);
		});
	}
	

	public Rectangle getPaddleShape() {
		return paddle;
	}

	public boolean isCollision(Ball ball) {
		return ball.getBallShape().intersects(paddle.getBoundsInLocal());
	}

	public boolean isCollision(Wall wall) {
		return wall.getWallShape().intersects(paddle.getBoundsInLocal());
	}

	public void setOnMouseDragged(EventHandler<? super MouseEvent> value) {
		paddle.setOnMouseDragged(value);
	}

	public void setOnMousePressed(EventHandler<? super MouseEvent> value) {
		paddle.setOnMousePressed(value);
	}

	private void moveUpOrDown(double y) {
		paddle.setY(y);
	}

	/**
	 * Moving paddle by positionX and positionY which can use in mouse event
	 */
	public void move(double x, double y) {
		paddle.setX(x);
		paddle.setY(y);
	}

	/**
	 * Moving paddle by a speed which can use in keyboard event
	 */

	public void moveRight(double speed) {
		//if (paddle.getX() >= 5 && paddle.getX() <= 495)
			paddle.setX(paddle.getX() + speed);
	}

	public void moveLeft(double speed) {
		paddle.setX(paddle.getX() - speed);
	}

	public void moveUp(double speed) {
		paddle.setY(paddle.getY() - speed);
	}

	public void moveDown(double speed) {
		//if (paddle.getY() >= 5 && paddle.getY() <= 495)
			paddle.setY(paddle.getY() + speed);
	}

	public double getX() {
		return paddle.getX();
	}

	public double getY() {
		return paddle.getY();
	}

	public double getHeight() {
		return paddle.getHeight();
	}

	public double getWidth() {
		return paddle.getWidth();
	}

	public void setFill(Paint value) {
		paddle.setFill(value);
	}

}