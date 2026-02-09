package org.example;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.Random;

/*
  מנהל תצוגה לגופים: יוצר Spheres לפי Bodies (mapping)
  ומעדכן את המיקומים שלהן בכל פריים/עדכון (render)
 */
public final class BodyViewManager {

	private final Group world3D;

	private final Random rand = new Random(); // על פעם שיקרא הוא יחזיר ערך חדש

	private Sphere[] spheres = new Sphere[0];
	private Body[] bodies = new Body[0];

	private double defaultRadius;

	// בנאי
	public BodyViewManager(Group world3D, double defaultRadius) {
		this.world3D = world3D;
		this.defaultRadius = defaultRadius;
	}

	/*
		 יוצר מחדש את מערך הכדורים על פי מערך הגופים
		 מוסיף אותם לעולם וממקם אותם במיקומם ההתחלתי
	 */
	public void bind(Body[] bodies) {
		if (bodies == null) bodies = new Body[0];

		this.bodies = bodies;
		this.spheres = new Sphere[bodies.length];

		world3D.getChildren().clear();

		for (int i = 0; i < bodies.length; i++) {
			Sphere s = new Sphere(defaultRadius);
			spheres[i] = s;
			setMaterial(i, new PhongMaterial());
			world3D.getChildren().add(s);
		}

		render(); // מיקום על פי ערכים התחלתיים
	}

	/*
		מזיז את כדורי הגופים על פי מיקומי הגופים
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

	// TODO לעשות שלכל גוף יהיה צבע שונה
	/*
	  אופציונלי: להחליף חומר (צבע) לכל גוף לפי אינדקס
	  לקרוא לזה אחרי bind אם בא לך צבעים שונים
	 */
	public void setMaterial(int index, PhongMaterial material) {
		if (index < 0 || index >= spheres.length) {
			return;
		}

		setRandomColor(material);
		spheres[index].setMaterial(material);
	}

	// נותן לחומר צבע רנדומלי וברק
	private void setRandomColor(PhongMaterial material) {
		Color color = Color.hsb(rand.nextDouble() * 360, 0.85, 0.95);
		material.setDiffuseColor(color);
		material.setSpecularColor(Color.WHITE);
		material.setSpecularPower(16); // נותן ברק לחומר
	}

	public Sphere[] getSpheres() {

		return spheres;
	}

	public void setDefaultRadius(double defaultRadius) {

		this.defaultRadius = defaultRadius;
	}

}
