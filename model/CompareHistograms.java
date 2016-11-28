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
	private double T_sub_S;
	
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
			SD_of_frames.add(new FrameSD(sample.getFileName(), sample2.getFileName(), SD, i, i+1));
		}
		double mean = sum/SD_of_frames.size();
		System.out.println("SDFsize: "+SD_of_frames.size()+" length:"+fileList.length);
		System.out.println("mean: "+mean);
		double temp = 0;
		for(FrameSD a :SD_of_frames){//Variance
            temp += (a.getValue()-mean)*(a.getValue()-mean);
		}
		double sdev = Math.sqrt(temp/SD_of_frames.size());
		T_sub_S = mean+(0.1*sdev);
		System.out.println("Sdev: "+sdev);
		return mean + (alpha*sdev);
	}
	
//	public void doTwinComparison(String imagePath){
//		boolean detectedGradualTransition = false;
//		
//		double T_sub_S = 540.0;	//EXPERIMENT
//		
//		double T_sub_B = computeTsubB(6, imagePath);	//5 -> EXPERIMENT
//		System.out.println("TsubB = "+T_sub_B);
//		
//		double transitionAccumulator = 0;
//		String gradTrans_prevFrame = "";
//		String gradTrans_nextFrame = "";
//		
//		for(int i=0; i<SD_of_frames.size(); i++){
//			FrameSD current = SD_of_frames.get(i);
//			if(SD_of_frames.get(i).getValue() > T_sub_B){
//				cameraBreaks.add(new FrameSD(current.getFrameName1(), current.getFrameName2(), current.getValue()));
//			}else if(SD_of_frames.get(i).getValue() > T_sub_S && SD_of_frames.get(i).getValue() <= T_sub_B){
//				gradTrans_prevFrame = current.getFrameName1();
//				detectedGradualTransition = true;
//				transitionAccumulator += current.getValue();
//			}else if(detectedGradualTransition && (SD_of_frames.get(i).getValue() > T_sub_S && SD_of_frames.get(i).getValue() <= T_sub_B)){
//				transitionAccumulator += current.getValue();
//			}else if(detectedGradualTransition && SD_of_frames.get(i).getValue() <= T_sub_S){
//				gradTrans_nextFrame = current.getFrameName2();
//				detectedGradualTransition = false;
//				transitionAccumulator += current.getValue();
//				if(transitionAccumulator > T_sub_B){
//					cameraBreaks.add(new FrameSD(gradTrans_prevFrame, gradTrans_nextFrame, transitionAccumulator));
//				}
//				transitionAccumulator = 0;
//			}
//		}
//	}
	
	/*public double[] getNormalizedHistogram(int[] histogram, int imageWidth, int imageHeight)
	{
		double[] nH = new double[159];
		
		for(int i=0; i<159; i++)
		{
			nH[i] = (double)histogram[i]/(imageWidth*imageHeight);
		}
		
		return nH;
	}*/
	
	// get shot from 2 camera breaks
	public ArrayList<ImageObject> getShot(FrameSD camBreak1, FrameSD camBreak2, String imageSetPath)
	{
		ArrayList<ImageObject> shot = new ArrayList<ImageObject>();
		
		File folder = new File(imageSetPath);
		File[] fileList = folder.listFiles();
		double sum = 0;
		for(int i=camBreak1.getIndexF2(); i<camBreak2.getIndexF2(); i++)
		{
			ImageObject sample = new ImageObject(imageSetPath, fileList[i].getName());
			sample.initializeHistogram();
			shot.add(sample);
		}
		
		return shot;
	}
	
	public int[] computeAvgHist(ArrayList<ImageObject> shot)
	{
		// assumed na normalized na yung hist ng nasa shot
		int[] aHist = new int[159];
		
		//initialize avg hist
		for(int x = 0; x < 159; x++)
		{
			aHist[x] = 0;
		}
		
		//add all pixels of shot
		for(int x = 0; x < shot.size(); x++)
		{
			for(int y = 0; y < 159; y++)
			{
				aHist[y] += shot.get(x).getHistogram()[y];
			}
		}
		
		// average all pixels
		for(int x = 0; x < 159; x++)
		{
			aHist[x] /= shot.size();
		}
		
		return aHist;
		
	}
	
	public Keyframe getKeyframe(ArrayList<ImageObject> shot)
	{
		int[] aHist = computeAvgHist(shot);
		Keyframe key = new Keyframe();
		int SD = 0;
		boolean first = true;
		
		// loop sa lahat ng nasa shot
		for(int x = 0; x < shot.size(); x++)
		{
			SD = 0;
			for(int j=0; j<aHist.length; j++){
				SD += Math.abs(aHist[j] - shot.get(x).getHistogram()[j]);
			}
			if(first || (!first && key.getDistance() > SD))
			{
				key.setDistance(SD);
				key.setFilename(shot.get(x).getFileName());
			}
			
			first = false;
		}
		
		return key;
	}
	
	public void computeTwinCompare(String imageSetPath){
		boolean detectedGradualTransition = false;
		double T_sub_B = computeTsubB(7, imageSetPath);	
		System.out.println("TsubB: "+T_sub_B);
		System.out.println("TsubS: "+T_sub_S);
		int allowance = 3;
		ImageObject Fs_Frame = null;
		ImageObject Fe_Frame = null;
		double transitionAccumulator = 0;
		File folder = new File(imageSetPath);
		File[] fileList = folder.listFiles();
		double sum = 0;
		for(int i=0; i<fileList.length-1; i++)
		{
			//implement dito lahat, mga if else, tapos yung gradual transition and shit
			ImageObject sample = new ImageObject(imageSetPath, fileList[i].getName());
			sample.initializeHistogram();
			int[] sampleHistogram = sample.getHistogram();
			
			ImageObject sample2 = new ImageObject(imageSetPath, fileList[i+1].getName());
			sample2.initializeHistogram();
			int[] sampleHistogram2 = sample2.getHistogram();
			double SD = 0;
			for(int j=0; j<sampleHistogram.length; j++){
				SD += Math.abs(sampleHistogram[j] - sampleHistogram2[j]);
			}
			
			if(SD > T_sub_B){
				System.out.println("WEW");
				cameraBreaks.add(new FrameSD(sample.getFileName(), sample2.getFileName(), SD, i, i+1));
			}else if(!detectedGradualTransition && (SD > T_sub_S && SD <= T_sub_B)){
				System.out.println("Chosen FS_Frame "+fileList[i+1].getName());
				Fs_Frame = new ImageObject(imageSetPath, fileList[i+1].getName());
				Fs_Frame.initializeHistogram();
				detectedGradualTransition = true;
			}else if(detectedGradualTransition && (SD > T_sub_S && SD <= T_sub_B)){
				System.out.println("compared FS_Frame w/"+sample2.getFileName());
				Fe_Frame = new ImageObject(imageSetPath, fileList[i+1].getName());
				int[] FSHistogram = Fs_Frame.getHistogram();
				
				double tempSD = 0;
				for(int z=0; z<FSHistogram.length; z++){
					tempSD += Math.abs(FSHistogram[z] - sampleHistogram2[z]);
				}
				transitionAccumulator += tempSD;
				
			}else if(detectedGradualTransition && SD <= T_sub_S){
				System.out.println("Less than TsubS");
				if(allowance>0){
					System.out.println("allowance > 0");
					System.out.println("compared FS_Frame w/"+sample2.getFileName());
					Fe_Frame = new ImageObject(imageSetPath, fileList[i+1].getName());
					allowance--;
					System.out.println("Allowance: "+allowance);
					int[] FSHistogram = Fs_Frame.getHistogram();
					double tempSD = 0;
					for(int z=0; z<FSHistogram.length; z++){
						tempSD += Math.abs(FSHistogram[z] - sampleHistogram2[z]);
					}
					transitionAccumulator += tempSD;
				}
				else if(allowance==0){
					if(transitionAccumulator > T_sub_B){
						System.out.println("Accumulator > TsubB");
						detectedGradualTransition = false;
						cameraBreaks.add(new FrameSD(Fs_Frame.getFileName(), Fe_Frame.getFileName(), transitionAccumulator, i, i+1));
						transitionAccumulator = 0;
					}else{
						System.out.println("allowance == 0");
						allowance=3;
						Fs_Frame = null;
						Fe_Frame = null;
						detectedGradualTransition = false;
						transitionAccumulator = 0;
					}
				}
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
