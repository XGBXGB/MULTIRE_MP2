package model;

public class FrameSD {
	private String frameName1;
	private String frameName2;
	private double value;
	
	
	public FrameSD(String frameName1, String frameName2, double value) {
		this.frameName1 = frameName1;
		this.frameName2 = frameName2;
		this.value = value;
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
