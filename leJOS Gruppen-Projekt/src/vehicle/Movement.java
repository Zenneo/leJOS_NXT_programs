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
	private NXTRegulatedMotor engine1;
	private NXTRegulatedMotor engine2;
	//Engine speed
	private int engine_speed = 100;
	
	public Movement(NXTRegulatedMotor motor1, NXTRegulatedMotor motor2, Position posi) {
		engine1 = motor1;
		engine1.setSpeed(engine_speed);
		
		engine2 = motor2;
		engine2.setSpeed(engine_speed);
		
		pos = posi;
		
		checkMovement();
	}

	public void checkMovement() {
		if (!engine1.isMoving() && !engine2.isMoving()) {
			vehicle_movement = 1; // still
		} else if (engine1.getRotationSpeed() > 0 || engine2.getRotationSpeed() > 0) { 
			vehicle_movement = 2; // moving forward
		} else if (engine2.getRotationSpeed() < 0 || engine2.getRotationSpeed() < 0) {
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
		engine1.stop(true);
		engine2.stop(true);
		vehicle_movement = 1;
	}
	
	public void forward() {
		engine1.forward();
		engine2.forward();
		vehicle_movement = 2;
	}
	
	public void backward() {
		engine1.backward();
		engine2.backward();
		vehicle_movement = 3;
	}
	
}
