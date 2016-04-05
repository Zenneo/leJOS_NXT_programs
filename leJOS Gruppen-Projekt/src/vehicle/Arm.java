package vehicle;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class Arm {

	/* --Engines */
	private NXTRegulatedMotor motor_rotate;
	private NXTRegulatedMotor motor_arm;
	// engine vars
	private int engine1_speed = 50; // rotation speed
	private int engine2_speed = 20; // lift speed
	private int engine1_acceleration = 50; // rotation acceleration
	private int engine2_acceleration = 25; // lift acceleration
	private int engine_stalled_error = 2; // stalled error
	private int engine_stalled_time = 50; // stalled time in ms

	private int motor_rotate_initialpos;
	private int motor_arm_initialpos;

	public Arm(NXTRegulatedMotor motor_rot, NXTRegulatedMotor motor_ar) {
		motor_rotate = motor_rot;
		motor_arm = motor_ar;

		motor_rotate.setSpeed(engine1_speed);
		motor_rotate.setAcceleration(engine1_acceleration);
		motor_rotate.setStallThreshold(engine_stalled_error, engine_stalled_time);
		motor_arm.setSpeed(engine2_speed);
		motor_arm.setAcceleration(engine2_acceleration);
		motor_arm.setStallThreshold(engine_stalled_error, engine_stalled_time);
	}

	public void rotateToInitial() {
		RConsole.println("TASK: Rotate arms to initial position");

		motor_rotate.rotate(-360);
		motor_rotate.rotate(10);
		motor_rotate_initialpos = motor_rotate.getPosition();

		motor_arm.rotate(-360);
		motor_rotate.rotate(10);
		motor_arm_initialpos = motor_arm.getPosition();
	}

	// load vehicle
	public void receive_package() {
		RConsole.println("TASK: Load vehicle");
		rotateToPos(2);
		rotateToPos(1);
	}

	// unloads vehicle
	public void deliver_package() {
		RConsole.println("TASK: Unload vehicle");
		rotateToPos(3);
		rotateToPos(1);
	}

	public int mrotate_pos() {
		return motor_rotate.getTachoCount();
	}

	public int marm_pos() {
		return motor_arm.getTachoCount();
	}

	private void rotateToPos(int posi) {
		// posis:
		// 1: ready for movement
		// 2: ready for loading
		// 3: ready for unloading

		RConsole.println("ACTION: Rotating arms to position " + posi);

		switch (posi) {
		case 1:
			motor_arm.rotateTo(motor_arm_initialpos);
			motor_rotate.rotateTo(motor_rotate_initialpos);
			break;
		case 2:
			// load vehicle
			// TODO determine correct angles
			motor_arm.rotate(-40);
			motor_rotate.rotate(90);
			motor_arm.rotate(-5);
			break;
		case 3:
			// unload vehicle
			// TODO determine correct angles
			motor_rotate.rotate(90);
			motor_arm.rotate(-50);
			Delay.msDelay(500);
			motor_rotate.rotate(-20);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}
}
