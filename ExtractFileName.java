package com.lu.ml.tool;

import java.util.Arrays;
import java.util.List;

public class ExtractFileName {

	public String getFileName(String filePath){
		List<String> separateFileName = Arrays.asList(filePath.split("\\."));
		return separateFileName.get(0);
	}
	
	public String getFileExtension(String filePath){
		List<String> separateFileName = Arrays.asList(filePath.split("\\."));
		return ("." + separateFileName.get(1));
	}

}
