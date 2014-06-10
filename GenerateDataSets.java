package com.lu.ml.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class GenerateDataSets {
	private String fileName;
	private String fileExtension;
	
	
	public static void main(String[] args) {
		GenerateDataSets generatedatasets = new GenerateDataSets();
		generatedatasets.process("data/training.arff", 10, 15000);
	}

	public void process(String filePath, int numFolder, int dataSize){
		setFileNameAndExtension(filePath);
		int sizeTest = dataSize / numFolder;
		for(int i = 0; i < numFolder; i++){
			crossValidation(i, sizeTest);
		}			
	}
	
	private void crossValidation(int serialNum, int sizeTest){
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName + fileExtension));		
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(fileName + "_train_" + serialNum + fileExtension));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(fileName + "_test_" + serialNum + fileExtension));
		
			String line = "";			
			int index = 0;
			while((line = br.readLine()) != null){
				index++;
				if(index < 114){
					continue;
				}
	
				if((((index-114) / sizeTest) == serialNum)){
					bw2.write(line);
					bw2.newLine();
				}
				else{
					bw1.write(line);
					bw1.newLine();
				}
			}
			
			bw1.close();
			bw2.close();
			br.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setFileNameAndExtension(String filePath){
		ExtractFileName extractfileName = new ExtractFileName();
		fileName = extractfileName.getFileName(filePath);
		fileExtension = extractfileName.getFileExtension(filePath);
	}
}
