package rotationStation;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class Motors {
	// this class controls allows for controlling the motor

	/* --Runtime constants-- */
	// -delays
	private final int delay_between_rotations = 500; // in ms
	// -motor speed and acceleration
	private final int m_accel = 800;
	private final int m_speed = 50;
	// -stall values
	private final int stallError = 3;
	private final int stallTime = 500;

	/* --Status variables-- */
	// keeps track whether rotate to 0 degrees
	private boolean is0degree;

	public boolean is0degree() {
		return is0degree;
	}

	// initial pos degrees
	private int initialPos;
	// keeps track whether motor stalled once
	private boolean hasStalled = false;

	public boolean hasStalled() {
		return hasStalled;
	}

	// Motor
	private final NXTRegulatedMotor motor1;

	public Motors(NXTRegulatedMotor motor1) {
		this.motor1 = motor1;

		// set motor constants
		this.motor1.setStallThreshold(stallError, stallTime);
		this.motor1.setSpeed(m_speed);
		this.motor1.setAcceleration(m_accel);

	}

	public void initMotor() {
		// resets motor to default position
		// rotate unitl stalled
		RConsole.println("ACTION: Init motor");
		motor1.rotate(-1000);
		initialPos = m_pos();
		is0degree = true;

	}

	public void doUTurn() {
		// this function rotates the motor by 180 degree.
		// the direction changes everytime
		RConsole.println("ACTION: Do U-Turn");

		// if 0 degree pos
		if (is0degree) {
			RConsole.println("STATUS: Rotating from 0 to +180 degrees");
			m_rotate(180);
			is0degree = false;
		}
		// else if 180 degree pos
		else {
			RConsole.println("STATUS: Rotating from +180 to 0 degrees");
			m_rotateTo(initialPos);
			is0degree = true;
		}

	}

	public int m_pos() {
		return motor1.getTachoCount();
	}

	public void m_rotateTo(int angle) {
		motor1.rotateTo(angle);
		if (motor1.isStalled()) {
			RConsole.println("WARNING: Motor has stalled!");
			hasStalled = true;
		}
		Delay.msDelay(delay_between_rotations);
	}

	public void m_rotate(int angle) {
		motor1.rotate(angle);
		if (motor1.isStalled()) {
			RConsole.println("WARNING: Motor has stalled!");
			hasStalled = true;
		}
		Delay.msDelay(delay_between_rotations);
	}

}
