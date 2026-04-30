package org.example;

public class ScenarioFactory {

	private ScenarioFactory() {
	}

	public static Body[] eightConfiguration() {
		Body[] ThreeBodies = {
				new Body("1", 9.265003382E11, new double[]{54.592300,-83.783535, 0}, new double[]{0.5, 0, 0}),
				new Body("2", 9.265003382E11, new double[]{-54.592300, 83.783535, 0}, new double[]{0.5,0, 0}),
				new Body("3", 9.265003382E11, new double[]{0, 0, 0}, new double[]{-1,0, 0})
		};
		return ThreeBodies.clone();
	}

	public static Body[] sunPlanetMoonSystem() {
		Body[] ThreeBodies = {
				new Body("Sun", 1.498286541E12, new double[]{0,0, 0}, new double[]{0, 0, 0}),
				new Body("Star", 3.745716351E9, new double[]{100, 0, 0}, new double[]{0,1, 0}),
				new Body("Moon", 1E6, new double[]{104, 0, 0}, new double[]{0,1.25, 0})
		};
		return ThreeBodies.clone();
	}

	public static Body[] TriangleSystem() {
		Body[] ThreeBodies = {
				new Body("1", 2.595104816E12, new double[]{100,0, 0}, new double[]{0, 1, 0}),
				new Body("2", 2.595104816E12, new double[]{-50, 86.602540, 0}, new double[]{-0.866025,-0.5, 0}),
				new Body("3", 2.595104816E12, new double[]{-50, -86.602540, 0}, new double[]{0.866025, -0.5, 0})
		};
		return ThreeBodies.clone();
	}




}
// TODO add more scenarios
