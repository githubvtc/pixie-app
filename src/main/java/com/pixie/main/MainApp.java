package com.pixie.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.pixie.util.BasicCalculation;
import com.pixie.util.Calculator;
import com.pixie.util.LogMsg;
import com.pixie.util.PixieUtils;

public class MainApp {
	static Logger LOG = Logger.getLogger(MainApp.class);
	DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

	public static void main(String[] args) throws InterruptedException {
		MainApp app = new MainApp();

//		System.out.println(str.getBytes(StandardCharsets.UTF_8));
//		Testing out Lambda
		PixieUtils util = PixieUtils.getInstance();
		LogMsg logg = util.getLogg();
		Calculator<Integer> calc = util.getCalctor(BasicCalculation.ADD, 13);
		logg.logging("Calculator is working - > " + calc.calculate(11, 3));
		
//		Testing log4j
		PropertyConfigurator.configure(app.getFile("/pixie-log4j.properties"));
		LOG.warn("Testing log4j");

//		Oekaki puzzle/picross puzzle solve
		Map<Integer, ArrayList<Integer>> xLineMap = null;
		Map<Integer, ArrayList<Integer>> yLineMap = null;

		String xLinePath = "/xLine.csv";
		String yLinePath = "/yLine.csv";

		List<Thread> tL = new ArrayList<Thread>();

		PixieThread<Map<Integer, ArrayList<Integer>>> t2 = new PixieThread<Map<Integer, ArrayList<Integer>>>(app, yLineMap) {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				logg.logging("Thread " + this.getName() + " is parsing Y Line ...");
				this.setMapValue(this.getApp().getMap(app, yLinePath));
			}
		};

//		ExecutorService executor = Executors.newFixedThreadPool(4);

		Callable<Map<Integer, ArrayList<Integer>>> callXMap = new PixieThread<Map<Integer, ArrayList<Integer>>>(app,
				xLineMap) {
			@Override
			public Map<Integer, ArrayList<Integer>> call() throws Exception {
				logg.logging("Thread " + this.getName() + " is parsing X Line...");
				this.setMapValue(this.getApp().getMap(app, xLinePath));
				return this.getMapValue();
			}
		};
		
		FutureTask<Map<Integer, ArrayList<Integer>>> fut01 = new FutureTask<Map<Integer, ArrayList<Integer>>>(callXMap);
		
		Thread t3 = new Thread(fut01);
		Thread t4 = new Thread(() -> {
			try {
				while (!fut01.isDone()) {
					logg.logging("XMAP thread is not done yet !");
					Thread.sleep(210);
				}
				Map<Integer, ArrayList<Integer>> tempData = fut01.get();
//				For X Line Map Values
				logg.logging("This is Xline Map");
				for (Map.Entry<Integer, ArrayList<Integer>> entry : tempData.entrySet()) {
					System.out.println(entry.getKey() + " with numbers : "
							+ entry.getValue().stream().map(e -> e.toString()).collect(Collectors.joining(",")));
				}
				logg.logging(" - Thread " + ((PixieThread<Map<Integer, ArrayList<Integer>>>)callXMap).getName() + " is finished...");
			} catch (InterruptedException | ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		Thread t6 = new Thread(() -> {
			try {
				while(t2.isAlive()) {
					logg.logging("YMAP thread is not done yet !");
					Thread.sleep(187);
				}
				t2.join();
				Map<Integer, ArrayList<Integer>> tempData = t2.getMapValue();
				logg.logging("This is Yline Map");
//				For Y Line Map Values
				for (Map.Entry<Integer, ArrayList<Integer>> entry : tempData.entrySet()) {
					System.out.println(entry.getKey() + " with numbers : "
							+ entry.getValue().stream().map(e -> e.toString()).collect(Collectors.joining(",")));
				}
				logg.logging("Thread " + t2.getName() + " is finished...");
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		tL.addAll(new ArrayList<Thread>(Arrays.asList(t2,t3,t4,t6)));
		for (Thread th : tL) {
			th.start();
		}
		try {
			xLineMap= fut01.get();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		yLineMap=t2.getMapValue();
	}

	public InputStream getFile(String path) {
		return getClass().getResourceAsStream(path);
	}

	public void runApp() {
//		// PropertyConfigurator.configure("log4j.properties");
//		LOG.info("Testing . ");
//
//		RulesPrototype rule01 = new MaxMinDots();
//
//		rule01.getAllResults();
	}

	/* Testing Stream */
	public void processStreamThread(String elem) {
//		List<String> strList = new ArrayList<String>(Arrays.asList("L", "B", "Das", "Russe", "Huskar", "Jin", "use"));
//		System.out.println("Using sequential stream: ");
//		strList.stream().forEach(System.out::println);
//		System.out.println("Using sequential stream: ");
//		strList.stream().forEach(app::processStreamThread);
//		System.out.println("Using parallel stream: ");
//		strList.parallelStream().forEach(app::processStreamThread);
		System.out.println(LocalDateTime.now().format(timeFormatter) + "- Thread now : "
				+ Thread.currentThread().getName() + " processing Element " + elem);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Map<Integer, ArrayList<Integer>> getMap(MainApp app, String path) {
		Map<Integer, ArrayList<Integer>> mapValues = new TreeMap<Integer, ArrayList<Integer>>();

		BufferedReader br = new BufferedReader(new InputStreamReader(app.getFile(path), StandardCharsets.US_ASCII));

		String line = "";
		try {
			line = br.readLine();
			while (line != null) {

//				Add Key and Value into map
				String[] stringArr = line.split(",");
				Integer key = Integer.parseInt(stringArr[0]);
				ArrayList<Integer> value = new ArrayList<Integer>();

				for (int i = 1; i < stringArr.length; i++) {
					Integer tempInt = Integer.parseInt(stringArr[i]);
					value.add(tempInt);
				}
				mapValues.put(key, value);

//				Read another line
				line = br.readLine();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return mapValues;
	}

	/* Crytography */
	public String testCipher(char testKey) {

//		Encoding (base64, Hex)
//		byte[] hexNum = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d".getBytes(StandardCharsets.US_ASCII);
//		String hexString = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
//		String hexString01 = "1c0111001f010100061a024b53535009181c";
//		BigInteger hexNum01 = new BigInteger(hexString01, 16);
//		BigInteger _hexNum01 = new BigInteger("686974207468652062756c6c277320657965", 16);
//		BigInteger xorHexNum01 = hexNum01.xor(_hexNum01);
//		logg.logging("hexNum01 : " + hexNum01 + " with length " + hexNum01.byteValue() + " XOR with " + _hexNum01
//				+ " with length " + _hexNum01.byteValue());
//		logg.logging("After XOR : " + xorHexNum01 + ". Hex String : " + xorHexNum01.toString(16));
//
////		byte[] _hexNum = BaseEncoding.base16().decode("686974207468652062756c6c277320657965".toUpperCase());
////		hexString = hexString.toUpperCase();
////		byte[] hexNum = BaseEncoding.base16().decode(hexString);
////		String base32Str0 = BaseEncoding.base32Hex().encode(hexNum);
////		String base64Str0 = BaseEncoding.base64().encode(hexNum);
//
//		BigInteger hexNum = new BigInteger(hexString, 16);
////		logg.logging("Testing radix: " + hexNum.toString(16));
//		String base32Str0 = BaseEncoding.base32().encode(hexNum.toByteArray());
//		String base64Str0 = BaseEncoding.base64().encode(hexNum.toByteArray());
//
//		BigInteger ckey = new BigInteger("a".getBytes(StandardCharsets.US_ASCII));
//		logg.logging("Keylength = " + ckey.toByteArray().length + " ; hexS length = " + hexNum.toByteArray().length);
//		logg.logging("hexString: " + hexString.length());
////		logg.logging("This is _Hex NUM: " + _hexNum + " with length " + _hexNum.length());
//		logg.logging("base64 string :" + base64Str0);
//		logg.logging("base32 string :" + base32Str0);
//
//		char[] myname = "Cooking MC's like a pound of bacon".toCharArray();
//		char mykey = 'T';
//		
//		BigInteger tempKey = new BigInteger(String.valueOf(mykey).getBytes(StandardCharsets.US_ASCII));
//		logg.logging("Byte length : " + String.valueOf(myname).getBytes(StandardCharsets.US_ASCII).length);
//		String encry = "";
//		
//		for (char temp : myname) {
//			BigInteger tempHex = new BigInteger(String.valueOf(temp).getBytes(StandardCharsets.US_ASCII));
//			String tempStr = new String(tempHex.xor(tempKey).toString(16));
//			encry += tempStr ;
//		}
//		
//		logg.logging("Decrypted : " + String.valueOf(myname) + " XOR encrypt to ... " + encry);
//		logg.logging("Encry : " + String.valueOf(myname) + " -> decry-ing ... " + app.testCipher(mykey));
//		
//		char[] charArr = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();
//		for(char temp : charArr) {
//			logg.logging("After XOR :- " + app.testCipher(temp));
//		}
		String hexString02 = "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736";

		BigInteger hexnum02 = new BigInteger(hexString02, 16);
		int bytelength = hexnum02.toByteArray().length;

		String testKeyPadded = "";
		for (int o = 0; o < bytelength; o++) {
			testKeyPadded += testKey;
		}

		BigInteger testnum = new BigInteger((String.valueOf(testKeyPadded).getBytes(StandardCharsets.US_ASCII)));

		return new String(hexnum02.xor(testnum).toByteArray(), StandardCharsets.US_ASCII);
	}
}
