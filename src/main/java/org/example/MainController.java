package org.example;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

public class MainController {


	@FXML
	private AnchorPane view3DHost;

	@FXML
	private VBox drawer;

	@FXML
	private Button drawerHandle;

	private boolean open = true;

	@FXML
	private void toggleDrawer() {
		setOpen(!open, true); // אם לא פתוח תפתח ותנפיש
	}

	private void setOpen(boolean shouldOpen, boolean animate) {
		open = shouldOpen;

		double w = drawer.getPrefWidth(); // נותן את הרוחב המועדף שסמתי לdrawer
		double targetX = open ? 0 : -w;   //אם open הוא true אז לא זזים אם הוא false אז זזים w (מינוס בגלל שזזים לכיוון הנגדי)
		if (!animate) {
			drawer.setTranslateX(targetX);
			drawerHandle.setText(shouldOpen ? "✕" : "☰"); // אם shouldOpen נכון אז x אחרת ☰
			return;
		}

		TranslateTransition tt = new TranslateTransition(Duration.millis(220), drawer); // יוצר משתנה חדש המנפיש את drawer בהזזה הנמשכת 220
		tt.setToX(targetX); // קובע עד מתי drawer צריכה לזוז
		tt.setInterpolator(Interpolator.EASE_BOTH); // עושה שהאנימציה תתחיל לאט תהיה מהיר באמצע ותסיים לאט
		tt.play();

		drawerHandle.setText(shouldOpen ? "✕" : "☰");
	}

	public static Sphere[] twoBodyDemoImplementer() {
		Sphere twoSphereDemo[] = new Sphere[2];
		for (int i = 0; i < ScenarioFactory.twoBodyDemo().length; i++) {
			twoSphereDemo[i] = new Sphere(30);
			PhongMaterial material1 = new PhongMaterial();
			material1.setDiffuseColor(Color.PURPLE);
			twoSphereDemo[i].setMaterial(material1);
		}
		twoSphereDemo[0].setTranslateX(100);
		twoSphereDemo[0].setTranslateZ(100);
		return twoSphereDemo;
	}

	@FXML
	public void initialize() {

		Group world = new Group();
		world.setScaleY(-1); // ברירת המחדל של ציר הY היא כלפי מטה לכן אני הופך אותו

		Group spheres = new Group();
		Sphere[] twoBodyDemoSpheres = twoBodyDemoImplementer();
		spheres.getChildren().addAll(twoBodyDemoSpheres);
		spheres.setScaleY(-1);
		world.getChildren().addAll(spheres);

		/*Sphere spheres = new Sphere(60); // יוצר עיגול עם רדיוס 60
		PhongMaterial material = new PhongMaterial(); // יוצר חומר חדש בשם material שמאוחר יותר ניתן לשייך לצורה
		material.setDiffuseColor(Color.DODGERBLUE); //קובע את הצבע של החומר material ל-DODGERBLUE
		spheres.setMaterial(material); // קובע את החומר של spheres לmaterial
		world.getChildren().add(spheres); // מוסיף את spheres לworld
		*/


		AmbientLight ambient = new AmbientLight(Color.WHITE); // יוצר אור בשם ambient בצבע לבן
		PointLight light = new PointLight(Color.WHITE); // יוצר מקור אור נקודתי בשם light בצבע לבן
		light.setTranslateX(0); // מזיז את אור שמאלה בציר X
		light.setTranslateY(0); // מזיז את אור למעלה בציר Y
		light.setTranslateZ(-1000); // מזיז את האור לכיוון המסך (קדימה) בציר Z
		world.getChildren().addAll(ambient, light); // מוסיף את שני האורות לעולם


		PerspectiveCamera camera = new PerspectiveCamera(true); // יוצר מצלמה פרספקטיבית שיש בה עומק (true)
		camera.setNearClip(0.1); // קובע מרחק מינימלי שממנו רואים (קרוב מדי ייחתך או יעלם)
		camera.setFarClip(10000); // קובע מרחק מקסימלי שממנו רואים (רחוק מדי ייחתך או יעלם)
		camera.setTranslateZ(-500); // מרחיק את המצלמה כדי שהכדור ייכנס לפריים


		SubScene subScene = new SubScene( // יוצר את מה שיחיל את התלת מימד
				world, 400, 400, // מצרף את subScene לworld ונותן לה מיקום התחלתי
				true, // מפעיל משהו שעושה שהעומק יעבוד נכון (ולא יהיה בו בעיות)
				SceneAntialiasing.BALANCED //מחליק קצוות כדי שייראה יפה יותר (עושה שלא יהיה שיניים משוננות לצורות בsubScene)
		);


		subScene.setCamera(camera); // מחבר את המצלמה לsubscene
		subScene.setFill(Color.rgb(20, 20, 20)); // קובע רקע בצבע הנתון (שחור)

		view3DHost.getChildren().add(subScene); // מוסיף את הסצנה הפנימית לתוך מיכל העוגן במסך

		AnchorPane.setTopAnchor(subScene, 0.0); // מצמיד את הsubScene למעלה
		AnchorPane.setRightAnchor(subScene, 0.0); // מצמיד את הsubScene לימין
		AnchorPane.setBottomAnchor(subScene, 0.0); // מצמיד את הsubScene למטה
		AnchorPane.setLeftAnchor(subScene, 0.0); //מצמיד את הsubScene לשמאל
		// בסופו של דבר יוצא שהsubscene ממלא את כל הview3DHost (רווח) (anchorPane)

		subScene.widthProperty().bind(view3DHost.widthProperty()); // קושר את האורך של subScene לview3DHost
		subScene.heightProperty().bind(view3DHost.heightProperty());// אותו הדבר עם גובה

		setOpen(true, false);
	}
}
