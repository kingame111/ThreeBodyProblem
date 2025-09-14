package org.example;

public class Variables {
    private double[] r = new double[3];
    private double[] v = new double[3];
    private double m;


    // ---getters---

    public double[] getR() { return r; }

    public double[] getV() { return v; }

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


}
