package org.example;

public class ScenarioFactory {

	private ScenarioFactory() {
	}

	public static Body[] twoBodyDemo() {
		Body[] a = {
				new Body("A", 5000, new double[]{-200, 0, 0}, new double[]{0, 0.8, 0}),
				new Body("B", 5000, new double[]{200, 0, 0}, new double[]{0, -0.8, 0})
		};
		return a.clone();
	}

}
// TODO add more scenarios
