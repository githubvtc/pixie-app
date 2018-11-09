package com.pixie.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.pixie.util.LogMsg;
import com.pixie.util.PixieUtils;

public class PlayGame {

	public static void main(String[] args) {
		PlayGame game1 = new PlayGame();

		String xLinePath = "/xLine.csv";
		String yLinePath = "/yLine.csv";

		Map<Integer, ArrayList<Integer>> xLineMap = game1.getMap(game1, xLinePath);
		Map<Integer, ArrayList<Integer>> yLineMap = game1.getMap(game1, yLinePath);

//		PixieUtils.getLogg().logging("" + xLineMap.entrySet());
		RulesController model = new RulesController(xLineMap,yLineMap,true);
//		logg.logging(model.getRules());
		model.solve();
		model.printMap2D();
	}

	public InputStream getFile(String path) {
		return getClass().getResourceAsStream(path);
	}

	private Map<Integer, ArrayList<Integer>> getMap(PlayGame app, String path) {
		Map<Integer, ArrayList<Integer>> mapValues = new LinkedHashMap<Integer, ArrayList<Integer>>();

		BufferedReader br = new BufferedReader(new InputStreamReader(app.getFile(path), StandardCharsets.US_ASCII));
		
		String line = "";
		try {
			line = br.readLine();
			Integer key = new Integer(0);
			while (line != null) {

//				Add Key and Value into map
				String[] stringArr = line.split(",");
				ArrayList<Integer> value = new ArrayList<Integer>();

				for (int i = 0; i < stringArr.length; i++) {
					Integer tempInt = Integer.parseInt(stringArr[i]);
					value.add(tempInt);
				}
				mapValues.put(key, value);

//				Read another line
				key ++;
				line = br.readLine();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return mapValues;
	}

}
