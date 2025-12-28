package org.example;


import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

public class MainController {

	public MainController() {
		System.out.println("=== CONTROLLER CONSTRUCTOR RUN ===");
	}

	@FXML
	private AnchorPane view3DHost;

	@FXML
	public void initialize() {

		Group world = new Group();
		world.setScaleY(-1); // ברירת המחדל של ציר הY היא כלפי מטה לכן אני הופך אותו


		Sphere sphere = new Sphere(60); // יוצר עיגול עם רדיוס 60
		PhongMaterial material = new PhongMaterial(); // יוצר חומר חדש בשם material שמאוחר יותר ניתן לשייך לצורה
		material.setDiffuseColor(Color.DODGERBLUE); //קובע את הצבע של החומר material ל-DODGERBLUE
		sphere.setMaterial(material); // קובע את החומר של sphere לmaterial
		world.getChildren().add(sphere); // מוסיף את sphere לworld


		AmbientLight ambient = new AmbientLight(Color.WHITE); // יוצר אור בשם ambient בצבע לבן
		PointLight light = new PointLight(Color.WHITE); // יוצר מקור אור נקודתי בשם light בצבע לבן
		light.setTranslateX(-200); // מזיז את אור שמאלה בציר X
		light.setTranslateY(-200); // מזיז את אור למעלה בציר Y
		light.setTranslateZ(-400); // מזיז את האור לכיוון המסך (קדימה) בציר Z
		world.getChildren().addAll(ambient, light); // מוסיף את שני האורות לעולם


		PerspectiveCamera camera = new PerspectiveCamera(true); // יוצר מצלמה פרספקטיבית שיש בה עומק (true)
		camera.setNearClip(0.1); // קובע מרחק מינימלי שממנו רואים (קרוב מדי ייחתך או יעלם)
		camera.setFarClip(10000); // קובע מרחק מקסימלי שממנו רואים (רחוק מדי ייחתך או יעלם)
		camera.setTranslateZ(-500); // מרחיק את המצלמה כדי שהכדור ייכנס לפריים


		SubScene subScene = new SubScene( // יוצר "סצנה פנימית" לתלת־ממד בתוך מסך רגיל
				world, 400, 400, // הקבוצה הראשית + רוחב/גובה התחלתיים
				true, // מפעיל בדיקת עומק כדי שאובייקטים יסתירו אחד את השני נכון
				SceneAntialiasing.BALANCED // מחליק קצוות כדי שייראה יפה יותר
		);

		subScene.setCamera(camera); // מחבר את המצלמה לsubscene
		subScene.setFill(Color.rgb(20, 20, 20)); // קובע רקע בצבע הנתון (שחור)

		view3DHost.getChildren().add(subScene); // מוסיף את הסצנה הפנימית לתוך מיכל העוגן במסך

		AnchorPane.setTopAnchor(subScene, 0.0); // מצמיד את הsubScene למעלה
		AnchorPane.setRightAnchor(subScene, 0.0); // מצמיד את הsubScene לימין
		AnchorPane.setBottomAnchor(subScene, 0.0); // מצמיד את הsubScene למטה
		AnchorPane.setLeftAnchor(subScene, 0.0); //מצמיד את הsubScene לשמאל
		// בסופו של דבר יוצא שהsubscene ממלא את כל המסך (היפותטית)


		// view3DHost = id of AnchorPane // and subscene is inside view3DHost
		view3DHost.widthProperty().addListener((obs, oldVal, newVal) ->
				subScene.setWidth(newVal.doubleValue())
		);
		/* addListener = view3DHost.widthProperty()פונקציה הנקראת אם בוצע שינוי ב
		obs = האפיין שבו קרה השינוי //	 במקרה הזה כבר ידוע שהמשתנה הזה הוא view3DHost.widthProperty()
		oldVal = obs הערך הקודם של (משוג Number)
		newVal = obs הערך החדש של (משוג Number)
		י Number הוא מחלקת אב של של המחלקות המספריות (Integer,Double,Long etc)
		בשורה newVal.doubleValue() אנחנו מבקשים שהערך של newVal יוחזר כdouble אבל המשתנה נשאר להיות משוג Number
		*/

		view3DHost.heightProperty().addListener((obs, oldVal, newVal) ->
				subScene.setHeight(newVal.doubleValue())
		);
		// אותו הדבר כמו הקודם רק לגובה
	}

}
