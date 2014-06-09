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

public class CaseLawTest {
	
	private static final String FILENAME = "training";
	private static final String FILENAME2 = "testing";
	private static final String EXTENSION = ".arff";
	private List<Integer> numeric;
	private Map<Integer, Double> numerica_max;
	public static void main(String[] args) {
		CaseLawTest caselaw = new CaseLawTest();
		caselaw.initial();
		caselaw.process();
	}

	private void process(){
		readFile();
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
	private String processALine(String line) throws IOException{
		List<String> aLineTest = Arrays.asList(line.split(","));
	
		BufferedReader br = new BufferedReader(new FileReader(FILENAME + EXTENSION));
		String aLine = "";
		double max = 0.0;
		String predictResult = "";
		String original = "";
		int index = 0;
		while((aLine = br.readLine()) != null){
			index++;
			if(index < 114){
				continue;
			}
			
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
		
		return predictResult;
	}
	
	private void readFile(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(FILENAME2 + EXTENSION));
			BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME2 + "_output.txt"));
			
			String aLine = "";	
			int index = 0;
			while((aLine = br.readLine()) != null){
				index++;
				if(index < 114){
					continue;
				}
				
				String predict = processALine(aLine);	
				
				bw.write(predict);
				bw.newLine();
				
				System.out.println(index-113);
			}
			bw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
