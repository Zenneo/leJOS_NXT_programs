package vehicle;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.RConsole;
import vehicle.Movement;

public class Main {

	/* --Connected Peripherals-- */
	// -sensors
	private static TouchSensor touch1 = new TouchSensor(SensorPort.S1);
	private static TouchSensor touch2 = new TouchSensor(SensorPort.S2);
	private static boolean touchOrientation = true; // see vehicle.position
	private static Position Pos = new Position(touch1, touch2, touchOrientation);

	// -engines
	private static NXTRegulatedMotor engine = Motor.A; // moves vehicle
	private static Movement Move = new Movement(engine);
	private static NXTRegulatedMotor arm1 = Motor.B; // Rotates arm
	private static NXTRegulatedMotor arm2 = Motor.C; // Lifts arm
	private static Arm Arms = new Arm(arm1, arm2);

	/* --Logic vars-- */
	// station that delivers package
	private static int deliver_station = 1;
	// station that receives package
	private static int receive_station = 2;

	/* --Status Codes-- */
	private static int current_task = 0;

	// 0 - nothing
	// 1 - move to station 1
	// 2 - move to station 2
	// 3 - wait
	// 4 - load vehicle
	// 5 - unload vehicle
	/**
	 * @return the current_task
	 */
	public static int getCurrent_task() {
		return current_task;
	}

	/**
	 * @param current_task
	 *            the current_task to set
	 */
	private static void setCurrent_task(int current_task) {
		Main.current_task = current_task;
	}

	// LCDthread object for drawing screen outputs
	private static Thread LCDthreadobj = new LCDthread(Pos, Move);

	// initiate program exit through exitProgram()
	private static boolean exitProgram = false;

	public static void main(String[] args) throws InterruptedException {
		enterDebuggingMode();

		LCDthreadobj.start();

		// vehicle initialization
		Arms.rotateToInitial();

		// Main Loop
		while (!exitProgram) {
			switch (Move.getVehicle_movement()) { // Check current vehicle
													// status

			// Vehicle still (no movement)
			case 1:
				if (Pos.getVehicle_position() == 1) { // Vehicle at station 1

					if (Pos.getVehicle_position() == deliver_station) {
						// TODO check if package can be received
						// TODO receive package from station
						setCurrent_task(4);
						waitForTrigger();
					} else if (Pos.getVehicle_position() == receive_station) {
						// TODO check if package can be delivered
						// TODO deliver package to station
						setCurrent_task(5);
						waitForTrigger();
					}

					// move to station 2
					setCurrent_task(2);
					Move.moveToStation(2);

				} else if (Pos.getVehicle_position() == 2) { // Vehicle at
																// station 2

					if (Pos.getVehicle_position() == deliver_station) {
						// TODO check if package can be received
						// TODO receive package from station
						waitForTrigger();
					} else if (Pos.getVehicle_position() == receive_station) {
						// TODO check if package can be delivered
						// TODO deliver package to station
						waitForTrigger();
					}

					// move to station 1
					setCurrent_task(1);
					Move.moveToStation(1);

				} else if (Pos.getVehicle_position() == 0) { // Position unknown
					Pos.checkPosition();
				} else if (Pos.getVehicle_position() == 3) { // Vehicle between
																// stations
					// NOP
				} else {
					throw new IllegalStateException();
				}
				break;

			// Vehicle moving forward
			case 2:
				if (Pos.getVehicle_position() == 1 || Pos.getVehicle_position() == 2) { // at
																						// station
					setCurrent_task(3);
					Move.stop();
				} else if (Pos.getVehicle_position() == 3) { // in between
					break;
				} else {
					Pos.checkPosition();
				}
				break;

			// Vehicle moving backward
			case 3:
				if (Pos.getVehicle_position() == 1 || Pos.getVehicle_position() == 2) { // at
																						// station
					setCurrent_task(3);
					Move.stop();
				} else if (Pos.getVehicle_position() == 3) {
					break;
				} else {
					Pos.checkPosition();
				}
				break;

			// Vehicle movement unknown
			case 0:
				Move.checkMovement();
				break;
			default:
				throw new UnsupportedOperationException();
			}

			// Exit if back button is pressed
			if (Button.ESCAPE.isDown()) {
				exitProgram();
			}

			// check for any changes
			Pos.checkPosition();
			Move.checkMovement();
		}
	}

	private static void waitForTrigger() {
		// DEBUG MSG
		RConsole.println("Waiting for trigger... ");

		// TODO Create a blocking function that releases when triggered
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
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

		for (int i = 10; i > 0; i--) {
			LCD.clear(6);
			LCD.drawString(i + " seconds left", 0, 6);
			if (Button.RIGHT.isDown()) {
				LCD.clear();
				return;
			} else if (Button.ENTER.isDown()) {
				LCD.clear();
				RConsole.open();
			}
			Thread.sleep(1000);
		}
	}

	public static void exitProgram() {
		exitProgram = true;
		LCDthreadobj.interrupt();
		RConsole.println("Exiting program...");
		RConsole.close();
	}
}
