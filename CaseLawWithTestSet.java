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
import java.util.List;
import java.util.Map;

import com.lu.ml.tool.ExtractFileName;
import com.lu.ml.tool.ValueProcessing;

public class CaseLawWithTestSet {
	private List<Integer> numericIndexList;
	private Map<Integer, Double> numeric_max;
	private String trainFileName;
	private String trainFileExtension;
	private String testFileName;
	private String testFileExtension;

	
	public static void main(String[] args) {
		CaseLawWithTestSet caselawwithtestset = new CaseLawWithTestSet();
		caselawwithtestset.process("data/training.arff", "data/testing.arff", 1);
	}

	public void process(String trainFilePath, String testFilePath, int heuristicChoice){
		initial(trainFilePath, testFilePath);
		readFile(heuristicChoice);
	}
	
	private void initial(String trainFilePath, String testFilePath){
		numericIndexList = Arrays.asList(2, 3, 5, 6,  8, 9, 10, 11, 13, 15, 16, 18, 39, 40, 41, 42, 
				43, 44, 45, 46, 47, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 
				65, 66, 67, 68, 69, 70);
		
		ValueProcessing vp = new ValueProcessing();
		numeric_max = vp.findMaxValuesForEachFeature(numericIndexList, trainFilePath);
		
		setFileNameAndExtension(trainFilePath, testFilePath);
	}
	
	private void setFileNameAndExtension(String trainFilePath, String testFilePath){
		ExtractFileName extractfileName = new ExtractFileName();
		trainFileName = extractfileName.getFileName(trainFilePath);
		trainFileExtension = extractfileName.getFileExtension(trainFilePath);
		testFileName = extractfileName.getFileName(testFilePath);
		testFileExtension = extractfileName.getFileExtension(testFilePath);
	}
	
	private String getCurrentDatetime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date current = new Date();
		return dateFormat.format(current);
	}
	
	private void readFile(int heuristicChoice){
		try {
			BufferedReader br = new BufferedReader(new FileReader(testFileName + testFileExtension));
			BufferedWriter bw = new BufferedWriter(new FileWriter(testFileName + "_output_" + getCurrentDatetime() + ".txt"));
			
			String line = "";	
			int index = 0;
			while((line = br.readLine()) != null){
				index++;
				if(index < 114){
					continue;
				}
				
				String predictResult = processALine(line, heuristicChoice);		
				bw.write(predictResult);
				bw.newLine();
				
				System.out.println(index-113);
			}
			bw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String processALine(String testLine, int heuristicChoice) throws IOException{
		List<String> testLineElement = Arrays.asList(testLine.split(","));
	
		BufferedReader br = new BufferedReader(new FileReader(trainFileName + trainFileExtension));
		String line = "";
		String predictResult = "";
		double max = 0.0;
		int index = 0;
		while((line = br.readLine()) != null){
			index++;
			if(index < 114){
				continue;
			}
			
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
		
		return predictResult;
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
}
