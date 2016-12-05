package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VideoProcessor
{
	private ArrayList<FrameSD> SD_of_frames;
	private ArrayList<FrameSD> cameraBreaks;
	private ArrayList<Keyframe> keyframes;
	private double T_sub_S;

	public VideoProcessor()
	{
		SD_of_frames = new ArrayList();
		cameraBreaks = new ArrayList();
		keyframes = new ArrayList();
	}

	public ArrayList<FrameSD> getCameraBreaks()
	{
		return cameraBreaks;
	}

	public double computeTsubB(int alpha, String imageSetPath)
	{
		File folder = new File(imageSetPath);
		File[] fileList = folder.listFiles();

		double sum = 0;
		for (int i = 0; i < fileList.length - 1; i++)
		{
			ImageObject sample = new ImageObject(imageSetPath, fileList[i].getName());
			sample.initializeHistogram();
			int[] sampleHistogram = sample.getHistogram();

			ImageObject sample2 = new ImageObject(imageSetPath, fileList[i + 1].getName());
			sample2.initializeHistogram();
			int[] sampleHistogram2 = sample2.getHistogram();
			double SD = 0;
			for (int j = 0; j < sampleHistogram.length; j++)
			{
				SD += Math.abs(sampleHistogram[j] - sampleHistogram2[j]);
				sum += Math.abs(sampleHistogram[j] - sampleHistogram2[j]);
			}
			SD_of_frames.add(new FrameSD(sample.getFileName(), sample2.getFileName(), SD, i, i + 1));
		}
		double mean = sum / SD_of_frames.size();
		System.out.println("SDFsize: " + SD_of_frames.size() + " length:" + fileList.length);
		System.out.println("mean: " + mean);
		double temp = 0;
		for (FrameSD a : SD_of_frames)
		{// Variance
			temp += (a.getValue() - mean) * (a.getValue() - mean);
		}
		double sdev = Math.sqrt(temp / SD_of_frames.size());
		// T_sub_S = mean+(0.1*sdev);
		// T_sub_S = (mean + (alpha * sdev)) / 4;
		//T_sub_S = 0.2 * (mean + (alpha * sdev));
		//T_sub_S = (mean + (alpha * sdev)) / 2;
		System.out.println("Sdev: " + sdev);
		System.out.println("alpha: " + alpha);
		return mean + (alpha * sdev);
	}

	public double preprocessedComputeTsubB(int alpha, ArrayList<ImageObject> images)
	{
		double sum = 0;
		for (int i = 0; i < images.size() - 1; i++)
		{
			double SD = 0;
			
			for (int j = 0; j < images.get(i).getHistogram().length; j++)
			{
				SD += Math.abs(images.get(i).getHistogram()[j] - images.get(i + 1).getHistogram()[j]);
				sum += Math.abs(images.get(i).getHistogram()[j] - images.get(i + 1).getHistogram()[j]);
			}
			SD_of_frames.add(new FrameSD(images.get(i).getFileName(), images.get(i + 1).getFileName(), SD, i, i + 1));
		}
		double mean = sum / SD_of_frames.size();
		System.out.println("SDFsize: " + SD_of_frames.size() + " length:" + images.size());
		System.out.println("mean: " + mean);
		double temp = 0;
		for (FrameSD a : SD_of_frames)
		{// Variance
			temp += (a.getValue() - mean) * (a.getValue() - mean);
		}
		double sdev = Math.sqrt(temp / SD_of_frames.size());
		// T_sub_S = mean+(0.1*sdev);
		// T_sub_S = (mean + (alpha * sdev)) / 4;
		//T_sub_S = 0.2 * (mean + (alpha * sdev));
		//T_sub_S = (mean + (alpha * sdev)) / 2;
		System.out.println("Sdev: " + sdev);
		System.out.println("alpha: " + alpha);
		return mean + (alpha * sdev);
	}
	
	

	public void computeForKeyframes(String imagePath)
	{
		for (int x = 0; x < cameraBreaks.size() - 1; x++)
		{
			if (cameraBreaks.get(x).getType().equals("Abrupt"))
			{
				keyframes.add(getKeyframe(getShot(cameraBreaks.get(x), cameraBreaks.get(x + 1), imagePath)));
				keyframes.get(keyframes.size() - 1).setLowerBoundary(cameraBreaks.get(x).getFrameName1());
				keyframes.get(keyframes.size() - 1).setUpperBoundary(cameraBreaks.get(x + 1).getFrameName1());
			}
			else
			{
				keyframes.add(getKeyframe(getShot(cameraBreaks.get(x), cameraBreaks.get(x), imagePath)));
				keyframes.get(keyframes.size() - 1).setLowerBoundary(cameraBreaks.get(x).getFrameName1());
				keyframes.get(keyframes.size() - 1).setUpperBoundary(cameraBreaks.get(x).getFrameName2());
			}
		}
	}

	public void preprocessedComputeForKeyframes(ArrayList<ImageObject> images)
	{
		for (int x = 0; x < cameraBreaks.size() - 1; x++)
		{
			if (cameraBreaks.get(x).getType().equals("Abrupt"))
			{
				keyframes.add(getKeyframe(preprocessedGetShot(cameraBreaks.get(x), cameraBreaks.get(x + 1), images)));
				keyframes.get(keyframes.size() - 1).setLowerBoundary(cameraBreaks.get(x).getFrameName1());
				keyframes.get(keyframes.size() - 1).setUpperBoundary(cameraBreaks.get(x + 1).getFrameName1());
			}
			else
			{
				keyframes.add(getKeyframe(preprocessedGetShot(cameraBreaks.get(x), cameraBreaks.get(x), images)));
				keyframes.get(keyframes.size() - 1).setLowerBoundary(cameraBreaks.get(x).getFrameName1());
				keyframes.get(keyframes.size() - 1).setUpperBoundary(cameraBreaks.get(x).getFrameName2());
			}
		}
	}
	
	// get shot from 2 camera breaks
	public ArrayList<ImageObject> getShot(FrameSD camBreak1, FrameSD camBreak2, String imageSetPath)
	{
		ArrayList<ImageObject> shot = new ArrayList<ImageObject>();

		File folder = new File(imageSetPath);
		File[] fileList = folder.listFiles();
		double sum = 0;
		for (int i = camBreak1.getIndexF1(); i < camBreak2.getIndexF2(); i++)
		{
			ImageObject sample = new ImageObject(imageSetPath, fileList[i].getName());
			sample.initializeHistogram();
			shot.add(sample);
		}

		return shot;
	}

	public ArrayList<ImageObject> preprocessedGetShot(FrameSD camBreak1, FrameSD camBreak2, ArrayList<ImageObject> images)
	{
		ArrayList<ImageObject> shot = new ArrayList<ImageObject>();

		double sum = 0;
		for (int i = camBreak1.getIndexF1(); i < camBreak2.getIndexF2(); i++)
		{
			ImageObject sample = new ImageObject(images.get(i).getFileName(), images.get(i).getHistogram());
			shot.add(sample);
		}

		return shot;
	}
	
	public int[] computeAvgHist(ArrayList<ImageObject> shot)
	{
		// assumed na normalized na yung hist ng nasa shot
		int[] aHist = new int[159];

		// initialize avg hist
		for (int x = 0; x < 159; x++)
		{
			aHist[x] = 0;
		}

		// add all pixels of shot
		for (int x = 0; x < shot.size(); x++)
		{
			for (int y = 0; y < 159; y++)
			{
				aHist[y] += shot.get(x).getHistogram()[y];
			}
		}

		// average all pixels
		for (int x = 0; x < 159; x++)
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
		for (int x = 0; x < shot.size(); x++)
		{
			SD = 0;
			for (int j = 0; j < aHist.length; j++)
			{
				SD += Math.abs(aHist[j] - shot.get(x).getHistogram()[j]);
			}
			if (first || (!first && key.getDistance() > SD))
			{
				key.setDistance(SD);
				key.setFilename(shot.get(x).getFileName());
			}

			first = false;
		}

		return key;
	}

	public void doTwinCompare(int alpha, double ts, String imageSetPath)
	{
		boolean detectedGradualTransition = false;
		double T_sub_B = computeTsubB(alpha, imageSetPath);
		T_sub_S = ts * T_sub_B;
		System.out.println("TsubB: " + T_sub_B);
		System.out.println("TsubS: " + T_sub_S);
		int allowance = 3;
		ImageObject Fs_Frame = null;
		ImageObject Fe_Frame = null;
		int FsIndex = 0;
		int FeIndex = 0;
		double transitionAccumulator = 0;
		File folder = new File(imageSetPath);
		File[] fileList = folder.listFiles();
		double sum = 0;
		for (int i = 0; i < fileList.length - 1; i++)
		{
			// implement dito lahat, mga if else, tapos yung gradual
			// transition and shit
			ImageObject sample = new ImageObject(imageSetPath, fileList[i].getName());
			sample.initializeHistogram();
			int[] sampleHistogram = sample.getHistogram();

			ImageObject sample2 = new ImageObject(imageSetPath, fileList[i + 1].getName());
			sample2.initializeHistogram();
			int[] sampleHistogram2 = sample2.getHistogram();
			double SD = 0;
			for (int j = 0; j < sampleHistogram.length; j++)
			{
				SD += Math.abs(sampleHistogram[j] - sampleHistogram2[j]);
			}
			// System.out.println("SD: " + SD);
			if (SD > T_sub_B)
			{
				cameraBreaks.add(new FrameSD(sample.getFileName(), sample2.getFileName(), SD, i, i + 1));
				cameraBreaks.get(cameraBreaks.size() - 1).setType("Abrupt");
			}
			else if (!detectedGradualTransition && (SD > T_sub_S && SD <= T_sub_B))
			{
				System.out.println("Chosen FS_Frame " + fileList[i + 1].getName());
				Fs_Frame = new ImageObject(imageSetPath, fileList[i + 1].getName());
				Fs_Frame.initializeHistogram();
				FsIndex = i;
				detectedGradualTransition = true;
			}
			else if (detectedGradualTransition && (SD > T_sub_S && SD <= T_sub_B))
			{
				System.out.println("compared FS_Frame w/" + sample2.getFileName());
				Fe_Frame = new ImageObject(imageSetPath, fileList[i + 1].getName());
				int[] FSHistogram = Fs_Frame.getHistogram();
				FeIndex = i;
				double tempSD = 0;
				for (int z = 0; z < FSHistogram.length; z++)
				{
					tempSD += Math.abs(FSHistogram[z] - sampleHistogram2[z]);
				}
				transitionAccumulator += tempSD;
			}
			else if (detectedGradualTransition && SD <= T_sub_S)
			{
				System.out.println("Less than TsubS");
				if (allowance > 0)
				{
					System.out.println("allowance > 0");
					System.out.println("compared FS_Frame w/" + sample2.getFileName());
					Fe_Frame = new ImageObject(imageSetPath, fileList[i + 1].getName());
					FeIndex = i;
					allowance--;
					System.out.println("Allowance: " + allowance);
					int[] FSHistogram = Fs_Frame.getHistogram();
					double tempSD = 0;
					for (int z = 0; z < FSHistogram.length; z++)
					{
						tempSD += Math.abs(FSHistogram[z] - sampleHistogram2[z]);
					}
					transitionAccumulator += tempSD;
				}
				else if (allowance == 0)
				{
					if (transitionAccumulator > T_sub_B)
					{
						System.out.println("Accumulator > TsubB");
						detectedGradualTransition = false;
						cameraBreaks.add(new FrameSD(Fs_Frame.getFileName(), Fe_Frame.getFileName(),
								transitionAccumulator, FsIndex, FeIndex));
						cameraBreaks.get(cameraBreaks.size() - 1).setType("Gradual");
						transitionAccumulator = 0;
					}
					else
					{
						System.out.println("allowance == 0");
						allowance = 3;
						Fs_Frame = null;
						FsIndex = 0;
						Fe_Frame = null;
						FeIndex = 0;
						detectedGradualTransition = false;
						transitionAccumulator = 0;
					}
				}
			}
		}
	}

	public void preprocessedComputeTwinCompare(int alpha, double ts, ArrayList<ImageObject> images)
	{
		boolean detectedGradualTransition = false;
		double T_sub_B = preprocessedComputeTsubB(alpha, images);
		T_sub_S = ts * T_sub_B;
		System.out.println("TsubB: " + T_sub_B);
		System.out.println("TsubS: " + T_sub_S);
		int allowance = 3;
		ImageObject Fs_Frame = null;
		ImageObject Fe_Frame = null;
		int FsIndex = 0;
		int FeIndex = 0;
		double transitionAccumulator = 0;

		double sum = 0;
		for (int i = 0; i < images.size() - 1; i++)
		{
			// implement dito lahat, mga if else, tapos yung gradual
			// transition and shit
			double SD = 0;
			
			for (int j = 0; j < images.get(i).getHistogram().length; j++)
			{
				SD += Math.abs(images.get(i).getHistogram()[j] - images.get(i + 1).getHistogram()[j]);
			}
			// System.out.println("SD: " + SD);
			if (SD > T_sub_B)
			{
				System.out.println("WEW");
				cameraBreaks.add(new FrameSD(images.get(i).getFileName(), images.get(i + 1).getFileName(), SD, i, i + 1));
				cameraBreaks.get(cameraBreaks.size() - 1).setType("Abrupt");
			}
			else if (!detectedGradualTransition && (SD > T_sub_S && SD <= T_sub_B))
			{
				System.out.println("Chosen FS_Frame " + images.get(i + 1).getFileName());
				Fs_Frame = new ImageObject(images.get(i + 1).getFileName(), images.get(i + 1).getHistogram());
				FsIndex = i;
				detectedGradualTransition = true;
			}
			else if (detectedGradualTransition && (SD > T_sub_S && SD <= T_sub_B))
			{
				System.out.println("compared FS_Frame w/" + images.get(i + 1).getFileName());
				Fe_Frame = new ImageObject(images.get(i + 1).getFileName(), images.get(i + 1).getHistogram());
				int[] FSHistogram = Fs_Frame.getHistogram();
				FeIndex = i;
				double tempSD = 0;
				for (int z = 0; z < FSHistogram.length; z++)
				{
					tempSD += Math.abs(FSHistogram[z] - images.get(i + 1).getHistogram()[z]);
				}
				transitionAccumulator += tempSD;
			}
			else if (detectedGradualTransition && SD <= T_sub_S)
			{
				System.out.println("Less than TsubS");
				if (allowance > 0)
				{
					System.out.println("allowance > 0");
					System.out.println("compared FS_Frame w/" + images.get(i + 1).getFileName());
					Fe_Frame = new ImageObject(images.get(i + 1).getFileName(), images.get(i + 1).getHistogram());
					FeIndex = i;
					allowance--;
					System.out.println("Allowance: " + allowance);
					int[] FSHistogram = Fs_Frame.getHistogram();
					double tempSD = 0;
					for (int z = 0; z < FSHistogram.length; z++)
					{
						tempSD += Math.abs(FSHistogram[z] - images.get(i + 1).getHistogram()[z]);
					}
					transitionAccumulator += tempSD;
				}
				else if (allowance == 0)
				{
					if (transitionAccumulator > T_sub_B)
					{
						System.out.println("Accumulator > TsubB");
						detectedGradualTransition = false;
						cameraBreaks.add(new FrameSD(Fs_Frame.getFileName(), Fe_Frame.getFileName(),
								transitionAccumulator, FsIndex, FeIndex));
						cameraBreaks.get(cameraBreaks.size() - 1).setType("Gradual");
						transitionAccumulator = 0;
					}
					else
					{
						System.out.println("allowance == 0");
						allowance = 3;
						Fs_Frame = null;
						FsIndex = 0;
						Fe_Frame = null;
						FeIndex = 0;
						detectedGradualTransition = false;
						transitionAccumulator = 0;
					}
				}
			}
		}
	}
	
	public void printCameraBreaks()
	{
		System.out.println("CAMERA BREAKS:");
		for (int i = 0; i < cameraBreaks.size(); i++)
		{
			System.out.println("Between " + cameraBreaks.get(i).getFrameName1() + " and "
					+ cameraBreaks.get(i).getFrameName2() + " = " + cameraBreaks.get(i).getValue() + " -> "
					+ cameraBreaks.get(i).getType());
		}
	}

	public void printAllSD()
	{
		System.out.println("All SD:");
		for (int i = 0; i < SD_of_frames.size(); i++)
		{
			System.out.println("Between " + SD_of_frames.get(i).getFrameName1() + " and "
					+ SD_of_frames.get(i).getFrameName2() + " == " + SD_of_frames.get(i).getValue());
		}
	}

	public void printKeyframes()
	{
		System.out.println("All keyframes:");
		for (int i = 0; i < keyframes.size(); i++)
		{
			System.out.println(
					"From: " + keyframes.get(i).getLowerBoundary() + " to " + keyframes.get(i).getUpperBoundary()
							+ ": " + keyframes.get(i).getFilename() + " -> " + keyframes.get(i).getDistance());
		}
	}

	public ArrayList<FrameSD> getAllCameraBreaks()
	{
		return cameraBreaks;
	}
	
	public ArrayList<Keyframe> getAllKeyFrames()
	{
		return keyframes;
	}
}
