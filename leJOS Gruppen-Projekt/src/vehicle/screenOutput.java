package vehicle;

import lejos.nxt.LCD;

public class screenOutput {
	
	/* --Mode for outputting data-- */
	private static int mode;
	// Modes:
	// 1 - Default user operation mode
	// 2 - Debugging mode: outputs current tasks and values
	
	public screenOutput(int operationMode) {
		mode = operationMode;
	}
	
	private void printScreen(String output_str) { // Function splits string into 8 lines and outputs them on LCD
		String[] output_array = splitStringEvery(output_str, 16); // 16 is the screen width
		
		LCD.clear();
		for (int i=0; i<8; i++) { // 8 is screen height
			LCD.drawString(output_array[i], 0, i);
		}
	}
	
	
	// Function that splits String after specific interval
	public String[] splitStringEvery(String s, int interval) {
	    int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
	    String[] result = new String[arrayLength];

	    int j = 0;
	    int lastIndex = result.length - 1;
	    for (int i = 0; i < lastIndex; i++) {
	        result[i] = s.substring(j, j + interval);
	        j += interval;
	    } //Add the last bit
	    result[lastIndex] = s.substring(j);

	    return result;
	}
}
