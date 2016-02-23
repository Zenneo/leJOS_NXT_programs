package arm_test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class Arm_rotate {

	// motors
	private static NXTRegulatedMotor motor = Motor.B;

	// runtime integers
	private static int rotation_tick = 1;
	private static int wait_between_press = 500;

	public static void main(String[] args) {
		// initial setup
		LCD.drawString("Left =negative", 0, 0);
		LCD.drawString("Right=positive", 0, 1);
		LCD.drawString("Current angle:", 0, 3);
		LCD.drawInt(motor.getLimitAngle(), 0, 4);
		
		long pressed_since = System.currentTimeMillis();

		// main loop
		while (!Button.ESCAPE.isDown()) {
			if (Button.LEFT.isUp() && Button.RIGHT.isUp()) {
				pressed_since = 0;
			} else if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
				if (pressed_since == 0) {
					pressed_since = System.currentTimeMillis();
					rotate();
				} else if (pressed_since > wait_between_press) {
					rotate();
				}
			}
		}

	}

	private static void rotate() {
		if (Button.LEFT.isDown() && Button.RIGHT.isUp()) {
			motor.rotate(0 - rotation_tick);
		} else if (Button.LEFT.isUp() && Button.RIGHT.isDown()) {
			motor.rotate(rotation_tick);
		}
		LCD.drawInt(motor.getLimitAngle(), 0, 4);
	}

}
