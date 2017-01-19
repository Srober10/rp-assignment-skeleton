package rp.assignments.individual.ex1;

import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;
import rp.config.RangeFinderDescription;
import rp.robotics.DifferentialDriveRobot;
import rp.robotics.TouchSensorEvent;
import rp.robotics.TouchSensorEventSource;
import rp.robotics.TouchSensorListener;
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
public class VirtualBumper implements TouchSensorEventSource {

	private  RangeFinderDescription _desc;
	private boolean m_running = false;
	private RangeFinder m_ranger;
	private boolean m_bumped = false;
	

	public VirtualBumper (RangeFinderDescription _desc, RangeFinder _ranger, Float _touchRange) {
		
		
	}

	@Override
	public void addTouchSensorListener(TouchSensorListener _listener) {
		// TODO Auto-generated method stub
		
	}

}
