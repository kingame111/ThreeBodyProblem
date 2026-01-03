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

	@FXML
	private TableView<BodyRow> bodiesTable;
	@FXML
	private TableColumn<BodyRow, String> colName;
	@FXML
	private TableColumn<BodyRow, Number> colM, colX, colY, colZ, colVx, colVy, colVz;

	@FXML
	private TextField nameField, massField, xField, yField, zField, vxField, vyField, vzField;

	private final ObservableList<BodyRow> rows = FXCollections.observableArrayList();


	@FXML
	private AnchorPane view3DHost;

	@FXML
	private VBox drawer;

	@FXML
	private Button drawerHandle;

	private boolean open = true;

	private double xOpen;
	private double xClosed;

	@FXML
	private void toggleDrawer() {
		setOpen(!open, true); // אם לא פתוח תפתח ותנפיש
	}

	private void setOpen(boolean shouldOpen, boolean animate) {
		open = shouldOpen;

		double w = drawer.getWidth(); // נותן את הרוחב המועדף ששמתי לdrawer
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

		bodiesTable.setItems(rows);

		colName.setCellValueFactory(c -> c.getValue().nameProperty());
		colM.setCellValueFactory(c -> c.getValue().mProperty());
		colX.setCellValueFactory(c -> c.getValue().xProperty());
		colY.setCellValueFactory(c -> c.getValue().yProperty());
		colZ.setCellValueFactory(c -> c.getValue().zProperty());
		colVx.setCellValueFactory(c -> c.getValue().vxProperty());
		colVy.setCellValueFactory(c -> c.getValue().vyProperty());
		colVz.setCellValueFactory(c -> c.getValue().vzProperty());

		// קליק על שורה -> מילוי הטופס
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
		// בסופו של דבר יוצא שהsubscene ממלא את כל הview3DHost (רווח) (anchorPane)

		subScene.widthProperty().bind(view3DHost.widthProperty()); // קושר את האורך של subScene לview3DHost
		subScene.heightProperty().bind(view3DHost.heightProperty());// אותו הדבר עם גובה

	}

	@FXML
	private void onAdd() {
		BodyRow r = readRowFromForm();
		if (r == null) return;
		rows.add(r);
		clearForm();
	}

	@FXML
	private void onUpdate() {
		BodyRow selected = bodiesTable.getSelectionModel().getSelectedItem();
		if (selected == null) {
			warn("בחר גוף לעדכון");
			return;
		}

		BodyRow updated = readRowFromForm();
		if (updated == null) return;

		int idx = rows.indexOf(selected);
		rows.set(idx, updated);
		bodiesTable.getSelectionModel().select(updated);
	}

	@FXML
	private void onRemove() {
		BodyRow selected = bodiesTable.getSelectionModel().getSelectedItem();
		if (selected != null) rows.remove(selected);
	}

	@FXML
	private void onClear() {
		clearForm();
		bodiesTable.getSelectionModel().clearSelection();
	}

	@FXML
	private void onRun() {
		Body[] bodies = buildBodiesArray();
		// פה אתה קורא לפונקציה שמתחילה סימולציה עם bodies
		// startSimulation(bodies);
		System.out.println("Bodies count = " + bodies.length);
	}

	private Body[] buildBodiesArray() {
		Body[] arr = new Body[rows.size()];
		for (int i = 0; i < rows.size(); i++) {
			BodyRow r = rows.get(i);
			double[] place = new double[]{r.getX(), r.getY(), r.getZ()};
			double[] v = new double[]{r.getVx(), r.getVy(), r.getVz()};
			arr[i] = new Body(r.getName(), r.getM(), place, v);
		}
		return arr;
	}

	private BodyRow readRowFromForm() {
		try {
			String name = safe(nameField.getText());
			double m = parse(massField, "m");
			double x = parse(xField, "x");
			double y = parse(yField, "y");
			double z = parse(zField, "z");
			double vx = parse(vxField, "vx");
			double vy = parse(vyField, "vy");
			double vz = parse(vzField, "vz");

			if (m <= 0) {
				warn("מסה חייבת להיות חיובית");
				return null;
			}
			if (name.isEmpty()) name = "Body";

			return new BodyRow(name, m, x, y, z, vx, vy, vz);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	private double parse(TextField f, String label) {
		String t = safe(f.getText());
		if (t.isEmpty()) {
			warn("חסר ערך בשדה: " + label);
			throw new NumberFormatException();
		}
		try {
			return Double.parseDouble(t);
		} catch (NumberFormatException e) {
			warn("ערך לא מספרי בשדה: " + label);
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

	private String safe(String s) {
		return s == null ? "" : s.trim();
	}
}

