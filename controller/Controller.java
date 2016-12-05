package controller;

import java.util.ArrayList;

import model.CompareHistograms;
import model.FrameSD;
import model.ImageObject;
import model.Keyframe;
import preprocess.Load;

public class Controller {
	CompareHistograms comparator;
	Load load;
	
	public Controller(){
		comparator = new CompareHistograms();
		load = new Load();
	}
	
	public void doTwinComparison(int alpha, double ts, String imagePath){
		comparator = new CompareHistograms();
		comparator.computeTwinCompare(alpha, ts, imagePath);
		//comparator.printAllSD();
		comparator.printCameraBreaks();
		comparator.computeForKeyframes(imagePath);
		comparator.printKeyframes();
	}
	
	public void preprocessedDoTwinComparison( int alpha, double ts, int imageSetChoice )
	{
		comparator = new CompareHistograms();
		ArrayList<ImageObject> images = new ArrayList<>(0);
		
		switch( imageSetChoice )
		{
			case 0: images = load.getUni(); break;
			case 1: images = load.getMjack(); break;
			case 2: images = load.get777(); break;
			case 3: images = load.getStanford(); break;
			case 4: images = load.getMj(); break;
		}
		
		comparator.preprocessedComputeTwinCompare(alpha, ts, images);
		comparator.printCameraBreaks();
		comparator.preprocessedComputeForKeyframes(images);
		comparator.printKeyframes();
	}
	
	public ArrayList<FrameSD> getAllCameraBreaks()
	{
		return comparator.getAllCameraBreaks();
	}
	
	public ArrayList<Keyframe> getAllKeyFrames()
	{
		return comparator.getAllKeyFrames();
	}
}
