package vehicle;

import lejos.nxt.LCD;

// This thread draws the current status onto the screen
public class LCDthread extends Thread {
	
	private Position pos;
	private Movement move;
	
	public LCDthread(Position posi, Movement moves) {
		pos = posi;
		move = moves;
	}
	
	public void run() {
		
		
		// Main loop
		while (!Thread.currentThread().isInterrupted()) {
			LCD.clear();
			
			//line 0
			LCD.drawString("--Vehicle--", 0, 0);
			
			//line 1
			LCD.drawString("Tsk: " + taskMsg(Main.getCurrent_task()), 0, 1);
			
			//line 2
			LCD.drawString("Move: " + movementMsg(move.getVehicle_movement()) , 0, 2);
			
			//line 3
			LCD.drawString("Pos: " + posMsg(pos.getVehicle_position()), 0, 3);
			
			//line 4
			
			
			//sleep to allow more CPU time for main thread
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				this.interrupt();
			}
		}
		
		// Draw exit message
		LCD.clear();
		LCD.drawString("Exit", 0, 0);
		
	}
	

	private String taskMsg(int taskint) {
		// 11 letters maximum length
		
		switch (taskint) {
		case 0:
			return "Nothing";
		case 1:
			return "To Stat1";
		case 2:
			return "To Stat2";
		case 3:
			return "Wait";
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
