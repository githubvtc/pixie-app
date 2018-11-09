package com.pixie.control;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.pixie.util.PixieUtils;

/**
 * @author voont This is the Controller for solver. It contains all the rules
 *         for solving. i.e. all the rules obtained It also contains the
 *         variable available for All Rules . i.e. THe 2D map,array in each
 *         row/col, thread to process. It also create a thread pool to execute
 *         each functions simultaneously
 *
 * @param <K> - The Key is number of X or Y-LINE number/ x or y-coordinates/
 * @param <V> - The Value is the numbers at each row/column
 */
public class RulesController {

	/**
	 * These instances are the rules available index 0 : MinMaxDots " 1 : ConsecDot
	 * " 2 : FitDots " 3 : DotValue
	 */
	private List<RulesPrototype> allrules = new ArrayList<RulesPrototype>();

	/* Variables for processing */
	private final Map<Integer, ArrayList<Integer>> xMap;
	private final Map<Integer, ArrayList<Integer>> yMap;
	private final int xMaxLength;
	private final int yMaxLength;
	private volatile Results[][] map2D;
	private boolean condition = false;

	public RulesController(Map<Integer, ArrayList<Integer>> xMap, Map<Integer, ArrayList<Integer>> yMap, boolean flag) {
		this.xMap = xMap;
		this.yMap = yMap;

		if (xMap != null && yMap != null) {
			this.xMaxLength = xMap.size();
			this.yMaxLength = xMap.size();
			condition = true;
		} else {
			loggin("Controller receives null map/s, please look into it. ");
			this.xMaxLength = 1;
			this.yMaxLength = 1;
		}

		initializeMap2D();

		/* Add all the rules manually */
		allrules.add(new MaxMinDots()); // code 0
		allrules.add(new ConsecDot()); // code 1
		allrules.add(new FitDots()); // code 2
		allrules.add(new DotValue()); // code 3
		allrules.add(new MaxMinDots()); // code 4
	}

	private void initializeMap2D() {
		map2D = new Results[xMaxLength][yMaxLength];
		for (int y = 0; y < yMaxLength; y++) {
			for (int x = 0; x < xMaxLength; x++) {
				map2D[x][y] = Results.WAITIN;
			}
		}

	}

	private void loggin(String msg) {
		PixieUtils.getLogg().logging(msg);
	}

	public Map<Integer, ArrayList<Integer>> getXMap() {
		return xMap;
	}

	public Map<Integer, ArrayList<Integer>> getYMap() {
		return yMap;
	}

	public int getXMaxLength() {
		return xMaxLength;
	}

	public int getYMaxLength() {
		return yMaxLength;
	}

	public boolean getCondition() {
		return condition;
	}

	private void setCondition(boolean condition) {
		this.condition = condition;
	}

	public void solve() {
		/* Not solvable */
		if (!condition) {
			return;
		}

		generateBuffer();
		int testy = 15;
		loggin("Y now : " + testy);
		int testx = 8;
		loggin("X now : " + testx);
		LocalTime startT = LocalTime.now();
		char[] inp = getMapRow(testy).toCharArray();
		char[] inp2 = getMapCol(testx).toCharArray();
		
//		Thread t1 = new Thread(() -> {
//			testRunn(inp, testy, true);
//		});
//		Thread t2 = new Thread(() -> {
//			testRunn(inp2, testx, false);
//		});
		for (int i = 0; i < xMaxLength; i++) {
			testRunn(getMapCol(i).toCharArray(), i, false);
		}
		for (int i = 0; i < yMaxLength; i++) {
			testRunn(getMapRow(i).toCharArray(), i, true);
		}
//		t1.start();
//		t2.start();
		loggin("Total time: " + String.valueOf(Duration.between(startT, LocalTime.now()).toMillis()));
//		try {
//			t1.join();
//			t2.join();
//		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private void generateBuffer() {

//		Queue<RulesPrototype> queBuffer = new LinkedBlockingQueue<RulesPrototype>(30);
//		Map<RulesPrototype, ArrayList<Integer>> jobBuffer = new LinkedHashMap<RulesPrototype, ArrayList<Integer>>();
//		Map<RulesPrototype, Integer> jobBuffer2 = new LinkedHashMap<RulesPrototype, Integer>();
	}

	/**
	 * basic running and logging block
	 * 
	 * @param input the Row/Col of the map to be processed
	 * @param coordinate the value of one of the coordinate pair values 
	 * @param isY regarding to the value, if isY = true (Row), and vice versa 
	 */
	private void testRunn(char[] input, int coordinate, boolean isY) {
//		LocalTime startT = LocalTime.now();
		Results res;
	
		if(isY) 
			res = allrules.get(0).proc(input, yMap.get((Integer) coordinate));
		else 
			res = allrules.get(4).proc(input, xMap.get((Integer) coordinate));
		
		Results[] output = PixieUtils.toResults(input);
		loggin("Output = " + String.valueOf(input));
		int anotherPair = 0;
		
		for (Results itm : output) {
			if (isY) {
				if (map2D[anotherPair][coordinate].getCode() == 'W') {
					map2D[anotherPair][coordinate] = itm;
				}
			} else {
				if (map2D[coordinate][anotherPair].getCode() == 'W') {
					map2D[coordinate][anotherPair] = itm;
				}
			}
			anotherPair++;
		}
//		loggin("Result = " + res.getMsg() + " =>duration:"
//				+ String.valueOf(Duration.between(startT, LocalTime.now()).toMillis()));
	}
	
	public Results[][] getMap2D() {
		return map2D;
	}

	/**
	 * This function get the data for whole row
	 * 
	 * @param y y-value of the row
	 * @return data the whole row in form of array 
	 */
	private String getMapRow(int y) {
		String data = "";
		for (int x = 0; x < xMaxLength; x++) {
			if (data == "") {
				data = String.valueOf(map2D[x][y].getCode());
			} else {
				data = data + map2D[x][y].getCode();
			}
		}
		return data;
	}

	/**
	 * This function get data for whole col
	 * 
	 * @param x x-value of the col
	 * @return data the whole col in form of array
	 */
	private String getMapCol(int x) {
		String data = "";
		for (int y = 0; y < xMaxLength; y++) {
			if (data == "") {
				data = String.valueOf(map2D[x][y].getCode());
			} else {
				data = data + map2D[x][y].getCode();
			}
		}
		return data;
	}

	public String getRules() {
		String result = "";

		for (RulesPrototype rule : allrules) {
			if (result == "") {
				result = rule.toString();
			} else
				result = result + "," + rule.toString();
		}
		return result;
	}

	/* DO NOT include this when package */
	public void printMap2D() {
		System.out.println("Printing 2D map based on current result ...\n");
		for (int y = 0; y < yMaxLength; y++) {
			for (int x = 0; x < xMaxLength; x++) {
				System.out.printf("%c ", map2D[x][y].getCode());
//				System.out.printf("%c ", map2D[x][y].getCode() == 'X' ? 'X' : ' ');
			}
			System.out.println("");
		}
		System.out.println("Finished printing .........................");
	}

	@Override
	public String toString() {
		return "Current controller contains rules : " + getRules();
	}
}
