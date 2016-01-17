package vehicle;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import vehicle.Movement;

public class Main {
	
	/* --Connected Peripherals-- */
	// sensors
	private TouchSensor touch1 = new TouchSensor(SensorPort.S1);
	private TouchSensor touch2 = new TouchSensor(SensorPort.S2);
	private static boolean touchOrientation = true;
	private Position Pos = new Position(touch1, touch2, touchOrientation);
	
	// engines
	private NXTRegulatedMotor engine1 = Motor.A;
	private NXTRegulatedMotor engine2 = Motor.B;
	private Movement Move = new Movement(engine1, engine2);
	
		
	/* --Status Codes-- */
	private int current_task = 0;
	// 0 - nothing
	// 1 - move to station 1
	// 2 - move to station 2
	// 3 - wait
	
	// initiate program exit through exitProgram()
	private boolean exitProgram = false;
	
	
	
	public void main() {
		// Main Loop
		while (!exitProgram) {
			switch (Move.getVehicle_movement()) { // Check current vehicle status
			
				// Vehicle still (no movement)
				case 1:
					if (Pos.getVehicle_position() == 1 || Pos.getVehicle_position() == 2) { // Vehicle at station
						current_task = 3;
						waitForTrigger();
							
						switch (Pos.getVehicle_position()) {
							case 1:
								current_task = 2; // move to station 2
								Move.moveToStation(2, current_task);
								break;
							case 2:
								current_task = 1; // move to staiton 1
								Move.moveToStation(1, current_task);
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
	
	private void waitForTrigger() {
		// TODO Create a blocking function that releases when triggered
		
	}

	public void exitProgram() {
		exitProgram = true;
	}
}
