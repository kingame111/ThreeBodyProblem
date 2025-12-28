package org.example;

public class ScenarioFactory {

	private ScenarioFactory() {}

	public Body[] twoBodiesDemo(){
		Body[] a = {
				new Body(new double[]{-200, 0, 0}, new double[]{0, 0.8, 0}, 5000, "A"),
				new Body(new double[]{ 200, 0, 0}, new double[]{0,-0.8, 0}, 5000, "B")
		};
		return a.clone();
	}

}
