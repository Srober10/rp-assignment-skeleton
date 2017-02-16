package rp.assignments.individual.ex2;

import rp.robotics.testing.RobotTest;
import rp.robotics.testing.TestViewer;

public class ViewTest {

	/**
	 * Example of how to visualise a test running
	 * 
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unused")
	private static void visualiseTest() throws ClassNotFoundException {
		Ex2Tests tests = new Ex2Tests();
		RobotTest<?> test = tests.createSlowTest();
		//RobotTest<?> test = tests.createFastTest();
		//RobotTest<?> test = tests.createMediumTest();
		TestViewer demo = new TestViewer(test, test.getSimulation());
		demo.run();
	}

	public static void main(String[] args) throws ClassNotFoundException {
		// Visualise an IA2 test. For further examples, reuse code from
		// ex1.ViewTest
		visualiseTest();

	}

}