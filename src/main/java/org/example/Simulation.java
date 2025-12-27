package org.example;

import org.hipparchus.ode.ODEState;
import org.hipparchus.ode.ODEStateAndDerivative;
import org.hipparchus.ode.OrdinaryDifferentialEquation;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;

public class Simulation {
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
		double absTol = 1e-10; // עשר בחזקת מינוס עשר
		double relTol = 1e-10; // עשר בחזקת מינוס עשר

		double[] y0 = NBodiesCalculations.StateSort(bodies);

		DormandPrince853Integrator integrator = new DormandPrince853Integrator(minStep, maxStep, absTol, relTol);

		ODEState initial = new ODEState(tStart, y0);
		ODEStateAndDerivative finalState = integrator.integrate(ODE, initial, tEnd);


		return finalState.getPrimaryState();
	}

	public static void Normalizer(Body[] bodies) {
		double averagePlace = 0;
		double sumplace = 0;

		double averageV = 0;
		double sumV = 0;


		for (int i = 0; i < bodies.length; i++) {
			sumplace = sumplace + bodies[i].getX() + bodies[i].getY() + bodies[i].getZ(); // מחבר את ערכי המיקום של כל הגופים
			sumV = sumV + bodies[i].getVx() + bodies[i].getVy() + bodies[i].getVz(); // vX
		}
		averagePlace = sumplace / (bodies.length * 3);
		averageV = sumV / (bodies.length * 3);


		for (int i = 0; i < bodies.length; i++) {
			bodies[i].setR(bodies[i].getX() / averagePlace, bodies[i].getY() / averagePlace, bodies[i].getZ() / averagePlace);
			bodies[i].setV(bodies[i].getVx() / averageV, bodies[i].getVy() / averageV, bodies[i].getVz() / averageV);

		}

	}

	public static void energyCalc() {

	}
}
