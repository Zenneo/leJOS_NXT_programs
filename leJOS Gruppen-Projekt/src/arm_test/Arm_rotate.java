package arm_test;

import lejos.nxt.Battery;
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
	private static int stalled_error; // error until detected as stalled
	private static int stalled_time; // time until recognized as stalled
	
	private static int rotateTarget = 0;

	
	public static void main(String[] args) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// screen: ask for speed
		speed = askForValue("Speed", 75, 5, wait_between_press, false);
		
		// screen: ask for acceleration
		acceleration = askForValue("Acceleration", 200, 25, wait_between_press, false);
		
		// screen: ask for stalled error
		stalled_error = askForValue("Stall error", 1, 1, wait_between_press, false);
		
		// screen: ask for stalled time
		stalled_time = askForValue("Stall time", 50, 25, wait_between_press, false);
		
		
		// set determined values
		motor.setSpeed(speed);
		motor.setAcceleration(acceleration);
		motor.setStallThreshold(stalled_error, stalled_time);
		

		// main loop
		while (Button.ESCAPE.isUp()) {
			LCD.clear();
			LCD.drawString("Speed: " + speed, 0, 1);
			LCD.drawString("Accel: " + acceleration, 0, 2);
			LCD.drawString("Cur angle: " + motor.getPosition(), 0, 3);
			LCD.drawString("Stalled: " + motor.isStalled(), 0, 4);
			LCD.drawString("Bat-Volt: " + Battery.getVoltage(), 0, 7);
			
			if (motor.getPosition() == rotateTarget || motor.isStalled()) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// dividing by 2 and multiplying by two ensures an even number
				rotateTarget = askForValue("Tar angle", motor.getPosition() / 2 * 2, 2, wait_between_press, true);
				motor.rotateTo(rotateTarget, true);
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

	
	private static int askForValue(String name, int initial, int step, int waitBetween, boolean allowNegative) {
		LCD.clear();
		LCD.drawString(name + ":", 0, 0);
		LCD.drawString("Confirm with", 0, 3);
		LCD.drawString("orange.", 0, 4);
		
		int value = initial;
		long pressed_since = 0;
		int cur_step = step;
		
		while (!Button.ENTER.isDown()) {
			LCD.clear(1);
			LCD.drawString("<< " + value + " >>", 0, 1);
			if (Button.LEFT.isUp() && Button.RIGHT.isUp()) {
				pressed_since = 0;
			} else if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
				if (pressed_since == 0) {
					pressed_since = System.currentTimeMillis();
					cur_step = step;
					if (Button.LEFT.isDown() && Button.RIGHT.isUp() && (value > 0 || allowNegative)) {
						value = value - cur_step;
					} else if (Button.LEFT.isUp() && Button.RIGHT.isDown()) {
						value = value + cur_step;
					}
				} else if (System.currentTimeMillis() - pressed_since > waitBetween) {
					if (Button.LEFT.isDown() && Button.RIGHT.isUp() && (value > 0 || allowNegative)) {
						value = value - cur_step;
					} else if (Button.LEFT.isUp() && Button.RIGHT.isDown()) {
						value = value + cur_step;
					}
					
					if (System.currentTimeMillis() - pressed_since > waitBetween * 5) {
						cur_step = step * 4;
					}
				}
			}
			
			try {
				Thread.sleep(175);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}
}
