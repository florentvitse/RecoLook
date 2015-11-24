package com.AlexFlo.recolouke;

public class Classe {
	private String classifierFile;
	private String brand;
	private String brandURL;
	private String[] images;
	
	public Classe(String file, String b, String url, String imgs)
	{
		classifierFile = file;
		brand = b;
		brandURL = url;
		images = imgs.split(";");
	}
	
	public String getClassifierFile() {
		return classifierFile;
	}
	public void setClassifierFile(String classifierF) {
		classifierFile = classifierF;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String b) {
		brand = b;
	}
	public String getBrandURL() {
		return brandURL;
	}
	public void setBrandURL(String URL) {
		brandURL = URL;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] imgs) {
		images = imgs;
	}
}
