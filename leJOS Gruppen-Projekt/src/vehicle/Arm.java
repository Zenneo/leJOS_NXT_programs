package vehicle;

import lejos.nxt.NXTRegulatedMotor;

public class Arm {

	/* --Engines */
	private NXTRegulatedMotor motor_rotate;
	private NXTRegulatedMotor motor_arm;
	// engine vars
	private int engine_speed1 = 50; // rotation speed
	private int engine_speed2 = 20; // lift speed
	private int engine_acceleration1 = 100; // rotation acceleration
	private int engine_acceleration2 = 50; // lift acceleration

	public Arm(NXTRegulatedMotor motor_rot, NXTRegulatedMotor motor_ar) {
		motor_rotate = motor_rot;
		motor_arm = motor_ar;

		motor_rotate.setSpeed(engine_speed1);
		motor_rotate.setAcceleration(engine_acceleration1);
		motor_arm.setSpeed(engine_speed2);
		motor_arm.setAcceleration(engine_acceleration2);
	}

	public void rotateToPos(int posi) {
		// posis:
		// 1: ready for movement
		// 2: ready for loading
		// 3: ready for unloading

		// TODO rotate arm to positions
	}
}
