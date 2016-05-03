package vehicle;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.RemoteDevice;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;

public class BlueComm {

	// bluetooth codes
	final short askForPackage = 1;
	final short packageWasDelivered = 7;
	// constant vars
	final String btname;
	final int waitBetweenSends;
	
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
		// reconnect or connect if no already done
		connectToDevice();
		boolean packageReceived = false;
		BlueRead blueread = new BlueRead(dis);
		blueread.start();

		try {
		while (!packageReceived) {
			Main.setCurrent_task(3);
			// check if package has been received
			dos.writeShort(askForPackage);
			dos.flush();
			
			if (blueread.returnValue() == packageWasDelivered) {
				packageReceived = true;
			}
			
			// sleep between sends
			try {
				Thread.sleep(waitBetweenSends);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
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
				e.printStackTrace();
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
}
