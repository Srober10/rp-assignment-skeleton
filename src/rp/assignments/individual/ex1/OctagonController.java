package rp.assignments.individual.ex1;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import rp.robotics.DifferentialDriveRobot;
import rp.robotics.MobileRobot;
import rp.systems.StoppableRunnable;

/**

 * Using interior angle be the angle needed 
 * to turn for each shape. Octagons have 8 sides, so 360/8 = 45
 * 
 * @author Suki
 *
 */
public class OctagonController implements StoppableRunnable {
	
	private boolean running = false;
	private final DifferentialPilot m_pilot;
	private final int interiorAngle = 360/8; //5 is number of sides. 72
	
	private float sideLength;

	public OctagonController(DifferentialDriveRobot robot, float sideLength) {
		
		m_pilot = robot.getDifferentialPilot(); // gets the movement pilot
		this.sideLength = sideLength;
	}

	@Override
	public void run() {
		running = true;
		
		while(running){ // keeps looping until stop is called, will make a full pentagon each time.
	
			m_pilot.travel(sideLength);
			m_pilot.rotate(interiorAngle);
		
		}
	}

	@Override
	public void stop() {
		running = false;
	}

}
