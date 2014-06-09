package com.lu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CaseLaw {

	private static final String FILENAME = "training";
	private static final String EXTENSION = ".arff";
	private List<Integer> numeric;
	private Map<Integer, Double> numerica_max;
	public static void main(String[] args) {
		CaseLaw caselaw = new CaseLaw();
		caselaw.initial();
		caselaw.process();
		//List<Integer> correctCount = caselaw.readFile(0);
	}

	private void process(){
		long startTime = System.currentTimeMillis();
		
		int numFolder = 10;
		Map<Integer, List<Integer>> accuracy = new HashMap<Integer, List<Integer>>();
		for(int i = 0; i < numFolder; i++){
			List<Integer> correctCount = readFile(i);
			accuracy.put(i, correctCount);
		}
		
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(accuracy);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt"));
			bw.write(accuracy.toString());
			bw.newLine();
			bw.newLine();
			
			int seconds = (int) (totalTime / 1000) % 60 ;
			int minutes = (int) (totalTime / (1000*60));				
			bw.write(totalTime + "");
			bw.newLine();
			bw.write(minutes + " minutes " + seconds + "seconds");
			bw.newLine();	
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Numeric index: 2, 3, 5, 6,  8, 9, 10, 11, 13, 15, 16, 18, 39, 40, 41, 42, 43,
	 *                44, 45, 46, 47, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
	 *                61, 62, 63, 64, 65, 66, 67, 68, 69, 70
	 */
	private void initial(){
		numeric = Arrays.asList(2, 3, 5, 6,  8, 9, 10, 11, 13, 15, 16, 18, 39, 40, 41, 42, 43,
			44, 45, 46, 47, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 
			66, 67, 68, 69, 70);
		
		numerica_max = new HashMap<Integer, Double>();
		for(int element : numeric){
			numerica_max.put(element, 0.0);
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(FILENAME + EXTENSION));	
			String aLine = "";		
			int index = 0;
			while((aLine = br.readLine()) != null){
				index++;
				if(index < 114){
					continue;
				}
				
				List<String> aLineElement = Arrays.asList(aLine.split(","));
				for(Entry<Integer, Double> entry : numerica_max.entrySet()){
					String value = aLineElement.get(entry.getKey());
					if(!value.equals("?")){
						if(Double.parseDouble(value) > entry.getValue()){	
							entry.setValue(Double.parseDouble(value));
						}
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param line
	 * @throws IOException 
	 */
	private int processALine(String line, int numSerial) throws IOException{
		//System.out.println(line);
		List<String> aLineTest = Arrays.asList(line.split(","));
	
		BufferedReader br = new BufferedReader(new FileReader(FILENAME + "_train_" + numSerial + EXTENSION));
		String aLine = "";
		double max = 0.0;
		String predictResult = "";
		String original = "";
		while((aLine = br.readLine()) != null){
			double currentScore = 0.0;
			List<String> aLineTrain = Arrays.asList(aLine.split(","));
			for(int i = 0; i < (aLineTrain.size() - 1); i++){
				String sTrain = aLineTrain.get(i);
				String sTest = aLineTest.get(i);
				if((!sTrain.equals("?"))&&(!sTest.equals("?"))){
					if(numeric.contains(i)){
						currentScore += (Math.abs(Double.parseDouble(sTrain) - Double.parseDouble(sTest)))/numerica_max.get(i);
					}
					else{
						if(sTrain.equals(sTest)){
							currentScore += 1;
						}
					}
				}
			}
			if(currentScore > max){
				max = currentScore;
				predictResult =  aLineTrain.get(aLineTrain.size() - 1);
				original = aLine;
			}
		}
		br.close();
		
		//0:TT  1:TF  2:FT  3:FF
		String actualAns = aLineTest.get(aLineTest.size() - 1);
		if(actualAns.equals("1")){
			if(predictResult.equals("0")){
				return 1;
			}
			else{
				return 0;
			}
		}
		else{
			if(predictResult.equals("0")){
				return 3;
			}
			else{
				return 2;
			}
		}
		/*
		if(aLineTest.get(aLineTest.size() - 1).equals(predictResult)){
			return true;
		}
		else{
			return false;
		}
		*/
		//System.out.println(aLineTest.get(aLineTest.size() - 1));
		//System.out.println(original);
		//System.out.println(predictResult);
		//System.out.println(max);
		//System.out.println(numerica_max);
	}
	
	private List<Integer> readFile(int numSerial){
		
		List<Integer> correctCount = Arrays.asList(0, 0, 0, 0);

		try {
			BufferedReader br = new BufferedReader(new FileReader(FILENAME + "_test_" + numSerial + EXTENSION));
			
			String aLine = "";	
			int index = 0;
			while((aLine = br.readLine()) != null){
				index++;
					
				//0:TT  1:TF  2:FT  3:FF
				int accuracy = processALine(aLine, numSerial);	
				int tmp = correctCount.get(accuracy) + 1;
				correctCount.set(accuracy, tmp);
				
				System.out.println(numSerial + ": " + index);

			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return correctCount;
	}

}
