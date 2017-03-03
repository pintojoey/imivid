package com.imago.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

public class Utilities {
	public static void main(String[] args) {
		String base64=Utilities.readFile("img_1.txt");
		Image image=Utilities.getImage(base64);
		System.out.println(image.getHeight());
	}
	public static Image readImage(String image_file){
		File input = new File(image_file);
		BufferedImage image = null;
		try {
			image = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int im_height=image.getHeight();

		int im_width=image.getWidth();
        Image img=new Image();
		Color[][]colors = new Color[im_height][im_width];
int j=0,k=0;
		for (j = 0; j < im_height; j++) {
			for ( k = 0; k < im_width; k++) {
				colors[j][k] = new Color(image.getRGB(k, j));

			}	
		}
		img.setHeight(j);
		img.setWidth(k);
		img.setSize(j*k);
		img.setPixel(colors);
		img.setPath(image_file);
		img.setName(image_file.substring(image_file.lastIndexOf(File.separator))+1);
		return img;
	}
	public static String readFile(String filename) {
		String config = "";
		 
		try {
	    	 
	    	BufferedReader br=new BufferedReader(new FileReader(filename));
	    	String read = null;
	    	StringBuffer sb = new StringBuffer(); 
	    	while((read = br.readLine()) != null ) { 
	    	
	    	//sb.append(read+"\n");
	    		} 
	    	config = sb.toString();
	        br.close();
	    } catch(Exception e) {
	        e.printStackTrace();
	    }
	    
	    		
	    return config;
	}
	public static File writeFile(String text,String location){
		
	    FileOutputStream out = null;
	    StringReader data=null;
	    try {
	    	 data=new StringReader(text);
	      
	       out = new FileOutputStream(location);
	       
	       int c;
	       while ((c = data.read()) != -1) {
	          out.write(c);
	       }
	    }
	       catch (Exception e){
	    	   e.printStackTrace();
	       }
	    finally {
	       if (data != null) {
	          try {
				data.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	       }
	       if (out != null) {
	          try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	       }
	    }
		return new File(location);
		
	}
	public static Image getImage(String base64String){
		 
		  
	
		
		    File file = null;
			try {
				file = File.createTempFile("temp",".data");
				
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		    byte imageData1[] = Base64.decodeBase64(base64String);
			FileOutputStream imageInFile = null;
			try {
				imageInFile = new FileOutputStream(file);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    try {
				imageInFile.write(imageData1);
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
			BufferedImage image = null;
			try {
				image = ImageIO.read(file);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			int im_height=0;

			int im_width=0;
	        Image img=new Image();
	        Color[][]colors = null;
	        int j=0,k=0;
			if(image.getHeight()>image.getWidth()){
				im_height=image.getWidth();
				im_width=image.getHeight();
				im_height=100;
				im_width=100;
				
				colors = new Color[im_height][im_width];
				
				for (j = 0; j < im_height; j++) {
					for ( k = 0; k < im_width; k++) {
						colors[j][k] = new Color(image.getRGB(j, k));

					}	
				}
				
			}
			else{
				im_height=image.getHeight();
				im_width=image.getWidth();
				im_height=100;
				im_width=100;
				colors = new Color[im_height][im_width];
			
				for (j = 0; j < im_height; j++) {
					for ( k = 0; k < im_width; k++) {
						colors[j][k] = new Color(image.getRGB(k, j));

					}	
				}
			}
	       
			
			img.setHeight(j);
			img.setWidth(k);
			img.setSize(j*k);
			img.setPixel(colors);
			return img;
		}
	public static HashMap<Integer, Integer> histogram_cal(Image image){
		int height=image.getHeight();
		int width=image.getWidth();
		HashMap< Integer, Integer> histogram=new HashMap<Integer, Integer>();
		 Color[][] colors = image.getPixel();
		 int average=0;
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				average=(colors[i][j].getRed()+colors[i][j].getGreen()+colors[i][j].getBlue())/3;
			 Integer key = histogram.get(average);
			// System.out.println(average+" "+key);
				if(key==null) histogram.put(average, 1);
				else histogram.put(average,key+1);
			}
		}
		return histogram;
	}
	
	
    public static BufferedImage resize(BufferedImage inputImage ,
            int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
      //  File inputFile = new File(inputImagePath);
        //BufferedImage inputImage = ImageIO.read(inputFile);
 
        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());
 
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();
 

        return outputImage;
    }
    
}
