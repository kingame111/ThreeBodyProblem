package org.example;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

import org.hipparchus.ode.ODEState;
import org.hipparchus.ode.ODEStateAndDerivative;
import org.hipparchus.ode.OrdinaryDifferentialEquation;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;

public class Main {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		scanner.useLocale(Locale.US); // עושה שקליטת מספרים עשרוניים תהיה עם נקודה ולא פסיק

		System.out.println("enter the number of bodies");
		Body[] bodies = new Body[scanner.nextInt()];

		double[] place;
		double[] v;
		double m;
		String name;

		//  לולאה המיישמת את הערכים של כל משתנה(מיקום, מהירות, מסה, שם הגוף)
		for (int i = 0; i < bodies.length; i++) {

			System.out.println("enter the position values (X,Y,Z) of body #" + (i + 1));
			place = new double[]{scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()};

			System.out.println("enter the velocity values (vX,vY,vZ) of body #" + (i + 1));
			v = new double[]{scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()};

			System.out.println("enter the mass value of body #" + (i + 1));
			m = scanner.nextDouble();

			System.out.println("enter the name of body #" + (i + 1));
			name = scanner.next();

			bodies[i] = new Body(place, v, m, name);

		}
		// הרצה לדוגמה: זמן התחלה 0, זמן סיום 10 שניות
		double[] stateFinal = simulator(0.0, 10.0, bodies);
		System.out.println("Final state at t = 10.0: ");
		System.out.println(Arrays.toString(stateFinal));

		scanner.close();
	}


	public static double[] simulator(double tStart, double tEnd, Body[] bodies) {

		double minStep = 1e-6; // עשר בחזקת מינוס שש
		double maxStep = 1;
		double absTol = 1e-10; // עשר בחזקת מינוס עשר
		double relTol = 1e-10; // עשר בחזקת מינוס עשר

		double[] masses = new double[bodies.length];
		// מיישם את ערכי המסה של כל הגופים במערך masses
		for (int i = 0; i < bodies.length; i++) {
			masses[i] = bodies[i].getM();
		}

		OrdinaryDifferentialEquation ODE = new NBodiesCalculations(masses);
		/*
		OrdinaryDifferentialEquation כדי להגדיר אובייקט מסוג
		צריך לתת לו את מספר המימדים ופעולה המחשבת את ערכי הנגזרת
		וזה בדיוק מה שיש בNBodiesCalculations(masses) והסיבה למה יצרתי אותה
		*/

		double[] y0 = new double[ODE.getDimension()];
		for (int i = 0; i < masses.length; i++) {
			int i6 = 6 * i;
			y0[i6] = bodies[i].getX();
			y0[i6 + 1] = bodies[i].getY();
			y0[i6 + 2] = bodies[i].getZ();
			y0[i6 + 3] = bodies[i].getVx();
			y0[i6 + 4] = bodies[i].getVy();
			y0[i6 + 5] = bodies[i].getVz();

		}

		DormandPrince853Integrator integrator =
				new DormandPrince853Integrator(minStep, maxStep, absTol, relTol);

		ODEState initial = new ODEState(tStart, y0);
		ODEStateAndDerivative finalState = integrator.integrate(ODE, initial, tEnd);


		return finalState.getPrimaryState();
	}
}
