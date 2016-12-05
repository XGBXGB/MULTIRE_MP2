package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;

import controller.Controller;
import model.FrameSD;
import model.Keyframe;
import java.awt.Font;

public class SystemView extends JFrame implements ActionListener {
	JPanel mainPanel;
	static JPanel resultsPanel, resultsPanel2;
	static JScrollPane resultsPanelScroller, resultsPanelScroller2;
	JLabel lbl_ActiveImageRepository, lbl_Preprocess, lbl_choose, lbl_chosenImage, lbl_alpha, lbl_ts, lbl_percent, lbl_shotBoundaries, lbl_keyFrames;
	JButton btn_start, btn_chooseImage;
	JComboBox<String> cb_imageSets;
	JRadioButton rb_Winona, rb_Xgb, rb_Kerrbie;
	JTextField tf_imagesPath, tf_alpha, tf_ts;
	JFileChooser fc_chooser;
	
	String[] imageSets = {"UNI", "MJACK", "777", "STANFORD", "MJ", "CUSTOM" };
	DefaultComboBoxModel<String> imageSetModel = new DefaultComboBoxModel<>(imageSets);
	ButtonGroup activeRepositoryGroup;
	
	Controller controller;
	double significance = 0.005;
	double[][] similarityMatrix;
	File file_chosenImage;
	
	static String xgbUNI = "C:\\Users\\xtiangabe\\Desktop\\MP2-JPEG\\uni";
	static String xgbMJACK = "C:\\Users\\xtiangabe\\Desktop\\MP2-JPEG\\mjack";
	static String xgb777 = "C:\\Users\\xtiangabe\\Desktop\\MP2-JPEG\\777";
	
	static String winonaUNI = "D:\\COLLEGE\\MULTIRE\\MP2\\MP2-JPEG\\uni";
	static String winonaMJACK = "D:\\COLLEGE\\MULTIRE\\MP2\\MP2-JPEG\\mjack";
	static String winona777 = "D:\\COLLEGE\\MULTIRE\\MP2\\MP2-JPEG\\777";
	
	static String kerrbieUNI = "C:\\Users\\Justin\\Desktop\\10th Term Files\\MULTIRE\\MP2\\MP2-JPEG\\uni";
	static String kerrbieMJACK = "C:\\Users\\Justin\\Desktop\\10th Term Files\\MULTIRE\\MP2\\MP2-JPEG\\mjack";
	static String kerrbie777 = "C:\\Users\\Justin\\Desktop\\10th Term Files\\MULTIRE\\MP2\\MP2-JPEG\\777";
	static String kerrbieSTANFORD = "C:\\Users\\Justin\\Desktop\\10th Term Files\\MULTIRE\\MP2\\MPEG2\\stanford";
	static String kerrbieMJ = "C:\\Users\\Justin\\Desktop\\10th Term Files\\MULTIRE\\MP2\\MPEG2b\\MJ";
	
	static String imagesRepository = "";
	
	ButtonModel activeRepository = (ButtonModel) rb_Winona;
	
	public SystemView() {
		String[] percentages = { "50", "75" };
		controller = new Controller();
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		// fc_chooser = new
		// JFileChooser("D:\\COLLEGE\\MULTIRE\\MP1\\MP1\\images");
		fc_chooser = new JFileChooser("..\\Desktop");

		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 850, 600);

		resultsPanel = new JPanel();
		resultsPanel.setLayout(null);
		resultsPanel2 = new JPanel();
		resultsPanel2.setLayout(null);

		resultsPanelScroller = new JScrollPane(resultsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		resultsPanelScroller.setBounds(5, 150, 484, 300);
		resultsPanelScroller.setBorder(raisedetched);

		resultsPanelScroller2 = new JScrollPane(resultsPanel2, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		resultsPanelScroller2.setBounds(499, 150, 320, 300);
		resultsPanelScroller2.setBorder(raisedetched);

		lbl_ActiveImageRepository = new JLabel("Active Image Repository");
		lbl_ActiveImageRepository.setBounds(5, 14, 140, 14);
		
		rb_Winona = new JRadioButton("Winona");
		rb_Winona.setBounds(5, 31, 70, 23);
		rb_Winona.setSelected(true);
		rb_Winona.addActionListener(this);
		
		rb_Xgb = new JRadioButton("Xgb");
		rb_Xgb.setBounds(77, 31, 55, 23);
		rb_Xgb.addActionListener(this);
		
		rb_Kerrbie = new JRadioButton("Kerrbie");
		rb_Kerrbie.setBounds(134, 31, 70, 23);
		rb_Kerrbie.addActionListener(this);
		
		activeRepositoryGroup = new ButtonGroup();
		activeRepositoryGroup.add(rb_Winona);
		activeRepositoryGroup.add(rb_Xgb);
		activeRepositoryGroup.add(rb_Kerrbie);
		
		lbl_Preprocess = new JLabel("Preprocessed Image Sets");
		lbl_Preprocess.setBounds(212, 14, 155, 14);
		
		cb_imageSets = new JComboBox(imageSetModel);
		cb_imageSets.setBounds(212, 32, 133, 20);
		cb_imageSets.addActionListener(this);
		
		lbl_choose = new JLabel("<-- Choose One -->");
		lbl_choose.setBounds(381, 35, 108, 14);
		
		lbl_keyFrames = new JLabel("Key Frames");
		lbl_keyFrames.setBounds(499, 130, 100, 20);

		lbl_shotBoundaries = new JLabel("Shot Boundaries");
		lbl_shotBoundaries.setBounds(5, 130, 100, 20);

		lbl_chosenImage = new JLabel("Image Set Path:");
		lbl_chosenImage.setBounds(499, 11, 100, 20);
		
		tf_imagesPath = new JTextField(20);
		tf_imagesPath.setFont(new Font("Tahoma", Font.PLAIN, 9));
		tf_imagesPath.setBounds(499, 30, 320, 25);
		tf_imagesPath.setEnabled(false);
		//tf_imagesPath.setText(imagesRepository);
		
		btn_chooseImage = new JButton("Choose Folder");
		btn_chooseImage.setBounds(699, 62, 120, 25);
		btn_chooseImage.addActionListener(this);
		
		lbl_alpha = new JLabel("Alpha:");
		lbl_alpha.setBounds(5, 67, 40, 14);
		
		tf_alpha = new JTextField();
		tf_alpha.setBounds(42, 64, 65, 20);
		tf_alpha.setColumns(10);
		tf_alpha.setText("7");
		
		lbl_ts = new JLabel("Ts (% of Tb):");
		lbl_ts.setBounds(141, 66, 70, 14);
		
		tf_ts = new JTextField();
		tf_ts.setBounds(212, 63, 72, 20);
		tf_ts.setColumns(10);
		tf_ts.setText("0.1");
		
		lbl_percent = new JLabel("%");
		lbl_percent.setBounds(283, 66, 20, 14);
		lbl_percent.setVisible(false);
		
		btn_start = new JButton("Start");
		btn_start.setBounds(5, 94, 280, 25);
		btn_start.addActionListener(this);

		mainPanel.add(resultsPanelScroller);
		mainPanel.add(resultsPanelScroller2);
		mainPanel.add(lbl_ActiveImageRepository);
		mainPanel.add(rb_Winona);
		mainPanel.add(rb_Xgb);
		mainPanel.add(rb_Kerrbie);
		mainPanel.add(lbl_Preprocess);
		mainPanel.add(cb_imageSets);
		mainPanel.add(lbl_choose);
		mainPanel.add(btn_chooseImage);
		mainPanel.add(tf_imagesPath);
		mainPanel.add(lbl_chosenImage);
		mainPanel.add(lbl_alpha);
		mainPanel.add(tf_alpha);
		mainPanel.add(lbl_ts);
		mainPanel.add(tf_ts);
		mainPanel.add(lbl_percent);
		mainPanel.add(btn_start);
		mainPanel.add(lbl_shotBoundaries);
		mainPanel.add(lbl_keyFrames);
		
		mainPanel.setLayout(null);
		getContentPane().add(mainPanel);
		
		getContentPane().setLayout(null);
		this.setTitle("Image Retrieval");
		this.setSize(850, 505);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		// similarityMatrix = controller.getLUVSimilarityMatrix();
	}

	public static void main(String args[]) 
	{
		SystemView sv = new SystemView();
	}


	public void actionPerformed(ActionEvent e) 
	{
		if( e.getSource() == rb_Winona || e.getSource() == rb_Xgb || e.getSource() == rb_Kerrbie )
		{
			activeRepository = activeRepositoryGroup.getSelection();
		}
		else if( e.getSource() == cb_imageSets )
		{
			activeRepositoryGroup.setSelected(activeRepository, true);
		}
		else if (e.getSource() == btn_chooseImage) 
		{
			fc_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int option = fc_chooser.showOpenDialog(null);

			if (option == JFileChooser.APPROVE_OPTION) 
			{
				file_chosenImage = fc_chooser.getSelectedFile();
				tf_imagesPath.setText("");
				tf_imagesPath.setText(fc_chooser.getSelectedFile().getAbsolutePath());
				imagesRepository = tf_imagesPath.getText().toString();
				cb_imageSets.setSelectedItem("CUSTOM");
				activeRepositoryGroup.clearSelection();
			}
		}
		else if (e.getSource() == btn_start) 
		{
			int alpha = 0;
			double ts = 0;
			
			try
			{
				alpha = Integer.parseInt(tf_alpha.getText().toString());
			}
			catch (NumberFormatException nfe)
			{
				tf_alpha.setText("7");
				alpha = 7;
			}
			
			try
			{
				ts = Double.parseDouble(tf_ts.getText().toLowerCase());
			}
			catch (NumberFormatException nfe)
			{
				tf_ts.setText("0.1");
				ts = 0.1;
			}
			
			setImagePath();
			
			if( cb_imageSets.getSelectedItem().equals("CUSTOM") )
			{
				controller.processVideo(alpha, ts, imagesRepository);
			}
			else
			{
				controller.preprocessedProcessVideo(alpha, ts, cb_imageSets.getSelectedIndex());
			}
			
			displayResults(controller.getAllCameraBreaks(), controller.getAllKeyFrames());
		}
	}
	
	private void setImagePath()
	{
		int repo = 0;
		
		if( rb_Winona.isSelected() )
		{
			repo = 0;
		}
		else if( rb_Xgb.isSelected() )
		{
			repo = 1;
		}
		else if( rb_Kerrbie.isSelected() )
		{
			repo = 2;
		}
		
		chooseImageRepo(repo);
		tf_imagesPath.setText(imagesRepository);
	}
	
	private void chooseImageRepo( int repo )
	{
		int imageSet = cb_imageSets.getSelectedIndex();
		switch( repo )
		{
			case 0: switch( imageSet )
				   {
					    case 0: imagesRepository = winonaUNI; break;
				         case 1: imagesRepository = winonaMJACK; break;
				         case 2: imagesRepository = winona777; break;
				         case 3: break;
				         case 4: break;
				   } break;
			case 1: switch( imageSet )
				   {
					    case 0: imagesRepository = xgbUNI; break;
				         case 1: imagesRepository = xgbMJACK; break;
				         case 2: imagesRepository = xgb777; break;
				         case 3: break;
				         case 4: break;
				   } break;
			case 2: switch( imageSet )
				   {
					    case 0: imagesRepository = kerrbieUNI; break;
				         case 1: imagesRepository = kerrbieMJACK; break;
				         case 2: imagesRepository = kerrbie777; break;
				         case 3: imagesRepository = kerrbieSTANFORD; break;
				         case 4: imagesRepository = kerrbieMJ; break;
				   } break;
		}
	}
	
	public static Image getImageFromPathAndFile(String fileName)
	{
		BufferedImage bi = null;
		Image rescaled = null;
//		String outputFileName = "D:\\College\\Multire\\MP1\\MP1\\images" + File.separatorChar + fileName;

		String outputFileName = imagesRepository + "\\" + fileName;
		
		try 
		{
	    		File file = new File(outputFileName);
	    		FileInputStream in = new FileInputStream(file);
	
	    		// decodes the JPEG data stream into a BufferedImage
	    		JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
	    		bi = decoder.decodeAsBufferedImage();
	    		rescaled = bi.getScaledInstance(195, 196, Image.SCALE_DEFAULT);
	    	} 
		catch (Exception ex) 
		{
	    		ex.printStackTrace();
	    	}
	    	return rescaled;
	}
	
	public static void displayResults( ArrayList<FrameSD> cameraBreaks, ArrayList<Keyframe> keyframes )
	{
//		cameraBreaks = new ArrayList<FrameSD>(0);
//		cameraBreaks.add(new FrameSD("0000.jpg", "0001.jpg", 0, 0, 0));
//		cameraBreaks.add(new FrameSD("0002.jpg", "0003.jpg", 0, 0, 0));
//		
//		keyframes = new ArrayList<Keyframe>(0);
//		keyframes.add(new Keyframe("0000.jpg", 0, "0000.jpg", "0001.jpg"));
//		keyframes.add(new Keyframe("0001.jpg", 0, "0001.jpg", "0002.jpg"));
//		keyframes.add(new Keyframe("0002.jpg", 0, "0002.jpg", "0003.jpg"));
		
		int y = 10;
		String type = "";
		
		resultsPanel.removeAll();
		for (int i = 0; i < cameraBreaks.size(); i++)
		{
			if( cameraBreaks.get(i).getType().equalsIgnoreCase("Abrupt") )
			{
				type = "A - ";
			}
			else
			{
				type = "G - ";
			}
			
			Image image1 = getImageFromPathAndFile(cameraBreaks.get(i).getFrameName1());
			Image image2 = getImageFromPathAndFile(cameraBreaks.get(i).getFrameName2());
			JLabel samplePic1 = new JLabel(new ImageIcon(image1));
			JLabel samplePic1Label = new JLabel(type + cameraBreaks.get(i).getFrameName1());
			JLabel samplePic2 = new JLabel(new ImageIcon(image2));
			JLabel samplePic2Label = new JLabel(type + cameraBreaks.get(i).getFrameName2());
			//samplePic.setBounds(offset * 200 + 5, y, 195, 196);
			samplePic1.setBounds(20, y, 195, 196);
			samplePic1Label.setBounds(90, y + 200, 100, 15);
			
			samplePic2.setBounds(240, y, 195, 196);
			samplePic2Label.setBounds(240 + 70, y + 200, 100, 15);
			y += 240;
			// System.out.println("value: "+imageResult.get(i).getValue());
			resultsPanel.add(samplePic1);
			resultsPanel.add(samplePic2);
			resultsPanel.add(samplePic1Label);
			resultsPanel.add(samplePic2Label);
			resultsPanel.repaint();
		}
		resultsPanel.setPreferredSize(new Dimension(440, y));
		resultsPanel.repaint();
		resultsPanelScroller.repaint();
		resultsPanelScroller.setViewport(resultsPanelScroller.getViewport());
		
		y = 10;
		resultsPanel2.removeAll();
		for (int i = 0; i < keyframes.size(); i++)
		{
			Image image1 = getImageFromPathAndFile(keyframes.get(i).getFilename());
			JLabel samplePic1 = new JLabel(new ImageIcon(image1));
			JLabel samplePic1Label = new JLabel("<html><div style='text-align: center;'>" + keyframes.get(i).getFilename() + "</div><div style='text-align: center;'>" + keyframes.get(i).getLowerBoundary() + " - " + keyframes.get(i).getUpperBoundary() + "</div><html>");
			//samplePic.setBounds(offset * 200 + 5, y, 195, 196);
			//samplePic1Label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
			samplePic1.setBounds(50, y, 195, 196);
			samplePic1Label.setBounds(100, y + 200, 150, 30);
			
			y += 240;
			// System.out.println("value: "+imageResult.get(i).getValue());
			resultsPanel2.add(samplePic1);
			resultsPanel2.add(samplePic1Label);
			resultsPanel2.repaint();
		}
		resultsPanel2.setPreferredSize(new Dimension(440, y));
		resultsPanel2.repaint();
		resultsPanelScroller2.repaint();
		resultsPanelScroller2.setViewport(resultsPanelScroller2.getViewport());
	}
}
