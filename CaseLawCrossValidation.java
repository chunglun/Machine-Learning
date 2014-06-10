package com.lu.ml.algorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lu.ml.tool.ExtractFileName;
import com.lu.ml.tool.ValueProcessing;

public class CaseLawCrossValidation {
	private List<Integer> numericIndexList;
	private Map<Integer, Double> numeric_max;
	private String fileName;
	private String fileExtension;
	
	public static void main(String[] args) {
		CaseLawCrossValidation caselawcrossvalidation = new CaseLawCrossValidation();
		caselawcrossvalidation.process("data/training.arff", 10, 1);
	}

	public void process(String filePath, int numFolder, int heuristicChoice){
		long startTime = System.currentTimeMillis();
		
		initial(filePath);
		
		Map<Integer, List<Integer>> results = new HashMap<Integer, List<Integer>>();
		for(int i = 0; i < numFolder; i++){
			List<Integer> resultIndex = readFile(i, heuristicChoice);
			results.put(i, resultIndex);
		}
		
		long endTime = System.currentTimeMillis();
		generateResultFile(startTime, endTime, results);
	}
	
	private void initial(String filePath){
		numericIndexList = Arrays.asList(2, 3, 5, 6,  8, 9, 10, 11, 13, 15, 16, 18, 39, 40, 41, 42, 
				43, 44, 45, 46, 47, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 
				65, 66, 67, 68, 69, 70);
		
		ValueProcessing vp = new ValueProcessing();
		numeric_max = vp.findMaxValuesForEachFeature(numericIndexList, filePath);
		
		setFileNameAndExtension(filePath);
	}
	
	private void setFileNameAndExtension(String filePath){
		ExtractFileName extractfileName = new ExtractFileName();
		fileName = extractfileName.getFileName(filePath);
		fileExtension = extractfileName.getFileExtension(filePath);
	}
	
	private List<Integer> readFile(int numSerial, int heuristicChoice){
		List<Integer> resultIndex = Arrays.asList(0, 0, 0, 0);
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName + "_test_" + numSerial + fileExtension));
			
			String line = "";	
			int index = 0;
			while((line = br.readLine()) != null){
				index++;
					
				//0:TT  1:TF  2:FT  3:FF
				int accuracy = processALine(line, numSerial, heuristicChoice);	
				int tmp = resultIndex.get(accuracy) + 1;
				resultIndex.set(accuracy, tmp);
				
				System.out.println(numSerial + ": " + index);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultIndex;
	}
	
	private int processALine(String testLine, int numSerial, int heuristicChoice) throws IOException{
		List<String> testLineElement = Arrays.asList(testLine.split(","));
		
		BufferedReader br = new BufferedReader(new FileReader(fileName + "_train_" + numSerial + fileExtension));
		String line = "";
		double max = 0.0;
		String predictResult = "";
		while((line = br.readLine()) != null){
			double currentScore = 0.0;
			List<String> trainLineElement = Arrays.asList(line.split(","));
			for(int i = 0; i < (trainLineElement.size() - 1); i++){
				String aTrainFeature = trainLineElement.get(i);
				String aTestFeature = testLineElement.get(i);
				
				if((!aTrainFeature.equals("?"))&&(!aTestFeature.equals("?"))){
					currentScore += chooseHeuristicFunction(heuristicChoice, i, aTrainFeature, aTestFeature);
				}
			}
			if(currentScore > max){
				max = currentScore;
				predictResult =  trainLineElement.get(trainLineElement.size() - 1);
			}
		}
		br.close();
		
		//0:TT  1:TF  2:FT  3:FF
		String actualAns = testLineElement.get(testLineElement.size() - 1);
		return generateResultCode(actualAns, predictResult);
	}
	
	private int generateResultCode(String actualAns, String predictResult){
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
	}
	
	private double chooseHeuristicFunction(int heuristicChoice, int index, String aTrainFeature, String aTestFeature){
		switch(heuristicChoice){
		case 1:
			return heuristic1(index, aTrainFeature, aTestFeature);
		case 2:
			return heuristic2(index, aTrainFeature, aTestFeature);
		}
		return 0;
	}
	
	private double heuristic1(int index, String aTrainFeature, String aTestFeature){
		if(numericIndexList.contains(index)){
			return (Math.abs(Double.parseDouble(aTrainFeature) - Double.parseDouble(aTestFeature)))/numeric_max.get(index);
		}
		else if(aTrainFeature.equals(aTestFeature)){
			return 1;
		}
		return 0;
	}
	
	private double heuristic2(int index, String aTrainFeature, String aTestFeature){
		if(aTrainFeature.equals(aTestFeature)){
			return 1;
		}
		return 0;
	}
	
	private String getCurrentDatetime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date current = new Date();
		return dateFormat.format(current);
	}
	
	private void generateResultFile(long startTime, long endTime, Map<Integer, List<Integer>> results){
		long totalTime = endTime - startTime;
		System.out.println(results);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("output_" + getCurrentDatetime() +".txt"));
			bw.write(results.toString());
			bw.newLine();
			bw.newLine();				
			bw.write(totalTime + "");
			bw.newLine();
			int seconds = (int) (totalTime / 1000) % 60 ;
			int minutes = (int) (totalTime / (1000*60));			
			bw.write(minutes + " minutes " + seconds + "seconds");
			bw.newLine();	
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
