package vehicle;

import arm_test.Arm_rotate;
import lejos.nxt.Button;

public class EXITthread extends Thread {
	int mode;
	
	public EXITthread(int mode_int) {
		mode = mode_int;
	}
	
	public void run() {
		while (Button.ESCAPE.isUp()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		switch (mode) {
		case 1:
			Main.exitProgram();
			this.interrupt();
		case 2:
			try {
				Arm_rotate.exitProgram();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.interrupt();
		}
	}
}
