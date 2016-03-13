package arm_test;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class LCDscreens {

	// wait in ms after screen confirmed
	public static int waitAfterConfirm = 750;
	// wait in ms after each while iteration
	public static int waitAfterWhile = 50;

	private final static String[] last_LCD_line_clean = { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
			"" };
	private static String[] last_LCD_line = last_LCD_line_clean;

	public static void checkedDraw(String string, int x, int y) {
		if (string != last_LCD_line[y]) {
			LCD.clear(y);
			LCD.drawString(string, x, y);
			last_LCD_line[y] = string;
		}
	}

	public static void checkedClear(int y) {
		LCD.clear(y);
		last_LCD_line[y] = "";
	}

	public static void checkedClear() {
		LCD.clear();
		last_LCD_line = last_LCD_line_clean;
	}

	public static int askForValue(String name, int initial, int step, int waitBetween, boolean allowNegative) {
		// maximum name length: 15

		int value = initial;
		long pressed_since = 0;
		int cur_step = step;

		checkedClear();
		checkedDraw(name + ":", 0, 0);
		checkedDraw(askVal_number(value), 0, 1);
		checkedDraw("Confirm with", 0, 3);
		checkedDraw("orange.", 0, 4);

		while (!Button.ENTER.isDown()) {
			if (Button.LEFT.isUp() && Button.RIGHT.isUp()) {
				pressed_since = 0;
			} else if (Button.LEFT.isDown() || Button.RIGHT.isDown()) {
				checkedDraw(askVal_number(value), 0, 1);
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

				// check if value was set accidently set to negative
				if (!allowNegative && value < 0) {
					value = 0;
				}
			}

			try {
				Thread.sleep(waitAfterWhile);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		checkedClear();

		try {
			Thread.sleep(waitAfterConfirm);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	private static String askVal_number(int number) {
		String whitespace1;
		String whitespace2;

		// 16 chars max

		double length = (10 - (number + "").length()) / 2.0;
		double ceil_length = Math.floor(length);

		if (length == ceil_length) {
			whitespace1 = repeatString(" ", (int) length);
			whitespace2 = whitespace1;
		} else {
			whitespace1 = repeatString(" ", (int) length + 1);
			whitespace2 = repeatString(" ", (int) length);
		}

		return " <<" + whitespace1 + number + whitespace2 + ">> ";
	}

	private static String repeatString(String s, int count) {
		StringBuilder r = new StringBuilder();
		for (int i = 0; i < count; i++) {
			r.append(s);
		}
		return r.toString();
	}

	public static int MultipleChoice(String msg1, String option1, String option2, String option3, String option4,
			String option5, String option6, String option7, int initial) {
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
		} else if (option6 == null) {
			option_count = 5;
		} else if (option7 == null) {
			option_count = 6;
		} else {
			option_count = 7;
		}

		if (initial < 1 || initial > option_count) {
			confirm_result = 1;
		} else {
			confirm_result = initial;
		}

		checkedClear();
		checkedDraw(msg1, 0, 0);
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
			checkedDraw(mchoice_line(option1, confirm_result == 1), 0, 1);
			// option2
			checkedDraw(mchoice_line(option2, confirm_result == 2), 0, 2);
			if (option_count > 2) {
				// option3
				checkedDraw(mchoice_line(option3, confirm_result == 3), 0, 3);
				if (option_count > 3) {
					// option4
					checkedDraw(mchoice_line(option4, confirm_result == 4), 0, 4);
					if (option_count > 4) {
						// option5
						checkedDraw(mchoice_line(option5, confirm_result == 5), 0, 5);
						if (option_count > 5) {
							// option 6
							checkedDraw(mchoice_line(option6, confirm_result == 6), 0, 6);
							if (option_count > 6) {
								// option 7
								checkedDraw(mchoice_line(option7, confirm_result == 7), 0, 7);
							}
						}
					}
				}
			}

			try {
				Thread.sleep(waitAfterWhile);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		checkedClear();

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

	public static int MultipleChoice(String msg1, String option1, String option2, int initial) {
		return MultipleChoice(msg1, option1, option2, null, null, null, null, null, initial);
	}

	public static int MultipleChoice(String msg1, String option1, String option2, String option3, int initial) {
		return MultipleChoice(msg1, option1, option2, option3, null, null, null, null, initial);
	}

	public static int MultipleChoice(String msg1, String option1, String option2, String option3, String option4,
			int initial) {
		return MultipleChoice(msg1, option1, option2, option3, option4, null, null, null, initial);
	}

	public static int MultipleChoice(String msg1, String option1, String option2, String option3, String option4,
			String option5, int initial) {
		return MultipleChoice(msg1, option1, option2, option3, option4, option5, null, null, initial);
	}

	public static int MultipleChoice(String msg1, String option1, String option2, String option3, String option4,
			String option5, String option6, int initial) {
		return MultipleChoice(msg1, option1, option2, option3, option4, option5, option6, null, initial);
	}

	public static boolean askForConfirmation(String custom_msg1, String custom_msg2, String custom_msg3,
			String custom_msg4, boolean initial) {
		// maximum msg length: 16

		boolean confirm_result = initial;
		boolean button_down = false;

		checkedClear();
		checkedDraw(custom_msg1, 0, 0);
		checkedDraw(custom_msg2, 0, 1);
		checkedDraw(custom_msg3, 0, 2);
		checkedDraw(custom_msg4, 0, 3);

		checkedDraw("Confirm?", 0, 5);
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

			if (confirm_result) {
				checkedDraw("  >>Yes      No ", 0, 7);
			} else {
				checkedDraw("    Yes    >>No ", 0, 7);
			}

			try {
				Thread.sleep(waitAfterWhile);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		checkedClear();

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
