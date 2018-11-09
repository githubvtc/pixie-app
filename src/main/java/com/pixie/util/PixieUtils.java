/**
 * 
 */
package com.pixie.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.pixie.control.Results;

/**
 * @author voont Application's Utility functions store
 *
 */
public final class PixieUtils {

	private static LogMsg logg;
	private static PixieUtils myInstance;
	private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

	private PixieUtils() {
		logg = msg -> System.out.println(LocalDateTime.now().format(timeFormatter) + " receiving msg ... " + msg);
	}

	/**
	 * Returns the instance for logging instance
	 * @return logger instance
	 */
	public static LogMsg getLogg() {
		return logg;
	}

	/**
	 * Calculator, unnecessary method
	 * 
	 * @param      <T>
	 * @param mode - Use BasicCalculation ENUM
	 * @return
	 */
	public <T extends Number> Calculator<T> getCalctor(BasicCalculation mode, T answer) throws ArithmeticException {
		Calculator<T> calctor = null;
		switch (mode) {
		case ADD:
			calctor = new Calculator<T>() {

				@Override
				public boolean calculate(T t0, T t1) {
					// TODO Auto-generated method stub

					return (Integer) ((Number) answer.intValue()) == (Integer) ((Number) t0.intValue())
							+ (Integer) ((Number) t1.intValue());
				}
			};
			break;
		case SUBSTRACT:
			calctor = new Calculator<T>() {

				@Override
				public boolean calculate(T t0, T t1) {
					// TODO Auto-generated method stub

					return (Integer) ((Number) answer.intValue()) == (Integer) ((Number) t0.intValue())
							- (Integer) ((Number) t1.intValue());
				}
			};
			break;
		case MULTIPLY:
			calctor = new Calculator<T>() {

				@Override
				public boolean calculate(T t0, T t1) {
					// TODO Auto-generated method stub

					return (Integer) ((Number) answer.intValue()) == (Integer) ((Number) t0.intValue())
							* (Integer) ((Number) t1.intValue());
				}
			};
			break;
		case DIVIDE:
			calctor = new Calculator<T>() {

				@Override
				public boolean calculate(T t0, T t1) {
					// TODO Auto-generated method stub

					return (Integer) ((Number) answer.doubleValue()) == (Integer) ((Number) t0.doubleValue())
							/ (Integer) ((Number) t1.doubleValue());
				}
			};
			break;
		}

		return calctor;
	}

	/**
	 * Convert {@code Results[]} to {@code char[]}, BEWARE of difference instance due to this function
	 * @param res array of Results
	 * @return char[]
	 */
	public synchronized static char[] toCharArray(Results[] res) {
		String tempStr = "";
		for (Results itm : res) {
			tempStr += itm.getCode();
		}
		return tempStr.toCharArray();
	}

	/**
	 * Convert {@code char[]} to {@code Results[]}, BEWARE of difference instance due to this function
	 * @param res char[]
	 * @return array of Results
	 */
	public synchronized static Results[] toResults(char[] res) {
		Results[] temp = new Results[res.length];
		int i = 0;
		for (char itm : res) {
			switch (itm) {
			case 'W':
				temp[i++] = Results.WAITIN;
				break;
			case 'X':
				temp[i++] = Results.MARK;
				break;
			case 'O':
				temp[i++] = Results.BLANK;
				break;
			case 'S':
				temp[i++] = Results.SUCCESS;
				break;
			case 'F':
				temp[i++] = Results.FAIL;
				break;
			case 'H':
				temp[i++] = Results.HOLD;
				break;
			}

		}
		return temp;
	}

	public static PixieUtils getInstance() {

		synchronized (PixieUtils.class) {
			if (myInstance == null) {
				myInstance = new PixieUtils();
			}
		}
		return myInstance;
	}

}
