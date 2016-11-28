package controller;

import model.CompareHistograms;

public class Controller {
	CompareHistograms comparator;
	
	public Controller(){
		comparator = new CompareHistograms();
	}
	
	public void doTwinComparison(String imagePath){
		comparator.computeTwinCompare(imagePath);
		comparator.printAllSD();
		comparator.printCameraBreaks();
	}
}
