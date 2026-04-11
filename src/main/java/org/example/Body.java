package org.example;

import java.util.Arrays;

public class Body {
	static public double G = 1;//6.67430 * Math.pow(10, -11);
	private double[] place = new double[3]; // מערך של x y z המכיל את מיקום האובייקט
	private double[] v = new double[3]; // מערך של vx vy vz המכיל את מהירויות האובייקט
	private double m; // מסת האובייקט
	private String name; // השם של האובייקט

	public Body(String name, double m, double[] place, double[] v) {
		this.name = name;
		this.m = m;
		this.place = place.clone();
		this.v = v.clone();
	}

	// פעולה לחישוב מרחק בין שתי גופים
	public static double distance(Body a, Body b) {
		double total = 0;
		for (int i = 0; i < 3; i++) { // מחשב את המרחק בין X,Y,Z בסדר זה ומחבר את המרחק הקודם עם העכשוי
			total = total + Math.pow(a.getR()[i] - b.getR()[i], 2);
		}
		return Math.sqrt(total);
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

	public void setM(double m) {
		if (m > 0) {
			this.m = m;
		} else {
			throw new IllegalArgumentException("יש לתת למסה ערך חיובי");
		}
	}
// ---setters---

	public String getName() {
		return name;
	}

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


// ---methods---

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


	/*
	 פעולה ההופכת את נתוני הגוף לString על פי String שהי מקבלת
	 לדוגמה הקלט "m" ידפיס את ערך מסת בגוף כString
	 */
	public String toString(String var) {
		var = var.toLowerCase();

		switch (var) {
			case "place" -> { //
				return Arrays.toString(place); // אם var שווה place (String) אז מחזיר את המערך place כString
			}
			case "v" -> {
				return Arrays.toString(v); // אם var שווה v (String) אז מחזיר את המערך v כString
			}
			case "m" -> {
				return Double.toString(m); // אם var שווה m (String) אז מחזיר את המשתנה m כString
			}
			default -> {
				return " משתנה לא ידוע" + var;
			}

		}
	}


}
