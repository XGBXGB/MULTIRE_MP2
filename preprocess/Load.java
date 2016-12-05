package preprocess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import model.ImageObject;

public class Load
{
	ArrayList<ImageObject> uniHistogram;
	ArrayList<ImageObject> mjackHistogram;
	ArrayList<ImageObject> sevensevensevenHistogram;
	ArrayList<ImageObject> stanfordHistogram;
	ArrayList<ImageObject> mjHistogram;
	
	public Load()
	{
		try
		{
			loadHistograms();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadHistograms() throws IOException
	{
		uniHistogram = loadHistogram("src\\preprocessedhistograms\\UNIHistogram.txt");
		mjackHistogram = loadHistogram("src\\preprocessedhistograms\\MJACKHistogram.txt");
		sevensevensevenHistogram = loadHistogram("src\\preprocessedhistograms\\777Histogram.txt");
		stanfordHistogram = loadHistogram("src\\preprocessedhistograms\\STANFORDHistogram.txt");
		mjHistogram = loadHistogram("src\\preprocessedhistograms\\MJHistogram.txt");
	}
	
	private ArrayList<ImageObject> loadHistogram( String path ) throws IOException
	{
		ArrayList<ImageObject> images = new ArrayList<>(0);
		
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		
		String filename = br.readLine();
		
		while (filename != null)
		{
			String histogramString = br.readLine();
			String[] histogramStringArray = histogramString.split(" ");
			int[] histogram = new int[histogramStringArray.length];
			
			for( int i = 0; i < histogramStringArray.length; i++ )
			{
				try
				{
					histogram[i] = Integer.parseInt(histogramStringArray[i]);
				}
				catch( NumberFormatException nfe )
				{
					
				}
			}
			
			images.add(new ImageObject(filename, histogram));
//			System.out.println("Filename: " + filename);
//			System.out.println(Arrays.toString(histogram) + "\n");
			
			br.readLine();
			filename = br.readLine();
		}
		
		br.close();
		
		return images;
	}
	
	public ArrayList<ImageObject> getUni()
	{
		return uniHistogram;
	}
	
	public ArrayList<ImageObject> getMjack()
	{
		return mjackHistogram;
	}
	
	public ArrayList<ImageObject> get777()
	{
		return sevensevensevenHistogram;
	}
	
	public ArrayList<ImageObject> getStanford()
	{
		return stanfordHistogram;
	}
	
	public ArrayList<ImageObject> getMj()
	{
		return mjHistogram;
	}
//	public static void main(String[] args)
//	{
//		Load load = new Load();
//	}
}
