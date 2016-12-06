package vehicle;

import java.io.DataInputStream;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;

public class BTClient {

	// bluetooth codes
	private final int packageWasDelivered = 7;
	// constant vars
	private final int waitBetweenSends;

	// bluetooth vars
	private final String btname;
	private RemoteDevice btrd;
	private BTConnection btc;
	private DataInputStream dis;
	// status vars
	private boolean btcIsEstablished = false;
	private boolean packageReceived = false;

	public BTClient(String btname, int waitBetweenSends) {
		this.btname = btname;
		this.waitBetweenSends = waitBetweenSends;
	}

	public void waitForPackage() {
		// reconnect or connect if not done already
		connectToDevice();
		Main.setCurrent_task(3);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		RConsole.println("[BT] Initiated package check");

		while (!packageReceived) {
			try {
				Main.setCurrent_task(3);

				try {
					Thread.sleep(250);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				if (dis.readInt() == packageWasDelivered) {
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

			// skip check through key combo
			if (Button.LEFT.isDown()) {
				packageReceived = true;
				RConsole.println("[BT] User skipped check");
			}

		}

		closeConnection();
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

				RConsole.println("[BT] Connected to " + btname);
				btcIsEstablished = true;

			} catch (BluetoothStateException e) {
				// e.printStackTrace();
				btcIsEstablished = false;
			}

			try {
				Thread.sleep(waitBetweenSends);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// skip check through key combo
			if (Button.LEFT.isDown()) {
				btcIsEstablished = true;
				packageReceived = true;
				RConsole.println("[BT] User skipped check");
			}
		}

	}

	void closeConnection() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			dis.close();
			btc.close();
		} catch (IOException e) {
			// e.printStackTrace();
		} catch (NullPointerException e2) {
			// e2.printStackTrace();
		}

		btcIsEstablished = false;

		dis = null;
		btc = null;

		RConsole.println("[BT] Connection was closed.");
	}
}
