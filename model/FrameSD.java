package model;

public class FrameSD {
	private String frameName1;
	private String frameName2;
	private double value;
	private int indexF1;
	private int indexF2;
	
	
	public FrameSD(String frameName1, String frameName2, double value, int indexF1, int indexF2) {
		this.frameName1 = frameName1;
		this.frameName2 = frameName2;
		this.value = value;
		this.indexF1 = indexF1;
		this.indexF2 = indexF2;
		
	}
	
	
	
	public int getIndexF1() {
		return indexF1;
	}



	public void setIndexF1(int indexF1) {
		this.indexF1 = indexF1;
	}



	public int getIndexF2() {
		return indexF2;
	}



	public void setIndexF2(int indexF2) {
		this.indexF2 = indexF2;
	}



	public String getFrameName1() {
		return frameName1;
	}
	public void setFrameName1(String frameName1) {
		this.frameName1 = frameName1;
	}
	public String getFrameName2() {
		return frameName2;
	}
	public void setFrameName2(String frameName2) {
		this.frameName2 = frameName2;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	
	
}
