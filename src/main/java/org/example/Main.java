package org.example;

import org.hipparchus.ode.ODEState;
import org.hipparchus.ode.ODEStateAndDerivative;
import org.hipparchus.ode.OrdinaryDifferentialEquation;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;

import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

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
		OrdinaryDifferentialEquation ODE = new NBodiesCalculations(bodies);
		/*
		OrdinaryDifferentialEquation כדי להגדיר אובייקט מסוג
		צריך לתת לו את מספר המימדים ופעולה המחשבת את ערכי הנגזרת
		וזה בדיוק מה שיש בNBodiesCalculations(bodies) והסיבה למה יצרתי אותה
		*/

		int dim = ODE.getDimension();

		double minStep = 1e-6; // עשר בחזקת מינוס שש
		double maxStep = 1;
		double[] absTol = new double[dim];
		double[] relTol = new double[dim];

		double[] y0 = NBodiesCalculations.StateSort(bodies);

		DormandPrince853Integrator integrator = new DormandPrince853Integrator(minStep, maxStep, absTol, relTol);

		ODEState initial = new ODEState(tStart, y0);
		ODEStateAndDerivative finalState = integrator.integrate(ODE, initial, tEnd);


		return finalState.getPrimaryState();
	}
	public static void Normalizer(Body[] bodies){
	double averagePlace = 0;
	double sumplace = 0;

	double averageV = 0;
	double sumV = 0;

	double averageM = 0;
	double sumM = 0;

	for (int i = 0; i<bodies.length; i++){
		sumplace = sumplace + bodies[i].getX() + bodies[i].getY() + bodies[i].getZ(); // מחבר את ערכי המיקום של כל הגופים
		sumV = sumV + bodies[i].getVx() + bodies[i].getVy() + bodies[i].getVz(); // vX
		sumM = sumM + bodies[i].getM();
	}
	averagePlace = sumplace / (bodies.length * 3);
	averageV = sumV / (bodies.length * 3);
	averageM = sumM / bodies.length;


	for (int i =0; i<bodies.length; i++){
		bodies[i].setR(bodies[i].getX() / averagePlace, bodies[i].getY() / averagePlace, bodies[i].getZ() / averagePlace);
		bodies[i].setV(bodies[i].getVx() / averageV, bodies[i].getVy() / averageV, bodies[i].getVz() / averageV);
		bodies[i].setM(bodies[i].getM() / averageM);

	}

	}

	public static void energyCalc(){

	}
}
