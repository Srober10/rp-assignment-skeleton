package rp.assignments.individual.ex1;

import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;
import rp.robotics.DifferentialDriveRobot;
import rp.robotics.TouchSensorEvent;
import rp.systems.ControllerWithTouchSensor;
import rp.util.Rate;

/**
 * A controller that moves the robot until it hits a wall, reverse and then it turns 180 and 
 * goes in the other direction.
 * 
 * Your controller should run indefinitely until its stop()  method is called, at which point the
 *  controller's run() method should exit within 100 milliseconds. Additionally any method call made to your controller via the
 *   TouchSensorListener interface should complete within 100 milliseconds. 
 *   These constraints are important because your controller should be reactive to external commands, and an event handler (listener) 
 *   should not unnecessarily block the event notification thread.
 * 
 * @author Suki 
 *
 */
public class BumperController implements ControllerWithTouchSensor {

	private final DifferentialDriveRobot m_robot;
	private boolean m_running = false;
	private final DifferentialPilot m_pilot;
	private boolean m_bumped = false;
	private RangeFinder m_ranger;

	public BumperController(DifferentialDriveRobot _robot) {
		m_robot = _robot;
		m_pilot = m_robot.getDifferentialPilot();
	}

	@Override
	public void run() {
		m_running = true;		
		m_pilot.setTravelSpeed(0.10f);
		Rate r = new Rate(40);
		
		while (m_running) {
			
			m_pilot.forward(); //moves forward infinitely and turns on iternal ismoving bool
			
			while (m_pilot.isMoving() && !m_bumped) {
				if (m_ranger != null) {
					if (m_ranger.getRange() < m_robot.getRobotLength()) {
						System.out.println("Watch out for that wall!");
					}
				}
				r.sleep(); // sleeps for rate of 40
			}

			if (m_bumped) {
				m_pilot.stop();
				m_pilot.travel(-0.3); // moves back only a short way before turning. (map must be bigger than this reverse distance)
				m_pilot.rotate(180);
				m_pilot.forward();

				m_bumped = false;
			}
			
		}
	}

	@Override
	public void stop() {
		m_running = false;
	}

	@Override
	public void sensorPressed(TouchSensorEvent _e) {
		m_bumped = true;
	}

	public void setRangeScanner(RangeFinder _ranger) {
		m_ranger = _ranger;
	}

	@Override
	public void sensorReleased(TouchSensorEvent _e) {
		// not triggered in sim 
		
	}

	@Override
	public void sensorBumped(TouchSensorEvent _e) {
		// not triggered in sim
		
	}

}
