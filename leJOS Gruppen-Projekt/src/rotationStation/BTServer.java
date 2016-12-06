package rotationStation;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;

public class BTServer extends Thread {
	// bluetooth codes
	private final int stationIsOccupied = 5;
	private final int stationIsNotOccupied = 7;
	// constant vars
	private final int waitBetweenSends;
	// box detect obj
	private final BoxDetect bdetect;
	
	private BTConnection btc;
	private DataOutputStream dos;
	
	public BTServer(BoxDetect bd, int waitBetweenSends) {
		this.bdetect = bd;
		this.waitBetweenSends = waitBetweenSends;
	}
	
	public void run() {
		while (true) {
			connectToDevice();
			reportToDevice();
			closeConnection();
			Delay.msDelay(waitBetweenSends);
		}
	}
	
	private void connectToDevice() {
		try{
			btc = Bluetooth.waitForConnection();
			if (btc == null) {
				RConsole.println("[BT] Connection error!");
				throw new BluetoothStateException("Connection error!");
			}
			
			dos = btc.openDataOutputStream();
			
		} catch (BluetoothStateException e) {
			e.printStackTrace();
			Delay.msDelay(waitBetweenSends);
			connectToDevice();
		}
	}
	
	private void reportToDevice() {
		short sendInt;
		
		if (bdetect.isBox()) {
			sendInt = stationIsOccupied;
		} else {
			sendInt = stationIsNotOccupied;
		}
		
		try {
			dos.writeInt(sendInt);
		} catch (IOException e) {
			RConsole.println("[BT] Send failed!");
			Delay.msDelay(waitBetweenSends);
			closeConnection();
			connectToDevice();
			reportToDevice();
		}
		
		RConsole.println("[BT] Status " + sendInt + " successfully reported!");
	}
	
	private void closeConnection() {
		try {
			dos.close();
			btc.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		dos = null;
		btc = null;
		
		RConsole.println("[BT] Connection was closed.");
	}
	
}
