package rotationStation;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class BoxDetect {
	private final UltrasonicSensor sensor;
	
	public BoxDetect(SensorPort port) {
		sensor = new UltrasonicSensor(port);
	}
	
	public synchronized boolean isBox() {
		int distance = sensor.getDistance();
		
		if (distance < 15) { // distance value has to be measured!
			return true;
		} else {
			return false;
		}
	}
}
