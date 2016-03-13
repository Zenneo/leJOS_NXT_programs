package vehicle;

import lejos.nxt.Battery;

// This thread draws the current status onto the screen
public class LCDthread extends Thread {

	// vars
	public int refreshRate = 500;

	private Position pos;
	private Movement move;
	private Arm arm;

	public LCDthread(Position posi, Movement moves, Arm arms) {
		pos = posi;
		move = moves;
		arm = arms;
	}

	public void run() {

		// Main loop
		while (!Thread.currentThread().isInterrupted()) {
			arm_test.LCDscreens.checkedClear();

			// line 0
			arm_test.LCDscreens.checkedDraw("--- Vehicle  ---", 0, 0);

			// line 1
			arm_test.LCDscreens.checkedDraw("Tsk: " + taskMsg(Main.getCurrent_task()), 0, 1);

			// line 2
			arm_test.LCDscreens.checkedDraw("Pos: " + posMsg(pos.getVehicle_position()), 0, 2);

			// line 3
			arm_test.LCDscreens.checkedDraw("Move: " + movementMsg(move.getVehicle_movement()), 0, 3);

			// line 4
			arm_test.LCDscreens.checkedDraw("Speed: " + move.getSpeed(), 0, 4);

			// line 5
			arm_test.LCDscreens.checkedDraw("---   Arms   ---", 0, 5);

			// line 6
			arm_test.LCDscreens.checkedDraw("A1:" + arm.mrotate_pos() + " A2:" + arm.marm_pos(), 0, 6);

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

		// Draw exit message
		arm_test.LCDscreens.checkedClear();
		arm_test.LCDscreens.checkedDraw("Exit", 0, 0);

	}

	private String taskMsg(int taskint) {
		// 11 letters maximum length

		switch (taskint) {
		case 0:
			return "Initialize";
		case 1:
			return "To Stat1";
		case 2:
			return "To Stat2";
		case 3:
			return "Wait";
		case 4:
			return "Load pkg";
		case 5:
			return "Unload pkg";
		default:
			throw new UnsupportedOperationException();
		}
	}

	private String movementMsg(int moveint) {
		// 10 letters maximum length

		switch (moveint) {
		case 0:
			return "Unknown";
		case 1:
			return "Still";
		case 2:
			return "Forward";
		case 3:
			return "Backward";
		default:
			throw new UnsupportedOperationException();
		}
	}

	private String posMsg(int posint) {
		// 11 letters maximum length

		switch (posint) {
		case 0:
			return "Unknown";
		case 1:
			return "Stat1";
		case 2:
			return "Stat2";
		case 3:
			return "Between";
		default:
			throw new UnsupportedOperationException();
		}
	}

}
