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
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;
import java.awt.image.ColorModel;

class ImageObject {

	String path;
	String fileName;
	// JTextField jTextArea1 = new JTextField();
	// JTextField jTextArea2 = new JTextField();
	// JPanel mainPanel;
	int[] histogram;
	int[][] histogram2D;
	BufferedImage imageObject;
	int[] histogramCenter;
	int[] histogramNonCenter;

	// public void showJPEG() {
	// BufferedImage bi = null;
	// String outputFileName = path + File.separatorChar + fileName;
	// System.out.println("OUTPUT: " + outputFileName);
	// try {
	// File file = new File(outputFileName);
	// FileInputStream in = new FileInputStream(file);
	//
	// // decodes the JPEG data stream into a BufferedImage
	// JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
	// bi = decoder.decodeAsBufferedImage();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	//
	// if (bi == null) {
	// System.out.println("NULL");
	// return;
	// }
	//
	// JLabel picLabel = new JLabel(new ImageIcon(bi));
	// picLabel.setBounds(0, 0, 500, 500);
	// mainPanel.add(picLabel);
	// }
	public ImageObject(String path, String fileName) {
		this.path = path;
		this.fileName = fileName;

		String outputFileName = path + File.separatorChar + fileName;
		// System.out.println("OUTPUT: " + outputFileName);
		try {
			File file = new File(outputFileName);
			FileInputStream in = new FileInputStream(file);

			// decodes the JPEG data stream into a BufferedImage
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
			imageObject = decoder.decodeAsBufferedImage();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (imageObject == null) {
			System.out.println("NULL");
			return;
		}
	}

	public int[] getHistogram() {
		return histogram;
	}

	public void setHistogram2D(int[][] histogram) {
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

	public int[][] getHistogram2D() {
		return histogram2D;
	}

	public void setHistogram(int[] histogram) {
		this.histogram = histogram;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public BufferedImage getImageObject() {
		return imageObject;
	}

	public void setImageObject(BufferedImage imageObject) {
		this.imageObject = imageObject;
	}

	public void initializeHistogram() {
		histogram = new int[159];
		// gets the RGB and Luv value at x, y
		BufferedImage bi1 = null;
		int RGB1;
		int totalPixels;

		try {
			File file = new File(path, fileName);
			FileInputStream in = new FileInputStream(file);
			// decodes the JPEG data stream into a BufferedImage
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
			bi1 = decoder.decodeAsBufferedImage();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (bi1 == null) {
			System.out.println("Null File");
			return;
		} else {
			BufferedImage grayFrame = new BufferedImage(bi1.getWidth(), bi1.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
			
			BufferedImageOp grayscaleConv= new ColorConvertOp(bi1.getColorModel().getColorSpace(),
					grayFrame.getColorModel().getColorSpace(), null);
			grayscaleConv.filter(bi1, grayFrame);
		}

		/* FIIIIIIIIIIIIIIIIIRSTT */
		for (int x = 0; x < bi1.getWidth(); x++) {
			for (int y = 0; y < bi1.getHeight(); y++) {
				ColorModel CM;
				CM = bi1.getColorModel();
				RGB1 = bi1.getRGB(x, y); // get the RGB value at x,y of the
											// image

				double R, G, B;
				R = CM.getRed(RGB1); // get the 8-bit values of RGB (0-255)
				G = CM.getGreen(RGB1);
				B = CM.getBlue(RGB1);

				cieConvert colorCIE = new cieConvert();
				colorCIE.setValues(R / 255.0, G / 255.0, B / 255.0);
				histogram[colorCIE.IndexOf()] += 1;
			}
		}
		/* FIIIIIIIIIIIIIIIIIRSTT 30239, 39712 30304 */

		/* SECONNNNND */
		// int[][] rgbVals = convertTo2DWithoutUsingGetRGB(bi1);
		// for(int i=0; i<rgbVals.length; i++){
		// for(int y=0; y<rgbVals[i].length; y++){
		// RGB1 = rgbVals[i][y];
		// ColorModel CM;
		// CM = bi1.getColorModel();
		// double R, G, B;
		// R = CM.getRed(RGB1); // get the 8-bit values of RGB (0-255)
		// G = CM.getGreen(RGB1);
		// B = CM.getBlue(RGB1);
		// //System.out.println(RGB1+" RGBeh: "+R+" "+G+" "+B);
		//
		// cieConvert colorCIE = new cieConvert();
		// colorCIE.setValues(R / 255.0, G / 255.0, B / 255.0);
		// histogram[colorCIE.IndexOf()] += 1;
		// }
		// }
		/* SECONNNNND 26053 25832 25912 */

		// for(int i=0; i<159; i++){
		// System.out.println("histogram["+i+"] = "+histogram[i]);
		// }
		// System.out.println("total: "+accu);
	}

	public static void main(String args[]) {

		System.out.println("Starting Image...");
		ImageObject mainFrame = new ImageObject("C:\\Users\\xtiangabe\\Desktop", "playlogo2.jpg");
		mainFrame.initializeHistogram();
		// mainFrame.showJPEG();
		// mainFrame.getRGB(50, 30);
	}
}
