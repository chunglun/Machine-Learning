package com.lu.ml.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CalculateDataSize {

	public static void main(String[] args) {
		CalculateDataSize calculatedatasize = new CalculateDataSize();
		calculatedatasize.process("data/training.arff", 10);
		
	}

	public void process(String filePath, int numFolder){
		ExtractFileName extractfileName = new ExtractFileName();
		String fileName = extractfileName.getFileName(filePath);
		String fileExtension = extractfileName.getFileExtension(filePath);
		
		List<Integer> sizesTrain = new ArrayList<Integer>();
		List<Integer> sizesTest = new ArrayList<Integer>();
		
		for(int i = 0; i < numFolder; i++){
			try {
				BufferedReader br = new BufferedReader(new FileReader(fileName + "_train_" + i + fileExtension));
				int count = 0;
				while(br.readLine() != null){
					count++;
				}
				sizesTrain.add(count);
				br.close();
				
				br = new BufferedReader(new FileReader(fileName + "_test_" + i + fileExtension));
				count = 0;
				while((br.readLine()) != null){
					count++;
				}
				sizesTest.add(count);
				br.close();	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
		printSizes(sizesTrain);
		printSizes(sizesTest);
	}
	
	private void printSizes(List<Integer> sizes){
		System.out.println(sizes);
	}
}
