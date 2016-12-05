package controller;

import java.util.ArrayList;

import model.VideoProcessor;
import model.FrameSD;
import model.ImageObject;
import model.Keyframe;
import preprocess.Load;

public class Controller {
	VideoProcessor comparator;
	Load load;
	
	public Controller(){
		comparator = new VideoProcessor();
		load = new Load();
	}
	
	public void processVideo(int alpha, double ts, String imagePath){
		comparator = new VideoProcessor();
		comparator.doTwinCompare(alpha, ts, imagePath);
		//comparator.printAllSD();
		comparator.printCameraBreaks();
		comparator.computeForKeyframes(imagePath);
		comparator.printKeyframes();
	}
	
	public void preprocessedProcessVideo( int alpha, double ts, int imageSetChoice )
	{
		comparator = new VideoProcessor();
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
