package com.lu.ml.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ValueProcessing {
	
	public Map<Integer, Double> findMaxValuesForEachFeature(List<Integer> numericIndexList, String filePath){
		Map<Integer, Double> results = new HashMap<Integer, Double>();
		for(int element : numericIndexList){
			results.put(element, 0.0);
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));	
			String line = "";		
			int index = 0;
			while((line = br.readLine()) != null){
				index++;
				if(index < 114){
					continue;
				}
				
				List<String> aLineElement = Arrays.asList(line.split(","));
				for(Entry<Integer, Double> entry : results.entrySet()){
					String value = aLineElement.get(entry.getKey());
					if((!value.equals("?"))&&(Double.parseDouble(value) > entry.getValue())){	
						entry.setValue(Double.parseDouble(value));
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return results;
	}
}
