package org.example;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;

public class MainController {

	private final ObservableList<BodyRow> rows = FXCollections.observableArrayList();
	@FXML
	private TableView<BodyRow> bodiesTable;
	@FXML
	private TableColumn<BodyRow, String> colName;
	@FXML
	private TableColumn<BodyRow, Number> colM, colX, colY, colZ, colVx, colVy, colVz;
	@FXML
	private TextField nameField, massField, xField, yField, zField, vxField, vyField, vzField;
	// י ObservableList<BodyRow> אומר שהרשימה 'חכמה' כלומר היא יודעת לדווח כשבוצעו בה שינויים והיא מסוג BodyRow
	// י FXCollections היא מחלקה וobservableArrayList() היא פעולה במחלקה שמחזירה לי אובייקט חדש מסוג ObservableList
	@FXML
	private AnchorPane view3DHost;

	@FXML
	private VBox drawer;

	@FXML
	private Button drawerHandle;

	private BodyViewManager viewManager;

	private boolean open = true;

	private double xOpen;
	private double xClosed;

	// כבר לא שימושי אבל אין סיבה למחוק
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
	private void toggleDrawer() {
		setOpen(!open, true);// אם לא פתוח תפתח ותנפיש
	}

	private void setOpen(boolean shouldOpen, boolean animate) {
		open = shouldOpen;

		double w = drawer.getWidth(); // נותן את הרוחב המועדף ששמתי לdrawer

		if (w <= 0) {
			w = drawer.getPrefWidth();
		}

		double targetX = open ? 0 : -w;   //אם open הוא true אז לא זזים אם הוא false אז זזים w (מינוס בגלל שזזים לכיוון הנגדי)


		drawerHandle.setLayoutX(shouldOpen ? xOpen : xClosed);
		drawerHandle.setText(shouldOpen ? "✕" : "☰");

		if (!animate) {
			drawer.setTranslateX(targetX);
			return;
		}

		TranslateTransition tt = new TranslateTransition(Duration.millis(220), drawer); // יוצר משתנה חדש המנפיש את drawer בהזזה הנמשכת 220
		tt.setToX(targetX); // קובע עד מתי drawer צריכה לזוז
		tt.setInterpolator(Interpolator.EASE_BOTH); // עושה שהאנימציה תתחיל לאט תהיה מהיר באמצע ותסיים לאט
		tt.play();


	}

	@FXML
	public void initialize() {


		Group world = new Group();

		world.setScaleY(-1); // ברירת המחדל של ציר הY היא כלפי מטה לכן אני הופך אותו

		Group spheres = new Group();
		world.getChildren().addAll(spheres);
		viewManager = new BodyViewManager(spheres, 5); // TODO להוסיף תגובה

		/*
		Sphere spheres = new Sphere(60); // יוצר עיגול עם רדיוס 60
		PhongMaterial material = new PhongMaterial(); // יוצר חומר חדש בשם material שמאוחר יותר ניתן לשייך לצורה
		material.setDiffuseColor(Color.DODGERBLUE); //קובע את הצבע של החומר material ל-DODGERBLUE
		spheres.setMaterial(material); // קובע את החומר של spheres לmaterial
		world.getChildren().add(spheres); // מוסיף את spheres לworld
		*/

		bodiesTable.setItems(rows); // י bodiesTable זה הid של הtableView
		// השורה הזאת מחברת בין הטבלה (bodiesTable) לבין הרשימה הכחמה של השורות (rows)
		// מה שהחיבור נותן לי זה שכאשר rows משתנה הטבלה מתעדכנת לבד

		colName.setCellValueFactory(c -> c.getValue().nameProperty());
		colM.setCellValueFactory(c -> c.getValue().mProperty());
		colX.setCellValueFactory(c -> c.getValue().xProperty());
		colY.setCellValueFactory(c -> c.getValue().yProperty());
		colZ.setCellValueFactory(c -> c.getValue().zProperty());
		colVx.setCellValueFactory(c -> c.getValue().vxProperty());
		colVy.setCellValueFactory(c -> c.getValue().vyProperty());
		colVz.setCellValueFactory(c -> c.getValue().vzProperty());
		/*
		אסביר בעזרת העמודה M:
		יש עמודה שלמה של ערכי m אז איך javafx יודע לאיזה ערך m לגשת?
		לכן javafx שולח פרמטר c שמחזיק מידע על התא שהיא צריכה לגשת אליו ובעזרת getValue היא מחלצת את השורה
		הפעולה מחזירה את הproperty של m כדי שהטבלה תוכל להאזין לשינויים בm ולהתעדכן בהתאם

		הסימון <- הוא קיצור שחוסך לי כאב ראש:
		 מה שהוא עושה זה אומר לפעולה לקבל משתנה c ולהחזיר c.getValue().vyProperty()
		 */


		bodiesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldRow, r) -> {
			if (r == null) return;
			nameField.setText(r.getName());
			massField.setText(Double.toString(r.getM()));
			xField.setText(Double.toString(r.getX()));
			yField.setText(Double.toString(r.getY()));
			zField.setText(Double.toString(r.getZ()));
			vxField.setText(Double.toString(r.getVx()));
			vyField.setText(Double.toString(r.getVy()));
			vzField.setText(Double.toString(r.getVz()));
		});
		/*
		י SelectionModel זה אובייקט שמצביע על השורה שהמשתמש כרגע בוחר
		(יכול להיות שורה או שום שורה) כך שאנחנו יודעים איזה שורה זאת
		י selectedItemProperty מחזיק את הproperty של השורה הזאת
		י addListener בודק אם בוצע שינוי בselectedItemProperty ואם אכן בוצע שינוי הוא מפעיל את הקוד שבתוכו
		*/


		xClosed = 12;
		xOpen = drawer.getPrefWidth() + 12;

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
		subScene.setFill(Color.rgb(0, 5, 16)); // קובע רקע בצבע הנתון (שחור)

		view3DHost.getChildren().add(subScene); // מוסיף את הסצנה הפנימית לתוך מיכל העוגן במסך

		AnchorPane.setTopAnchor(subScene, 0.0); // מצמיד את הsubScene למעלה
		AnchorPane.setRightAnchor(subScene, 0.0); // מצמיד את הsubScene לימין
		AnchorPane.setBottomAnchor(subScene, 0.0); // מצמיד את הsubScene למטה
		AnchorPane.setLeftAnchor(subScene, 0.0); //מצמיד את הsubScene לשמאל
		// בסופו של דבר יוצא שהsubScene ממלא את כל הview3DHost (רווח) (anchorPane)

		subScene.widthProperty().bind(view3DHost.widthProperty()); // קושר את האורך של subScene לview3DHost
		subScene.heightProperty().bind(view3DHost.heightProperty());// אותו הדבר עם גובה

		setOpen(true, false);
	}

	@FXML
	private void onAdd() {
		BodyRow r = readRowFromForm(); // י r שווה לערכים שהמשתמש הכניס לgridPane
		if (r == null) return;
		rows.add(r);
		clearForm();
	}
	// מוסיף שורה עם נתונים לרשימה 'החכמה' (rows) של השורות ובגלל שהיא מקושרת לטבלה אז זה מוסיף גם לטבלה את שורת הנתונים

	@FXML
	private void onUpdate() {
		BodyRow selected = bodiesTable.getSelectionModel().getSelectedItem();
		// י getSelectionModel מחזיר את השורה שכרגע בחורה וgetSelectedItem מחזיר את הproperty של השורה הבחורה
		if (selected == null) {
			warn("בחר גוף לעדכון");
			return;
		}

		BodyRow updated = readRowFromForm(); // י updated שווה לערכים שהמשתמש הכניס לgridPane
		if (updated == null) {
			return;
		}

		int idx = rows.indexOf(selected); // י idx שווה לאינדקס של השורה ברשימת השורות
		rows.set(idx, updated); // מיישם את הערכים החדשים
		bodiesTable.getSelectionModel().select(updated); // מסמן את השורה ששונתה כדי שהמשתמש יוכל לראות שבוצע שינוי
	}

	@FXML
	private void onRemove() {
		BodyRow selected = bodiesTable.getSelectionModel().getSelectedItem();
		if (selected != null) {
			rows.remove(selected);
		}
	}
	// מוחק את השורה שנבחרה

	@FXML
	private void onClear() {
		clearForm();
		bodiesTable.getSelectionModel().clearSelection();
	}
	// מוחק את כל הערכים בgridPane

	@FXML
	private void onRun() {

		if (rows.isEmpty()) {
			warn("אין גופים להרצה");
			return;
		}

		Body[] bodies = buildBodiesArray();

		// זה יוצר את מערך הספירות לפי מערך הגופים
		viewManager.bind(bodies);

		new Thread(() -> {
			Simulation.simulator(0.0, 1000.0, bodies, viewManager);
		}, "sim-thread").start();
		// מה שיגרום לסימולציה לרוץ
	}


	private Body[] buildBodiesArray() {
		Body[] arr = new Body[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			BodyRow r = rows.get(i); // שומר את הנתונים של השורה i שבrows בr
			double[] place = new double[]{r.getX(), r.getY(), r.getZ()};
			double[] v = new double[]{r.getVx(), r.getVy(), r.getVz()};
			arr[i] = new Body(r.getName(), r.getM(), place, v);
		}
		return arr;
	}
	// יוצר מערך של גופים על בסיס הנתונים ברשימה 'החכמה' (rows)

	private BodyRow readRowFromForm() {
		try {
			String name = safe(nameField.getText()); // י safe מוגדר בהמשך
			double m = parse(massField, "m"); // י parse מוגדר בהמשך
			double x = parse(xField, "x");
			double y = parse(yField, "y");
			double z = parse(zField, "z");
			double vx = parse(vxField, "vx");
			double vy = parse(vyField, "vy");
			double vz = parse(vzField, "vz");

			if (m <= 0) {
				warn("Enter a positive Mass value");
				return null;
			}
			if (name.isEmpty()) {
				warn("Enter a name");
				return null;
			}

			return new BodyRow(name, m, x, y, z, vx, vy, vz);
		} catch (NumberFormatException ex) { // בו
			return null;
		}
	}

	private double parse(TextField f, String label) {
		String t = safe(f.getText());
		if (t.isEmpty()) {
			warn("Value is missing in field: " + label);
			throw new NumberFormatException();
		}
		try {
			return Double.parseDouble(t);
		} catch (NumberFormatException e) {
			warn("Non-numeric value in field: " + label);
			throw e;
		}
	}

	private void clearForm() {
		nameField.clear();
		massField.clear();
		xField.clear();
		yField.clear();
		zField.clear();
		vxField.clear();
		vyField.clear();
		vzField.clear();
	}

	private void warn(String msg) {
		Alert a = new Alert(Alert.AlertType.WARNING);
		a.setHeaderText(null);
		a.setContentText(msg);
		a.showAndWait();
	}
	// פותח חלון אזהרה אם התוכן שwarn מקבל

	private String safe(String s) {
		return s == null ? "" : s.trim(); // י trim מוחק אם יש רווחים בסוף ובהתחלה
	}
}

