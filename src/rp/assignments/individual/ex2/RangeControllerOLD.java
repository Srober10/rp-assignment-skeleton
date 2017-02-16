package rp.assignments.individual.ex2;

import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;
import rp.config.RangeFinderDescription;
import rp.robotics.DifferentialDriveRobot;
import rp.systems.StoppableRunnable;
import rp.util.Rate;
import java.time.Duration;

public class RangeControllerOLD implements StoppableRunnable  {
	
	private RangeFinder ranger;
	private DifferentialDriveRobot robot;	
	private DifferentialPilot pilot;
	
	private float maxDistance = 0;
	private float maxRange =0;
	private float minRange = 0;
	private float rangeNoise = 0;
	private boolean inRange = false;
	private float travelSpeed = 0.10f;
	private double maxTravelSpeed;
	private boolean running = false;
	
public RangeControllerOLD(DifferentialDriveRobot _robot, RangeFinderDescription _desc,
									RangeFinder _ranger, Float _maxDistance) 
	{
		
	maxDistance = _maxDistance;
	maxRange = _desc.getMaxRange();
	minRange = _desc.getMinRange();
	rangeNoise = _desc.getNoise();
	ranger = _ranger;
	robot = _robot;
	pilot = robot.getDifferentialPilot();
	maxTravelSpeed = robot.getDifferentialPilot().getMaxTravelSpeed();
	
	}

//while the robot is less than the max range away, keep speed the same, otherwise speed up.
	@Override
	public void run() {
		Rate r = new Rate(40);
		pilot.setTravelSpeed(travelSpeed); 
		running =true;
		boolean inRange = false;
		//readings for the object's distance
		float initialRead;
		float read1;
		float read2;
		float objectMoveDistance;
		float approxObjectMoveSpeed;
		//readings for robots position
		float intialX;
		float oldX;
		float newX;
		float robotMoveDistance;
		float totalDistance;
		
		long startTime;
		long endTime;
		
		pilot.forward();

		while(running){
			//want to get change in position of robot as close as possible to change in range per same unit of time.
			initialRead = ranger.getRange(); // range from the object initially
			intialX = robot.getPose().getX();
			totalDistance = initialRead - intialX;
			
			while( totalDistance >= (maxDistance + rangeNoise) ) // 1) closes the gap, if the object is too far.
			{					
				read1 = ranger.getRange();
				oldX = robot.getPose().getX();
				startTime = System.nanoTime();
				r.sleep();
				endTime = System.nanoTime();
				read2 = ranger.getRange();
				newX = robot.getPose().getX();
				objectMoveDistance = Math.abs(read1 - read2);
				approxObjectMoveSpeed = objectMoveDistance/(endTime - startTime) ;
				robotMoveDistance = Math.abs(oldX-newX);
				
				if(isCloser(objectMoveDistance, robotMoveDistance) && pilot.getTravelSpeed() < approxObjectMoveSpeed){ // if first is close then robot is too slow, and should speed up
					if(!inRange){
					speedChange(true);}
				}
				else{ //robot is in range and closing the gap. Don't want to get too close here either, if the maxRange is big.
					// don't change speed.
					inRange = true;
					read1 = ranger.getRange();
					oldX = intialX = robot.getPose().getX();
					r.sleep(); //40ms sleep
					read2 = ranger.getRange();
					newX = robot.getPose().getX();
					objectMoveDistance = Math.abs(read1 - read2);
					robotMoveDistance = Math.abs(oldX-newX);
					float deltaDistance = Math.abs(objectMoveDistance - robotMoveDistance);
					if(isCloser(objectMoveDistance, robotMoveDistance) ){ // robot is moving faster than object, slow it down 
						speedChange(false);
						r.sleep();
					}
					else{ 												//robot is slower than the object
						speedChange(true);
						r.sleep();
					}		
					
				}				
			}
		}
	
			 // this is where the robot should slow down if its going too fast.
			 while(ranger.getRange() < ( maxDistance - rangeNoise )) { //2 maintains distance from the object once in range
				read1 = ranger.getRange();
				oldX = intialX = robot.getPose().getX();
				r.sleep(); //40ms sleep
				read2 = ranger.getRange();
				newX = robot.getPose().getX();
				objectMoveDistance = Math.abs(read1 - read2);
				robotMoveDistance = Math.abs(oldX-newX);
				float deltaDistance = Math.abs(objectMoveDistance - robotMoveDistance);
				if(isCloser(objectMoveDistance, robotMoveDistance) ){ // robot is moving faster than object, slow it down 
					speedChange(false);
					r.sleep();
				}
				else{ 												//robot is slower than the object
					speedChange(true);
					r.sleep();
				}		
				//safety brake, not hitting?
				if(ranger.getRange() < robot.getRobotLength()){
					pilot.stop();
					pilot.setTravelSpeed(travelSpeed);
					pilot.forward();
				}
			}			
			
		} 
	
	
	public void speedChange(boolean faster){ // sets the speed faster if true, slower if false
		if(faster){
			travelSpeed = travelSpeed * 1.1f;
			 pilot.setTravelSpeed(Math.abs(Math.max(travelSpeed, maxTravelSpeed))); //must be >0 and <getMaxTravelSpeed			
		} else{
			travelSpeed = travelSpeed*0.9f;
			 pilot.setTravelSpeed(Math.abs(Math.max(travelSpeed, maxTravelSpeed))); 
		}	
	}
	
	
	//if the first read is closer than the second reading, the robot is further away from the object.
	public boolean isCloser(float firstRange, float secondRange){
		
		if(firstRange>secondRange){
			return true;
		}
		return false;
	}
	
	@Override
	public void stop() {
		running = false;
		
	}
				
	}

