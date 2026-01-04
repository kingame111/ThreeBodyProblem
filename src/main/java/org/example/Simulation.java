package org.example;

import javafx.application.Platform;
import org.hipparchus.ode.ODEState;
import org.hipparchus.ode.ODEStateAndDerivative;
import org.hipparchus.ode.OrdinaryDifferentialEquation;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;

public class Simulation {

	public static double[] simulator(double tStart, double tEnd, Body[] bodies, BodyViewManager viewManager) {

		OrdinaryDifferentialEquation ode = new NBodiesCalculations(bodies);

		double minStep = 1e-6;   // 1e-6 (10⁻⁶)
		double maxStep = 1.0;
		double absTol = 1e-10;  // 1e-10 (10⁻¹⁰)
		double relTol = 1e-10;  // 1e-10 (10⁻¹⁰)

		double[] y0 = NBodiesCalculations.StateSort(bodies);

		DormandPrince853Integrator integrator =
				new DormandPrince853Integrator(minStep, maxStep, absTol, relTol);

		final int[] steps = {0};
		final int uiEvery = 1;

		integrator.addStepHandler(interpolator -> {
			steps[0]++;

			if (steps[0] % uiEvery != 0){
				return;
			}

			// לוקחים state נוכחי
			double[] y = interpolator.getCurrentState().getPrimaryState();

			// מעתיקים כדי שלא יהיה race בין Threads
			double[] yCopy = y.clone();

			Platform.runLater(() -> {
				NBodiesCalculations.applyStateToBodies(yCopy, bodies);
				viewManager.render();
			});
		});

		ODEState initial = new ODEState(tStart, y0);
		ODEStateAndDerivative finalState = integrator.integrate(ode, initial, tEnd);

		double[] yEnd = finalState.getPrimaryState();
		double[] yEndCopy = yEnd.clone();

		Platform.runLater(() -> {
			NBodiesCalculations.applyStateToBodies(yEndCopy, bodies);
			viewManager.render();
		});

		return yEnd;
	}

	public static void Normalizer(Body[] bodies) {
		double sumplace = 0;
		double sumV = 0;

		for (int i = 0; i < bodies.length; i++) {
			sumplace += bodies[i].getX() + bodies[i].getY() + bodies[i].getZ();
			sumV += bodies[i].getVx() + bodies[i].getVy() + bodies[i].getVz();
		}

		double averagePlace = sumplace / (bodies.length * 3);
		double averageV = sumV / (bodies.length * 3);

		for (int i = 0; i < bodies.length; i++) {
			bodies[i].setR(
					bodies[i].getX() / averagePlace,
					bodies[i].getY() / averagePlace,
					bodies[i].getZ() / averagePlace
			);
			bodies[i].setV(
					bodies[i].getVx() / averageV,
					bodies[i].getVy() / averageV,
					bodies[i].getVz() / averageV
			);
		}
	}

	public static void energyCalc() {
	}
}
