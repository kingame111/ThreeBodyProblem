package org.example;

public class Variables {
    private double x,y,z;
    private double Vx,Vy,Vz;
    private double m;


    // ---getters---

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public double getVx() { return Vx; }
    public double getVy() { return Vy; }
    public double getVz() { return Vz; }
    public double getM() { return m; }


// ---setters---

    public void setX (double x){ this.x = x;}
    public void setY (double y){ this.y = y;}
    public void setZ (double z){ this.z = z;}
    public void setVx (double Vx){ this.Vx = Vx;}
    public void setVy (double Vy){ this.Vy = Vy;}
    public void setVz (double Vz){ this.Vz = Vz;}
    public void setM (double m){
        if ( m > 0){
            this.m = m;
        }
        else {
            throw new IllegalArgumentException ("יש לתת למסה ערך חיובי");
        }
    }


}
