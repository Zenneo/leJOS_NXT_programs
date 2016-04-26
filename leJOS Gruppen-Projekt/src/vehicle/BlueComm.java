package vehicle;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;

public class BlueComm extends Thread {

	// constant vars
	final String btname;
	final int waitBetweenSends;
	// bluetooth vars
	RemoteDevice btrd;
	BTConnection btc;
	DataInputStream dis;
	DataOutputStream dos;

	public BlueComm(String btname, int waitBetweenSends) {
		this.btname = btname;
		this.waitBetweenSends = waitBetweenSends;
	}

	public void run() {
		try {
			connectToDevice();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
			//TODO reconnect
		}
		
	}

	public void waitForPackage() {
		boolean packageReceived = false;
		
		while (!packageReceived) {
			// check if package can be delivered
			
			// sleep between sends
			try {
				Thread.sleep(waitBetweenSends);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	void connectToDevice() throws BluetoothStateException {
		RConsole.println("[BT] Establishing connection with: " + btname);
		btrd = Bluetooth.getKnownDevice(btname);

		// given device name is unknown
		if (btrd == null) {
			RConsole.println("[BT] Device " + btname + " unknown!");
			throw new BluetoothStateException("Device " + btname + " unknown!");
		}

		btc = Bluetooth.connect(btrd);
		// connection failed
		if (btc == null) {
			RConsole.println("[BT] Can't connect to " + btname + "!");
			throw new BluetoothStateException("Can't connect to " + btname
					+ "!");
		}

		dis = btc.openDataInputStream();
		dos = btc.openDataOutputStream();
		
		RConsole.println("[BT] Connected to "+ btname);
	}
}
