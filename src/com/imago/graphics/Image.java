package com.imago.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

public class Image {
private int height,width,size;
private String name,path;
private Color pixel[][];
public int getHeight() {
	return height;
}
public void setHeight(int height) {
	this.height = height;
}
public int getWidth() {
	return width;
}
public void setWidth(int width) {
	this.width = width;
}
public int getSize() {
	return size;
}
public void setSize(int size) {
	this.size = size;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPath() {
	return path;
}
public void setPath(String path) {
	this.path = path;
}
public Color[][] getPixel() {
	return pixel;
}
public void setPixel(Color[][] pixel) {
	this.pixel = pixel;
}

public String serialize(){
	File file=new File(path);
	FileInputStream imageInFile = null;
	try {
		imageInFile = new FileInputStream(path);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    byte imageData[] = new byte[(int) file.length()];
    try {
		imageInFile.read(imageData);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    // Converting Image byte array into Base64 String
    
    String imageDataString = Base64.encodeBase64URLSafeString(imageData);
	return imageDataString;
}

public Image(int height,int width){
	this.pixel=new Color[height][width];
	this.height=height;
	this.width=width;
	for(int i=0;i<height;i++){
		for(int j=0;j<width;j++){
			this.pixel[i][j]=new Color(0, 0, 0);
		}
	}
	this.size=height*width;
	
	
	
}
public Image() {
	// TODO Auto-generated constructor stub
}
public  HashMap<Long,Long> histogram_cal(){
Image image=this;
	int height=image.getHeight();
	int width=image.getWidth();
	HashMap< Long,Long> histogram=new HashMap<Long,Long>();
	 Color[][] colors = image.getPixel();
	 Long average=0l;
	for(int i=0;i<height;i++){
		for(int j=0;j<width;j++){
			average=(long) (colors[i][j].getRed()+colors[i][j].getGreen()+colors[i][j].getBlue()) /3;
		 Long key = histogram.get(average);
		// System.out.println(average+" "+key);
			if(key==null) histogram.put(average, 1l);
			else histogram.put(average,key+1);
		}
	}
	return histogram;
}


}
