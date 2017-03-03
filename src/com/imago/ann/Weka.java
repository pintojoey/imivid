package com.imago.ann;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import com.imago.graphics.Image;
import com.imago.graphics.Utilities;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


public class Weka {
	public static FastVector getAttributeSet(int image_size,ArrayList<String>class_names){
		FastVector fvWekaAttributes=new FastVector(image_size+class_names.size());
		for(int i=0;i<image_size;i++){
			fvWekaAttributes.add("pixel_"+i);
			
		}
		Iterator<String> class_iterator=class_names.iterator();
		while(class_iterator.hasNext()){
			fvWekaAttributes.add(class_iterator.next());
		}
		return fvWekaAttributes;
		

	}
	public static Instances getTrainingSet(ArrayList<Image>images,ArrayList<String>class_names,FastVector fvWekaAttributes){
		if(images.size()!=class_names.size())return null;
		Iterator<Image> image_iterator = images.iterator();
		Iterator<String>class_name_iterator=class_names.iterator();
		while(image_iterator.hasNext()){
			Image image=image_iterator.next();
			String class_name=class_name_iterator.next();
			Instance instance=getTrainingInstance(image, class_name, fvWekaAttributes);
		}
	}
	public static Instance getTrainingInstance(Image image,String class_name,FastVector fvWekaAttributes){

		Color color[][]=image.getPixel();
		int im_height=image.getHeight();
		int im_width=image.getWidth();
		Instance instance=new DenseInstance(1);
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
