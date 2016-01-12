package vehicle;

import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import vehicle.Movement;

public class Main {
	
	/* --Connected Peripherals-- */
	// engines
	private NXTRegulatedMotor engine1 = Motor.A;
	private NXTRegulatedMotor engine2 = Motor.B;
	private Movement Move = new Movement(engine1, engine2);
	
	// sensors
	private TouchSensor touch1 = new TouchSensor(SensorPort.S1);
	private TouchSensor touch2 = new TouchSensor(SensorPort.S2);
	
	
	/* --Status Codes-- */
	private int vehicle_position = 0;
	// vehicle position codes
	// 0 - unknown
	// 1 - at station 1
	// 2 - at station 2
	// 3 - moving to station 1
	// 4 - moving to station 2
	// 5 - between stations
	
	// initiate program exit through exitProgram()
	private boolean exitProgram = false;
	
	
	
	public void main() {
		// Main Loop
		while (!exitProgram) {
			switch (Move.status()) { // Check current vehicle status
			
				// Vehicle still (no movement)
				case 1:
					if (vehicle_position == 1 || vehicle_position == 2) { // Vehicle at station
						waitForTrigger();
						
						switch (vehicle_position) {
							case 1:
								moveToStation(2);
								break;
							case 2:
								moveToStation(1);
								break;
						}
					} else if (vehicle_position == 0) { // Position unknown
						check_position();
					} else if (vehicle_position == 5) { // Vehicle between stations
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
			
			// check position for any changes
			check_position();
			Move.checkMovement();
		}
	}
	
	private void check_position() {
		// TODO Check position based on sensors
		vehicle_position = 5; // please change this
		
		
	}

	private void moveToStation(int station) {
		if (station == 1) {
			vehicle_position = 3;
			// TODO Move vehicle to station
		} else if (station == 2) {
			vehicle_position = 4;
			// TODO Move vehicle to station
		} else {
			throw new UnsupportedOperationException();
		}
		
	}

	private void waitForTrigger() {
		// TODO Create a blocking function that releases when triggered
		
	}

	public void exitProgram() {
		exitProgram = true;
	}
}
