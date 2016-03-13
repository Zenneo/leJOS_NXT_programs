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
	private static int delivering_station = 1;
	// station that receives package
	private static int receiving_station = 2;

	/* --Status Codes-- */
	private static int current_task = 0;

	// 0 - initialize
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
	private static Thread LCDthreadobj = new LCDthread(Pos, Move, Arms);

	public static void main(String[] args) throws InterruptedException {
		// set up exit listener
		Button.ESCAPE.addButtonListener(new ExitListener());

		// ask for debugging mode
		enterDebuggingMode();

		LCDthreadobj.setPriority(Thread.NORM_PRIORITY - 1);
		LCDthreadobj.start();

		// vehicle initialization
		Arms.rotateToInitial();

		// Main Loop
		while (true) {
			switch (Move.getVehicle_movement()) { // Check current vehicle
													// status

			// Vehicle still (no movement)
			case 1:
				// Vehicle at delivering station
				if (Pos.getVehicle_position() == delivering_station) {
					// TODO check if package can be received
					waitForTrigger();

					// receive package from station
					setCurrent_task(4);
					Arms.receive_package();

					// move to receiving station
					setCurrent_task(receiving_station);
					Move.moveToStation(receiving_station);

				}
				// Vehicle at delivering station
				else if (Pos.getVehicle_position() == receiving_station) {
					// TODO check if package can be delivered
					waitForTrigger();

					// deliver package to station
					setCurrent_task(5);
					Arms.deliver_package();

					// move to delivering station
					setCurrent_task(delivering_station);
					Move.moveToStation(delivering_station);

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
					// NOP
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
				} else if (Pos.getVehicle_position() == 3) { // in between
					// NOP
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

			// Reduce CPU load
			Thread.sleep(50);

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

}
