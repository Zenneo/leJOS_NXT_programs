package vehicle;

import java.io.DataInputStream;
import java.io.IOException;

public class BlueRead extends Thread {

	final DataInputStream inS;
	short inNumber = 0;
	IOException exception;

	public BlueRead(DataInputStream inStream) {
		inS = inStream;
	}

	public void run() {
		try {
			inNumber = inS.readShort();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exception = e;
		}
	}

	public short returnValue() throws IOException {
		if (exception != null) {
			return inNumber;
		} else {
			throw exception;
		}
	}
}
