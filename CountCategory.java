package com.lu.ml.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

public class CountCategory {
	
	public static void main(String[] args) {
		CountCategory countcategory = new CountCategory();
		List<Integer> results = countcategory.process("data/training.arff");
		System.out.println(results);
	}
	
	public List<Integer> process(String filePath){
		List<Integer> results = Arrays.asList(0, 0);

		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = "";
			int index = 0;
			while((line = br.readLine()) != null){
				index++;
				if(index < 114){
					continue;
				}
				
				int intClassReesult = handleALine(line);
				results.set(intClassReesult, results.get(intClassReesult)+1);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
	
	private int handleALine(String line){
		List<String> lineElement = Arrays.asList(line.split(","));
		String classResult = lineElement.get(lineElement.size()-1);
		return Integer.parseInt(classResult);
	}
}
