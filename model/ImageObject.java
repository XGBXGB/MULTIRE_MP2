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

public class ImageObject {

	String path;
	String fileName;
	int[] histogram;
	BufferedImage imageObject;
	
	public ImageObject( String fileName, int[] histogram )
	{
		this.fileName = fileName;
		this.histogram = histogram;
	}
	
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
		
	}
}
