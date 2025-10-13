package org.example;

import org.hipparchus.ode.OrdinaryDifferentialEquation;

public class NBodiesCalculations implements OrdinaryDifferentialEquation {
    private final int bodies;    // מספר הגופים בסימולציה
    private final double[] m;   // המסות של הגופים בסימולציה

    public NBodiesCalculations(double[] masses){
        this.bodies = masses.length;
        this.m = masses.clone();
    }


    // פעולה שמסדרת את המערך state למקרא שהוא מגיע מסודר בצורה לא נוחה
    public static double[] toStateArray(Body[] bodies) {
        double[] state = new double[6 * bodies.length];
        for (int i = 0; i < bodies.length; i++) {
            int k = 6 * i;
            state[k]   = bodies[i].getX();
            state[k+1] = bodies[i].getY();
            state[k+2] = bodies[i].getZ();
            state[k+3] = bodies[i].getVx();
            state[k+4] = bodies[i].getVy();
            state[k+5] = bodies[i].getVz();
        }
        return state;
    }




    @Override
    public int getDimension() {
        return (6 * bodies); /* קובע שהמימד של המערכת יהיה תלת מימדי
    בגלל שלכל גוף בתלת מימד יש שישה ערכים התחלתיים (XYZ,vXvYvZ)

    הערך שהפעולה מחזירה (6 * n) מבוטא כמערך שנרא כך:
    [x1, y1, z1, vx1, vy1, vz1, x2, y2, z2, vx2, vy2, vz2, ..., xN, yN, zN, vxN, vyN, vzN]
    כלומר הערכים במערך מסודרים על פי מיקום ואז מהירות לכל גוף

    */
    }

    @Override
    public double[] computeDerivatives(double t, double[] state) {
        double[] stateDerivative = new double[6 * bodies];

        // :stateDerivative לולאה ליישום ערכי המהירות בתוך
        for (int i=0; i < bodies; i++){
            int k = i*6;
            stateDerivative[k] = state[k+3];
            stateDerivative[k+1] = state[k+4];
            stateDerivative[k+2] = state[k+5];

        }

        // :stateDerivative לולאה להמרת המהירות לתאוצה והשמת התוצאה בתוך
        for (int i=0; i < bodies; i++){
            int k = i*6;
            stateDerivative[k+3] = state[k*3];
            stateDerivative[k+4] = state[k*4];
            stateDerivative[k+5] = state[k*5];

        }
        return stateDerivative;
    }


}