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

import org.opencv.features2d.KeyPoint;


import weka.classifiers.functions.MultilayerPerceptron;
import weka.clusterers.SelfOrganizingMap;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;


public class WekaClustering {
	public static void main(String[] args) throws Exception {
		
		
//		
//		long tStart = System.currentTimeMillis();
//		
//
//		
//	
//		ArrayList<Image>images=new ArrayList<Image>();  //Input vector
//		Image image1 = Utilities.readImage("SampleImages/1.png");
//		Image image2 = Utilities.readImage("SampleImages/2.png");
//		images.add(image1);
//		images.add(image2);
//	
//				
//		System.out.println("readImages"+(System.currentTimeMillis()-tStart)/100);
//		FastVector fvWekaAttributes = getAttributeSet(4);
//		System.out.println("attribute set"+(System.currentTimeMillis()-tStart)/100);
//		Instances trainingSet = getTrainingSet(images,  fvWekaAttributes);
//		System.out.println("training set"+(System.currentTimeMillis()-tStart)/100);
//		SelfOrganizingMap som = trainSOM(trainingSet, fvWekaAttributes);
//		System.out.println("training time"+(System.currentTimeMillis()-tStart)/100);
//		writeSOMFile("self_organizing_map", som);
//		Instance result=classify(getTestingInstance(image1, fvWekaAttributes), som);
//		System.out.println("classify time"+(System.currentTimeMillis()-tStart)/100);
//		System.out.println(result.toString());
		
	}
	public static FastVector getAttributeSet(){
		FastVector fvWekaAttributes=new FastVector();
			
			fvWekaAttributes.addElement(new Attribute("angle"));
			fvWekaAttributes.addElement(new Attribute("octave"));
			fvWekaAttributes.addElement(new Attribute("response"));
			fvWekaAttributes.addElement(new Attribute("size"));
			fvWekaAttributes.addElement(new Attribute("image"));
			
	
		

		
	

		return fvWekaAttributes;


	}
	public static Instances getTrainingSet(ArrayList<KeyPoint>keypoints,ArrayList<Long>images,FastVector fvWekaAttributes) throws Exception{
	
		if(keypoints.size()!=images.size())throw new Exception("Number of keypoints not same as labels!");
		Iterator<KeyPoint> keypoint_iterator = keypoints.iterator();
		Iterator<Long> image_iterator = images.iterator();
		
		Instances trainingSet = new Instances("Rel", fvWekaAttributes, keypoints.size());
		
		while(keypoint_iterator.hasNext()){
			KeyPoint keypoint=keypoint_iterator.next();
			Long image=image_iterator.next();
		
			Instance instance=getTrainingInstance(keypoint, image,fvWekaAttributes);
			trainingSet.add(instance);
			
			
		}
		trainingSet.setClassIndex(4);
	
		return trainingSet;
	}
	public static Instances getTestingSet(ArrayList<KeyPoint>keypoints,FastVector fvWekaAttributes) throws Exception{
		
		Iterator<KeyPoint> keypoint_iterator = keypoints.iterator();
		
		Instances testingSet = new Instances("Rel", fvWekaAttributes, keypoints.size());
	
		
		while(keypoint_iterator.hasNext()){
			KeyPoint keypoint=keypoint_iterator.next();
		
			Instance instance=getTestingInstance(keypoint,fvWekaAttributes);
			testingSet.add(instance);
			
			
		}
		testingSet.setClassIndex(4);
		Remove filter = new Remove();
		  filter.setAttributeIndices("" + (testingSet.classIndex() + 1));
		  filter.setInputFormat(testingSet);
		  Instances newTest = Filter.useFilter(testingSet, filter);
		  
		  
	
		return newTest;
	}
	public static SelfOrganizingMap trainSOM(Instances trainingSet, FastVector fvWekaAttributes) throws Exception {
		SelfOrganizingMap som=new SelfOrganizingMap();
		

		Remove filter = new Remove();
		  filter.setAttributeIndices("" + (trainingSet.classIndex() + 1));
		  filter.setInputFormat(trainingSet);
		  Instances newTrain = Filter.useFilter(trainingSet, filter);
		  
		  som.setHeight(2);
		  som.setWidth(2);
		som.buildClusterer(newTrain);
		
	
		return som;
		

	}
	public static Instance getTrainingInstance(KeyPoint keypoint,Long image,FastVector fvWekaAttributes){

	Instance instance=new DenseInstance(5);	
	instance.setValue((Attribute)fvWekaAttributes.get(0),keypoint.angle);
	instance.setValue((Attribute)fvWekaAttributes.get(1),keypoint.octave);
	instance.setValue((Attribute)fvWekaAttributes.get(2),keypoint.response);
	instance.setValue((Attribute)fvWekaAttributes.get(3),keypoint.size);
	instance.setValue((Attribute)fvWekaAttributes.get(4),image);

		return instance;
	}
	public static Instance getTestingInstance(KeyPoint keypoint,FastVector fvWekaAttributes){

		Instance instance=new DenseInstance(5);	
		instance.setValue((Attribute)fvWekaAttributes.get(0),keypoint.angle);
		instance.setValue((Attribute)fvWekaAttributes.get(1),keypoint.octave);
		instance.setValue((Attribute)fvWekaAttributes.get(2),keypoint.response);
		instance.setValue((Attribute)fvWekaAttributes.get(3),keypoint.size);
	

			return instance;
	}
	
	public static void writeSOMFile(String file,SelfOrganizingMap som) throws Exception{

		weka.core.SerializationHelper.write(file, som);

	}

	public static SelfOrganizingMap readSOMFile(String file) throws Exception{

		SelfOrganizingMap som = (SelfOrganizingMap)	weka.core.SerializationHelper.read(file);
		return som;

	}
	public static double classify(Instance instance,SelfOrganizingMap som) throws Exception{

		  
		 int clsLabel = som.clusterInstance(instance);
		
		som.getClusters();
		
			System.out.println("Number of clusters:"+som.numberOfClusters());
			
		System.out.println(clsLabel);
		instance.setClassValue(clsLabel);


		return clsLabel+1;

	}

	

}