package org.example;

public class Body {
	private double[] place = new double[3]; // מערך של x y z המכיל את מיקום האובייקט
	private double[] v = new double[3]; // מערך של vx vy vz המכיל את מהירויות האובייקט
	private double m; // מסת האובייקט
	private double radius; // הרדיוס של האובייקט
	private String name; // השם של האובייקט
	static final public double G = 6.67430 * Math.pow(10, -11);

	public Body(double[] place, double[] v, double m, double radius, String name) {
		this.place = place;
		this.v = v;
		this.m = m;
		this.radius = radius;
		this.name = name;
	}

	// ---getters---
	public double[] getR() {
		return java.util.Arrays.copyOf(place, place.length);
		// יוצר מערך מועתק מr באורך r.length
	}

	public double getX() {
		return place[0];
	}

	public double getY() {
		return place[1];
	}

	public double getZ() {
		return place[2];
	}

	public double[] getV() {
		return java.util.Arrays.copyOf(v, v.length);
		// יוצר מערך מועתק מv באורך v.length
	}

	public double getVx() {
		return v[0];
	}

	public double getVy() {
		return v[1];
	}

	public double getVz() {
		return v[2];
	}

	public double getM() {
		return m;
	}

	public double getRadius() {
		return radius;
	}

	public String getName() {
		return name;
	}
// ---setters---

	public void setR(double x, double y, double z) {
		this.place[0] = x;
		this.place[1] = y;
		this.place[2] = z;
	}

	public void setV(double vx, double vy, double vz) {
		this.v[0] = vx;
		this.v[1] = vy;
		this.v[2] = vz;
	}

	public void setM(double m) {
		if (m > 0) {
			this.m = m;
		} else {
			throw new IllegalArgumentException("יש לתת למסה ערך חיובי");
		}
	}

	public void setRadius(double radius) {
		if (radius > 0) {
			this.radius = radius;
		} else {
			throw new IllegalArgumentException("יש לתת לרדיוס ערך חיובי");
		}
	}


// ---methods---

	// פעולה לחישוב מרחק בין שתי גופים
	public static double distance(Body a, Body b) {
		double total = 0;
		for (int i = 0; i < 3; i++) { // מחשב את המרחק בין X,Y,Z בסדר זה ומחבר את המרחק הקודם עם העכשוי
			total = total + Math.pow(a.getR()[i] - b.getR()[i], 2);
		}
		return Math.sqrt(total);
	}

	// פעולה המחשבת את הכוח שגוף מפעיל על האובייקט (ב 2 גופים)
	public double[] gravity(Body b) {
		double d = distance(this, b);
		double magnitude = (G * this.getM() * b.getM()) / (d * d); // נוסחאת גודל כוח כבידה
		double[] Fg = new double[3];
		Fg[0] = magnitude * (b.getX() - this.getX()) / d;
		Fg[1] = magnitude * (b.getY() - this.getY()) / d;
		Fg[2] = magnitude * (b.getZ() - this.getZ()) / d;
		return Fg;
	}

	// פעולה המחשבת את סך הכוחות הפועלים על האובייקט בn גופים
	public double[] totalGravitationalForce(Body target, Body[] bodies) {
		double[] Fg = new double[3];

		for (int i = 0; i < bodies.length; i++) {

			if (target == bodies[i]) {
				continue;
			}

			double sqrtD, d, dX, dY, dZ, m;
			dX = bodies[i].getX() - target.getX();
			dY = bodies[i].getY() - target.getY();
			dZ = bodies[i].getZ() - target.getZ();
			m = target.getM() * bodies[i].getM();
			d = (dX * dX) + (dY * dY) + (dZ * dZ);
			sqrtD = Math.sqrt(d);

			double magnitude = (G * m) / d; // נוסחאת גודל כוח כבידה

			Fg[0] = Fg[0] + (magnitude * dX / sqrtD);
			Fg[1] = Fg[1] + (magnitude * dY / sqrtD);
			Fg[2] = Fg[2] + (magnitude * dZ / sqrtD);

		}
		return Fg;
	}

}
