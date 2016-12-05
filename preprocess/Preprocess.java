package preprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.ImageObject;

public class Preprocess
{
	public static void main(String[] args) throws IOException
	{
		System.out.println("Preprocessing Histograms...");
		
		// UNI FOLDER
		System.out.println("UNI Folder Start");
		String path = "C:\\Users\\Justin\\Desktop\\10th Term Files\\MULTIRE\\MP2\\MP2-JPEG\\uni";
		File folder = new File(path);
		File[] fileList = folder.listFiles();
		
		File textFile = new File("UNIHistogram.txt");
		FileWriter fw = new FileWriter(textFile);
		BufferedWriter bw = new BufferedWriter(fw);
		
		for (int i = 0; i < fileList.length; i++)
		{
			ImageObject sample = new ImageObject(path, fileList[i].getName());
			sample.initializeHistogram();
			int[] sampleHistogram = sample.getHistogram();
			
			bw.write(fileList[i].getName());
			bw.newLine();
			
			for( int j = 0; j< sampleHistogram.length; j++ )
			{
				bw.write(sampleHistogram[j] + " ");
			}
			
			bw.newLine();
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
		System.out.println("UNI Folder End");
		
		// MJACK FOLDER
		System.out.println("MJACK Folder Start");
		path = "C:\\Users\\Justin\\Desktop\\10th Term Files\\MULTIRE\\MP2\\MP2-JPEG\\mjack";
		folder = new File(path);
		fileList = folder.listFiles();
		
		textFile = new File("MJACKHistogram.txt");
		fw = new FileWriter(textFile);
		bw = new BufferedWriter(fw);
		
		for (int i = 0; i < fileList.length; i++)
		{
			ImageObject sample = new ImageObject(path, fileList[i].getName());
			sample.initializeHistogram();
			int[] sampleHistogram = sample.getHistogram();
			
			bw.write(fileList[i].getName());
			bw.newLine();
			
			for( int j = 0; j< sampleHistogram.length; j++ )
			{
				bw.write(sampleHistogram[j] + " ");
			}
			
			bw.newLine();
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
		System.out.println("MJACK Folder End");
		
		// 777 FOLDER
		System.out.println("777 Folder Start");
		path = "C:\\Users\\Justin\\Desktop\\10th Term Files\\MULTIRE\\MP2\\MP2-JPEG\\777";
		folder = new File(path);
		fileList = folder.listFiles();
		
		textFile = new File("777Histogram.txt");
		fw = new FileWriter(textFile);
		bw = new BufferedWriter(fw);
		
		for (int i = 0; i < fileList.length; i++)
		{
			ImageObject sample = new ImageObject(path, fileList[i].getName());
			sample.initializeHistogram();
			int[] sampleHistogram = sample.getHistogram();
			
			bw.write(fileList[i].getName());
			bw.newLine();
			
			for( int j = 0; j< sampleHistogram.length; j++ )
			{
				bw.write(sampleHistogram[j] + " ");
			}
			
			bw.newLine();
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
		System.out.println("777 Folder End");
		
		// STANFORD FOLDER
		System.out.println("STANFORD Folder Start");
		path = "C:\\Users\\Justin\\Desktop\\10th Term Files\\MULTIRE\\MP2\\MPEG2\\stanford";
		folder = new File(path);
		fileList = folder.listFiles();
		
		textFile = new File("STANFORDHistogram.txt");
		fw = new FileWriter(textFile);
		bw = new BufferedWriter(fw);
		
		for (int i = 0; i < fileList.length; i++)
		{
			ImageObject sample = new ImageObject(path, fileList[i].getName());
			sample.initializeHistogram();
			int[] sampleHistogram = sample.getHistogram();
			
			bw.write(fileList[i].getName());
			bw.newLine();
			
			for( int j = 0; j< sampleHistogram.length; j++ )
			{
				bw.write(sampleHistogram[j] + " ");
			}
			
			bw.newLine();
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
		System.out.println("STANFORD Folder End");
		
		// MJ FOLDER
		System.out.println("MJ Folder Start");
		path = "C:\\Users\\Justin\\Desktop\\10th Term Files\\MULTIRE\\MP2\\MPEG2b\\MJ";
		folder = new File(path);
		fileList = folder.listFiles();
		
		textFile = new File("MJHistogram.txt");
		fw = new FileWriter(textFile);
		bw = new BufferedWriter(fw);
		
		for (int i = 0; i < fileList.length; i++)
		{
			ImageObject sample = new ImageObject(path, fileList[i].getName());
			sample.initializeHistogram();
			int[] sampleHistogram = sample.getHistogram();
			
			bw.write(fileList[i].getName());
			bw.newLine();
			
			for( int j = 0; j< sampleHistogram.length; j++ )
			{
				bw.write(sampleHistogram[j] + " ");
			}
			
			bw.newLine();
			bw.newLine();
		}
		
		bw.flush();
		bw.close();
		System.out.println("MJ Folder End");
		
		System.out.println("Done.");
	}
}
