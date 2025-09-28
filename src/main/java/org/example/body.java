package org.example;

public class body {
	private double[] r = new double[3]; // מערך של x y z
	private double[] v = new double[3]; // מערך של vx vy vz
	private double m; // מסה
    final public double G = 6.67430*Math.pow(10, -11);
    body object [] = new body[3];


    // ---getters---
    //
    public double[] getR() { return r; }
    public double getX() { return r[0]; }
    public double getY() { return r[1]; }
    public double getZ() { return r[2]; }

	public double getVx() { return v[0]; }
    public double getVy() { return v[1]; }
    public double getVz() { return v[2]; }

	public double getM() { return m; }


    // ---setters---

	public void setR (double x, double y, double z){
		this.r[0] = x;
		this.r[0] = y;
		this.r[0] = z;
	}

	public void setV (double vx, double vy, double vz){
		this.v[0] = vx;
		this.v[0] = vy;
		this.v[0] = vz;
	}
	public void setM (double m){
		if ( m > 0){
			this.m = m;
		}
		else {
			throw new IllegalArgumentException ("יש לתת למסה ערך חיובי");
		}
	}


	public double distance (body a, body b){ // מרחק בין שתי גופים
		double total = 0;
		for (int i = 0; i< 3; i++){// מחשב את המרחק בין X,Y,Z בסדר זה ומחבר את המרחק הקודם עם העכשוי
			total = total + Math.pow(a.getR()[i]-b.getR()[i],2);
		}
		return Math.sqrt(total);
	}


	public double gravity (body a, body b){
		double magnitude = (G*a.getM()*b.getM())/Math.pow(a.getX() - b.getX(), 2); // נוסחאת גודל כוח כבידה
		return magnitude;
	}




}
