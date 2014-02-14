

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
	
	
	static UltrasonicSensor usonic = new UltrasonicSensor(SensorPort.S1); //UltraSonic Sensor
	static LightSensor lightRight = new LightSensor(SensorPort.S2); //Light Sensor
	static LightSensor lightLeft = new LightSensor(SensorPort.S3); //Light Sensor
	static long tStart;
	//controls both wheels simutaneously
	static DifferentialPilot pilot = new DifferentialPilot(2.25f, 5.5f, Motor.A, Motor.B);
	
	static boolean run = true;
	static Random rand = new Random();
	public static void main(String[] args) {	
		pilot.setTravelSpeed(8.0f);
		pilot.setRotateSpeed(10.0f);
		
		//Set mode to continually check for objects
		usonic.continuous();
		
		tStart = System.currentTimeMillis();
		long delta = 0;
		while(run)
		{	
			long tCurr = System.currentTimeMillis();
			delta = tCurr - tStart;
			
			tStart = tCurr;
			//Move forward if nothing nearby
			if(usonic.getDistance() > 40)
			{		
				pilot.setTravelSpeed(10.0f);
				//moveForward();
				pilot.forward();
				delta = 0;
			}
			//something nearby 
//			else
//			{
//				stop();
//				pilot.forward();
////				pilot.rotate(15);				
//			}
////			
			if(usonic.getDistance() <= 40){
				stop();
				pilot.setTravelSpeed(5.0f);
				pilot.forward();
				
			int randomNum = rand.nextInt(2);
				if(randomNum == 0)
					pilot.rotate(15);
				else if(randomNum == 1)
					pilot.rotate(-15);
				
				LCD.drawInt(randomNum, 10, 0);
				LCD.drawString("rand", 10, 1);
			}
			
			else if(lightLeft.readValue() > 45 && usonic.getDistance() > 40){
				pilot.rotate(10);
				LCD.drawInt(lightLeft.readValue(), 0, 0);
				LCD.drawChar('L', 0, 1);
			}
			else if(lightRight.readValue() > 45 && usonic.getDistance() > 40){
				pilot.rotate(-10);
				LCD.drawInt(lightRight.readValue(), 5, 0);
				LCD.drawChar('R', 5, 1);
			}
			
			double newDouble = (double)(delta);
			int newDelta = (int) (newDouble);
			
			LCD.drawInt(newDelta, 4, 4);
			LCD.drawString("delta", 4, 5);
			
			if(!pilot.isMoving())
				LCD.drawString("!moving", 0, 6);
			
			if(!pilot.isMoving()){
				pilot.backward();
				Delay.msDelay(1000);
			}
			if(Button.ESCAPE.isDown())
			{
				run = false;
			}
			
		} //Main Loop
		//Stop Motors
		stop();
	}


	
	private static void stop()
	{
		Motor.A.stop();
		Motor.B.stop();
	}
	
	
	private static int ultrasonicCheck()
	{
		//motor A corresponds to left wheel
		//motor B corresponds to right wheel
		
		int val = 0; 
				
		
		if(usonic.getDistance() <= 10)//something is on the right
		{
			val = 1;
			
		}
		
		Motor.C.rotate(180); //rotate left
			
		if(usonic.getDistance() <= 10) //something on the left
		{
			val = 2;
				
		}
		else //nothing on right or left, randomly select
		{
			val = randomNum();
			
		}
		return val;
		
		
	}
	
//	private static int ultrasonicCheck()
//	{
//		//motor A corresponds to left wheel
//		//motor B corresponds to right wheel
//		
//		int val = 0; 
//		
//		Motor.C.rotate(-90); //rotate right
//		
//		if(usonic.getDistance() <= 10)//something is on the right
//		{
//			val = 1;
//			
//		}
//		
//		Motor.C.rotate(180); //rotate left
//			
//		if(usonic.getDistance() <= 10) //something on the left
//		{
//			val = 2;
//				
//		}
//		else //nothing on right or left, randomly select
//		{
//			val = randomNum();
//			
//		}
//		return val;
//		
//		
//	}
	
	private static int randomNum()
	{
		int Min = 0;
		int Max = 1;
		
		//return either 1 or 2
		return (Min + (int)(Math.random() * ((Max - Min) + 1)));
		
	}
}