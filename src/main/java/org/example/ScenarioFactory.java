package org.example;

public class ScenarioFactory {

	private ScenarioFactory() {
	}

	public static Body[] twoBodyCircle() {
		Body[] a = {
				new Body("1", 1.761615564981984E13, new double[]{80, -80, 0}, new double[]{0, 0.8, 0}),
				new Body("2", 1.761615564981984E13, new double[]{200, 0, 0}, new double[]{0, -0.8, 0})
		};
		return a.clone();
	}


}
// TODO add more scenarios
