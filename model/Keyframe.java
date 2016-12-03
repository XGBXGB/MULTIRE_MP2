package model;

public class Keyframe {

	private String filename;
	private double distance;
	private String lowerBoundary;
	private String upperBoundary;
	
	
	
	public Keyframe() {
		this.filename = "";
		this.distance = 0;
		this.lowerBoundary = "";
		this.upperBoundary = "";

	}
	public Keyframe(String filename, double distance, String lowerBoundary, String upperBoundary) {
		this.filename = filename;
		this.distance = distance;
		this.lowerBoundary = lowerBoundary;
		this.upperBoundary = upperBoundary;
	}
	
	public String getLowerBoundary() {
		return lowerBoundary;
	}
	public void setLowerBoundary(String lowerBoundary) {
		this.lowerBoundary = lowerBoundary;
	}
	public String getUpperBoundary() {
		return upperBoundary;
	}
	public void setUpperBoundary(String upperBoundary) {
		this.upperBoundary = upperBoundary;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
	
}
