package org.example;


import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.control.TableView;
import javafx.fxml.FXML;
import javafx.scene.SubScene;

public class MainController {
	@FXML
	private SubScene simulationSubScene; // זה המשטח לתלת-ממד

	@FXML
	private AnchorPane sideMenu; // זה התפריט שצבעת

	@FXML
	private TableView<?> bodyTable; // הטבלה של הגופים

	@FXML
	private Button menuButton;

	@FXML
	public void initialize() {
		// כשהתפריט פתוח בהתחלה – להצמיד את הכפתור לקצה שלו
		if (sideMenu.isVisible()) {
			menuButton.setTranslateX(sideMenu.getWidth() - 18);
		} else {
			menuButton.setTranslateX(0);
		}

		// אם רוחב התפריט משתנה, הכפתור נשאר צמוד לקצה שלו כשהוא פתוח
		sideMenu.widthProperty().addListener((obs, oldV, newV) -> {
			if (sideMenu.isVisible()) {
				menuButton.setTranslateX(newV.doubleValue() - 18);
			}
		});
	}

	@FXML
	private void handleToggleMenu() {
		// כאן אתה כותב מה קורה כשלוחצים על הכפתור
		boolean isVisible = sideMenu.isVisible();
		sideMenu.setVisible(!isVisible);
		sideMenu.setManaged(!isVisible);
	}

	@FXML
	private void moveMenu(javafx.event.ActionEvent event) {
		boolean opening = !sideMenu.isVisible();

		sideMenu.setVisible(opening);
		sideMenu.setManaged(opening);

		if (opening) {
			// להצמיד לקצה של התפריט כשהוא פתוח
			menuButton.setTranslateX(sideMenu.getPrefWidth() - 18); // 18 = כמה שיבלוט
		} else {
			// להצמיד לשמאל המסך כשהוא סגור
			menuButton.setTranslateX(0);
		}
	}
}
