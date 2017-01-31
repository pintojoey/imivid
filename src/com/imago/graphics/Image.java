package com.imago.graphics;

import java.awt.Color;

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


}
