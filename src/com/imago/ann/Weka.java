package com.imago.ann;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.imago.graphics.Image;
import com.imago.graphics.Utilities;

import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


public class Weka {
	public static void main(String[] args) throws Exception {
		long tStart = System.currentTimeMillis();
		

		
	
		ArrayList<Image>images=new ArrayList<Image>();  //Input vector
		Image image1 = Utilities.readImage("SampleImages/1.png");
		Image image2 = Utilities.readImage("SampleImages/2.png");
		images.add(image1);
		images.add(image2);
		ArrayList<Double> class_names=new ArrayList<Double>(); //Target Vector
		class_names.add(1.0);
		class_names.add(-1.0);
				
		System.out.println("readImages"+(System.currentTimeMillis()-tStart)/100);
		FastVector fvWekaAttributes = getAttributeSet(image1.getSize(), class_names);
		System.out.println("attribute set"+(System.currentTimeMillis()-tStart)/100);
		Instances trainingSet = getTrainingSet(images, class_names, fvWekaAttributes);
		System.out.println("training set"+(System.currentTimeMillis()-tStart)/100);
		MultilayerPerceptron mlp = trainMLP(trainingSet, fvWekaAttributes);
		System.out.println("training time"+(System.currentTimeMillis()-tStart)/100);
		writeMLPFile("neural_network", mlp);
		Instance result=classify(getTestingInstance(image1, fvWekaAttributes), mlp);
		System.out.println("classify time"+(System.currentTimeMillis()-tStart)/100);
		System.out.println(result.toString());
		
	}
	public static FastVector getAttributeSet(int image_size,ArrayList<Double>class_names){
		FastVector fvWekaAttributes=new FastVector(image_size+class_names.size());
		for(int i=0;i<image_size;i++){
			
			fvWekaAttributes.addElement(new Attribute("pixel_"+i));

		}
		Iterator<Double> class_iterator=class_names.iterator();
		FastVector fvClassVal = new FastVector(class_names.size());
		
		
		while(class_iterator.hasNext()){
			fvClassVal.addElement(class_iterator.next());
		}
		Attribute ClassAttribute = new Attribute("Class", String.valueOf(fvClassVal));
		fvWekaAttributes.addElement(ClassAttribute);

		return fvWekaAttributes;


	}
	public static Instances getTrainingSet(ArrayList<Image>images,ArrayList<Double>class_names,FastVector fvWekaAttributes){
		if(images.size()!=class_names.size())return null;
		Iterator<Image> image_iterator = images.iterator();
		Iterator<Double>class_name_iterator=class_names.iterator();
		Instances trainingSet = new Instances("Rel", fvWekaAttributes, images.size());
int count=0;
		while(image_iterator.hasNext()){
			Image image=image_iterator.next();
			Double class_name=class_name_iterator.next();
			Instance instance=getTrainingInstance(image, class_name, fvWekaAttributes);
			trainingSet.add(instance);
			
			
		}
		trainingSet.setClassIndex(256);
		return trainingSet;
	}
	public static MultilayerPerceptron trainMLP(Instances trainingSet, FastVector fvWekaAttributes) throws Exception {
		MultilayerPerceptron mlp =new MultilayerPerceptron();
		mlp.setLearningRate(0.1);
		mlp.setMomentum(0.2);
		mlp.setTrainingTime(0);
		mlp.setHiddenLayers("10");

		mlp.buildClassifier(trainingSet);
		return mlp;

	}
	public static Instance getTrainingInstance(Image image,Double class_name,FastVector fvWekaAttributes){
		HashMap<Long,Long> histogram=image.histogram_cal(); 
		Instance instance=new DenseInstance(256+1);
		int count=0;
		
		int im_height=image.getHeight();
		
		for (Long j = 0l; j < 256l; j++) {
			Long value=histogram.get((Long)j);
			if(value==null)value=0l;
			
				instance.setValue((Attribute)fvWekaAttributes.elementAt(count),value);
				
				count++;
				
		}
		
		if (class_name!=null){
	
			instance.setValue((Attribute)fvWekaAttributes.elementAt(count),class_name);
		}
	
	
		return instance;
	}
	public static Instance getTestingInstance(Image image,FastVector fvWekaAttributes){
		return getTrainingInstance(image, null, fvWekaAttributes);
	}
	public static void writeMLPFile(String file,MultilayerPerceptron mlp) throws Exception{

		weka.core.SerializationHelper.write(file, mlp);

	}
	public static File getMLPFile(String file_name,MultilayerPerceptron mlp) throws Exception{
		File file=File.createTempFile(file_name, String.valueOf(new Date().getTime()));
		FileOutputStream os=new FileOutputStream(file);
		
		weka.core.SerializationHelper.write(os, mlp);
		os.close();
		return file;

	}
	public static MultilayerPerceptron readMLPFile(String file) throws Exception{

		MultilayerPerceptron mlp = (MultilayerPerceptron)	weka.core.SerializationHelper.read(file);
		return mlp;

	}
	public static Instance classify(Instance instance,MultilayerPerceptron mlp) throws Exception{
		double clsLabel = mlp.classifyInstance(instance);
		instance.setClassValue(clsLabel);

		return instance;

	}

}
