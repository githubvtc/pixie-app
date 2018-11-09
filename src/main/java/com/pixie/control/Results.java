package com.pixie.control;

public enum Results {
	/**
	 * SUCCESS: data complete processing and has process some cells / map is done. 
	 * FAIL: Something is wrong in processing , data not processed properly, rarely seen in logs
	 * HOLD: row/col is not done yet, it has 'W' , and has been processed before / row/col did not change anything after process.
	 * WAITIN: row/col has not been processed at all / a cell has not been processed yet
	 * MARK: a cell marked 'X';
	 * BLANK: a cell marked 'O';
	 */
	SUCCESS("Success", 'S'), FAIL("Failed", 'F'), HOLD("Holdon", 'H'), WAITIN("Waitin", 'W'), MARK("Marked", 'X'),
	BLANK("Blank", 'O');

	private final String msg;
	private final char code;

	Results(String msg, char code) {
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public char getCode() {
		return code;
	}
	
	
}
