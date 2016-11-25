package model;

/*
 * @(#)Image.java 1.0 03/07/08
 *
 * displays a JPEG image on the panel and  
 * gets the RGB value per pixel 
 */

import java.awt.*;
import java.awt.event.*;
import com.sun.image.codec.jpeg.*;

import model.CenteringTest.Data;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import java.awt.image.ColorModel;

class ImageObject 
{

	String path;
	String fileName;
//	JTextField jTextArea1 = new JTextField();
//	JTextField jTextArea2 = new JTextField();
	//JPanel mainPanel;
	int[] histogram;
	int[][] histogram2D;
	BufferedImage imageObject;
	int[] histogramCenter;
	int[] histogramNonCenter;

//	public void showJPEG() {
//		BufferedImage bi = null;
//		String outputFileName = path + File.separatorChar + fileName;
//		System.out.println("OUTPUT: " + outputFileName);
//		try {
//			File file = new File(outputFileName);
//			FileInputStream in = new FileInputStream(file);
//
//			// decodes the JPEG data stream into a BufferedImage
//			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
//			bi = decoder.decodeAsBufferedImage();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//		if (bi == null) {
//			System.out.println("NULL");
//			return;
//		}
//
//		JLabel picLabel = new JLabel(new ImageIcon(bi));
//		picLabel.setBounds(0, 0, 500, 500);
//		mainPanel.add(picLabel);
//	}
	public ImageObject(String path, String fileName) 
	{
		this.path = path;
		this.fileName = fileName;
		
		String outputFileName = path + File.separatorChar + fileName;
		System.out.println("OUTPUT: " + outputFileName);
		try 
		{
			File file = new File(outputFileName);
			FileInputStream in = new FileInputStream(file);

			// decodes the JPEG data stream into a BufferedImage
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
			imageObject = decoder.decodeAsBufferedImage();
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}

		if (imageObject == null) 
		{
			System.out.println("NULL");
			return;
		}
		
//		mainPanel = new JPanel();
//		mainPanel.setLayout(null);
//
//		mainPanel.setBounds(0, 0, 500, 700);
//		jTextArea1.setBounds(20, 570, 250, 50);
//		jTextArea2.setBounds(20, 510, 250, 50);
//
//		this.add(mainPanel);
//		mainPanel.add(this.jTextArea1);
//		mainPanel.add(this.jTextArea2);
//
//		this.setSize(500, 700);
//		this.setTitle("Image");
//		this.setVisible(true);
//		this.setLayout(null);
//		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public int[] getHistogram() 
	{
		return histogram;
	}

	public void setHistogram2D(int[][] histogram) 
	{
		this.histogram2D = histogram;
	}

	public int[] getHistogramCenter() {
		return histogramCenter;
	}

	public void setHistogramCenter(int[] histogramCenter) {
		this.histogramCenter = histogramCenter;
	}

	public int[] getHistogramNonCenter() {
		return histogramNonCenter;
	}

	public void setHistogramNonCenter(int[] histogramNonCenter) {
		this.histogramNonCenter = histogramNonCenter;
	}

	public int[][] getHistogram2D() 
	{
		return histogram2D;
	}

	public void setHistogram(int[] histogram) 
	{
		this.histogram = histogram;
	}

	
	public String getFileName() 
	{
		return fileName;
	}

	public void setFileName(String fileName) 
	{
		this.fileName = fileName;
	}

	public BufferedImage getImageObject() 
	{
		return imageObject;
	}

	public void setImageObject(BufferedImage imageObject) 
	{
		this.imageObject = imageObject;
	}

	public void initializeHistogram() 
	{
		histogram = new int[159];
		// gets the RGB and Luv value at x, y
		BufferedImage bi1 = null;
		int RGB1;
		int totalPixels;

		try 
		{
			File file = new File(path, fileName);
			FileInputStream in = new FileInputStream(file);
			// decodes the JPEG data stream into a BufferedImage
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
			bi1 = decoder.decodeAsBufferedImage();
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}

		if (bi1 == null) 
		{
			System.out.println("Null File");
			return;
		}
		
		/*FIIIIIIIIIIIIIIIIIRSTT*/
		for(int x=0; x<bi1.getWidth(); x++)
		{
			for(int y=0; y<bi1.getHeight(); y++)
			{
				ColorModel CM;
				CM = bi1.getColorModel();
				RGB1 = bi1.getRGB(x, y); // get the RGB value at x,y of the image

				double R, G, B;
				R = CM.getRed(RGB1); // get the 8-bit values of RGB (0-255)
				G = CM.getGreen(RGB1);
				B = CM.getBlue(RGB1);
				
				cieConvert colorCIE = new cieConvert();
				colorCIE.setValues(R / 255.0, G / 255.0, B / 255.0);
				histogram[colorCIE.IndexOf()] += 1;
			}
		}
		/*FIIIIIIIIIIIIIIIIIRSTT 30239, 39712 30304*/

		/*SECONNNNND*/
//		int[][] rgbVals = convertTo2DWithoutUsingGetRGB(bi1);
//		for(int i=0; i<rgbVals.length; i++){
//			for(int y=0; y<rgbVals[i].length; y++){
//				RGB1 = rgbVals[i][y];
//				ColorModel CM;
//				CM = bi1.getColorModel();
//				double R, G, B;
//				R = CM.getRed(RGB1); // get the 8-bit values of RGB (0-255)
//				G = CM.getGreen(RGB1);
//				B = CM.getBlue(RGB1);
//				//System.out.println(RGB1+" RGBeh: "+R+" "+G+" "+B);
//				
//				cieConvert colorCIE = new cieConvert();
//				colorCIE.setValues(R / 255.0, G / 255.0, B / 255.0);
//				histogram[colorCIE.IndexOf()] += 1;
//			}
//		}
		/*SECONNNNND 26053 25832 25912*/
		
		
//		for(int i=0; i<159; i++){
//			System.out.println("histogram["+i+"] = "+histogram[i]);
//		}
//		System.out.println("total: "+accu);
	}
	
	public static int getPositionOffset(int length, int chunkLength) {
		for (int i = 0; i < length; i++) {
			if (i == length - (i + chunkLength)) {//6 == 12-(6+0)
				return i;
			}
		}
		return -1;
	}
	
	public void initializeHistogramCR(float percent) 
	{	
		histogramCenter = new int[159];
		histogramNonCenter = new int[159];
		// gets the RGB and Luv value at x, y
		BufferedImage bi1 = null;
		int RGB1;
		int totalPixels;

		try 
		{
			File file = new File(path, fileName);
			FileInputStream in = new FileInputStream(file);
			// decodes the JPEG data stream into a BufferedImage
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
			bi1 = decoder.decodeAsBufferedImage();
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}

		if (bi1 == null) 
		{
			System.out.println("Null File");
			return;
		}
		
		int width = bi1.getWidth(), height = bi1.getHeight(), centerW=0, centerH=0;
		int num = (int) ((width*height)*(percent/100f));
		int incrementer = 1;
		if (num % 2 != 0) {
			incrementer = 2; // only test the odd ones
		}
		int big=0,small=0;
		if(width>height){
			big=width;
			small = height;
		}else{
			big=height;
			small=width;
		}
		ArrayList<Data> numbers = new ArrayList();
		for (int i = 1; i <= num / 2; i += incrementer) {
			if (num % i == 0) {
				int temp1 = i;
				int temp2 = num / i;
				if (temp1 <= small && temp2 <= big) {
					if(width>height){
						numbers.add(new Data(temp2, temp1));
					}else{
						numbers.add(new Data(temp1, temp2));
					}
				}
			}
		}
		
		int startPosW=0, startPosH=0;
		for(int i=numbers.size()-1; i>=0; i--){
			startPosW = getPositionOffset(width, numbers.get(i).getInt1());
			startPosH = getPositionOffset(height, numbers.get(i).getInt2());
			if(startPosH!=-1 && startPosW!=-1){
				centerW = numbers.get(i).getInt1();
				centerH = numbers.get(i).getInt2();
				break;
			}
		}
		int endPosW = bi1.getWidth()-startPosW;
		int endPosH = bi1.getHeight()-startPosH;
		
		for(int x=0; x<bi1.getWidth(); x++)
		{
			for(int y=0; y<bi1.getHeight(); y++)
			{
				ColorModel CM;
				CM = bi1.getColorModel();
				RGB1 = bi1.getRGB(x, y); // get the RGB value at x,y of the image

				double R, G, B;
				R = CM.getRed(RGB1); // get the 8-bit values of RGB (0-255)
				G = CM.getGreen(RGB1);
				B = CM.getBlue(RGB1);
				
				cieConvert colorCIE = new cieConvert();
				colorCIE.setValues(R / 255.0, G / 255.0, B / 255.0);
				
				
				if(x>=startPosW && x<endPosW && y>=startPosH && y<endPosH){
					histogramCenter[colorCIE.IndexOf()] += 1;
				}else{
					histogramNonCenter[colorCIE.IndexOf()] += 1;
				}
			}
		}
	}

	public void initialize2DHistogram(int height, int width) 
	{
		histogram2D = new int[height][width];
		// gets the RGB and Luv value at x, y
		BufferedImage bi1 = null;
		int RGB1;
		int totalPixels;

		try 
		{
			File file = new File(path, fileName);
			FileInputStream in = new FileInputStream(file);
			// decodes the JPEG data stream into a BufferedImage
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
			bi1 = decoder.decodeAsBufferedImage();
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}

		if (bi1 == null) 
		{
			System.out.println("Null File");
			return;
		}
		
		/*FIIIIIIIIIIIIIIIIIRSTT*/
		for(int x=0; x<bi1.getHeight(); x++)
		{
			for(int y=0; y<bi1.getWidth(); y++)
			{
				ColorModel CM;
				CM = bi1.getColorModel();
				RGB1 = bi1.getRGB(y, x); // get the RGB value at x,y of the image

				double R, G, B;
				R = CM.getRed(RGB1); // get the 8-bit values of RGB (0-255)
				G = CM.getGreen(RGB1);
				B = CM.getBlue(RGB1);
				
				cieConvert colorCIE = new cieConvert();
				colorCIE.setValues(R / 255.0, G / 255.0, B / 255.0);
				histogram2D[x][y] = colorCIE.IndexOf();
			}
		}
		
		/*System.out.println("IMG " + fileName);
		for(int x = 0 ; x < height; x++)
		{
			for(int y = 0; y < width; y++)
			{
				System.out.print(histogram2D[x][y] + " ");
			}
			System.out.println("");
		}
		Scanner sc = new Scanner(System.in);
		sc.nextLine();*/
		
		/*FIIIIIIIIIIIIIIIIIRSTT 30239, 39712 30304*/

		/*SECONNNNND*/
//		int[][] rgbVals = convertTo2DWithoutUsingGetRGB(bi1);
//		for(int i=0; i<rgbVals.length; i++){
//			for(int y=0; y<rgbVals[i].length; y++){
//				RGB1 = rgbVals[i][y];
//				ColorModel CM;
//				CM = bi1.getColorModel();
//				double R, G, B;
//				R = CM.getRed(RGB1); // get the 8-bit values of RGB (0-255)
//				G = CM.getGreen(RGB1);
//				B = CM.getBlue(RGB1);
//				//System.out.println(RGB1+" RGBeh: "+R+" "+G+" "+B);
//				
//				cieConvert colorCIE = new cieConvert();
//				colorCIE.setValues(R / 255.0, G / 255.0, B / 255.0);
//				histogram[colorCIE.IndexOf()] += 1;
//			}
//		}
		/*SECONNNNND 26053 25832 25912*/
		
		
//		for(int i=0; i<159; i++){
//			System.out.println("histogram["+i+"] = "+histogram[i]);
//		}
//		System.out.println("total: "+accu);
	}
	
	private static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image1)
	{
		BufferedImage image = new BufferedImage(image1.getWidth(), image1.getHeight(),  BufferedImage.TYPE_3BYTE_BGR);
	     final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	      
	     for(int i=0; i<5; i++)
	     {
	     	System.out.println("pixel: "+pixels[i]);
	     }
	     System.out.println("length "+pixels.length);
	     final int width = image.getWidth();
	     final int height = image.getHeight();
	     final boolean hasAlphaChannel = image.getAlphaRaster() != null;

	     int[][] result = new int[height][width];
	     if (hasAlphaChannel) 
	     {
	     	final int pixelLength = 4;
	     	for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) 
	     	{
	     		int argb = 0;
	     		argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
	     		argb += ((int) pixels[pixel + 1] & 0xff); // blue
	     		argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
	     		argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
	     		result[row][col] = argb; 
	            
	     		col++;
	     		if (col == width) 
	     		{
	     			col = 0;
	     			row++;
	     		}
	       	}
	     } 
	     else 
	     {	
			final int pixelLength = 3;
			for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength)
			{
				int argb = 0;
				argb += -16777216; // 255 alpha
				argb += ((int) pixels[pixel] & 0xff); // blue
				argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
				argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
				result[row][col] = argb;
				// System.out.println(argb+" argb");
				col++;
				if (col == width)
				{
					col = 0;
					row++;
				}
			}
	     }

	     return result;
	}
	
//	public void getRGB(int x, int y) {
//		// gets the RGB and Luv value at x, y
//		BufferedImage bi1 = null;
//		int RGB1;
//		int i, j;
//		int totalPixels;
//
//		try {
//			File file = new File(path, fileName);
//			FileInputStream in = new FileInputStream(file);
//
//			// decodes the JPEG data stream into a BufferedImage
//			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
//			bi1 = decoder.decodeAsBufferedImage();
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//		if (bi1 == null) {
//			System.out.println("Null File");
//			return;
//		}
//
//		totalPixels = bi1.getHeight() * bi1.getWidth();
//		ColorModel CM;
//		CM = bi1.getColorModel();
//		RGB1 = bi1.getRGB(x, y); // get the RGB value at x,y of the image
//
//		double R, G, B;
//		R = CM.getRed(RGB1); // get the 8-bit values of RGB (0-255)
//		G = CM.getGreen(RGB1);
//		B = CM.getBlue(RGB1);
//
//		cieConvert ColorCIE = new cieConvert();
//		ColorCIE.setValues(R / 255.0, G / 255.0, B / 255.0);
//
//		jTextArea2.setText("RGB:(" + Double.toString(R) + "," + Double.toString(G) + "," + Double.toString(B) + ") -> "
//				+ Integer.toString(ColorCIE.IndexOf()));
//		jTextArea1.setText("  = LUV:(\n" + Double.toString(ColorCIE.L) + "," + Double.toString(ColorCIE.u) + ","
//				+ Double.toString(ColorCIE.v) + ")");
//		this.repaint();
//	}

	public static void main(String args[]) 
	{
		
		System.out.println("Starting Image...");
		ImageObject mainFrame = new ImageObject("C:\\Users\\xtiangabe\\Desktop", "playlogo2.jpg");
		mainFrame.initializeHistogram();
//		mainFrame.showJPEG();
//		mainFrame.getRGB(50, 30);
	}
}
