package vehicle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;

public class BlueComm {

	// bluetooth codes
	final byte askForPackage = 2;
	final byte packageWasDelivered = 7;
	// constant vars
	final String btname;
	final int waitBetweenSends;
	final int maxSendAttempts = 5;

	// bluetooth vars
	RemoteDevice btrd;
	BTConnection btc;
	DataInputStream dis;
	DataOutputStream dos;
	// status vars
	boolean btcIsEstablished = false;

	public BlueComm(String btname, int waitBetweenSends) {
		this.btname = btname;
		this.waitBetweenSends = waitBetweenSends;
	}

	public boolean waitForPackage() {
		// var for interrupt code
		boolean stage_1 = false;

		// var for counting send attempts
		int attempts = 0;

		// reconnect or connect if not done already
		connectToDevice();
		boolean packageReceived = false;
		Main.setCurrent_task(3);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		RConsole.println("[BT] Initiated package check");

		while (!packageReceived) {
			if (attempts < maxSendAttempts) {
				try {
					Main.setCurrent_task(3);
					// check if package has been received
					dos.writeShort(askForPackage);
					dos.flush();

					attempts++;
					RConsole.println("[BT] Attempt " + attempts);

					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if (dis.readShort() == packageWasDelivered) {
						packageReceived = true;
						RConsole.println("[BT] Package was delivered");
					} else {
						packageReceived = false;
						RConsole.println("[BT] Package check - no response");
					}
					// sleep between sends
					try {
						Thread.sleep(waitBetweenSends);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IOException e) {
					// e.printStackTrace();
					closeConnection();
					connectToDevice();
				} catch (NullPointerException e2) {
					// e.printStackTrace();
					closeConnection();
					connectToDevice();
				}

			} else {
				closeConnection();
				connectToDevice();
			}

			// skip check through key combo
			if (Button.RIGHT.isDown()) {
				stage_1 = true;
			} else if (stage_1 && Button.LEFT.isDown()) {
				packageReceived = true;
				RConsole.println("[BT] User skipped check");
			}

		}

		closeConnection();

		return true;
	}

	void connectToDevice() {
		while (!btcIsEstablished) {
			Main.setCurrent_task(10); // establishing connection

			try {
				RConsole.println("[BT] Establishing connection with: " + btname);
				btrd = Bluetooth.getKnownDevice(btname);

				// given device name is unknown
				if (btrd == null) {
					RConsole.println("[BT] Device " + btname + " unknown!");
					throw new BluetoothStateException("Device " + btname
							+ " unknown!");
				}

				btc = Bluetooth.connect(btrd);
				// connection failed
				if (btc == null) {
					RConsole.println("[BT] Can't connect to " + btname + "!");
					throw new BluetoothStateException("Can't connect to "
							+ btname + "!");
				}

				dis = btc.openDataInputStream();
				dos = btc.openDataOutputStream();

				RConsole.println("[BT] Connected to " + btname);
				btcIsEstablished = true;

			} catch (BluetoothStateException e) {
				// e.printStackTrace();
				btcIsEstablished = false;
			}

			try {
				Thread.sleep(waitBetweenSends);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	void closeConnection() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			dis.close();
			dos.close();
			btc.close();
		} catch (IOException e) {
			// e.printStackTrace();
		} catch (NullPointerException e2) {
			// e2.printStackTrace();
		}

		dis = null;
		dos = null;
		btc = null;
	}
}