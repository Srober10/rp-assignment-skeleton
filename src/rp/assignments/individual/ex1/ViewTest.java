package rp.assignments.individual.ex1;

import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.Pose;
import rp.config.WheeledRobotConfiguration;
import rp.robotics.DifferentialDriveRobot;
import rp.robotics.MobileRobotWrapper;
import rp.robotics.TouchSensorListener;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.simulation.SimulatedRobots;
import rp.robotics.testing.RobotTest;
import rp.robotics.testing.TestMaps;
import rp.robotics.testing.TestViewer;
import rp.robotics.testing.ZoneSequence;
import rp.robotics.testing.ZoneSequenceTest;
import rp.systems.StoppableRunnable;

/**
 * This class gives you an example of how to visualise a controller running in a
 * test.
 * 
 * @author Nick Hawes
 *
 */
public class ViewTest {

	/**
	 * Example of how to visualise a test running
	 * 
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unused")
	private static void visualiseTest() throws ClassNotFoundException {
		// Instantiate the tests
		Ex1Tests tests = new Ex1Tests();
		// Create the test object plus the controller for the test (via
		// SolutionFactory)
		//RobotTest<?> test = tests.createPentagonTest();
		//RobotTest<?> test = tests.createOctagonTest();
		RobotTest<?> test = tests.createBumperTest(); //part 2 
		// Create the visualisation of the test, then run everything
		TestViewer demo = new TestViewer(test, test.getSimulation());
		demo.run();
	}

	/**
	 * Example of how to run a test without visualisation
	 * 
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unused")
	private static void runTest() throws ClassNotFoundException {

		// Instantiate the tests
		Ex1Tests tests = new Ex1Tests();
		// Create the test object plus the controller for the test (via
		// SolutionFactory)
		//RobotTest<?> test = tests.createPentagonTest();
		//RobotTest<?> test = tests.createOctagonTest();
		RobotTest<?> test = tests.createBumperTest(); //part 2 
		test.run();
	}

	/**
	 * Example of how to construct and run a test such that more things can //
	 * be manually edited during development
	 */
	@SuppressWarnings("unused")
	private static void manallyCreateTest() {
		// Create the simulation using the given map. This simulation object can
		// run with or without a GUI. In this case we use an empty map which is
		// 8m by 6m.
		MapBasedSimulation sim = new MapBasedSimulation(TestMaps.EMPTY_8_x_6);

		// Create a configuration object to describe to the kind of robot you
		// want to add to the simulation.
		// The dimensions of the simulated robot are defined in metres, thus all
		// other parts of your code should use metres too.
		// Note that it is also possible to create a configuration to describe a
		// real robot and use that with the same control code on real hardware.
		boolean withTouchSensor = true;
		boolean withRangeSensor = true;
		WheeledRobotConfiguration robotConfig = SimulatedRobots
				.makeWheeledConfiguration(sim.getSimulationCore(),
						withTouchSensor, withRangeSensor);

		// Add a robot with this configuration to the simulation at the given
		// starting pose. The return value from addRobot is the object you use
		// to actually control the
		// simulated robot.
		Pose startingPose = new Pose(3f, 3f, 0);
		MobileRobotWrapper<DifferentialDriveRobot> wrapper = sim.addRobot(
				robotConfig, startingPose);

		// This object implements a simple random move controller for a wheeled
		// robot. We pass it the robot object from the simulator.
		// It is important to note that this controller could also be used with
		// a real robot provided you have a configuration object to describe it.
		BumperController controller = new BumperController(  //change this to xController
				wrapper.getRobot());

		// Manually create the touch sensor if we need one.
		if (withTouchSensor) {
			sim.addTouchSensorListener(wrapper,
					(TouchSensorListener) controller);
		}

		// Get the ranger if you need one
		if (withRangeSensor) {
			RangeFinder ranger = sim.getRanger(wrapper);
			// Pass the range finder into the controller (assuming you have the
			// right method available)
			// controller.setRangerFinder(ranger);
		}

		// Get the sequence of zones to visit. You could replace this with your
		// own list of zones.
		//ZoneSequence sequence = Ex1Tests.getOctagonTestSequence();
		//dont need this for p2?? 
		ZoneSequence sequence = Ex1Tests.getBumperSequence(); //works?

		// Create a test from these zones, the controller, and the robot to run
		// it on
		ZoneSequenceTest<?> test = new ZoneSequenceTest<StoppableRunnable>(sim,
				sequence, controller, wrapper.getRobot(), 30000, false);

		TestViewer viewer = new TestViewer(test, sim);
		viewer.run();

	}

	public static void main(String[] args) throws ClassNotFoundException {

		// Example of how to visualise a test running
		visualiseTest();

		// Example of how to run a test without visualisation
		// runTest();

		// Example of how to construct and run a test such that more things can
		// be manually edited during development
		// manallyCreateTest();
	}

}
