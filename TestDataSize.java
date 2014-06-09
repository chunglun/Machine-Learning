package com.lu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TestDataSize {
	private static final String FILENAME = "training";
	private static final String EXTENSION = ".arff";
	private static final int NUM_FOLDER = 10;
	
	public static void main(String[] args) {
		TestDataSize testdatasize = new TestDataSize();
		testdatasize.process();
	}

	private void process(){
		List<Integer> sizes = new ArrayList<Integer>();
		
		for(int i = 0; i < NUM_FOLDER; i++){
			try {
				BufferedReader br = new BufferedReader(new FileReader(FILENAME + "_train_" + i + EXTENSION));
				String line = "";
				int count = 0;
				while((line = br.readLine()) != null){
					count++;
				}
				sizes.add(count);
				br.close();
				
				br = new BufferedReader(new FileReader(FILENAME + "_test_" + i + EXTENSION));
				count = 0;
				while((line = br.readLine()) != null){
					count++;
				}
				sizes.add(count);
				br.close();	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		printSizes(sizes);
	}
	
	private void printSizes(List<Integer> sizes){
		System.out.println(sizes);
	}
}
