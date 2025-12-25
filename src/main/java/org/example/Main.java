package org.example;

import org.hipparchus.ode.ODEState;
import org.hipparchus.ode.ODEStateAndDerivative;
import org.hipparchus.ode.OrdinaryDifferentialEquation;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

// ===== JavaFX בסיסי =====
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;

// ===== Layouts =====
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

// ===== Controls (קלט/כפתורים/טקסט) =====
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.SplitPane;


// ===== Table Editing =====
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;

// ===== Collections =====
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// ===== Properties (לשורות הטבלה) =====
import javafx.beans.property.SimpleDoubleProperty;

// ===== Animation / Rendering Loop =====
import javafx.animation.AnimationTimer;

// ===== Concurrency (חישוב סימולציה ברקע) =====
import javafx.concurrent.Task;

// ===== Events (עכבר/גלגלת/מקלדת) =====
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

// ===== JavaFX 3D Scene =====
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Camera;

// ===== 3D Shapes + Materials =====
import javafx.scene.shape.Sphere;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.paint.Color;

// ===== 3D Transforms =====
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.geometry.Point3D;

// ===== Lighting (כדי שזה ייראה תלת־ממדי באמת) =====
import javafx.scene.AmbientLight;
import javafx.scene.PointLight;

// ===== Java כללי =====
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
		Scanner scanner = new Scanner(System.in);
		scanner.useLocale(Locale.US); // עושה שקליטת מספרים עשרוניים תהיה עם נקודה ולא פסיק

		System.out.println("enter the number of bodies");
		Body[] bodies = new Body[scanner.nextInt()];

		double[] place;
		double[] v;
		double m;
		String name;

		//  לולאה המיישמת את הערכים של כל משתנה(מיקום, מהירות, מסה, שם הגוף)
		for (int i = 0; i < bodies.length; i++) {

			System.out.println("enter the position values (X,Y,Z) of body #" + (i + 1));
			place = new double[]{scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()};

			System.out.println("enter the velocity values (vX,vY,vZ) of body #" + (i + 1));
			v = new double[]{scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()};

			System.out.println("enter the mass value of body #" + (i + 1));
			m = scanner.nextDouble();

			System.out.println("enter the name of body #" + (i + 1));
			name = scanner.next();

			bodies[i] = new Body(place, v, m, name);

		}
		// הרצה לדוגמה: זמן התחלה 0, זמן סיום 10 שניות
		double[] stateFinal = simulator(0.0, 10.0, bodies);
		System.out.println("Final state at t = 10.0: ");
		System.out.println(Arrays.toString(stateFinal));

		scanner.close();
	}


	public static double[] simulator(double tStart, double tEnd, Body[] bodies) {
		OrdinaryDifferentialEquation ODE = new NBodiesCalculations(bodies);
				/*
				OrdinaryDifferentialEquation כדי להגדיר אובייקט מסוג
				צריך לתת לו את מספר המימדים ופעולה המחשבת את ערכי הנגזרת
				וזה בדיוק מה שיש בNBodiesCalculations(bodies) והסיבה למה יצרתי אותה
				*/

		int dim = ODE.getDimension();

		double minStep = 1e-6; // עשר בחזקת מינוס שש
		double maxStep = 1;
		double absTol = 1e-10; // עשר בחזקת מינוס עשר
		double relTol = 1e-10; // עשר בחזקת מינוס עשר

		double[] y0 = NBodiesCalculations.StateSort(bodies);

		DormandPrince853Integrator integrator = new DormandPrince853Integrator(minStep, maxStep, absTol, relTol);

		ODEState initial = new ODEState(tStart, y0);
		ODEStateAndDerivative finalState = integrator.integrate(ODE, initial, tEnd);


		return finalState.getPrimaryState();
	}

	public static void Normalizer(Body[] bodies) {
		double averagePlace = 0;
		double sumplace = 0;

		double averageV = 0;
		double sumV = 0;


		for (int i = 0; i < bodies.length; i++) {
			sumplace = sumplace + bodies[i].getX() + bodies[i].getY() + bodies[i].getZ(); // מחבר את ערכי המיקום של כל הגופים
			sumV = sumV + bodies[i].getVx() + bodies[i].getVy() + bodies[i].getVz(); // vX
		}
		averagePlace = sumplace / (bodies.length * 3);
		averageV = sumV / (bodies.length * 3);


		for (int i = 0; i < bodies.length; i++) {
			bodies[i].setR(bodies[i].getX() / averagePlace, bodies[i].getY() / averagePlace, bodies[i].getZ() / averagePlace);
			bodies[i].setV(bodies[i].getVx() / averageV, bodies[i].getVy() / averageV, bodies[i].getVz() / averageV);

		}

	}

	public static void energyCalc() {

	}

	private int counter = 0;

	@Override
	public void start(Stage stage) throws Exception {


		Label label = new Label("עוד לא לחצת.");
		Button button = new Button("לחץ אותי");

		button.setOnAction(e -> {
			counter++;
			label.setText("לחצת " + counter + " פעמים");
		});

		VBox root = new VBox(12, label, button); // 12 = רווח בין רכיבים
		Scene scene = new Scene(root, 400, 200);

		stage.setTitle("Hello JavaFX");
		stage.setScene(scene);
		stage.show();
	}
}




