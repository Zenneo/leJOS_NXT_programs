package rotationStation;

import vehicle.ExitListener;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class Main {

	/* --Connected Peripherals-- */

	// -engines
	private static final NXTRegulatedMotor engine = Motor.A; // moves vehicle
	private static final Motors motor = new Motors(engine);

	// -sensors
	private static final TouchSensor touch1 = new TouchSensor(SensorPort.S1);
	private static final BoxDetect bdetect = new BoxDetect(SensorPort.S2);

	// -bluetooth server
	private static final BTServer btserver = new BTServer(bdetect, 250);

	/* --Status Codes-- */
	private static int current_task = 0;

	// 0 - initialize
	// 1 - rotate to pos 1
	// 2 - rotate to pos 2
	// 3 - wait for sensors

	/* --Runtime constants-- */
	// time in ms between sensor checks
	private static final int sleepSensorCheck = 500;
	// time in ms to wait after vehicle left
	private static final int sleepVehicleLeave = 3000;

	public static int getCurrent_task() {
		return current_task;
	}

	public static void setCurrent_task(int current_task) {
		Main.current_task = current_task;
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		// set up exit listener
		Button.ESCAPE.addButtonListener(new ExitListener());

		// ask for debugging mode
		enterDebuggingMode();

		// BT thread
		btserver.setPriority(Thread.NORM_PRIORITY - 1);
		btserver.start();

		// BT thread
		btserver.setPriority(Thread.NORM_PRIORITY - 1);
		btserver.start();
		// LCD thread
		Thread LCDthreadobj = new LCDthread(motor);
		LCDthreadobj.setPriority(Thread.NORM_PRIORITY - 2);
		LCDthreadobj.start();

		// initialise
		setCurrent_task(0);
		motor.initMotor();

		// main loop
		while (true) {
			// TODO finish this loop
			setCurrent_task(3);
			// once vehicle arrived
			if (touch1.isPressed()) {
				RConsole.println("STATUS: Vehicle arrived");

				// wait for vehicle to leave
				while (touch1.isPressed()) {
					Delay.msDelay(sleepSensorCheck);
				}
				// delay afterwards
				Delay.msDelay(sleepVehicleLeave);

				RConsole.println("STATUS: Vehicle left");

				// now do the U-Turn
				setCurrent_task(1);
				motor.doUTurn();
			}

			// if vehicle is not there yet
			else {
				Delay.msDelay(sleepSensorCheck);
			}
		}

	}

	// This function will activate RemoteDebugging after a prompt
	private static void enterDebuggingMode() throws InterruptedException {
		// print LCD prompt
		LCD.clear();
		Thread.sleep(1000);
		LCD.drawString("Press orange to", 0, 0);
		LCD.drawString("connect console.", 0, 1);
		LCD.drawString("Skip timer with", 0, 3);
		LCD.drawString("right.", 0, 4);

		for (int i = 5; i > 0; i--) {
			LCD.clear(6);
			LCD.drawString(i + " seconds left", 0, 6);
			if (Button.RIGHT.isDown()) {
				LCD.clear();
				return;
			} else if (Button.ENTER.isDown()) {
				LCD.clear();
				RConsole.openAny(0);
				i = 0;
			}
			Thread.sleep(1000);
		}
	}
}
