package vehicle;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class Arm {

	/* --Engines */
	private final NXTRegulatedMotor motor_rotate; // ^= engine_rot
	private final NXTRegulatedMotor motor_arm; // ^= engine_arm
	// engine vars
	private final int delay_between_rotations = 500; // in ms
	private final int engine_rot_speed = 45; // rotation speed
	private final int engine_arm_speed = 130; // lift speed
	private final int engine_rot_acceleration = 800; // rotation acceleration
	private final int engine_arm_acceleration = 600; // lift acceleration
	
	// stall values
	// --loading
	private final int engine_rot_loading_stalled_error = 3; // stalled error
	private final int engine_rot_loading_stalled_time = 750; // stalled time in ms
	private final int engine_arm_loading_stalled_error = 2; // stalled error
	private final int engine_arm_loading_stalled_time = 750; // stalled time in ms
	// --unloading
	private final int engine_rot_unloading_stalled_error = 5; // stalled error
	private final int engine_rot_unloading_stalled_time = 750; // stalled time in ms
	private final int engine_arm_unloading_stalled_error = 6; // stalled error
	private final int engine_arm_unloading_stalled_time = 750; // stalled time in ms

	private int motor_rotate_initialpos;
	private int motor_arm_initialpos;

	public Arm(NXTRegulatedMotor motor_rot, NXTRegulatedMotor motor_ar) {
		motor_rotate = motor_rot;
		motor_arm = motor_ar;

		motor_rotate.setSpeed(engine_rot_speed);
		motor_rotate.setAcceleration(engine_rot_acceleration);
		
		motor_arm.setSpeed(engine_arm_speed);
		motor_arm.setAcceleration(engine_arm_acceleration);
		
	}

	public void setStallThresholds ( int profile_id)
	{
		if ( profile_id == 1)
		{
			// for loading phase
			motor_rotate.setStallThreshold(engine_rot_loading_stalled_error,
					engine_rot_loading_stalled_time);
			motor_arm.setStallThreshold(engine_arm_loading_stalled_error,
					engine_arm_loading_stalled_time);
		}
		else if ( profile_id == 2)
		{
			// for unloading phase
			motor_rotate.setStallThreshold(engine_rot_unloading_stalled_error,
					engine_rot_unloading_stalled_time);
			motor_arm.setStallThreshold(engine_arm_unloading_stalled_error,
					engine_arm_unloading_stalled_time);
		}
		else {
			throw new UnsupportedOperationException("Unknowwn freshhold profile id");
		}
	}
	
	public void rotateToInitial() {
		RConsole.println("TASK: Rotate arms to initial position");

		setStallThresholds( 1 );
		motor_rotate.rotate(1500); // rotate until stalled
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
			setStallThresholds( 1 );
			m_rotateTo(motor_arm, motor_arm_initialpos);
			m_rotateTo(motor_rotate, motor_rotate_initialpos + 1100);
			break;
		case 2:
			// load vehicle
			setStallThresholds( 1 );
			m_rotateTo(motor_arm, motor_arm_initialpos); // ensure fork is up
			m_rotate(motor_arm, 1000); // lower fork
			m_rotate(motor_arm, -304);
			m_rotate(motor_rotate, -1000); // rotate arm to the right
			break;
		case 3:
			// unload vehicle
			setStallThresholds( 2 );
			m_rotate(motor_rotate, 500);
			m_rotate(motor_arm, 677);
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
