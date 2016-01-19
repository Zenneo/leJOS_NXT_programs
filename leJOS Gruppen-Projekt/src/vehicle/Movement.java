package vehicle;

import lejos.nxt.NXTRegulatedMotor;

public class Movement {
	
	/* --Vehicle Movement-- */
	private int vehicle_movement = 0;
	// vehicle status codes
	// 0 - unknown
	// 1 - still
	// 2 - moving forward
	// 3 - moving backward
	
	/**
	 * @return the vehicle_movement
	 */
	public int getVehicle_movement() {
		return vehicle_movement;
	}	
	
	// vehicle position
	private Position pos;
	
	/* --Engines-- */	
	private NXTRegulatedMotor engine;
	//Engine speed
	private int engine_speed = 100;
	
	public Movement(NXTRegulatedMotor motor1, Position posi) {
		engine = motor1;
		engine.setSpeed(engine_speed);
		
		pos = posi;
		
		checkMovement();
	}

	public void checkMovement() {
		if (!engine.isMoving() ) {
			vehicle_movement = 1; // still
		} else if (engine.getRotationSpeed() > 0 ) { 
			vehicle_movement = 2; // moving forward
		} else if (engine.getRotationSpeed() < 0 ) {
			vehicle_movement = 3; // moving backward
		}
	}
	
	public void moveToStation(int station) {
		if (station == 1) {
			// TODO Move vehicle to station
		} else if (station == 2) {
			// TODO Move vehicle to station
		} else {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public void stop() {
		engine.stop(true);
		vehicle_movement = 1;
	}
	
	public void forward() {
		engine.forward();
		vehicle_movement = 2;
	}
	
	public void backward() {
		engine.backward();
		vehicle_movement = 3;
	}
	
}
