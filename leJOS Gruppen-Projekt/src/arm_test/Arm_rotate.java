package arm_test;

import lejos.nxt.Battery;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.util.Delay;

public class Arm_rotate {

	// motors
	private static NXTRegulatedMotor motor;

	// runtime integers
	private static int wait_between_press = 1000; // in milliseconds
	private static int speed = 0; // rotation speed
	private static int acceleration = 0; // motor acceleration
	private static int stalled_error = 0; // error until detected as stalled
	private static int stalled_time = 0; // time until recognized as stalled

	private static int current_task = 0;
	private static int rotateTarget = 0;
	private static int rotateTime = 1000;
	private static long targetTime = 0;
	private static char motorPort = 'B';

	public static void main(String[] args) {

		// initial notice
		LCDscreens.checkedDraw("--General info--", 0, 0);
		LCDscreens.checkedDraw("Accel and speed", 0, 2);
		LCDscreens.checkedDraw("in degree per", 0, 3);
		LCDscreens.checkedDraw("second.", 0, 4);
		LCDscreens.checkedDraw("Time in ms", 0, 5);
		LCDscreens.checkedDraw("Bat-Volt: " + Battery.getVoltage(), 0, 7);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			// screen: ask for motor port
			askForMotorPort();

			// screen: ask for speed
			speed = LCDscreens.askForValue("Speed", 75, 5, wait_between_press, false);

			// screen: ask for acceleration
			acceleration = LCDscreens.askForValue("Acceleration", 200, 25, wait_between_press, false);

			// screen: ask for stalled error
			stalled_error = LCDscreens.askForValue("Stall error", 2, 1, wait_between_press, false);

			// screen: ask for stalled time
			stalled_time = LCDscreens.askForValue("Stall time", 50, 25, wait_between_press, false);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(1);
		}

		// set determined values
		motor.setSpeed(speed);
		motor.setAcceleration(acceleration);
		motor.setStallThreshold(stalled_error, stalled_time);

		boolean interruptTask = false;

		// main loop
		while (true) {
			drawStats();
			Delay.msDelay(500); // sleep to prevent double exit

			if ((motor.getPosition() == rotateTarget && targetTime < System.currentTimeMillis()) || motor.isStalled()
					|| interruptTask) {

				// ask for Task
				try {
					current_task = LCDscreens.MultipleChoice("Enter task:", "Target angle", "Rotate time", "Show stats",
							"Floating mode", "Change values", "Change motor", "Exit", current_task);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
					System.exit(1);
				}

				switch (current_task) {
				case 1: // rotate to target angle
					// dividing by 2 and multiplying by two ensures an even
					// number
					try {
						rotateTarget = LCDscreens.askForValue("Tar angle", motor.getPosition() / 2 * 2, 2,
								wait_between_press, true);
						motor.rotateTo(rotateTarget, true);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						interruptTask = true;
					}
					break;
				case 2: // rotate for a specific amount of time
					try {
						int orientation = LCDscreens.MultipleChoice("Direction:", "Forward", "Backward", 1);
						rotateTime = LCDscreens.askForValue("Time in ms", rotateTime, 250, wait_between_press, false);
						targetTime = System.currentTimeMillis() + rotateTime;
						if (orientation == 1) {
							motor.forward();
						} else {
							motor.backward();
						}
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						interruptTask = true;
					}

					break;
				case 3: // show stats
					long show_until = System.currentTimeMillis() + 10000;
					while (show_until > System.currentTimeMillis() && Button.ESCAPE.isUp()) {
						drawStats();
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				case 4: // enter floating mode
					try {
						if (LCDscreens.askForConfirmation("Disable motor", "regulations for", "manual rotation?",
								true)) {
							motor.flt(true);
						}
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					interruptTask = true;
					break;
				case 5: // change values
					try {
						int value_for_change = LCDscreens.MultipleChoice("Value to change:", "Speed", "Acceleration",
								"Stall error", "Stall time", 1);
						if (value_for_change == 1) {
							speed = LCDscreens.askForValue("Speed", speed, 5, wait_between_press, false);
							motor.setSpeed(speed);
						} else if (value_for_change == 2) {
							acceleration = LCDscreens.askForValue("Acceleration", acceleration, 25, wait_between_press,
									false);
							motor.setAcceleration(acceleration);
						} else if (value_for_change == 3) {
							stalled_error = LCDscreens.askForValue("Stall error", stalled_error, 1, wait_between_press,
									false);
							motor.setStallThreshold(stalled_error, stalled_time);
						} else if (value_for_change == 4) {
							stalled_time = LCDscreens.askForValue("Stall time", stalled_time, 25, wait_between_press,
									false);
							motor.setStallThreshold(stalled_error, stalled_time);
						}
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					interruptTask = true;
					break;
				case 6: // change motor
					motor.flt(true);
					try {
						askForMotorPort();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						motor.stop(true);
					}
					interruptTask = true;
					break;
				case 7: // exit program
					try {
						if (LCDscreens.askForConfirmation("Exit program?", true)) {
							System.exit(0);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					throw new IllegalStateException("Menu option not existing");
				}

			} else {

				if (Button.ESCAPE.isDown() || Button.ENTER.isDown()) {
					motor.stop();
					interruptTask = true;
				} else if (current_task == 2 && targetTime < System.currentTimeMillis()) {
					motor.stop();
					interruptTask = true;
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

	private static void drawStats() {
		LCDscreens.checkedDraw("--- Motor " + motorPort + " ---", 0, 0);
		LCDscreens.checkedDraw("Speed: " + motor.getSpeed(), 0, 1);
		LCDscreens.checkedDraw("Accel: " + motor.getAcceleration(), 0, 2);
		LCDscreens.checkedDraw("Cur angle: " + motor.getTachoCount(), 0, 3);
		LCDscreens.checkedDraw("Stalled: " + motor.isStalled(), 0, 6);

		if (current_task == 1) { // currently rotating to target
			LCDscreens.checkedDraw("Tar angle: " + rotateTarget, 0, 4);
		} else if (current_task == 2) { // currently rotating for specified
										// amount of time
			LCDscreens.checkedDraw("Time left: " + (targetTime - System.currentTimeMillis()), 0, 4);
		}
	}

	private static void askForMotorPort() throws InterruptedException {
		int initial_port;
		if (motorPort == 'A') {
			initial_port = 1;
		} else if (motorPort == 'B') {
			initial_port = 2;
		} else {
			initial_port = 3;
		}
		int motor_Port = 0;
		motor_Port = LCDscreens.MultipleChoice("Choose motor:", "Port A", "Port B", "Port C", initial_port);

		switch (motor_Port) {
		case 1:
			motor = Motor.A;
			motorPort = 'A';
			break;
		case 2:
			motor = Motor.B;
			motorPort = 'B';
			break;
		case 3:
			motor = Motor.C;
			motorPort = 'C';
			break;
		default:
			throw new IllegalArgumentException("Invalid motor port");
		}

		// set determined values
		motor.setSpeed(speed);
		motor.setAcceleration(acceleration);
		motor.setStallThreshold(stalled_error, stalled_time);

		motor.stop(true); // leave float mode
	}

}
