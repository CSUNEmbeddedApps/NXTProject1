

import java.util.Random;
import lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.*;
import lejos.robotics.*;

public class Main {
	

	//----Motor Ports---
	//Motor A and B are back wheels
	//Motor C helps ultrasonic rotate
	
	//-----Sensors----
	//Port 1 --> Ultrasonic
	//Port 2 --> Light 
	
	static long tStart;
	static UltrasonicSensor usonic = new UltrasonicSensor(SensorPort.S1); //UltraSonic Sensor
	static LightSensor lightRight = new LightSensor(SensorPort.S3); //Light Sensor
	static LightSensor lightLeft = new LightSensor(SensorPort.S2); //Light Sensor
	
	
	//controls both wheels simutaneously
	static DifferentialPilot pilot = new DifferentialPilot(2.25f, 5.5f, Motor.A, Motor.B);
	
	static boolean run = true;
	static Random rand = new Random();
	
	public static void main(String[] args) {	
		
		//Initialize Speeds
		pilot.setTravelSpeed(8.0f); //juan sucks fat big hairy balls
		pilot.setRotateSpeed(15.0f);
		
		//Set mode to continually check for objects
		usonic.continuous();
		
		
		tStart = System.currentTimeMillis();
		long delta = 0;
		
		while(run)
		{	
			long tCurr = System.currentTimeMillis();
			delta = tCurr - tStart;
			
			//tStart = tCurr;
			
			//Move forward if nothing nearby
		
			if(usonic.getDistance() > 30)
			{		
				pilot.setTravelSpeed(10.0f);
				//moveForward();
				pilot.forward();
				delta = 0;
				tStart = System.currentTimeMillis();

			}
			
			else if(usonic.getDistance() <= 40){
				//pilot.quickStop();          //Stop the robot quickly
				pilot.setTravelSpeed(5.0f);
				pilot.forward();
				
			int randomNum = rand.nextInt(2);
			
				if(randomNum == 0)
					pilot.rotate(35);
				else if(randomNum == 1)
					pilot.rotate(-35);
				
				LCD.drawInt(randomNum, 10, 0);
				LCD.drawString("rand", 10, 1);
				
				Delay.msDelay(500);
			}
			
			else if(lightLeft.readValue() > 45 && usonic.getDistance() < 30){
				pilot.rotate(20);
				LCD.drawInt(lightLeft.readValue(), 0, 0);
				LCD.drawChar('L', 0, 1);
				Delay.msDelay(500);

			}
			else if(lightRight.readValue() > 40 && usonic.getDistance() < 30){
				pilot.rotate(-20);
				LCD.drawInt(lightRight.readValue(), 5, 0);
				LCD.drawChar('R', 5, 1);
				Delay.msDelay(500);

			}
			
			double newDouble = (double)(delta);
			int newDelta = (int) (newDouble);
			
			LCD.drawInt(newDelta, 4, 4);
			LCD.drawString("delta", 4, 5);
			
			if(!pilot.isMoving())
				LCD.drawString("!moving", 0, 6);
			
			if(!pilot.isMoving() && newDelta > 5000)
			{
				pilot.backward();
				Delay.msDelay(1000);
			}
			if(Button.ESCAPE.isDown())
			{
				run = false;
			}
			
		} //Main Loop
		
		pilot.quickStop(); //Stop the robot quickly
		
	
		
	}
	
}