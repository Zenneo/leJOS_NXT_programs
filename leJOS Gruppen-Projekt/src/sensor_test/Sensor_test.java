package sensor_test;

import arm_test.LCDscreens;
import lejos.nxt.Battery;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import vehicle.ExitListener;

public class Sensor_test {

	// --test program for testing a distance sensor

	// runtime consts
	private static final int refreshEvery = 250; // in ms

	// status vars
	private static SensorPort sensorPort;

	public static void main(String[] args) {
		// Allow instant program exit with Back-Key
		Button.ESCAPE.addButtonListener(new ExitListener());

		// Initial screen
		LCDscreens.checkedDraw("-Us-Sensor Test-", 0, 0);
		LCDscreens.checkedDraw("Connect sensor", 0, 2);
		LCDscreens.checkedDraw("to any port.", 0, 3);
		LCDscreens.checkedDraw("Use orange to", 0, 4);
		LCDscreens.checkedDraw("reset min/max", 0, 5);
		Delay.msDelay(4000);
		LCDscreens.checkedClear();

		// Ask for sensor port
		askForSensorPort();
		LCDscreens.checkedClear();

		// create Distance object
		Distance dist = new Distance(sensorPort);

		// -main loop
		while (true) {
			// --line 0
			LCDscreens.checkedDraw("-Us-Sensor Test-", 0, 0);
			// --line 1
			LCDscreens.checkedDraw("Reset min/max", 0, 1);
			// --line 2
			LCDscreens.checkedDraw("with orange.", 0, 2);
			// --line 3
			LCDscreens.checkedDraw("Dist: " + dist.getCurDistance(), 0, 3);
			// --line 4
			LCDscreens.checkedDraw("Min-D:" + dist.getMinDistance(), 0, 4);
			// --line 5
			LCDscreens.checkedDraw("Max-D:" + dist.getMaxDistance(), 0, 5);
			// --line 7
			LCDscreens.checkedDraw("Bat-Volt: " + Battery.getVoltage(), 0, 7);

			// reset Min/Max if orange is pressed
			if (Button.ENTER.isDown()) {
				dist.resetMinMaxDistance();
			}

			// sleep between refreshs
			Delay.msDelay(refreshEvery);
		}
	}

	private static void askForSensorPort() {
		try {
			switch (LCDscreens.MultipleChoice("Choose sensor:", "S1", "S2", "S3", "S4", 1)) {
			case 1:
				sensorPort = SensorPort.S1;
				break;
			case 2:
				sensorPort = SensorPort.S2;
				break;
			case 3:
				sensorPort = SensorPort.S3;
				break;
			case 4:
				sensorPort = SensorPort.S4;
			default:
				throw new IllegalArgumentException("Invalid sensor port");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
