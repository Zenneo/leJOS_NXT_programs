package rotationStation;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class Motors {

	// vars
	private int delay_between_rotations = 500; // in ms
	// Engines and Sensors
	private NXTRegulatedMotor motor1;
	
	public Motors (NXTRegulatedMotor motor1) {
		this.motor1 = motor1;
		
	}
	
	public int m_pos() {
		return motor1.getTachoCount();
	}
	
	public void m_rotateTo(int angle) {
		motor1.rotateTo(angle);
		if (motor1.isStalled()) {
			RConsole.println("WARNING: Motor has stalled!");
		}
		Delay.msDelay(delay_between_rotations);
	}

	public void m_rotate(int angle) {
		motor1.rotate(angle);
		if (motor1.isStalled()) {
			RConsole.println("WARNING: Motor has stalled!");
		}
		Delay.msDelay(delay_between_rotations);
	}
	
}
