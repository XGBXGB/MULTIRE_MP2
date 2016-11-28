package model;

public class Keyframe {

	private String filename;
	private double distance;
	
	
	
	public Keyframe() {
		this.filename = "";
		this.distance = 0;

	}
	public Keyframe(String filename, double distance) {
		this.filename = filename;
		this.distance = distance;
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
