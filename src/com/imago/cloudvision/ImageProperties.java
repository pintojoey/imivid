package com.imago.cloudvision;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class Color{
	private int red,green,blue;

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}
}
public class ImageProperties{
	private double score,pixel_fraction,percent,percent_rounded;
	private Color colour;
	private String hex,rgb;

	public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}
	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getPixel_fraction() {
		return pixel_fraction;
	}

	public void setPixel_fraction(double pixel_fraction) {
		this.pixel_fraction = pixel_fraction;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public double getPercent_rounded() {
		return percent_rounded;
	}

	public void setPercent_rounded(double percent_rounded) {
		this.percent_rounded = percent_rounded;
	}

	public String getRgb() {
		return rgb;
	}

	public void setRgb(String rgb) {
		this.rgb = rgb;
	}

	public String getHex() {
		return hex;
	}

	public void setHex(String hex) {
		this.hex = hex;
	}
	public static void main(String[] args) {

	}

	public static ArrayList<ImageProperties> imageproperties(JSONObject imageProperties ){
		ArrayList<ImageProperties> ip=new ArrayList<ImageProperties>();
		try {
			//JSONObject imageProperties = new JSONObject("imagePropertiesAnnotations");
			JSONObject dominantColors=imageProperties.getJSONObject("dominantColors");
			JSONArray colors=dominantColors.getJSONArray("colors");

			for(int i=0;i<colors.length();i++){
				Color color=new Color();
				ImageProperties image=new ImageProperties();
				JSONObject current= colors.getJSONObject(i);
				JSONObject colour=current.getJSONObject("color");
				color.setBlue(colour.getInt("blue"));
				color.setGreen(colour.getInt("green"));
				color.setRed(colour.getInt("red"));
				image.setColour(color);
				image.setScore(current.getDouble("score"));
				if(current.has("percent"))		image.setPercent(current.getDouble("percent"));
				image.setPixel_fraction(current.getDouble("pixelFraction"));
				if(current.has("hex"))		image.setHex(current.getString("hex"));
				if(current.has("rgb"))	image.setRgb(current.getString("rgb"));
				ip.add(image);
			}
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip;		
	}
}

