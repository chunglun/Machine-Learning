package com.lu;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CalculateResult {
	private static final int NUM_FOLDER = 10;
	
	public static void main(String[] args) {
		CalculateResult calculateresult = new CalculateResult();
		calculateresult.process();
	}
	
	private void process(){
		
		//0:TN  1:FN  2:FP  3:TP
		List<Integer> result0 = Arrays.asList(0, 468, 2, 1030);
		List<Integer> result1 = Arrays.asList(0, 493, 6, 1001);
		List<Integer> result2 = Arrays.asList(0, 478, 10, 1012);
		List<Integer> result3 = Arrays.asList(0, 489, 6, 1005);
		List<Integer> result4 = Arrays.asList(0, 472, 3, 1025);
		List<Integer> result5 = Arrays.asList(0, 451, 3, 1046);
		List<Integer> result6 = Arrays.asList(0, 491, 7, 1002);
		List<Integer> result7 = Arrays.asList(0, 459, 2, 1039);
		List<Integer> result8 = Arrays.asList(0, 485, 8, 1007);
		List<Integer> result9 = Arrays.asList(0, 491, 9, 1000);
		
		/*
		List<Integer> result0 = Arrays.asList(234, 234, 220, 812);
		List<Integer> result1 = Arrays.asList(251, 242, 256, 751);
		List<Integer> result2 = Arrays.asList(252, 226, 227, 795);
		List<Integer> result3 = Arrays.asList(253, 236, 198, 813);
		List<Integer> result4 = Arrays.asList(224, 248, 248, 780);
		List<Integer> result5 = Arrays.asList(249, 202, 217, 832);
		List<Integer> result6 = Arrays.asList(232, 259, 185, 824);
		List<Integer> result7 = Arrays.asList(214, 245, 242, 799);
		List<Integer> result8 = Arrays.asList(217, 268, 224, 791);
		List<Integer> result9 = Arrays.asList(231, 260, 207, 802);
		*/
		
		Map<Integer, List<Integer>> results = new HashMap<Integer, List<Integer>>();
		results.put(0, result0);
		results.put(1, result1);
		results.put(2, result2);
		results.put(3, result3);
		results.put(4, result4);
		results.put(5, result5);
		results.put(6, result6);
		results.put(7, result7);
		results.put(8, result8);
		results.put(9, result9);
		
		calculatePrecision(results);
	}
	
	private void calculatePrecision(Map<Integer, List<Integer>> results){
		Map<Integer, Double> precisions = new HashMap<Integer, Double>();
		Map<Integer, Double> recalls = new HashMap<Integer, Double>();
		Map<Integer, Double> accuracies = new HashMap<Integer, Double>();
		
		//0:TN  1:FN  2:FP  3:TP
		for(int i = 0; i < NUM_FOLDER; i++){
			double tp = results.get(i).get(3); 
			double fp = results.get(i).get(2); 
			double fn = results.get(i).get(1); 
			double tn = results.get(i).get(0); 
			
			precisions.put(i, (tp / (tp + fp)));
			recalls.put(i, (tp / (tp + fn)));
			accuracies.put(i, ((tp + tn)/ (tp + tn + fp + fn)));
		}
		
		double avg_precision = 0.0;
		for(Entry<Integer, Double> entry : precisions.entrySet()){
			avg_precision += entry.getValue();
		}
		avg_precision /= ((double)NUM_FOLDER);
		
		double avg_recall = 0.0;
		for(Entry<Integer, Double> entry : recalls.entrySet()){
			avg_recall += entry.getValue();
		}
		avg_recall /= ((double)NUM_FOLDER);
		
		double avg_accuracy = 0.0;
		for(Entry<Integer, Double> entry : accuracies.entrySet()){
			avg_accuracy += entry.getValue();
		}
		avg_accuracy /= ((double)NUM_FOLDER);
		
		System.out.println("avg_precision: " + avg_precision);
		System.out.println("avg_recall: " + avg_recall);
		System.out.println("avg_accuracy: " + avg_accuracy);
		
		
		double se_precision = 0.0;
		for(Entry<Integer, Double> entry : precisions.entrySet()){
			se_precision += Math.pow((entry.getValue() - avg_precision), 2);
		}
		se_precision = Math.sqrt(se_precision / (NUM_FOLDER - 1));
		System.out.println("se_precision: " + se_precision);
		
		double se_recall = 0.0;
		for(Entry<Integer, Double> entry : recalls.entrySet()){
			se_recall += Math.pow((entry.getValue() - avg_recall), 2);
		}
		se_recall = Math.sqrt(se_recall / (NUM_FOLDER - 1));
		System.out.println("se_recall: " + se_recall);
		
		double se_accuracy = 0.0;
		for(Entry<Integer, Double> entry : accuracies.entrySet()){
			se_accuracy += Math.pow((entry.getValue() - avg_accuracy), 2);
		}
		se_accuracy = Math.sqrt(se_accuracy / (NUM_FOLDER - 1));
		System.out.println("se_accuracy: " + se_accuracy);
		
		double f1 = (2 * avg_precision * avg_recall) / (avg_precision + avg_recall);
		System.out.println("f1: " + f1);
	}
}
