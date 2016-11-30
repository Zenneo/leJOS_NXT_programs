package sensor_test;

import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class Distance {

	// consts
	private UltrasonicSensor sensor;
	
	// status vars
	private int minDistance;
	private int maxDistance;
	public int getMinDistance() {
		return minDistance;
	}
	public int getMaxDistance() {
		return maxDistance;
	}
	
	
	public Distance(SensorPort sonic) {
		sensor = new UltrasonicSensor(sonic);
		resetMinMaxDistance();
	}
	
	public void resetMinMaxDistance() {
		minDistance = 255;
		maxDistance = 255;
	}
	
	public int getCurDistance() {
		int distance = sensor.getDistance();
		
		// update min/max distance
		if (minDistance == 255 || distance < minDistance) {
			minDistance = distance;
		}
		if (maxDistance == 255 || distance > maxDistance) {
			maxDistance = distance;
		}
		
		// finally return measured distance
		return distance;
	}
}
