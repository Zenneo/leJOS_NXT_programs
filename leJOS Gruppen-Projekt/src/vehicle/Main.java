package vehicle;
//imports necessary leJos packages
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;
import vehicle.Movement;

public class Main {

	/* --Connected Peripherals-- */
	// -bluetooth
	// connects with station 1 (Timur & Lukas)
	private static final BTClient BTfactory = new BTClient("verladen", 500);
	// connects with rotationStation
	private static final BTClient BTrotStat = new BTClient("drehStation", 250);
	private static final BlueComm bluecomm = new BlueComm("verladen", 5000);
	// -sensors
	// creates objects for touch sensor
	private static final TouchSensor touch1 = new TouchSensor(SensorPort.S1);
	private static final TouchSensor touch2 = new TouchSensor(SensorPort.S2);
	private static final boolean touchOrientation = true; // see vehicle.position		
	private static final Position Pos = new Position(touch1, touch2, touchOrientation);

	// -engines
	// creates objects for engines
	private static final NXTRegulatedMotor engine = Motor.A; // moves vehicle
	// deliveres the main engine to class Movement
	private static final Movement Move = new Movement(engine);
	private static final NXTRegulatedMotor arm1 = Motor.B; // Rotates arm
	private static final NXTRegulatedMotor arm2 = Motor.C; // Lifts arm
	// deliveres engines B and C to class Arm
	private static final Arm Arms = new Arm(arm1, arm2);

	/* --Logic vars-- */
	// station that delivers package
	// delivering and receiving stations can be swapped if necessary
	private static final int delivering_station = 1;
	// station that receives package
	private static final int receiving_station = 2;

	/* --Status Codes-- */
	// shows current position/situation
	private static int current_task = 0;

	// what does a current_task number mean?
	// 0 - initialize
	// 1 - move to station 1
	// 2 - move to station 2
	// 3 - wait
	// 4 - load vehicle
	// 5 - unload vehicle

	// returns current_task
	public static int getCurrent_task() {
		return current_task;
	}

	// change current_task
	public static void setCurrent_task(int current_task) {
		Main.current_task = current_task;
	}

	// LCDthread object for drawing screen outputs
	private static Thread LCDthreadobj = new LCDthread(Pos, Move, Arms);

	// main method
	public static void main(String[] args) throws InterruptedException {
		// set up exit listener (if you press the dark grey button, everything stops
		Button.ESCAPE.addButtonListener(new ExitListener());

		// ask for debugging mode
		enterDebuggingMode();

		// sets priority
		LCDthreadobj.setPriority(Thread.NORM_PRIORITY - 2);

		// starts LCD display
		LCDthreadobj.start();

		// vehicle initialisation
		setCurrent_task(0);
		Arms.rotateToInitial();

		// Main Loop
		while (true) {
			switch (Move.getVehicle_movement()) { // Check current vehicle status
			// Vehicle still (no movement)
			case 1:
				// checks if Vehicle is at delivering station
				if (Pos.getVehicle_position() == delivering_station) {
					Arms.receive_package_phase1();
					BTfactory.waitForPackage();
					Delay.msDelay(1000); // wait to ensure that the box has slided down
					// receive package from station
					setCurrent_task(4);
					Arms.receive_package_phase2();

					// wait until rotation station was freed
					setCurrent_task(6);
					BTrotStat.waitForPackage();

					// move to receiving station
					setCurrent_task(receiving_station); //changes current to task to move to receiving station
					Move.moveToStation(receiving_station); //commands Move to move to receiving station

				}
				// checks if Vehicle is at receiving station
				else if (Pos.getVehicle_position() == receiving_station) {
					// deliver package to station
					setCurrent_task(5);
					Arms.deliver_package();

					// move to delivering station
					setCurrent_task(delivering_station); //changes current to task to move to delivering station
					Move.moveToStation(delivering_station); //commands Move to move to delivering station

				} else if (Pos.getVehicle_position() == 0) { // checks if Position is unknown
					Pos.checkPosition(); // checks the current position
				} else if (Pos.getVehicle_position() == 3) { // Vehicle between
																// stations
					if (getCurrent_task() == 0) {
						Move.moveToStation(delivering_station);
					}
					// else NOP

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
				RConsole.openAny(0);
				i = 0;
			}
			Thread.sleep(1000);
		}
	}

}
