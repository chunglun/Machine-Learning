package com.lu.ml.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class WekaToSVM {
	private String fileName;
	private String fileExtension;
	private List<Integer> numericIndexList;
	private List<Integer> categoryIndexList;
	
	public static void main(String[] args) {
		WekaToSVM wekatosvm = new WekaToSVM();
		wekatosvm.process("data/training.arff");
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
		categoryIndexList = Arrays.asList(1, 7, 37, 38, 48);
	}
	
	private void convert(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName + fileExtension));
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName + "_svm_" + getCurrentDatetime() + ".txt"));
			
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

		result += aLineElement.get(aLineElement.size()-1) + " ";
		int count = 1;
		for(int i = 0; i < aLineElement.size()-1; i++){
			String s = aLineElement.get(i);
						
			if(numericIndexList.contains(i)){
				if(s.equals("?")){
					result += (count + ":-1 ");
				}	
				else {
					result += (count + ":" + s + " ");
				}
				count++;
			}
			else if(!categoryIndexList.contains(i)){
				if(s.equals("?")){
					result += (count + ":-1 ");
				}
				else if(s.equals("TRUE")){
					result += (count + ":1 ");
				}
				else if(s.equals("FALSE")){
					result += (count + ":0 ");
				}
				count++;
			}
			else if(i == 1){
				if(s.equals("?")){
					result += (count + ":-1 ");
				}
				else if(s.equals("male")){
					result += (count + ":1 ");
				}
				else if(s.equals("female")){
					result += (count + ":0 ");
				}
				count++;
			}
		}
		
		return result;
	}
	
	private String getCurrentDatetime(){
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date current = new Date();
		return dateFormat.format(current);
	}
}
