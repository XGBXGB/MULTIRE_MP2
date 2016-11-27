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

import controller.Controller;

public class SystemView extends JFrame implements ActionListener {
	JPanel mainPanel, resultsPanel, resultsPanel2;
	JLabel lbl_chosenImage, lbl_shotBoundaries, lbl_keyFrames;
	JButton btn_start, btn_chooseImage;
	JTextField tf_imagesPath;
	JFileChooser fc_chooser;
	File file_chosenImage;
	JScrollPane resultsPanelScroller, resultsPanelScroller2;
	Controller controller;
	static String xgbRepo = "C:\\Users\\xtiangabe\\Desktop\\MP1\\images";
	static String winonaRepo = "D:\\College\\Multire\\MP1\\MP1\\images";
	static String kerrbieRepo = "C:\\Users\\Justin\\Documents\\Eclipse\\MULTIRE-MP1\\images";
	static String imagesRepository = xgbRepo;
	double significance = 0.005;
	double[][] similarityMatrix;

	public SystemView() {
		String[] percentages = { "50", "75" };
		controller = new Controller();
		Border raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		// fc_chooser = new
		// JFileChooser("D:\\COLLEGE\\MULTIRE\\MP1\\MP1\\images");
		fc_chooser = new JFileChooser("..\\Desktop");

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

		lbl_keyFrames = new JLabel("Key Frames");
		lbl_keyFrames.setBounds(295, 130, 100, 20);

		lbl_shotBoundaries = new JLabel("Shot Boundaries");
		lbl_shotBoundaries.setBounds(5, 130, 100, 20);

		lbl_chosenImage = new JLabel("Image Set Path:");

		tf_imagesPath = new JTextField(20);
		tf_imagesPath.setBounds(5, 30, 200, 25);

		lbl_chosenImage.setBounds(5, 10, 100, 20);

		btn_chooseImage = new JButton("Choose Folder");
		btn_chooseImage.setBounds(210, 30, 120, 25);
		btn_chooseImage.addActionListener(this);
		btn_start = new JButton("Start");
		btn_start.setBounds(5, 60, 250, 25);
		btn_start.addActionListener(this);

		mainPanel.add(resultsPanelScroller);
		mainPanel.add(resultsPanelScroller2);
		mainPanel.add(btn_chooseImage);
		mainPanel.add(tf_imagesPath);
		mainPanel.add(lbl_chosenImage);
		mainPanel.add(btn_start);
		mainPanel.add(lbl_shotBoundaries);
		mainPanel.add(lbl_keyFrames);

		tf_imagesPath.setEnabled(false);

		mainPanel.setLayout(null);
		this.add(mainPanel);

		this.setLayout(null);
		this.setTitle("Image Retrieval");
		this.setSize(600, 505);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// similarityMatrix = controller.getLUVSimilarityMatrix();
	}

	public static void main(String args[]) {
		SystemView sv = new SystemView();
	}


	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btn_chooseImage) {
			fc_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int option = fc_chooser.showOpenDialog(null);

			if (option == JFileChooser.APPROVE_OPTION) {
				file_chosenImage = fc_chooser.getSelectedFile();
				tf_imagesPath.setText("");
				tf_imagesPath.setText(fc_chooser.getSelectedFile().getAbsolutePath());
			}
		}else if (e.getSource() == btn_start) {
			controller.doTwinComparison(tf_imagesPath.getText());
		}
	}

}
