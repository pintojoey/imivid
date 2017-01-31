package com.imago.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Utilities {
	public static Image readImage(String image_file){
		File input = new File(image_file);

		BufferedImage image = null;
		try {
			image = ImageIO.read(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		int im_height=image.getHeight();
         Image img=new Image();
		int im_width=image.getWidth();
		Color[][]colors = new Color[im_height][im_width];
int j=0,k=0;
		for (j = 0; j < im_height; j++) {
			for ( k = 0; k < im_width; k++) {
				colors[j][k] = new Color(image.getRGB(k, j));

			}	
		}
		img.setHeight(j);
		img.setWidth(k);
		img.setPixel(colors);
		img.setPath(image_file);
		img.setName(image_file.substring(image_file.lastIndexOf("\\")));
		return img;
	}
}
