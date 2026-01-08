package org.example;

import javafx.scene.Group;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/*
  מנהל תצוגה לגופים: יוצר Spheres לפי Bodies (mapping)
  ומעדכן את המיקומים שלהן בכל פריים/עדכון (render)
 */
public final class BodyViewManager {

	private final Group world3D;

	private Sphere[] spheres = new Sphere[0];
	private Body[] bodies = new Body[0];

	private double defaultRadius;


	public BodyViewManager(Group world3D, double defaultRadius) {
		this.world3D = world3D;
		this.defaultRadius = defaultRadius;
	}

	/*
	  יוצר מחדש את ה-Spheres לפי bodies
	  לקרוא לזה כשאת לוחצת Run או כשמספר הגופים משתנה
	 */
	public void bind(Body[] bodies) {
		if (bodies == null) bodies = new Body[0];

		this.bodies = bodies;
		this.spheres = new Sphere[bodies.length];

		world3D.getChildren().clear();

		for (int i = 0; i < bodies.length; i++) {
			Sphere s = new Sphere(defaultRadius);
			spheres[i] = s;
			world3D.getChildren().add(s);
		}

		// למקם אותם מיד לפי ערכי התחלה
		render();
	}

	/*
	  עדכון מיקומים: bodies[i] -> spheres[i]
	  לקרוא לזה מתוך Platform.runLater(...) בזמן הסימולציה
	 */
	public void render() {
		int n = Math.min(bodies.length, spheres.length);

		for (int i = 0; i < n; i++) {
			Body b = bodies[i];
			Sphere s = spheres[i];

			s.setTranslateX(b.getX());
			s.setTranslateY(b.getY());
			s.setTranslateZ(b.getZ());
		}
	}

	/*
	  אופציונלי: להחליף חומר (צבע) לכל גוף לפי אינדקס
	  לקרוא לזה אחרי bind אם בא לך צבעים שונים
	 */
	public void setMaterial(int index, PhongMaterial material) {
		if (index < 0 || index >= spheres.length) return;
		spheres[index].setMaterial(material);
	}

	public Sphere[] getSpheres() {

		return spheres;
	}

	public void setDefaultRadius(double defaultRadius) {

		this.defaultRadius = defaultRadius;
	}

}
