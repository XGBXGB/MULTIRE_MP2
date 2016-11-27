package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.CenteringTest.Data;

public class CompareHistograms 
{
	private ArrayList<FrameSD> SD_of_frames;
	private ArrayList<FrameSD> cameraBreaks;
	
	public CompareHistograms(){
		SD_of_frames = new ArrayList();
		cameraBreaks = new ArrayList();
	}
	
	public double computeTsubB(int alpha, String imageSetPath){
		File folder = new File(imageSetPath);
		File[] fileList = folder.listFiles();
		
		double sum = 0;
		for(int i=0; i<fileList.length-1; i++)
		{
			ImageObject sample = new ImageObject(imageSetPath, fileList[i].getName());
			sample.initializeHistogram();
			int[] sampleHistogram = sample.getHistogram();
			
			ImageObject sample2 = new ImageObject(imageSetPath, fileList[i+1].getName());
			sample2.initializeHistogram();
			int[] sampleHistogram2 = sample2.getHistogram();
			double SD = 0;
			for(int j=0; j<sampleHistogram.length; j++){
				SD += Math.abs(sampleHistogram[j] - sampleHistogram2[j]);
				sum += Math.abs(sampleHistogram[j] - sampleHistogram2[j]);
			}
			SD_of_frames.add(new FrameSD(sample.getFileName(), sample2.getFileName(), SD));
		}
		double mean = sum/fileList.length;
		double temp = 0;
		for(FrameSD a :SD_of_frames){//Variance
            temp += (a.getValue()-mean)*(a.getValue()-mean);
		}
		double sdev = Math.sqrt(temp/SD_of_frames.size());
		return mean + (alpha*sdev);
	}
	
	public void doTwinComparison(String imagePath){
		boolean detectedGradualTransition = false;
		
		double T_sub_S = 540.0;	//EXPERIMENT
		
		double T_sub_B = computeTsubB(6, imagePath);	//5 -> EXPERIMENT
		System.out.println("TsubB = "+T_sub_B);
		
		double transitionAccumulator = 0;
		String gradTrans_prevFrame = "";
		String gradTrans_nextFrame = "";
		
		for(int i=0; i<SD_of_frames.size(); i++){
			FrameSD current = SD_of_frames.get(i);
			if(SD_of_frames.get(i).getValue() > T_sub_B){
				cameraBreaks.add(new FrameSD(current.getFrameName1(), current.getFrameName2(), current.getValue()));
			}else if(SD_of_frames.get(i).getValue() > T_sub_S && SD_of_frames.get(i).getValue() <= T_sub_B){
				gradTrans_prevFrame = current.getFrameName1();
				detectedGradualTransition = true;
				transitionAccumulator += current.getValue();
			}else if(detectedGradualTransition && (SD_of_frames.get(i).getValue() > T_sub_S && SD_of_frames.get(i).getValue() <= T_sub_B)){
				transitionAccumulator += current.getValue();
			}else if(detectedGradualTransition && SD_of_frames.get(i).getValue() <= T_sub_S){
				gradTrans_nextFrame = current.getFrameName2();
				detectedGradualTransition = false;
				transitionAccumulator += current.getValue();
				if(transitionAccumulator > T_sub_B){
					cameraBreaks.add(new FrameSD(gradTrans_prevFrame, gradTrans_nextFrame, transitionAccumulator));
				}
				transitionAccumulator = 0;
			}
		}
	}
	
	public void printCameraBreaks(){
		System.out.println("CAMERA BREAKS:");
		for(int i=0; i<cameraBreaks.size(); i++){
			System.out.println("Between "+cameraBreaks.get(i).getFrameName1()+" and "+cameraBreaks.get(i).getFrameName2());
		}
	}
	
	public void printAllSD(){
		System.out.println("All SD:");
		for(int i=0; i<SD_of_frames.size(); i++){
			System.out.println("Between "+SD_of_frames.get(i).getFrameName1()+" and "+SD_of_frames.get(i).getFrameName2()+" == "+SD_of_frames.get(i).getValue());
		}
	}
	
	
}
