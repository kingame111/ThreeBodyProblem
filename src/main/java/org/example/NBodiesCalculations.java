package org.example;

import org.hipparchus.ode.OrdinaryDifferentialEquation;

public class NBodiesCalculations implements OrdinaryDifferentialEquation {
	private final int bodiesNum; // מספר הגופים בסימולציה
	private final double[] m; // המסות של הגופים בסימולציה
	private final double[] a; // התאוצות של הגופים השונים בצירים השונים
	private final double[] stateDer; // הנגזרת של state


	// בנאי
	public NBodiesCalculations(double[] masses) {
		this.bodiesNum = masses.length;
		this.m = masses.clone();
		this.a = new double[3 * masses.length];
		this.stateDer = new double[6 * masses.length];

	}


	// פעולה שמסדרת את המערך state למקרא שהוא מגיע מסודר בצורה לא נוחה
	// [x1, y1, z1, vx1, vy1, vz1...] סדר:
	public static double[] StateSort(Body[] bodies) {
		double[] state = new double[6 * bodies.length];
		for (int i = 0; i < bodies.length; i++) {
			int k = 6 * i;
			state[k] = bodies[i].getX();
			state[k + 1] = bodies[i].getY();
			state[k + 2] = bodies[i].getZ();
			state[k + 3] = bodies[i].getVx();
			state[k + 4] = bodies[i].getVy();
			state[k + 5] = bodies[i].getVz();
		}
		return state;
	}

	// נוסחה לחישוב מרחק בריבוע (משאירים את הריבוע מטעמי נוחות)
	public double distance2(double x1, double y1, double z1, double x2, double y2, double z2) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		double dz = z1 - z2;
		return dx * dx + dy * dy + dz * dz;
	}


	// פעולה המחשבת תאוצה של גוף
	public void accelerationCalc(double[] state) {

		for (int i = 0; i < a.length; i++) { // מאפס את כל הערכים של a
			a[i] = 0.0;
		}

		for (int i = 0; i < bodiesNum; i++) {
			int i6 = 6 * i;
			double xi = state[i6];
			double yi = state[i6 + 1];
			double zi = state[i6 + 2];

			for (int j = 0; j < bodiesNum; j++) {

				if (i == j) continue;

				int j6 = 6 * j;
				double dx = state[j6] - xi;
				double dy = state[j6 + 1] - yi;
				double dz = state[j6 + 2] - zi;

				double r2 = dx * dx + dy * dy + dz * dz; // מרחק בריבוע בין שני גופים
				double invR = 1.0 / Math.sqrt(r2); // נוצר בשביל המשתנה הבא
				double invR3 = invR * invR * invR; //    נוצר בשביל סדר ויעילות| פירוט לגבי הסיבה ללמה המשתנה בשלישית בעוד מספר שורות

				double acc = Body.G * m[j] * invR3; // נוסחה לחישוב תאוצה

				a[3 * i] += acc * dx; // dx אמור להיות חלקי r כדי שיהיה רק עם כיוון ללא גודל אך זה נעשה בinvR3
				a[3 * i + 1] += acc * dy; // dy אמור להיות חלקי r כדי שיהיה רק עם כיוון ללא גודל אך זה נעשה בinvR3
				a[3 * i + 2] += acc * dz; // dz אמור להיות חלקי r כדי שיהיה רק עם כיוון ללא גודל אך זה נעשה בinvR3

			}
		}
	}


	@Override
	public int getDimension() {
		return (6 * bodiesNum); /* קובע שהמימד של המערכת יהיה תלת מימדי
		בגלל שלכל גוף בתלת מימד יש שישה ערכים התחלתיים (XYZ,vXvYvZ)

		הערך שהפעולה מחזירה (6 * n) מבוטא כמערך שנרא כך:
		[x1, y1, z1, vx1, vy1, vz1, x2, y2, z2, vx2, vy2, vz2, ..., xN, yN, zN, vxN, vyN, vzN]
		כלומר הערכים במערך מסודרים על פי מיקום ואז מהירות לכל גוף

		*/
	}

	@Override
	public double[] computeDerivatives(double t, double[] state) {
		double[] stateDerivative = new double[6 * bodiesNum];

		// :stateDerivative לולאה ליישום ערכי המהירות בתוך
		for (int i = 0; i < bodiesNum; i++) {
			int k = i * 6;
			stateDerivative[k] = state[k + 3];
			stateDerivative[k + 1] = state[k + 4];
			stateDerivative[k + 2] = state[k + 5];

		}

		// :stateDerivative לולאה להמרת המהירות לתאוצה והשמת התוצאה בתוך
		for (int i = 0; i < bodiesNum; i++) {
			int k = i * 6;
			stateDerivative[k + 3] = state[k * 3];
			stateDerivative[k + 4] = state[k * 4];
			stateDerivative[k + 5] = state[k * 5];

		}
		return stateDerivative;
	}


}