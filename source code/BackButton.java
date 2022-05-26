import javafx.scene.control.*;

public class BackButton extends Button {
	BackButton() {
		super.setText("<-BACK--");
		super.setStyle(
				"-fx-border-color: transparent; -fx-border-width: 0; -fx-background-radius: 0;-fx-background-color: transparent;"
				+ "-fx-font-size: 14px;-fx-text-fill: black;");
	}
}
