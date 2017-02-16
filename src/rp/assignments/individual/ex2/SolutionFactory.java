package rp.assignments.individual.ex2;



import lejos.robotics.RangeFinder;
import rp.config.RangeFinderDescription;
import rp.robotics.DifferentialDriveRobot;
import rp.systems.StoppableRunnable;

/**
 * In this class you must edit the "create*" methods to provide instances of
 * your controller objects in order to pass the automated tests.
 *
 */

public class SolutionFactory {

	/***
	 * Create a controller that stays up to _maxDistance away from an obstacle.
	 * @param _robot The robot object. access to change motor speed
	 * @param _desc The description of the range finder (min/max range etc.)
	 * @param _ranger The range finder itself, ie range to the robot
	 * @param _maxDistance The max distance the robot should be from the obstacle detected by the range finder.
	 * @return The controller object.
	 */
	public static StoppableRunnable createRangeController(
			DifferentialDriveRobot _robot, RangeFinderDescription _desc,
			RangeFinder _ranger, Float _maxDistance) {
		return new RangeController( _robot,  _desc,	 _ranger,  _maxDistance);
	}

}