package vehicle;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;

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
	
	/* --Engines-- */	
	private NXTRegulatedMotor engine;
	//Engine speed
	private int engine_speed = 100;
	
	public Movement(NXTRegulatedMotor motor1) {
		engine = motor1;
		engine.setSpeed(engine_speed);
		
		checkMovement();
	}

	public void checkMovement() {
		//DEBUG MSG
		RConsole.println("ACTION: Checking vehicle movement... ");
		
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
			//DEBUG MSG
			RConsole.println("TASK: Move to station" + station);
			
			// TODO Move vehicle to station
			forward();
		} else if (station == 2) {
			//DEBUG MSG
			RConsole.println("TASK: Move to station" + station);
			
			// TODO Move vehicle to station
			backward();
		} else {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public void stop() {
		//DEBUG MSG
		RConsole.println("ACTION: Stopping vehicle... ");
		
		engine.stop(true);
		vehicle_movement = 1;
	}
	
	public void forward() {
		//DEBUG MSG
		RConsole.println("ACTION: Moving vehicle forward... ");
		engine.forward();
		vehicle_movement = 2;
	}
	
	public void backward() {
		//DEBUG MSG
		RConsole.println("ACTION:Moving vehicle backward... ");
		engine.backward();
		vehicle_movement = 3;
	}
	
}
