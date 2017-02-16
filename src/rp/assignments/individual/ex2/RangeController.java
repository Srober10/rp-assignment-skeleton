package rp.assignments.individual.ex2;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.config.RangeFinderDescription;
import rp.robotics.DifferentialDriveRobot;
import rp.robotics.simulation.SimulationSteppable;
import rp.systems.StoppableRunnable;
import rp.util.Rate;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

public class RangeController implements StoppableRunnable {

	private RangeFinder ranger;
	private DifferentialDriveRobot robot;
	private DifferentialPilot pilot;

	private float maxDistance;
	private float idealDistance;
	private float maxRange;
	private float minRange;
	private float rangeNoise;
	private boolean inRange = false;
	private float travelSpeed = 0.10f;
	private float estimatedSpeed;
	private double maxTravelSpeed;
	private boolean running = false;

	private float timeWait = 100;
	private float timeWaitS = timeWait / 1000;

	public RangeController(DifferentialDriveRobot _robot, RangeFinderDescription _desc, RangeFinder _ranger,
			Float _maxDistance) {

		maxDistance = _maxDistance;
		maxRange = _desc.getMaxRange();
		minRange = _desc.getMinRange();
		rangeNoise = _desc.getNoise();
		ranger = _ranger;
		robot = _robot;
		pilot = robot.getDifferentialPilot();
		maxTravelSpeed = robot.getDifferentialPilot().getMaxTravelSpeed();
		idealDistance = Math.max(maxDistance / 2 , maxDistance - rangeNoise);
	}

	@Override
	public void run() {

		// estimate speed before starting to move

		float medianSpeed; // of the barrier
		double adjustedSpeed;
		Rate r = new Rate(100);
		running = true;

		medianSpeed = estimateSpeed();
		
		pilot.setTravelSpeed(Math.abs(Math.max(proportionalSpeed(), maxTravelSpeed)));
		System.out.println("speed is :" + pilot.getTravelSpeed() + "idealDistance is" + idealDistance);
		pilot.forward();
		// while robot is < maxDistance, speed it up.
		while (running) {

			while (ranger.getRange() >= maxDistance) {
				pilot.setTravelSpeed(maxTravelSpeed);
				Delay.msDelay(100);
			}
			pilot.setTravelSpeed(medianSpeed); // not very useful if speed changes, otherwise helpful
			//System.out.println(medianSpeed);								

			while (ranger.getRange() < idealDistance) { // robot is faster, set
														// speed to proportional
				//System.out.println("proportional speed set");
				pilot.setTravelSpeed(proportionalSpeed());
				Delay.msDelay(100);

			}
			Delay.msDelay(100);
		}
		Delay.msDelay(100);
	}

	// if the speed is too high, set it too approx the object's speed

	public double proportionalSpeed() {

		double objectDistance1 = ranger.getRange();
		double k = 0.4; // something to turn a range of the maxrange - minrange within 0 - 0.4
		double speed = (objectDistance1 - minRange) / (maxRange - minRange) * k;
		return Math.abs(Math.min(speed, maxTravelSpeed));

	}

//	public boolean robotIsFaster() {
//
//		float initialPos = robot.getPose().getX();
//		float objectDistance1 = ranger.getRange();
//		Delay.msDelay((long) timeWait);
//		float finalPos = robot.getPose().getX();
//		float objectDistance2 = ranger.getRange();
//		float posChange = finalPos - initialPos;
//		float distanceChange = objectDistance2 - objectDistance1;
//		// System.out.println(posChange + " vs " + distanceChange);
//		// if robot has moved more of a distance than the object, its
//		// speed/acceleration is higher
//		// however if distance change is very small then speed of robot is very
//		// similar to the object's and so its good
//		if (Math.abs(distanceChange) < 0.05) {
//			System.out.println("robot doesn't need to speed up or slow down");
//			inRange = false;
//			return false;
//		} else if (distanceChange > 0.05) {
//
//			// System.out.println("speeding up robot");
//			return false;
//		} else if (distanceChange > 0.05) {
//			return true;
//		}
//	}

	public double calculateSpeed() { // speed of object/barrier, given robot is
										// stationary.

		float initialDistance = ranger.getRange();
		// System.out.println("range 1 :" + initialDistance);
		Delay.msDelay((long) timeWait);
		float finalDistance = ranger.getRange();
		// System.out.println("range 2 :" + finalDistance);
		float distanceMoved = (finalDistance - initialDistance);
		// System.out.println("distance " + distanceMoved + "divided by " +
		// "time" + timeWaitS);
		// using the time in seconds to get m/s,
		float speed = distanceMoved / timeWaitS;
		// System.out.println( "speed is " + speed);

		return speed;

	}

	// public double calculateSpeedMoving(double robotSpeed) {
	// // speed is distance divided by time
	// // converts to seconds
	// float initialDistance = ranger.getRange();
	// // System.out.println("range 1 :" + initialDistance);
	// Delay.msDelay((long) timeWait);
	// float finalDistance = ranger.getRange();
	// // System.out.println("range 2 :" + finalDistance);
	// float distanceMoved = (finalDistance - initialDistance);
	//
	// // first term can be negative if robot is faster than object
	// double speed = (distanceMoved / timeWaitS) + robotSpeed;
	// // System.out.println("distance " + distanceMoved + "divided by " +
	// // "time" + timeWaitS + "= " + distanceMoved/robotSpeed);
	// return speed;
	// }

	public float estimateSpeed() { // calculates an average of the speeds taken
									// and returns the median

		float medianSpeed;
		int sizeOfArray = 10;
		float[] speedEstimates = new float[sizeOfArray];

		for (int i = 0; i < sizeOfArray; i++) {
			speedEstimates[i] = (float) calculateSpeed();
		}
		Arrays.sort(speedEstimates);
		medianSpeed = speedEstimates[sizeOfArray / 2];
		// System.out.println("Estimated speed : " + medianSpeed);

		return medianSpeed;
	}

	// public double estimateSpeedDouble() { // calculates an average of the
	// speeds
	// // taken and returns the median
	// // movement of the robot + the distance dt of the object. this is
	// // returning increasing speeds ??
	// double medianSpeed;
	// int sizeOfArray = 10;
	// double[] speedEstimates = new double[sizeOfArray];
	//
	// for (int i = 0; i < sizeOfArray; i++) {
	// speedEstimates[i] = calculateSpeedMoving(pilot.getTravelSpeed());
	// }
	// Arrays.sort(speedEstimates);
	// medianSpeed = speedEstimates[sizeOfArray / 2];
	// // System.out.println("Estimated speed : " + medianSpeed);
	// System.out.println(medianSpeed + "edited speed");
	// return medianSpeed;
	// }

	@Override
	public void stop() {
		running = false;

	}

}
