package arm_test;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class LCDscreens {

	public static int waitAfterConfirm = 1000; // wait in ms after screen
												// confirmed

	public static int askForValue(String name, int initial, int step, int waitBetween, boolean allowNegative) {
		// maximum name length: 15

		LCD.clear();
		LCD.drawString(name + ":", 0, 0);
		LCD.drawString("Confirm with", 0, 3);
		LCD.drawString("orange.", 0, 4);

		int value = initial;
		long pressed_since = 0;
		int cur_step = step;

		while (!Button.ENTER.isDown()) {
			LCD.clear(1);
			LCD.drawString("<< " + value + " >>", 0, 1);
			if (Button.LEFT.isUp() && Button.RIGHT.isUp()) {
				pressed_since = 0;
			} else if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
				if (pressed_since == 0) {
					pressed_since = System.currentTimeMillis();
					cur_step = step;
					if (Button.LEFT.isDown() && Button.RIGHT.isUp() && (value > 0 || allowNegative)) {
						value = value - cur_step;
					} else if (Button.LEFT.isUp() && Button.RIGHT.isDown()) {
						value = value + cur_step;
					}
				} else if (System.currentTimeMillis() - pressed_since > waitBetween) {
					if (Button.LEFT.isDown() && Button.RIGHT.isUp() && (value > 0 || allowNegative)) {
						value = value - cur_step;
					} else if (Button.LEFT.isUp() && Button.RIGHT.isDown()) {
						value = value + cur_step;
					}

					if (System.currentTimeMillis() - pressed_since > waitBetween * 5) {
						cur_step = step * 4;
					}
				}
			}

			try {
				Thread.sleep(175);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(waitAfterConfirm);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public static int MultipleChoice(String option1, String option2, String option3, String option4, String option5,
			int initial) {
		// maximum option string length: 14

		boolean button_down = false;
		int confirm_result;
		int option_count;
		// count amount of options
		if (option3 == null) {
			option_count = 2;
		} else if (option4 == null) {
			option_count = 3;
		} else if (option5 == null) {
			option_count = 4;
		} else {
			option_count = 5;
		}

		if (initial < 1 || initial > option_count) {
			confirm_result = 1;
		} else {
			confirm_result = initial;
		}

		LCD.clear();
		LCD.drawString("Please choose:", 0, 0);
		while (Button.ENTER.isUp()) {
			if (Button.LEFT.isDown() && Button.RIGHT.isUp()) {
				if (!button_down) {
					if (confirm_result > 1) {
						confirm_result--;
					} else {
						confirm_result = option_count;
					}
				}
				button_down = true;
			} else if (Button.LEFT.isUp() && Button.RIGHT.isDown()) {
				if (!button_down) {
					if (confirm_result == option_count) {
						confirm_result = 1;
					} else {
						confirm_result++;
					}
				}
				button_down = true;
			} else {
				button_down = false;
			}

			// draw options
			// option1
			LCD.drawString(mchoice_line(option1, confirm_result == 1), 0, 1);
			// option2
			LCD.drawString(mchoice_line(option2, confirm_result == 2), 0, 2);
			if (option_count > 2) {
				// option3
				LCD.drawString(mchoice_line(option3, confirm_result == 3), 0, 3);
				if (option_count > 3) {
					// option4
					LCD.drawString(mchoice_line(option4, confirm_result == 4), 0, 4);
					if (option_count > 4) {
						// option5
						LCD.drawString(mchoice_line(option5, confirm_result == 5), 0, 5);
					}
				}
			}

		}

		try {
			Thread.sleep(waitAfterConfirm);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return confirm_result;
	}

	private static String mchoice_line(String msg, boolean isSelected) {
		if (isSelected) {
			return "> " + msg;
		} else {
			return "  " + msg;
		}
	}

	public static int MultipleChoice(String option1, String option2, int initial) {
		return MultipleChoice(option1, option2, null, null, null, initial);
	}

	public static int MultipleChoice(String option1, String option2, String option3, int initial) {
		return MultipleChoice(option1, option2, option3, null, null, initial);
	}

	public static int MultipleChoice(String option1, String option2, String option3, String option4, int initial) {
		return MultipleChoice(option1, option2, option3, option4, null, initial);
	}

	public static boolean askForConfirmation(String custom_msg1, String custom_msg2, String custom_msg3,
			String custom_msg4, boolean initial) {
		// maximum msg length: 16

		boolean confirm_result = initial;
		boolean button_down = false;

		LCD.clear();
		LCD.drawString(custom_msg1, 0, 0);
		LCD.drawString(custom_msg2, 0, 1);
		LCD.drawString(custom_msg3, 0, 2);
		LCD.drawString(custom_msg4, 0, 3);

		LCD.drawString("Confirm?", 0, 5);
		while (Button.ENTER.isUp()) {
			if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
				if (!button_down) {
					confirm_result = !confirm_result;
				}
				button_down = true;
				;
			} else {
				button_down = false;
			}

			LCD.clear(6);
			if (confirm_result) {
				LCD.drawString("  >>Yes      No ", 0, 7);
			} else {
				LCD.drawString("    Yes    >>No ", 0, 7);
			}

			try {
				Thread.sleep(175);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(waitAfterConfirm);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return confirm_result;
	}

	public static boolean askForConfirmation(String custom_msg1, boolean initial) {
		return askForConfirmation(custom_msg1, "", "", "", initial);
	}

	public static boolean askForConfirmation(String custom_msg1, String custom_msg2, boolean initial) {
		return askForConfirmation(custom_msg1, custom_msg2, "", "", initial);
	}

	public static boolean askForConfirmation(String custom_msg1, String custom_msg2, String custom_msg3,
			boolean initial) {
		return askForConfirmation(custom_msg1, custom_msg2, custom_msg3, "", initial);
	}
}
