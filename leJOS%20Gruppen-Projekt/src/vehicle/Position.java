package vehicle;

import lejos.nxt.TouchSensor;

public class Position {

	/* Vehicle Position */
	private int vehicle_position = 0;

	// vehicle position codes
	// 0 - unknown
	// 1 - at station 1
	// 2 - at station 2
	// 3 - between stations
	/**
	 * @return the vehicle_position
	 */
	public int getVehicle_position() {
		return vehicle_position;
	}

	// TouchOrientation
	// determines which touch sensor represents which station
	// true: 1=pressed - station 1, 2=pressed - station 2
	// false: 1=pressed - station 2, 2=pressed - station 1
	private boolean touchOrientation;

	// Sensors
	private TouchSensor touch1;
	private TouchSensor touch2;

	// Constructor
	public Position(TouchSensor touch1, TouchSensor touch2, boolean touchOrientation) {
		super();
		this.touchOrientation = touchOrientation;
		this.touch1 = touch1;
		this.touch2 = touch2;

		checkPosition();
	}

	public void checkPosition() {
		// DEBUG MSG
		// RConsole.println("ACTION: Checking vehicle position...");

		if (touch1.isPressed() == touchOrientation && touch2.isPressed() != touchOrientation) { // station
																								// 1
			vehicle_position = 1;
		} else if (touch2.isPressed() == touchOrientation && touch1.isPressed() != touchOrientation) { // station
																										// 2
			vehicle_position = 2;
		} else if (!touch1.isPressed() && !touch2.isPressed()) {
			vehicle_position = 3;
		} else {
			vehicle_position = 0; // if someone presses both touch sensors at
									// the same time
		}

	}
}
