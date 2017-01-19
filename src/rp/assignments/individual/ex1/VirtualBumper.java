package rp.assignments.individual.ex1;

import java.util.ArrayList;

import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.config.RangeFinderDescription;
import rp.robotics.DifferentialDriveRobot;
import rp.robotics.EventBasedTouchSensor;
import rp.robotics.TouchSensorEvent;
import rp.robotics.TouchSensorEventSource;
import rp.robotics.TouchSensorListener;
import rp.systems.ControllerWithTouchSensor;
import rp.systems.StoppableRunnable;
import rp.util.Rate;

/**
 *first part using touch sensor event source manages a bunch of listeners based on TouchSensorEventSource
 *
 *2nd part uses a thread to continually read the range finder and generate events appropriate.
 * 
 * @author Suki 
 *
 */
public class VirtualBumper implements  EventBasedTouchSensor, TouchSensorEventSource , lejos.robotics.Touch, StoppableRunnable {

	private  RangeFinderDescription _desc;
	private boolean m_running = false;
	private RangeFinder m_ranger;
	private boolean m_bumped = false;
	 
	private float _touchRange;
	
	private ArrayList <TouchSensorListener> listeners = new ArrayList();
	private long minRate; // (min) rate the sensors should update at
	private boolean running = false;
	private boolean pressed = false;

	public VirtualBumper (RangeFinderDescription _desc, RangeFinder _ranger, Float _touchRange) {
		this._desc = _desc;
		this.m_ranger = _ranger;
		this._touchRange = _touchRange;
		minRate = (long) _desc.getRate();
		new Thread(this).start(); //starts a thread up for p2
		
		
		
	}

	@Override
	public void addTouchSensorListener(TouchSensorListener _listener) {
		
		listeners.add(_listener);
		
		
	}

	@Override
	public boolean isPressed() {
		// TODO Auto-generated method stub
		//something similar to lecture slides
		return false;
	}

	@Override
	public void run() { //listener methods.
		
		if(isPressed() && !pressed){
			for(TouchSensorListener listener : listeners){
				listener.sensorPressed(new TouchSensorEvent (m_ranger.getRange(), _touchRange));
				
			}
		pressed = true;
		}
		else if(!isPressed() && pressed){ // its the released trigger
			for(TouchSensorListener listener : listeners){
				listener.sensorReleased(new TouchSensorEvent (m_ranger.getRange(), _touchRange));
				
			}
		}
		else{
			for(TouchSensorListener listener : listeners){ //else its a bump
			listener.sensorBumped(new TouchSensorEvent (m_ranger.getRange(), _touchRange));		
		}
			pressed = false;
		}
		Delay.msDelay(minRate);
	}

	@Override
	public void stop() {
		running = false;
		
	}

}
