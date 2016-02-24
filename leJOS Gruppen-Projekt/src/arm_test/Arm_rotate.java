package arm_test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class Arm_rotate {

	// motors
	private static NXTRegulatedMotor motor = Motor.B;

	// runtime integers
	private static int rotation_tick = 1; // degree
	private static int wait_between_press = 1000; // in milliseconds

	public static void main(String[] args) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// initial setup
		long pressed_since = System.currentTimeMillis();

		// initial screen
		LCD.drawString("Intervall:", 0, 0);
		LCD.drawString("Confirm with", 0, 4);
		LCD.drawString("orange.", 0, 5);
		while (!Button.ENTER.isDown()) {
			LCD.clear(1);
			LCD.drawString("<< " + rotation_tick + " >>", 0, 1);
			if (Button.LEFT.isUp() && Button.RIGHT.isUp()) {
				pressed_since = 0;
			} else if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
				if (pressed_since == 0) {
					pressed_since = System.currentTimeMillis();
					change_tick();
				} else if (System.currentTimeMillis() - pressed_since > wait_between_press) {
					change_tick();
				}
			}
		}

		// main screen
		LCD.clear();
		LCD.drawString("Left =negative", 0, 0);
		LCD.drawString("Right=positive", 0, 1);
		LCD.drawString("Intervall: " + rotation_tick, 0, 2);
		LCD.drawString("Current angle:", 0, 4);
		LCD.drawInt(motor.getPosition(), 0, 5);

		// main loop
		while (Button.ESCAPE.isUp()) {
			if (Button.LEFT.isUp() && Button.RIGHT.isUp()) {
				pressed_since = 0;
			} else if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
				if (pressed_since == 0) {
					pressed_since = System.currentTimeMillis();
					rotate();
				} else if (System.currentTimeMillis() - pressed_since > wait_between_press) {
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
		LCD.clear(5);
		LCD.drawInt(motor.getPosition(), 0, 5);
	}

	private static void change_tick() {
		if (Button.LEFT.isDown() && Button.RIGHT.isUp()) {
			if (rotation_tick > 0) {
				rotation_tick--;
			}
		} else if (Button.LEFT.isUp() && Button.RIGHT.isDown()) {
			rotation_tick++;
		}
		try {
			Thread.sleep(250);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
