package com.imago.opencv;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.imago.ann.WekaClustering;
import com.imago.graphics.Utilities;

import weka.clusterers.SelfOrganizingMap;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class PictureClusterer {
	public static final int max_images=16;
	public static JSONArray searchImage(String real_path) throws Exception{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	
		String query_image = real_path;
		//String query_image ="/home/joey/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/imago/Fruits//img_1.jpg";
		
		
		ArrayList<KeyPoint>keypoints_set=new ArrayList<KeyPoint>();
		ArrayList<Long>images_set=new ArrayList<Long>();
		
		for (Long i = 1l; i <= max_images; i++) {
	
			String test_image = "/home/maggie/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/imago/Fruits//img_"+i+".jpg";
			String filename="Fruits//img_"+i+".jpg";

			
			
			ArrayList<KeyPoint>keypoints=keypoint_extract(test_image);
			ArrayList<Long>image_labels=new ArrayList<Long>();
			for(int j=0;j<keypoints.size();j++){
				image_labels.add(i);
			}
			keypoints_set.addAll(keypoints);
			images_set.addAll(image_labels);
			
			
		
	

			//System.out.println(test_image+"=>"+simlarity_score);
		
			
			

		}
		WekaClustering.height=10;
		WekaClustering.width=10;
		FastVector fvWekaAttributes = WekaClustering.getAttributeSet();
		System.out.println("1");
		Instances training_instances = WekaClustering.getTrainingSet(keypoints_set,images_set, fvWekaAttributes);
		System.out.println("2");
		SelfOrganizingMap som = WekaClustering.trainSOM(training_instances,images_set, fvWekaAttributes);
		System.out.println("3");
//MatOfKeyPoint queryDescriptors = feature_extract(query_image);
		System.out.println("4");
		ArrayList<KeyPoint>keypoints=keypoint_extract(query_image);
		System.out.println("5");
		Instances query_instances=WekaClustering.getTestingSet(keypoints, fvWekaAttributes);
		System.out.println("6");
		Iterator<Instance> instance_iterator = query_instances.iterator();
		System.out.println("7");
		HashMap<String,Integer>set=new HashMap<String,Integer>();
		while(instance_iterator.hasNext()){
			
			String class_label="Fruits//img_"+String.valueOf(WekaClustering.classify(instance_iterator.next(), som)).trim()+".jpg";
			
			if(set.containsKey(class_label)){
				set.put(class_label,set.get(class_label)+1);
			}
			else{
				set.put(class_label,1);
			}
			
		}
		System.out.println(set);
	

		
		List<Map.Entry<String,Integer>>  list = new ArrayList<Entry<String, Integer>>(set.entrySet());
		Collections.sort( list, new Comparator<Map.Entry<String, Integer>>()
		{
			public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
			{
				if(o1.getValue()<o2.getValue() )
					return 0;
				else return -1;//Ascending order

			}
		} );
		
		JSONArray results=new JSONArray();
		int max_results=2;
		for(Map.Entry<String, Integer> entry:list){
			System.out.println(entry.getKey()+" ==== "+entry.getValue());
			if(entry.getValue()>0){
		        
				JSONObject jobject=new JSONObject();
				jobject.put("file", entry.getKey());
			jobject.put("similarity", entry.getValue());
				
		        results.put(jobject);
			}
			if(--max_results<=0)break;
	
	        
	    }
		return results;
		
	}
	public static void main(String[] args) {
	try {
		searchImage("SampleImages/1.jpg");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	}
	public static MatOfKeyPoint feature_extract(String query_image) throws IOException{
		BufferedImage inputImage=ImageIO.read(new File(query_image));
		
		BufferedImage resized_image=Utilities.resize(inputImage, 128, 128);
		 File temp = File.createTempFile("temp_"+new Date().getTime(), ".jpg");
		 ImageIO.write(resized_image,"JPG",temp);
		 System.out.println(resized_image.getType());
		 
		Mat queryImage = Highgui.imread(temp.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_COLOR);
	

		MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
		FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
		//		System.out.println("Detecting key points...");
		featureDetector.detect(queryImage, objectKeyPoints);
		
		KeyPoint[] keypoints = objectKeyPoints.toArray();
//				System.out.println(keypoints.length);
//				for (KeyPoint point:keypoints){
//					System.out.println(point.angle);
//				}

		MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
		DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
		//		System.out.println("Computing descriptors...");
		descriptorExtractor.compute(queryImage, objectKeyPoints, objectDescriptors);	
		return objectDescriptors;
	}
public static ArrayList<KeyPoint> keypoint_extract(String query_image) throws IOException{

	BufferedImage inputImage=ImageIO.read(new File(query_image));
	
	BufferedImage resized_image=Utilities.resize(inputImage, 512, 512);
	String file_name="temp_"+new Date().getTime();
	 File temp = File.createTempFile(file_name, ".jpg");
	 ImageIO.write(resized_image,"JPG",temp);
	 
	Mat queryImage = Highgui.imread(temp.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_COLOR);

		MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
		FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
		//		System.out.println("Detecting key points...");
		featureDetector.detect(queryImage, objectKeyPoints);
		


		MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
		DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
		//		System.out.println("Computing descriptors...");
		descriptorExtractor.compute(queryImage, objectKeyPoints, objectDescriptors);	
		KeyPoint[] keys=objectDescriptors.toArray();
		ArrayList<KeyPoint>keypoints=new ArrayList<KeyPoint>();
		
		int feature=0;
		for(int i=0;i<keys.length;i++){
			//System.out.println(keys[i].angle+" "+keys[i].octave+" "+keys[i].response+" "+keys[i].size);
			if(keys[i].angle!=0){
				feature++;
				keypoints.add(keys[i]);
				
			}
			
		}
		Collections.sort(keypoints, new Comparator<KeyPoint>() {
	        @Override
	        public int compare(KeyPoint o1, KeyPoint o2) {
	        	
	        	if(o1.response<o2.response)return 0;
	        	else return -1;
	            
	        }
	    });
		
		System.out.println(keys.length+" "+feature);
		System.out.println("first "+keypoints.get(0).response+" last "+keypoints.get(keypoints.size()-1).response);
		
//		return  new ArrayList<KeyPoint>(keypoints);
		return  new ArrayList<KeyPoint>(keypoints.subList(0, 100));
		
	}
	
	public static int test(MatOfKeyPoint objectDescriptors,MatOfKeyPoint sceneDescriptors) {

	




		List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
		DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
				System.out.println("Matching object and scene images...");
		descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);
//		KeyPoint[] keys=objectDescriptors.toArray();
//		ArrayList<KeyPoint>keypoints=new ArrayList<KeyPoint>();
//		
//		int feature=0;
//		for(int i=0;i<keys.length;i++){
//	//		System.out.println(keys[i].angle+" "+keys[i].octave+" "+keys[i].response+" "+keys[i].size);
//			if(keys[i].angle!=0){
//				feature++;
//				keypoints.add(keys[i]);
//			}
//			
//		}
//		
//		System.out.println(keys.length+" "+feature);
//		return keypoints;
		
//
				System.out.println("Calculating good match list...");
		LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

		float nndrRatio = 0.7f;

		for (int i = 0; i < matches.size(); i++) {
			MatOfDMatch matofDMatch = matches.get(i);
			DMatch[] dmatcharray = matofDMatch.toArray();
			DMatch m1 = dmatcharray[0];
			DMatch m2 = dmatcharray[1];

			if (m1.distance <= m2.distance * nndrRatio) {
				goodMatchesList.addLast(m1);

			}
		}
		return goodMatchesList.size();

		//		if (goodMatchesList.size() >= 5) {
		//			System.out.println("Object Found!!!");
		//
		//			List<KeyPoint> objKeypointlist = objectKeyPoints.toList();
		//			List<KeyPoint> scnKeypointlist = sceneKeyPoints.toList();
		//
		//			LinkedList<Point> objectPoints = new LinkedList<>();
		//			LinkedList<Point> scenePoints = new LinkedList<>();
		//
		//			for (int i = 0; i < goodMatchesList.size(); i++) {
		//				objectPoints.addLast(objKeypointlist.get(goodMatchesList.get(i).queryIdx).pt);
		//				scenePoints.addLast(scnKeypointlist.get(goodMatchesList.get(i).trainIdx).pt);
		//			}
		//
		//			MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
		//			objMatOfPoint2f.fromList(objectPoints);
		//			MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
		//			scnMatOfPoint2f.fromList(scenePoints);
		//
		//			Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);
		//
		//			Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
		//			Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);
		//
		//			obj_corners.put(0, 0, new double[]{0, 0});
		//			obj_corners.put(1, 0, new double[]{queryImage.cols(), 0});
		//			obj_corners.put(2, 0, new double[]{queryImage.cols(), queryImage.rows()});
		//			obj_corners.put(3, 0, new double[]{0, queryImage.rows()});
		//
		//			System.out.println("Transforming object corners to scene corners...");
		//			Core.perspectiveTransform(obj_corners, scene_corners, homography);
		//
		//			Mat img = Highgui.imread(test_image, Highgui.CV_LOAD_IMAGE_COLOR);
		//
		//			Core.line(img, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
		//			Core.line(img, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
		//			Core.line(img, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
		//			Core.line(img, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);
		//
		//			System.out.println("Drawing matches image...");
		//			MatOfDMatch goodMatches = new MatOfDMatch();
		//			goodMatches.fromList(goodMatchesList);
		//
		//			Features2d.drawMatches(queryImage, objectKeyPoints, testImage, sceneKeyPoints, goodMatches, matchoutput, matchestColor, newKeypointColor, new MatOfByte(), 2);
		//
		//			Highgui.imwrite("Fruits//outputImage.jpg", outputImage);
		//			Highgui.imwrite("Fruits//matchoutput.jpg", matchoutput);
		//			Highgui.imwrite("Fruits//img.jpg", img);
		//		} else {
		//			System.out.println("Object Not Found");
		//		}

		//		System.out.println("Ended....");
	}
	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

}
