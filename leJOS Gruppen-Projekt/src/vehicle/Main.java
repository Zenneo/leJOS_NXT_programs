package vehicle;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.RConsole;
import vehicle.Movement;

public class Main {
	
	/* --Connected Peripherals-- */
	// sensors
	private static TouchSensor touch1 = new TouchSensor(SensorPort.S1);
	private static TouchSensor touch2 = new TouchSensor(SensorPort.S2);
	private static boolean touchOrientation = true;
	private static Position Pos = new Position(touch1, touch2, touchOrientation);
	
	// engines
	private static NXTRegulatedMotor engine = Motor.A;
	private static Movement Move = new Movement(engine);
	
		
	/* --Status Codes-- */
	private static int current_task = 0;
	// 0 - nothing
	// 1 - move to station 1
	// 2 - move to station 2
	// 3 - wait
	/**
	 * @return the current_task
	 */
	public static int getCurrent_task() {
		return current_task;
	}
	/**
	 * @param current_task the current_task to set
	 */
	private static void setCurrent_task(int current_task) {
		Main.current_task = current_task;
	}
	
	
	// LCDthread object for drawing screen outputs
	private static Thread LCDthreadobj = new LCDthread(Pos, Move);
	
	// initiate program exit through exitProgram()
	private static boolean exitProgram = false;
	
	
	public Main() throws InterruptedException {
		enterDebuggingMode();
		
		LCDthreadobj.setPriority(Thread.NORM_PRIORITY - 2);
		LCDthreadobj.start();
	}
	
	
	public static void main() throws InterruptedException {
		// Main Loop
		while (!exitProgram) {
			switch (Move.getVehicle_movement()) { // Check current vehicle status
			
				// Vehicle still (no movement)
				case 1:
					if (Pos.getVehicle_position() == 1 || Pos.getVehicle_position() == 2) { // Vehicle at station
						setCurrent_task(3); // wait
						waitForTrigger();
							
						switch (Pos.getVehicle_position()) {
							case 1:
								setCurrent_task(2); // move to station 2
								Move.moveToStation(2);
								break;
							case 2:
								setCurrent_task(1); // move to station 1
								Move.moveToStation(1);
								break;
						}
					} else if (Pos.getVehicle_position() == 0) { // Position unknown
						Pos.checkPosition();
					} else if (Pos.getVehicle_position() == 3) { // Vehicle between stations
						// TODO Define what happens if vehicle is initially between stations
					} else {
						throw new IllegalStateException();
					}
					break;
					
				// Vehicle moving forward
				case 2:
					// TODO Define case when moving forward
					if (Pos.getVehicle_position() == 1 || Pos.getVehicle_position() == 2) { // at station
						setCurrent_task(3);
						Move.stop();
					} else if (Pos.getVehicle_position() == 3){
						break;
					} else {
						Pos.checkPosition();
					}
					break;
				
				// Vehicle moving backward
				case 3:
					// TODO Define case when moving backward
					break;
				
				// Vehicle movement unknown
				case 0:
					Move.checkMovement();
					break;
				default:
					throw new UnsupportedOperationException();
			}
			
			
			// Exit if back button is pressed
			if (Button.ESCAPE.isDown()) {
				exitProgram();
			}
			
			// check for any changes
			Pos.checkPosition();
			Move.checkMovement();
		}
	}
	
	private static void waitForTrigger() {
		//DEBUG MSG
		RConsole.println("Waiting for trigger... ");
		
		// TODO Create a blocking function that releases when triggered
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// This function will activate RemoteDebugging after a prompt
	private static void enterDebuggingMode() throws InterruptedException {
		// print LCD prompt
		LCD.clear();
		LCD.drawString("Press orange to", 0, 0);
		LCD.drawString("connect console.", 0, 1);
		LCD.drawString("Skip timer with", 0, 3);
		LCD.drawString("right.", 0, 4);
		
		for (int i=10; i>0; i--) {
			LCD.clear(6);
			LCD.drawString(i + " seconds left", 0, 6);
			if (Button.RIGHT.isDown()) {
				LCD.clear();
				return;
			} else if (Button.ENTER.isDown()) {
				LCD.clear();
				RConsole.open();
			}
			Thread.sleep(1000);
		}
	}
	public static void exitProgram() {
		exitProgram = true;
		LCDthreadobj.interrupt();
	}
}
