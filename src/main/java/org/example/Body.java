package org.example;

public class Body {
	private double[] r = new double[3]; // מערך של x y z
	private double[] v = new double[3]; // מערך של vx vy vz
	private double m; // מסה
	static final public double G = 6.67430*Math.pow(10, -11);

	public Body (double[] r, double[] v, double m){
		this.r=r;
		this.v=v;
		this.m=m;


	}
	// ---getters---
	//
	public double[] getR() { return java.util.Arrays.copyOf( r, r.length );} // יוצר מערך מועתק מr באורך r.length
	public double getX() { return r[0]; }
	public double getY() { return r[1]; }
	public double getZ() { return r[2]; }

	public double[] getV() { return java.util.Arrays.copyOf( v , v.length );} // יוצר מערך מועתק מv באורך v.length
	public double getVx() { return v[0]; }
	public double getVy() { return v[1]; }
	public double getVz() { return v[2]; }

	public double getM() { return m; }


	// ---setters---

	public void setR (double x, double y, double z){
		this.r[0] = x;
		this.r[1] = y;
		this.r[2] = z;
	}

	public void setV (double vx, double vy, double vz){
		this.v[0] = vx;
		this.v[1] = vy;
		this.v[2] = vz;
	}
	public void setM (double m){
		if ( m > 0){
			this.m = m;
		}
		else {
			throw new IllegalArgumentException ("יש לתת למסה ערך חיובי");
		}
	}

    // פעולה לחישוב מרחק בין שתי גופים
	public static double distance (Body a, Body b){
		double total = 0;
		for (int i = 0; i< 3; i++){// מחשב את המרחק בין X,Y,Z בסדר זה ומחבר את המרחק הקודם עם העכשוי
			total = total + Math.pow(a.getR()[i]-b.getR()[i],2);
		}
		return Math.sqrt(total);
	}

    // פעולה המחשבת את הכוח שגוף מפעיל על האובייקט (ב 2 גופים)
	public double[] gravity ( Body b){
		double d = distance(this,b);
		double magnitude = (G*this.getM()*b.getM())/(d*d); // נוסחאת גודל כוח כבידה
		double[] Fg = new double[3];
		Fg[0] = magnitude * (b.getX() - this.getX()) / d;
		Fg[1] = magnitude * (b.getY() - this.getY()) / d;
		Fg[2] = magnitude * (b.getZ() - this.getZ()) / d;
		return Fg;
	}
    // פעולה המחשבת את סך הכוחות הפועלים על האובייקט בn גופים
	public double[] totalGravitationalForce(Body target, Body[] bodies){
		double[] Fg = new double[3];

		for (int i = 0; i < bodies.length; i++) {

			if (target == bodies[i]) {continue;}

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
