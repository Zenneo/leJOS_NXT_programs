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
	private final int m_init_accel = 2000;
	private final int m_init_speed = 50;
	private final int m_accel = 400;
	private final int m_speed = 125;
	// -stall values
	private final int stall_init_Error = 2;
	private final int stall_init_Time = 60;
	private final int stallError = 5;
	private final int stallTime = 300;


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
		this.motor1.setStallThreshold(stall_init_Error, stall_init_Time);

	}

	public void initMotor() {
		// resets motor to default position
		// rotate unitl stalled
		RConsole.println("ACTION: Init motor");
		
		// set speed and accel for init
		motor1.setSpeed(m_init_speed);
		motor1.setAcceleration(m_init_accel);
		
		motor1.rotate(-100);
		motor1.rotate(1000);
		initialPos = m_pos();
		is0degree = true;
		
		// set speed and accel for main
		motor1.setSpeed(m_speed);
		motor1.setAcceleration(m_accel);
		
		// set stall threshold
		this.motor1.setStallThreshold(stallError, stallTime);
	}

	public void doUTurn() {
		// this function rotates the motor by 180 degree.
		// the direction changes everytime
		RConsole.println("ACTION: Do U-Turn");

		// if 0 degree pos
		if (is0degree) {
			RConsole.println("STATUS: Rotating from 0 to +180 degrees");
			m_rotateTo(initialPos - 480);
			is0degree = false;
		}
		// else if 180 degree pos
		else {
			RConsole.println("STATUS: Rotating from +180 to 0 degrees");
			m_rotateTo(initialPos - 45);
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
