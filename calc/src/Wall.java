import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Paint;

public class Wall {
	Rectangle wall;

	public Wall(double positionX, double positionY, double width, double height) {
		wall = new Rectangle(positionX, positionY, width, height);
	}

	public boolean isCollision(Ball ball) {
		return ball.getBallShape().intersects(wall.getBoundsInLocal());
	}

	public Rectangle getWallShape() {
		return wall;
	}

	public double getX() {
		return wall.getX();
	}

	public double getY() {
		return wall.getY();
	}

	public double getHeight() {
		return wall.getHeight();
	}

	public double getWidth() {
		return wall.getWidth();
	}

	public void setFill(Paint value) {
		wall.setFill(value);
	}

}