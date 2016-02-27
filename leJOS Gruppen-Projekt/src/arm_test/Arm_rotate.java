package arm_test;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class Arm_rotate {

	// motors
	private static NXTRegulatedMotor motor = Motor.B;

	// runtime integers
	private static int wait_between_press = 1000; // in milliseconds
	private static int speed; // rotation speed
	private static int acceleration; // motor acceleration
	private static int rotateTarget = 0;

	public static void main(String[] args) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// screen: ask for speed
		speed = askForValue("Speed", 500, 25, wait_between_press);
		
		// screen: ask for acceleration
		acceleration = askForValue("Acceleration", 1000, 50, wait_between_press);
		
		
		// set determined values
		motor.setSpeed(speed);
		motor.setAcceleration(acceleration);
		

		// main loop
		while (Button.ESCAPE.isUp()) {
			LCD.clear();
			LCD.drawString("Speed: " + speed, 0, 1);
			LCD.drawString("Accel: " + acceleration, 0, 2);
			LCD.drawString("Cur angle: " + motor.getPosition(), 0, 3);
			
			if (motor.getPosition() == rotateTarget) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				rotateTarget = askForValue("Tar angle", motor.getPosition(), 1, wait_between_press);
				motor.rotateTo(rotateTarget);
			} else {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}

	}

	
	private static int askForValue(String name, int initial, int step, int waitBetween) {
		LCD.clear();
		LCD.drawString(name + ":", 0, 0);
		LCD.drawString("Confirm with", 0, 3);
		LCD.drawString("orange.", 0, 4);
		
		int value = initial;
		long pressed_since = 0;
		
		while (!Button.ENTER.isDown()) {
			LCD.clear(1);
			LCD.drawString("<< " + value + " >>", 0, 1);
			if (Button.LEFT.isUp() && Button.RIGHT.isUp()) {
				pressed_since = 0;
			} else if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
				if (pressed_since == 0) {
					pressed_since = System.currentTimeMillis();
					if (Button.LEFT.isDown() && Button.RIGHT.isUp() && value > 0) {
						value = value - step;
					} else if (Button.LEFT.isUp() && Button.RIGHT.isDown()) {
						value = value + step;
					}
				} else if (System.currentTimeMillis() - pressed_since > waitBetween) {
					if (Button.LEFT.isDown() && Button.RIGHT.isUp() && value > 0) {
						value = value - step;
					} else if (Button.LEFT.isUp() && Button.RIGHT.isDown()) {
						value = value + step;
					}
					try {
						Thread.sleep(175);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		return value;
	}

}
