import lejos.nxt.*;

// Wenn sensor 1 kleiner als sensors 2, dann Motor a forward, Motor B backwards

public class driveWithSensors {
	private static int waitBetweenCommands = 100; // Pause zwischen Kommandos in
													// Millisekunden
	private static int distanceClose = 25; // Distanz, ab der der sich der Roboter
											// umdreht
	private static int distanceFar = 50; // Distanz, die erreicht werden muss,
											// damit der Roboter weiterfährt
	private static int speedForward = 1000; // Geschwindigkeit beim
											// Vorwärtsfahren
	private static int speedTurning = 150; // Geschwindigkeit beim Umdrehen

	private static UltrasonicSensor sonic1 = new UltrasonicSensor(SensorPort.S1);
	private static UltrasonicSensor sonic2 = new UltrasonicSensor(SensorPort.S2);
	private static NXTRegulatedMotor motor1 = Motor.A;
	private static NXTRegulatedMotor motor2 = Motor.B;
	private static long timeSinceRedraw = System.currentTimeMillis();

	public static void main(String[] args) throws Exception {
		while (!Button.ESCAPE.isDown()) {
			// Vorwärtsfahren
			setSpeedTotal(speedForward);
			while (sonic1.getDistance() > distanceClose
					&& sonic2.getDistance() > distanceClose) {
				drawLCD(1);
				goForward();

				// Exit while loop
				if (Button.ESCAPE.isDown()) {
					break;
				}
			}

			// Umdrehen
			setSpeedTotal(speedTurning);
			while (sonic1.getDistance() < distanceFar
					&& sonic2.getDistance() < distanceFar) {
				if (sonic1.getDistance() > sonic2.getDistance()) {
					turnLeft();
				} else {
					turnRight();
				}

				// Exit while loop
				if (Button.ESCAPE.isDown()) {
					break;
				}
			}
		}
		drawLCD(0);
		stopMotors();
		Thread.sleep(2000);
	}

	private static void turnLeft() throws InterruptedException {
		motor1.forward();
		motor2.backward();
		drawLCD(2);
		Thread.sleep(waitBetweenCommands);
	}

	private static void turnRight() throws InterruptedException {
		motor1.backward();
		motor2.forward();
		drawLCD(3);
		Thread.sleep(waitBetweenCommands);
	}

	private static void goForward() throws InterruptedException {
		motor1.forward();
		motor2.forward();
		Thread.sleep(waitBetweenCommands);
	}

	private static void setSpeedTotal(int speed) {
		motor1.setSpeed(speed);
		motor2.setSpeed(speed);
	}

	private static void stopMotors() {
		motor1.stop();
		motor2.stop();
	}

	private static void drawLCD(int state) { // 1=Vorwärts, 2=Linksdrehen,
												// 3=Rechtsdrehen

		// Bildschirm wird nur alle 500ms neugezeichnet
		if (System.currentTimeMillis() - timeSinceRedraw > 500) { 
			timeSinceRedraw = System.currentTimeMillis();
			// aktueller Zustand
			String current;
			if (state == -1) {
				current = "Init";
			} else if (state == 1) {
				current = "Vorne";
			} else if (state == 2) {
				current = "Links";
			} else if (state == 3) {
				current = "Rechts";
			} else {
				current = "Beenden";
			}
			// tatsächliches Zeichnen auf dem Bildschirm
			LCD.clear();
			LCD.drawString("Distanz 1: " + sonic1.getDistance(), 0, 0);
			LCD.drawString("Distanz 2: " + sonic2.getDistance(), 0, 1);
			LCD.drawString("Aktuell: " + current, 0, 2);
			LCD.drawString("Speed 1: " + motor1.getRotationSpeed(), 0, 3);
			LCD.drawString("Speed 2: " + motor2.getRotationSpeed(), 0, 4);
		}
	}
}
