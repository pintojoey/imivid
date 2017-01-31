package com.imago.ann;

import java.awt.Color;
import java.util.ArrayList;

import com.imago.graphics.Image;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;


public class Weka {
	public static FastVector getAttributeSet(int image_size,ArrayList<String>class_names){
		
	}
	public static Instances getTrainingSet(ArrayList<Image>images,ArrayList<String>class_names,FastVector fvWekaAttributes){
		
	}
	public static Instance getTrainingInstance(Image image,String class_name,FastVector fvWekaAttributes){

		Color color[][]=image.getPixel();
		int im_height=image.getHeight();
		int im_width=image.getWidth();
		Instance instance=new Instance(1);
		int count=0;
		for (int j = 0; j < im_height; j++) {
			for (int k = 0; k < im_width; k++) {
				instance.setValue((Attribute)fvWekaAttributes.elementAt(count),color[j][k].getBlue());
				count++;
			}	
		}
		instance.setValue((Attribute)fvWekaAttributes.elementAt(count),class_name);
		return instance;
	}
}
