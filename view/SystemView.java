package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;


public class SystemView extends JFrame implements ActionListener
{
	JPanel mainPanel, resultsPanel, resultsPanel2;
	JLabel lbl_chosenImage, lbl_shotBoundaries, lbl_keyFrames;
	JButton btn_retrieveImages, btn_chooseImage;
	JTextField tf_imagePath;
	JFileChooser fc_chooser;
	File file_chosenImage;
	JScrollPane resultsPanelScroller, resultsPanelScroller2;

	static String xgbRepo = "C:\\Users\\xtiangabe\\Desktop\\MP1\\images";
	static String winonaRepo = "D:\\College\\Multire\\MP1\\MP1\\images";
	static String kerrbieRepo = "C:\\Users\\Justin\\Documents\\Eclipse\\MULTIRE-MP1\\images";
	static String imagesRepository = xgbRepo;
	double significance = 0.005;
	double[][] similarityMatrix;
	
	public SystemView()
	{
		String[] percentages = {"50", "75"};
		
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
//		fc_chooser = new JFileChooser("D:\\COLLEGE\\MULTIRE\\MP1\\MP1\\images");
		fc_chooser = new JFileChooser("C:\\Users\\xtiangabe\\Desktop");
		
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 600, 600);
		
		resultsPanel = new JPanel();
		resultsPanel.setLayout(null);
		resultsPanel2 = new JPanel();
		resultsPanel2.setLayout(null);
		
		resultsPanelScroller = new JScrollPane(resultsPanel);
		resultsPanelScroller.setBounds(5, 150, 280, 300);
		resultsPanelScroller.setBorder(raisedetched);

		resultsPanelScroller2 = new JScrollPane(resultsPanel2);
		resultsPanelScroller2.setBounds(295, 150, 280, 300);
		resultsPanelScroller2.setBorder(raisedetched);
		
		ButtonGroup radio_btn_group = new ButtonGroup(); //forces only one radio btn can be selected at a time


		lbl_keyFrames = new JLabel("Key Frames");
		lbl_keyFrames.setBounds(295, 130, 100, 20);
		
		lbl_shotBoundaries = new JLabel("Shot Boundaries");
		lbl_shotBoundaries.setBounds(5, 130, 100, 20);
		
		lbl_chosenImage = new JLabel("Image Set Path:");
		
		tf_imagePath = new JTextField(20);
		tf_imagePath.setBounds(5, 30, 200, 25);
		
		
		lbl_chosenImage.setBounds(5, 10, 100, 20);
		
		
		btn_chooseImage = new JButton("Choose Image");
		btn_chooseImage.setBounds(210, 30, 120, 25);
		btn_chooseImage.addActionListener(this);
		btn_retrieveImages = new JButton("Retrieve Images");
		btn_retrieveImages.setBounds(5, 60, 250, 25);
		btn_retrieveImages.addActionListener(this);
		
		mainPanel.add(resultsPanelScroller);
		mainPanel.add(resultsPanelScroller2);
		mainPanel.add(btn_chooseImage);
		mainPanel.add(tf_imagePath);
		mainPanel.add(lbl_chosenImage);
		mainPanel.add(btn_retrieveImages);
		mainPanel.add(lbl_shotBoundaries);
		mainPanel.add(lbl_keyFrames);
		
		tf_imagePath.setEnabled(false);
		
		mainPanel.setLayout(null);
		this.add(mainPanel);
		
		this.setLayout(null);
		this.setTitle("Image Retrieval");
		this.setSize(600,505);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//similarityMatrix = controller.getLUVSimilarityMatrix();
	}
	
	public static void main(String args[])
	{
		SystemView sv = new SystemView();
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		
		
	}
	
	public static Image getImageFromPathAndFile(String fileName)
	{
		BufferedImage bi = null;
		Image rescaled = null;
//		String outputFileName = "D:\\College\\Multire\\MP1\\MP1\\images" + File.separatorChar + fileName;
		String outputFileName = imagesRepository + File.separatorChar + fileName;
    	
		try 
		{
	    		File file = new File(outputFileName);
	    		FileInputStream in = new FileInputStream(file);
	
//	    		// decodes the JPEG data stream into a BufferedImage
//	    		JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
//	    		bi = decoder.decodeAsBufferedImage();
//	    		rescaled = bi.getScaledInstance(195, 196, Image.SCALE_DEFAULT);
	    	} 
		catch (Exception ex) 
		{
	    		ex.printStackTrace();
	    	}
	    	return rescaled;
	}
//	try {
//	File file = new File(outputFileName);
//	FileInputStream in = new FileInputStream(file);
//
//	// decodes the JPEG data stream into a BufferedImage
//	JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
//	bi = decoder.decodeAsBufferedImage();
//} catch (Exception ex) {
//	ex.printStackTrace();
//}
	
	
	
}
