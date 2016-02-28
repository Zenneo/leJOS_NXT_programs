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
	// Engine speed
	private int engine_speed = 300;
	private int engine_acceleration = 600;

	public Movement(NXTRegulatedMotor motor1) {
		engine = motor1;
		engine.setSpeed(engine_speed);
		engine.setAcceleration(engine_acceleration);

		checkMovement();
	}

	public void checkMovement() {
		if (!engine.isMoving()) {
			vehicle_movement = 1; // still
		} else if (engine.getRotationSpeed() > 0) {
			vehicle_movement = 2; // moving forward
		} else if (engine.getRotationSpeed() < 0) {
			vehicle_movement = 3; // moving backward
		}
	}

	public int getSpeed() {
		return engine.getRotationSpeed();
	}

	public void moveToStation(int station) {
		if (station == 1) {
			// DEBUG MSG
			RConsole.println("TASK: Move to station" + station);

			forward();
		} else if (station == 2) {
			// DEBUG MSG
			RConsole.println("TASK: Move to station" + station);

			backward();
		} else {
			throw new UnsupportedOperationException();
		}

	}

	public void stop() {
		// DEBUG MSG
		RConsole.println("ACTION: Stopping vehicle... ");
		engine.stop();
		vehicle_movement = 1;
	}

	public void forward() {
		// DEBUG MSG
		RConsole.println("ACTION: Moving vehicle forward... ");
		engine.forward();
		vehicle_movement = 2;
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void backward() {
		// DEBUG MSG
		RConsole.println("ACTION: Moving vehicle backward... ");
		engine.backward();
		vehicle_movement = 3;
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
