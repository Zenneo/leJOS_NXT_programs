package vehicle;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class Arm {

	/* --Engines */
	private NXTRegulatedMotor motor_rotate;
	private NXTRegulatedMotor motor_arm;
	// engine vars
	private int delay_between_rotations = 500; // in ms
	private int engine1_speed = 35; // rotation speed
	private int engine2_speed = 120; // lift speed
	private int engine1_acceleration = 500; // rotation acceleration
	private int engine2_acceleration = 600; // lift acceleration
	private int engine1_stalled_error = 2; // stalled error
	private int engine1_stalled_time = 150; // stalled time in ms
	private int engine2_stalled_error = 3; // stalled error
	private int engine2_stalled_time = 300; // stalled time in ms

	private int motor_rotate_initialpos;
	private int motor_arm_initialpos;

	public Arm(NXTRegulatedMotor motor_rot, NXTRegulatedMotor motor_ar) {
		motor_rotate = motor_rot;
		motor_arm = motor_ar;

		motor_rotate.setSpeed(engine1_speed);
		motor_rotate.setAcceleration(engine1_acceleration);
		motor_rotate.setStallThreshold(engine1_stalled_error, engine1_stalled_time);
		motor_arm.setSpeed(engine2_speed);
		motor_arm.setAcceleration(engine2_acceleration);
		motor_arm.setStallThreshold(engine2_stalled_error, engine2_stalled_time);
	}

	public void rotateToInitial() {
		RConsole.println("TASK: Rotate arms to initial position");

		motor_rotate.rotate(1000); // rotate until stalled
		motor_rotate_initialpos = motor_rotate.getPosition();

		motor_arm.rotate(2000); // rotate until stalled
		motor_arm.rotate(-1000); // ready for driving
		motor_arm_initialpos = motor_arm.getPosition();
	}

	// load vehicle
	public void receive_package_phase1() {
		RConsole.println("TASK: Loading vehicle phase 1");
		rotateToPos(2);
	}
	
	public void receive_package_phase2() {
		RConsole.println("TASK: Loading vehicle phase 2");
		rotateToPos(1);
	}

	// unloads vehicle
	public void deliver_package() {
		RConsole.println("TASK: Unload vehicle");
		rotateToPos(3);
	}

	// returns the current rotate motor position; needed by LCD
	public int mrotate_pos() {
		return motor_rotate.getTachoCount();
	}

	// returns the current arm motor position; needed by LCD
	public int marm_pos() {
		return motor_arm.getTachoCount();
	}

	// executes rotations necessary to get motor to rotate into a specific
	// position
	private void rotateToPos(int posi) {
		// posis:
		// 1: ready for movement
		// 2: ready for loading
		// 3: ready for unloading

		RConsole.println("ACTION: Rotating to position " + posi);

		switch (posi) {
		case 1:
			m_rotateTo(motor_arm, motor_arm_initialpos);
			m_rotateTo(motor_rotate, motor_rotate_initialpos + 1000);
			break;
		case 2:
			// load vehicle
			m_rotateTo(motor_arm, motor_arm_initialpos); // ensure fork is up
			m_rotate(motor_rotate, -900); // rotate arm to the right
			m_rotate(motor_arm, 1000); // lower fork
			break;
		case 3:
			// unload vehicle
			m_rotate(motor_arm, 900);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}

	// -- functions for motor operations
	// these are described in separate functions in order to detect and report
	// stalls
	private void m_rotateTo(NXTRegulatedMotor motor, int angle) {
		motor.rotateTo(angle);
		if (motor.isStalled()) {
			char port = getMotorPortChar(motor);
			RConsole.println("WARNING: Motor " + port + " has stalled!");
		}
		Delay.msDelay(delay_between_rotations);
	}

	private void m_rotate(NXTRegulatedMotor motor, int angle) {
		motor.rotate(angle);
		if (motor.isStalled()) {
			char port = getMotorPortChar(motor);
			RConsole.println("WARNING: Motor " + port + " has stalled!");
		}
		Delay.msDelay(delay_between_rotations);
	}

	// detects which motor port the given motor is connected to
	private char getMotorPortChar(NXTRegulatedMotor motor) {
		if (motor == Motor.A) {
			return 'A';
		} else if (motor == Motor.B) {
			return 'B';
		} else if (motor == Motor.C) {
			return 'C';
		} else {
			return '-';
		}
	}

}
