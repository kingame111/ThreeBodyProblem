package org.example;

import java.util.Arrays;

public class Body {
	static public double G = 6.67430 * Math.pow(10, -11);
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

}
