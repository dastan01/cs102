import javafx.scene.control.Label;
import javafx.scene.paint.Paint;

public class Labels {
	Label label;

	public Labels(String text, double x, double y) {
		label = new Label(text);
		label.setLayoutX(x);
		label.setLayoutY(y);
	}

	public void TextFill(Paint paint) {
		label.setTextFill(paint);
	}

	public void SetText(String text) {
		label.setText(text);
	}

	public Label getLabelType() {
		return label;
	}

	public void SetVisible(boolean bool) {
		label.setVisible(bool);
	}

	public void SetLayoutX(double x) {
		label.setLayoutX(x);
	}

	public void SetLayoutY(double y) {
		label.setLayoutY(y);
	}
}
