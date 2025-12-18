package org.example;

import org.hipparchus.ode.OrdinaryDifferentialEquation;

import java.util.Arrays;

public class NBodiesCalculations implements OrdinaryDifferentialEquation {
	private final int bodiesNum; // מספר הגופים בסימולציה
	private final Body[] bodies;
	private final double[] a; // התאוצות של הגופים השונים בצירים השונים
	private final double[] stateDer; // הנגזרת של state


	// בנאי
	public NBodiesCalculations(Body[] bodies) {
		this.bodiesNum = bodies.length;
		this.bodies = bodies.clone();          // הגנה מפני שינוי מערך מבחוץ
		this.a = new double[3 * bodiesNum];
		this.stateDer = new double[6 * bodiesNum];
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

		if (state.length != getDimension()) { // בודק שהאורך של state תקני
			throw new IllegalArgumentException(
					"State length mismatch: expected " + getDimension() +
							", got " + state.length
			);
		}

		Arrays.fill(a, 0.0); // פעולה הממלאת את כל ערכי המערך בערך נתון

		double softening2 = 1e-12; // (10^-12) softening

		for (int i = 0; i < bodiesNum; i++) {
			int i6 = 6 * i;
			double xi = state[i6]; // ערך מיקום הx של גוף i
			double yi = state[i6 + 1]; // ערך מיקום הy של גוף i
			double zi = state[i6 + 2]; // ערך מיקום הz של גוף i

			for (int j = 0; j < bodiesNum; j++) {

				if (i == j) continue;

				int j6 = 6 * j;
				double dx = state[j6] - xi;
				double dy = state[j6 + 1] - yi;
				double dz = state[j6 + 2] - zi;

				double r2 = dx*dx + dy*dy + dz*dz + softening2; //  מרחק בריבוע בין שני גופים + softening
				double invR = 1.0 / Math.sqrt(r2); // נוצר בשביל המשתנה הבא
				double invR3 = invR * invR * invR; //    נוצר בשביל סדר ויעילות| פירוט לגבי הסיבה ללמה המשתנה בשלישית בעוד מספר שורות

				double acc = Body.G * bodies[j].getM() * invR3; // נוסחה לחישוב תאוצה

				a[3 * i] += acc * dx; // dx אמור להיות חלקי r כדי שיהיה רק עם כיוון ללא גודל אך זה נעשה בinvR3
				a[3 * i + 1] += acc * dy; // dy אמור להיות חלקי r כדי שיהיה רק עם כיוון ללא גודל אך זה נעשה בinvR3
				a[3 * i + 2] += acc * dz; // dz אמור להיות חלקי r כדי שיהיה רק עם כיוון ללא גודל אך זה נעשה בinvR3

			}
		}
	}


	@Override
	public int getDimension() {
		return (6 * bodiesNum); /*
		 קובע שהמימד של המערכת יהיה תלת מימדי
		בגלל שלכל גוף בתלת מימד יש שישה ערכים התחלתיים (XYZ,vXvYvZ)

		הערך שהפעולה מחזירה (6 * n) מבוטא כמערך שנרא כך:
		[x1, y1, z1, vx1, vy1, vz1, x2, y2, z2, vx2, vy2, vz2, ..., xN, yN, zN, vxN, vyN, vzN]
		כלומר הערכים במערך מסודרים על פי מיקום ואז מהירות לכל גוף

		*/
	}

	@Override
	public double[] computeDerivatives(double t, double[] state) {

		if (state.length != getDimension()) { // בודק שהאורך של state תקני
			throw new IllegalArgumentException(
					"State length mismatch: expected " + getDimension() +
							", got " + state.length
			);
		}

		accelerationCalc(state); // מעדכן את מערך a לתאוצות העדכניות

		// :stateDer לולאה להשמת ערכי המהירות בתוך
		for (int i = 0; i < bodiesNum; i++) {
			int i6 = i * 6;
			int i3 = i * 3;

			stateDer[i6] = state[i6 + 3]; //מעביר את ערכי המיהירות בx שבstate לstateDerivative
			stateDer[i6 + 1] = state[i6 + 4]; // מעביר את ערכי המיהירות בy שבstate לstateDerivative
			stateDer[i6 + 2] = state[i6 + 5]; // מעביר את ערכי המיהירות בz שבstate לstateDerivative
			stateDer[i6 + 3] = a[i3];  // מיישם את ערך ax בstateDerivative
			stateDer[i6 + 4] = a[i3 + 1]; // מיישם את ערך ay בstateDerivative
			stateDer[i6 + 5] = a[i3 + 2]; // מיישם את ערך az בstateDerivative

		}
		return stateDer;
	}


}