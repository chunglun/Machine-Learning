package com.lu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;

public class FileAnalysis {

	public static void main(String[] args) {
		FileAnalysis fileanalysis = new FileAnalysis();
		fileanalysis.process();
	}
	
	private void process(){
		List<Integer> sums = Arrays.asList(0, 0);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("training.arff"));
			String aLine = "";
			int index = 0;
			while((aLine = br.readLine()) != null){
				index++;
				if(index < 114){
					continue;
				}
				
				List<String> aLineElement = Arrays.asList(aLine.split(","));
				String result = aLineElement.get(aLineElement.size()-1);
				if(result.equals("0")){
					int pre = sums.get(0);
					pre++;
					sums.set(0, pre);
				}
				else if(result.equals("1")){
					int pre = sums.get(1);
					pre++;
					sums.set(1, pre);
				}
			}
			br.close();

			System.out.println(sums);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
