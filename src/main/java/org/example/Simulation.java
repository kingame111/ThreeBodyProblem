package org.example;

import org.hipparchus.ode.ODEState;
import org.hipparchus.ode.ODEStateAndDerivative;
import org.hipparchus.ode.OrdinaryDifferentialEquation;
import org.hipparchus.ode.nonstiff.DormandPrince853Integrator;

public class Simulation {

	public static double[] runSimulation(double tStart, double tEnd, Body[] bodies, TimeManager timeManager) {

		OrdinaryDifferentialEquation ode = new NBodiesCalculations(bodies); // TODO להוסיף תגובה

		double minStep = 1e-12;   // 1e-6 (10⁻⁶)
		double maxStep = 1.0;
		double absTol = 1e-6;  // 1e-10 (10⁻¹⁰)
		double relTol = 1e-6;  // 1e-10 (10⁻¹⁰)

		double[] y0 = NBodiesCalculations.StateSort(bodies); // מסדר את מערך state למקרה שלא בא מסודר

		double[] pos0 = extractPositions(y0, bodies.length);
		double[] vel0 = extractVelocities(y0, bodies.length);
		timeManager.record(tStart, pos0, vel0);

		DormandPrince853Integrator integrator =
				new DormandPrince853Integrator(minStep, maxStep, absTol, relTol);

		integrator.addStepHandler(interpolator -> {

			if (stopCalculation) {
				throw new CalculationStoppedException();
			}

			double[] y = interpolator.getCurrentState().getPrimaryState();
			double[] yCopy = y.clone();

			double[] pos = extractPositions(yCopy, bodies.length);
			double[] vel = extractVelocities(yCopy, bodies.length);

			timeManager.record(interpolator.getCurrentState().getTime(), pos, vel);
		});

		ODEState initial = new ODEState(tStart, y0);
		ODEStateAndDerivative finalState;

		try {
			finalState = integrator.integrate(ode, initial, tEnd);
		} catch (CalculationStoppedException e) {
			return y0;
		}

		FrameSnapshot latest = timeManager.getLatest();

		if (latest == null || Math.abs(latest.getTime() - finalState.getTime()) > 1e-9) {
			double[] yEnd = finalState.getPrimaryState().clone();

			double[] posEnd = extractPositions(yEnd, bodies.length);
			double[] velEnd = extractVelocities(yEnd, bodies.length);

			timeManager.record(finalState.getTime(), posEnd, velEnd);
		}

		return finalState.getPrimaryState();
	}

	private static volatile boolean stopCalculation = false;

	public static void stopCalculation() {
		stopCalculation = true;
	}

	public static void resetStopCalculation() {
		stopCalculation = false;
	}

	private static class CalculationStoppedException extends RuntimeException {
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

	public static void energyCalc() { // י TODO להוסיף פעולה של שמחשבת את האנרגיה
	}

	private static double[] extractPositions(double[] state, int bodyCount) {
		double[] pos = new double[bodyCount * 3];
		for (int i = 0; i < bodyCount; i++) {
			int s = 6 * i;
			int p = 3 * i;
			pos[p] = state[s];
			pos[p + 1] = state[s + 1];
			pos[p + 2] = state[s + 2];
		}
		return pos;
	}

	private static double[] extractVelocities(double[] state, int bodyCount) {
		double[] vel = new double[bodyCount * 3];
		for (int i = 0; i < bodyCount; i++) {
			int s = 6 * i;
			int v = 3 * i;
			vel[v] = state[s + 3];
			vel[v + 1] = state[s + 4];
			vel[v + 2] = state[s + 5];
		}
		return vel;
	}
}
