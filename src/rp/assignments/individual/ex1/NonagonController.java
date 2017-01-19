package rp.assignments.individual.ex1;

import lejos.nxt.Motor;
import lejos.robotics.navigation.DifferentialPilot;
import rp.robotics.DifferentialDriveRobot;
import rp.robotics.MobileRobot;
import rp.systems.StoppableRunnable;

/**

 * Using interior angle be the angle needed 
 * to turn for each shape. nonagons have 9 sides, so 360/9 = 40
 * 
 * @author Suki
 *
 */
public class NonagonController implements StoppableRunnable {
	
	private boolean running = false;
	private final DifferentialPilot m_pilot;
	private final int interiorAngle = 360/9; //9 is number of sides. 
	
	private float sideLength;

	public NonagonController(DifferentialDriveRobot robot, float sideLength) {
		
		m_pilot = robot.getDifferentialPilot(); // gets the movement pilot
		this.sideLength = sideLength;
	}

	@Override
	public void run() {
		running = true;
		
		while(running){ // keeps looping until stop is called, will make a full nonagon each time.
	for( int i= 0; i <9 ; i++){ //9 times for each side
			m_pilot.travel(sideLength);
			m_pilot.rotate(interiorAngle);			
			
			}
		}
	}

	@Override
	public void stop() {
		running = false;
	}

}
