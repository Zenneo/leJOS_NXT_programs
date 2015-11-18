import lejos.nxt.*;

//Motor A und B für die Räder
//Beim Drehen Motor A vorwärts, Motor B rückwärts
//Sensor 1 für den Ultraschallsensor

public class avoidWhenClose {
	public static void main(String[] args) throws Exception {
		UltrasonicSensor sonic = new UltrasonicSensor(SensorPort.S1);
		
		while (!Button.ESCAPE.isDown()) {
			avoidWhenClose.setSpeedTotal(1000);
			while (sonic.getDistance() > 25) {
				avoidWhenClose.forwardFahren();
				
				//Exit while loop
				if (Button.ESCAPE.isDown()) {
					break;
				}
			}
			
			avoidWhenClose.setSpeedTotal(200);
			while (sonic.getDistance() < 70) {
                                avoidWhenClose.rechtsDrehen();
				
				//Exit while loop
				if (Button.ESCAPE.isDown()) {
					break;
				}
			}
		}
		Motor.A.stop();
		Motor.B.stop();
	}
	
	private static void rechtsDrehen() throws Exception {
		Motor.A.backward();
		Motor.B.forward();
		Thread.sleep(100);
	}
	
	private static void forwardFahren() {
		Motor.A.forward();
		Motor.B.forward();
	}
	
	private static void setSpeedTotal(int speed) {
		Motor.A.setSpeed(speed);
		Motor.B.setSpeed(speed);
	}

}