package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.CenteringTest.Data;

public class CompareHistograms 
{

	public double[] getNormalizedHistogram(int[] histogram, int imageWidth, int imageHeight)
	{
		double[] nH = new double[159];
		
		for(int i=0; i<159; i++)
		{
			nH[i] = (double)histogram[i]/(imageWidth*imageHeight);
			//System.out.println("sample double value: "+nH[i]+" "+histogram[i]+" "+imageWidth+" "+imageHeight);
		}
		
		return nH;
	}
	
	public double getSimilarity(double sig, int[] histogram1, int imageWidth1, int imageHeight1, int[] histogram2, int imageWidth2, int imageHeight2)
	{
//		for(int i=0; i<histogram1.length; i++){
//			System.out.println("hVal: "+histogram1[i]);
//		}
		double[] nh1 = getNormalizedHistogram(histogram1, imageWidth1, imageHeight1);
		double[] nh2 = getNormalizedHistogram(histogram2, imageWidth2, imageHeight2);
		int bigN = 0;
		
		double sum = 0;
		for(int i=0; i<159; i++)
		{
			if(nh1[i]>sig)
			{
				bigN++;
				sum += 1-(Math.abs(nh1[i]-nh2[i])/Math.max(nh1[i], nh2[i]));
			}
		}
		return sum/bigN;
	}
	
	public ArrayList<ResultImageData> compare(double sig, String imagePath1, String imageFilename1, String imagesRepo)
	{
		ArrayList<ResultImageData> images = new ArrayList();
		ImageObject basis = new ImageObject(imagePath1, imageFilename1);
		int basisWidth = basis.getImageObject().getWidth();
		int basisHeight = basis.getImageObject().getHeight();
		basis.initializeHistogram();
		int[] basisHistogram = basis.getHistogram();
		
		File folder = new File(imagesRepo);
		File[] fileList = folder.listFiles();
		
		for(int i=0; i<1703; i++)
		{
			ImageObject sample = new ImageObject(imagesRepo, fileList[i].getName());
			int sampleWidth = sample.getImageObject().getWidth();
			int sampleHeight = sample.getImageObject().getHeight();
			sample.initializeHistogram();
			int[] sampleHistogram = sample.getHistogram();
			double weight = getSimilarity(sig, basisHistogram, basisWidth, basisHeight, sampleHistogram, sampleWidth, sampleHeight);
			images.add(new ResultImageData(sample.getFileName(), weight));
		}
		
		Collections.sort(images, new Comparator<ResultImageData>() 
		{
	        @Override public int compare(ResultImageData img1, ResultImageData img2) 
	        {
	        	return Double.compare(img2.getValue(), img1.getValue());
	        }

		});
		
		return images;
	}
	
	public double computeTsubB(int alpha, String imageSetPath){
		File folder = new File(imageSetPath);
		File[] fileList = folder.listFiles();
		double[] data = new double[fileList.length-1];
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
			data[i] = SD;
		}
		double mean = sum/fileList.length;
		double temp = 0;
		for(double a :data){//Variance
            temp += (a-mean)*(a-mean);
		}
		double sdev = Math.sqrt(temp/data.length);
		return mean + (alpha*sdev);
	}
	
	// PERCEPTUAL SIMILARITY
	// Q -> Query -> Chosen Image
	// S -> Sample -> Database Image/s
	public double getPerceptualSimilarity( double threshold, int[] histogramQ, int imageWidthQ, int imageHeightQ, int[] histogramS, int imageWidthS, int imageHeightS, double[][] similarityMatrix )
	{
		double perceptualSimilarity = 0;
		double[] nhQ = getNormalizedHistogram(histogramQ, imageWidthQ, imageHeightQ);
		double[] nhS = getNormalizedHistogram(histogramS, imageWidthS, imageHeightS);
		
		for( int i = 0; i < 159; i++ )
		{
			if( nhQ[i] > threshold )
			{
				perceptualSimilarity += getSimExactCol(nhQ[i], nhS[i]) * (1 + getSimPerCol(i, nhQ[i], nhS, similarityMatrix)) * nhQ[i];
			}
		}
		
		return perceptualSimilarity;
	}
	
	public double getSimExactCol( double colorQ, double colorS )
	{
		return 1 - (Math.abs(colorQ - colorS) / Double.max(colorQ, colorS));
	}
	
	public double getSimPerCol( int index, double histogramQ, double[] nhS, double[][] similarityMatrix )
	{
		double perCol = 0;
		
		for( int j = 0; j < 159; j++ )
		{
			perCol += 1 - (Math.abs(histogramQ - nhS[j]) / Double.max(histogramQ, nhS[j])) * similarityMatrix[index][j];
		}
		
		return perCol;
	}
	
	public ArrayList<ResultImageData> comparePerceptualSimilarity( double threshold, String imagePath1, String imageFilename1, String imagesRepo, double[][] similarityMatrix )
	{
		ArrayList<ResultImageData> images = new ArrayList();
		ImageObject basis = new ImageObject(imagePath1, imageFilename1);
		int basisWidth = basis.getImageObject().getWidth();
		int basisHeight = basis.getImageObject().getHeight();
		basis.initializeHistogram();
		int[] basisHistogram = basis.getHistogram();
		
		File folder = new File(imagesRepo);
		File[] fileList = folder.listFiles();
		
		for(int i=0; i<1703; i++)
		{
			ImageObject sample = new ImageObject(imagesRepo, fileList[i].getName());
			int sampleWidth = sample.getImageObject().getWidth();
			int sampleHeight = sample.getImageObject().getHeight();
			sample.initializeHistogram();
			int[] sampleHistogram = sample.getHistogram();
			double weight = getPerceptualSimilarity(threshold, basisHistogram, basisWidth, basisHeight, sampleHistogram, sampleWidth, sampleHeight, similarityMatrix);
			images.add(new ResultImageData(sample.getFileName(), weight));
		}
		
		Collections.sort(images, new Comparator<ResultImageData>() 
		{
	        @Override public int compare(ResultImageData img1, ResultImageData img2) 
	        {
	        	return Double.compare(img2.getValue(), img1.getValue());
	        }

		});
		
		return images;
	}
	
	public int[][] get2DHist(int[] histogram, int height, int width)
	{
		int[][] histogram2 = new int[height][width];
		int index = 0;
		
		for(int x = 0; x < height; x++)
		{
			for(int y = 0; y < width; y++)
			{
				histogram2[x][y] = histogram[index];
				System.out.println("Index "+index);
				index++;
			}
		}
		
		return histogram2;
		
	}
	
	public ArrayList<ResultImageData> compareWithCR(int percent, String imagePath1, String imageFilename1, String imagesRepo)
	{
		ArrayList<ResultImageData> images = new ArrayList();
		ImageObject basis = new ImageObject(imagePath1, imageFilename1);
		int basisWidth = basis.getImageObject().getWidth();
		int basisHeight = basis.getImageObject().getHeight();
		basis.initializeHistogramCR(percent);
		int[] basisHistogramCenter= basis.getHistogramCenter();
		int[] basisHistogramNonCenter= basis.getHistogramNonCenter();
		
		
		File folder = new File(imagesRepo);
		File[] fileList = folder.listFiles();
		
		for(int i=0; i<1703; i++)
		{
			ImageObject sample = new ImageObject(imagesRepo, fileList[i].getName());
			int sampleWidth = sample.getImageObject().getWidth();
			int sampleHeight = sample.getImageObject().getHeight();
			sample.initializeHistogramCR(percent);
			int[] sampleHistogramCenter = sample.getHistogramCenter();
			int[] sampleHistogramNonCenter = sample.getHistogramNonCenter();
			
			double weightCenter = getSimilarity(0, basisHistogramCenter, basisWidth, basisHeight, sampleHistogramCenter, sampleWidth, sampleHeight);
			double weightNonCenter = getSimilarity(0, basisHistogramNonCenter, basisWidth, basisHeight, sampleHistogramNonCenter, sampleWidth, sampleHeight);
			images.add(new ResultImageData(sample.getFileName(), (weightCenter+weightNonCenter)/2.0));
		}
		
		Collections.sort(images, new Comparator<ResultImageData>() 
		{
	        @Override public int compare(ResultImageData img1, ResultImageData img2) 
	        {
	        	return Double.compare(img2.getValue(), img1.getValue());
	        }

		});
		
		return images;
	}
	
	public ArrayList<ResultImageData> compareCCV4(String imagePath1, String imageFilename1, String imagesRepo, int threshold, int nColors)
	{
		ArrayList<ResultImageData> images = new ArrayList<ResultImageData>();
		ImageObject basis = new ImageObject(imagePath1, imageFilename1);
		int basisWidth = basis.getImageObject().getWidth();
		int basisHeight = basis.getImageObject().getHeight();
		basis.initialize2DHistogram(basisHeight, basisWidth);
		int[][] basisHistogram = basis.getHistogram2D();
		//int[][] bHist = this.get2DHist(basisHistogram, basisHeight, basisWidth);
		
		File folder = new File(imagesRepo);
		File[] fileList = folder.listFiles();
		
		for(int i=0; i<1703; i++)
		{
			ImageObject sample = new ImageObject(imagesRepo, fileList[i].getName());
			int sampleWidth = sample.getImageObject().getWidth();
			int sampleHeight = sample.getImageObject().getHeight();
			sample.initialize2DHistogram(sampleHeight, sampleWidth);
			int[][] sampleHistogram = sample.getHistogram2D();
			//int[][] sHist = this.get2DHist(sampleHistogram, sampleHeight, sampleWidth);
			double weight = getComparedCCV4(basisHistogram, basisWidth, basisHeight, sampleHistogram, sampleWidth, sampleHeight, threshold, nColors);
			images.add(new ResultImageData(sample.getFileName(), weight));
		}
		
		Collections.sort(images, new Comparator<ResultImageData>() 
		{
	        @Override public int compare(ResultImageData img1, ResultImageData img2) 
	        {
	        	return Double.compare(img1.getValue(), img2.getValue());
	        }

		});
		
		return images;
	}
	
	public static double getComparedCCV4(int[][] image1, int width, int height,int[][] image2, int i2Width, int i2Height, int threshold, int nColors)
	{
		double[][] c1 = getCCV4(image1, nColors, width, height, threshold);
		double[][] c2 = getCCV4(image2, nColors, i2Width, i2Height, threshold);
		//double[] compared = new double[nColors];
		double compared = 0;
		
		for(int x = 0; x < nColors; x++)
		{
			double co = c1[x][0] - c2[x][0];
			if(co < 0) co *= -1;
			double nco = c1[x][1] - c2[x][1];
			if(nco < 0) nco *= -1;
			compared += co + nco;
			
		}
		
		return compared;
	}
	
	public static double[][] getCCV4(int[][] image,int nColors, int width, int height, int threshold)
	{
		// results because nColors then coherent and not coherent
		double[][] coherence = new double[nColors][2]; 
		// picture
		int[][] picture = new int[height][width];
		//groups of the picture
		int[][] groups = new int[height][width];
		// for children group #s
		Child child = new Child(width, height);
		
		//group number init
		int gNum = 1;
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				picture[y][x] = image[y][x]; //index of dapat 
				groups[y][x] = 0;
				
			}
		}
		
		//first pass
		
		//loop for all the colors na meron
		for(int currColor = 0; currColor < nColors; currColor++)
		{
			// loop for all the pixels in the image
			for(int x = 0; x < height; x++)
			{
				for(int y = 0; y < width; y++)
				{
					// kung yung color nya is yung current na color na cinocompare
					if(currColor == picture[x][y])
					{
						//kung first pixel sya
						if(x == 0 && y == 0)
						{
							//iseset mo lang yung group number sa unang group number.
							groups[x][y] = gNum;
							gNum++;
						}
						//kung top pixel sya
						else if(x == 0)
						{
							//kung yung prev color sa left is same sa color nya
							if(picture[x][y] == picture[x][y-1])
							{
								//kunin mo lang yung group nya
								groups[x][y] = groups[x][y-1];
							}
							//kung hindi, magsstart sya bali ng bagong group
							else
							{
								groups[x][y] = gNum;
								gNum++;
							}
						}
						//left pixel lang sya
						else if(y == 0)
						{
							//kung yung prev color sa left is same sa color nya
							if(picture[x][y] == picture[x-1][y])
							{
								//kunin mo lang yung group nya
								groups[x][y] = groups[x-1][y];
							}
							//kung hindi, magsstart sya bali ng bagong group
							else
							{
								groups[x][y] = gNum;
								gNum++;
							}
						}
						//kung mid pixel lang sya
						else
						{
							//kung same color yung left and top nya
							if(picture[x][y] == picture[x-1][y] && picture[x][y] == picture[x][y-1])
							{
								//kunin lang yung lesser gNum tapos add sa child yung isa
								//kung si left mas maliit
								if(groups[x][y-1] < groups[x-1][y])
								{
									//assign left group to it
									groups[x][y] = groups[x][y-1];
									// add top to the child ng left
									child.addChild(groups[x][y], groups[x-1][y]);
								}
								else if(groups[x][y-1] == groups[x-1][y])
								{
									//assign top to it
									groups[x][y] = groups[x-1][y];
									// wala nang add kasi equal
								}
								else
								{
									//assign top to it
									groups[x][y] = groups[x-1][y];
									// add left as it's child
									child.addChild(groups[x][y], groups[x][y-1]);
								}
								
							}
							//kung same color yung left nya
							else if(picture[x][y] == picture[x][y-1])
							{
								groups[x][y] = groups[x][y-1];
							}
							//kung same color yung top nya
							else if(picture[x][y] == picture[x-1][y])
							{
								groups[x][y] = groups[x-1][y];
							}
							//kung walang same sa top and left
							else
							{
								groups[x][y] = gNum;
								gNum++;
							}
						}
					}
					
				}
			}
			
		}
		
		//-----------------------------------SECOND PASS-------------------------//
		// include counting per group here
		
		// 2 cols for color and count
		int[][] cntPerGrp = new int[gNum][2];
		
		//initialize
		for(int x = 1; x < gNum; x++)
		{
			cntPerGrp[x][0] = 0;
			cntPerGrp[x][1] = 0;
		}
		
		//loop for all the colors na meron
		for(int currColor = 0; currColor < nColors; currColor++)
		{
			// loop for all the pixels in the image
			for(int x = 0; x < height; x++)
			{
				for(int y = 0; y < width; y++)
				{
					//change to the parent #
					groups[x][y] = child.getParent(groups[x][y]);
				}
			}
		}
		
		// loop for all the pixels in the image
		for(int x = 0; x < height; x++)
		{
			for(int y = 0; y < width; y++)
			{
				//count per group
				cntPerGrp[groups[x][y]][0] = image[x][y]; //color ng group
				cntPerGrp[groups[x][y]][1] = cntPerGrp[groups[x][y]][1]+1; // add to ctr
			}
		}
		
		
		//-------------------------COLOR COHERENCE CTR-----------------------//
		
		for(int x = 0; x < nColors; x++)
		{
			coherence[x][0] = 0; //coherent
			coherence[x][1] = 0; //noncoherent
			
		}
		
		for(int x = 1; x < gNum; x++)
		{
			if(cntPerGrp[x][1] >= threshold)
				coherence[cntPerGrp[x][0]][0] += cntPerGrp[x][1]; // coherent
			else coherence[cntPerGrp[x][0]][1] += cntPerGrp[x][1]; // noncoherent
		}
		
		return coherence;
	}


}
