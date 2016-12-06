package rotationStation;

import lejos.nxt.Battery;

// This thread draws the current status onto the screen
public class LCDthread extends Thread {

	// vars
	public int refreshRate = 1000; // ms between each value update

	private Motors motors;

	public LCDthread(Motors motor) {
		this.motors = motor;
	}

	public void run() {
		arm_test.LCDscreens.checkedClear();

		// Main loop
		while (true) {

			// line 0
			arm_test.LCDscreens.checkedDraw("--- Rotator  ---", 0, 0);

			// line 1

			// line 2
			arm_test.LCDscreens.checkedDraw("Tsk: " + taskMsg(Main.getCurrent_task()), 0, 2);

			// line 3
			arm_test.LCDscreens.checkedDraw("MPos: " + motors.m_pos(), 0, 3);

			// line 4
			arm_test.LCDscreens.checkedDraw("Orient: " + orientationMsg(motors.is0degree()), 0, 4);

			// line 5
			arm_test.LCDscreens.checkedDraw("Stalled: " + motors.hasStalled(), 0, 5);

			// line 6

			// line 7
			arm_test.LCDscreens.checkedDraw("Bat-Volt: " + Battery.getVoltage(), 0, 7);

			// sleep to allow more CPU time for main thread
			try {
				Thread.sleep(refreshRate);
			} catch (InterruptedException e) {
				e.printStackTrace();
				this.interrupt();
			}
		}

	}

	private String taskMsg(int taskint) {
		// 11 letters maximum length

		switch (taskint) {
		case 0:
			return "Initialise";
		case 1:
			return "Rotate Pos1";
		case 2:
			return "Rotate Pos2";
		case 3:
			return "Wait sensor";
		default:
			throw new UnsupportedOperationException("Unknown task message ID");
		}

	}

	private String orientationMsg(boolean isNullDegree) {
		if (isNullDegree) {
			return "1";
		} else {
			return "2";
		}
	}

}
