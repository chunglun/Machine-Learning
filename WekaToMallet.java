package com.lu.ml.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

public class WekaToMallet {
	private String fileName;
	private String fileExtension;
	private List<Integer> numericIndexList;
	
	public static void main(String[] args) {
		WekaToMallet wekatomallet = new WekaToMallet();
		wekatomallet.process("data/training.arff");;
	}

	public void process(String filePath){
		setFileNameAndExtension(filePath);
		initial();
		convert();
	}
	
	private void setFileNameAndExtension(String filePath){
		ExtractFileName extractfileName = new ExtractFileName();
		fileName = extractfileName.getFileName(filePath);
		fileExtension = extractfileName.getFileExtension(filePath);
	}
	
	private void initial(){
		numericIndexList = Arrays.asList(2, 3, 5, 6,  8, 9, 10, 11, 13, 15, 16, 18, 39, 40, 41, 42, 
				43, 44, 45, 46, 47, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 
				65, 66, 67, 68, 69, 70);
	}
	
	private void convert(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName + fileExtension));
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName + "_mallet.txt"));
			
			String line = "";			
			int index = 0;
			while((line = br.readLine()) != null){
				index++;
				if(index < 114){
					continue;
				}
				String writeLine = convertInstance(line);
				bw.write(writeLine);
				bw.newLine();
			}
			
			br.close();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private String convertInstance(String line){
		List<String> aLineElement = Arrays.asList(line.split(","));
		String result = "";

		for(int i = 0; i < aLineElement.size(); i++){
			if(!numericIndexList.contains(i)){
				result += (aLineElement.get(i) + " ");
			}
		}
		/*
		for(String s : aLineElement){
			result += (s + " ");
		}
		*/
		return result;
	}
}
