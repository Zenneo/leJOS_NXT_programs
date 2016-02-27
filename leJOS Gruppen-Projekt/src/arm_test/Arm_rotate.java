package arm_test;

import lejos.nxt.Battery;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

public class Arm_rotate {

	// motors
	private static NXTRegulatedMotor motor;

	// runtime integers
	private static int wait_between_press = 1000; // in milliseconds
	private static int speed; // rotation speed
	private static int acceleration; // motor acceleration
	private static int stalled_error; // error until detected as stalled
	private static int stalled_time; // time until recognized as stalled

	private static int rotateTarget = 0;

	public static void main(String[] args) {
		// initial notice
		LCD.drawString("--General info--", 0, 0);
		LCD.drawString("Accel and speed", 0, 2);
		LCD.drawString("in degree per", 0, 3);
		LCD.drawString("second.", 0, 4);
		LCD.drawString("Time in ms", 0, 5);
		LCD.drawString("Bat-Volt: " + Battery.getVoltage(), 0, 7);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// screen: ask for motor port
		int motor_port = LCDscreens.MultipleChoice("Motor A", "Motor B", "Motor C", 2);
		switch (motor_port) {
		case 1:
			motor = Motor.A;
			break;
		case 2:
			motor = Motor.B;
			break;
		case 3:
			motor = Motor.C;
			break;
		}

		// screen: ask for speed
		speed = LCDscreens.askForValue("Speed", 75, 5, wait_between_press, false);

		// screen: ask for acceleration
		acceleration = LCDscreens.askForValue("Acceleration", 200, 25, wait_between_press, false);

		// screen: ask for stalled error
		stalled_error = LCDscreens.askForValue("Stall error", 1, 1, wait_between_press, false);

		// screen: ask for stalled time
		stalled_time = LCDscreens.askForValue("Stall time", 50, 25, wait_between_press, false);

		// set determined values
		motor.setSpeed(speed);
		motor.setAcceleration(acceleration);
		motor.setStallThreshold(stalled_error, stalled_time);

		boolean exit_program = false;

		// main loop
		while (!exit_program) {
			if (Button.ESCAPE.isDown()) {
				exit_program = true;
			}

			LCD.clear();
			LCD.drawString("Speed: " + speed, 0, 1);
			LCD.drawString("Accel: " + acceleration, 0, 2);
			LCD.drawString("Cur angle: " + motor.getPosition(), 0, 3);
			LCD.drawString("Stalled: " + motor.isStalled(), 0, 4);

			if (motor.getPosition() == rotateTarget || motor.isStalled()) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (LCDscreens.askForConfirmation("Enter a target", "angle?", true)) {
					// dividing by 2 and multiplying by two ensures an even
					// number
					rotateTarget = LCDscreens.askForValue("Tar angle", motor.getPosition() / 2 * 2, 2,
							wait_between_press, true);
					motor.rotateTo(rotateTarget, true);
				} else {
					if (LCDscreens.askForConfirmation("Exit program", false)) {
						exit_program = true;
					}
				}

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

}
