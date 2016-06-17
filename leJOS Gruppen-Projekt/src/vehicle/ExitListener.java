package vehicle;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;

public class ExitListener implements ButtonListener {

	public void buttonPressed(Button b) {
		LCD.clear();
		LCD.drawString("Program exit", 0, 0);
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);

	}

	public void buttonReleased(Button b) {
		// NOP
	}

}
