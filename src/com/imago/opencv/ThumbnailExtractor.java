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

import com.imago.ann.Cluster;
import com.imago.ann.WekaClustering;
import com.imago.graphics.Image;
import com.imago.graphics.Utilities;
import com.imago.graphics.VideoThumbnails;
import com.imago.services.ExtractThumbnail;

import weka.clusterers.SelfOrganizingMap;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ThumbnailExtractor {
	
	public static JSONArray extract(String video_path,String dumpDirectory) throws Exception{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	
		String query_video = video_path;
		
		
	ArrayList<Image> frames = VideoThumbnails.extractFrames(query_video, dumpDirectory);
		ArrayList<KeyPoint>keypoints_set=new ArrayList<KeyPoint>();
		ArrayList<Long>images_set=new ArrayList<Long>();
		
		for (int i = 0; i < frames.size(); i++) {
	
			String test_image = frames.get(i).getPath();
			
			File old_file = new File(test_image);
			
	        File new_file = new File(dumpDirectory+"//img_"+(i+1)+".jpg");
	        old_file.renameTo(new_file);
	        
	        test_image=dumpDirectory+"/img_"+(i+1)+".jpg";
	        frames.get(i).setPath(test_image);
			
			ArrayList<KeyPoint>keypoints=keypoint_extract(test_image);
			ArrayList<Long>image_labels=new ArrayList<Long>();
			for(int j=0;j<keypoints.size();j++){
				image_labels.add(Long.valueOf(i));
			}
			keypoints_set.addAll(keypoints);
			images_set.addAll(image_labels);
			
			
		
	

		}
		WekaClustering.height=2;
		WekaClustering.width=2;
		FastVector fvWekaAttributes = WekaClustering.getAttributeSet();
		System.out.println("1");
		Instances training_instances = WekaClustering.getTrainingSet(keypoints_set,images_set, fvWekaAttributes);
		System.out.println("2");
		ArrayList<Cluster> clusters = WekaClustering.trainSOMClusters(training_instances,images_set, fvWekaAttributes);
		HashMap<String,Long>cluster_map=new HashMap<>();
		for(Cluster cluster:clusters){
			cluster_map.put(String.valueOf(cluster.getMajorClass()), cluster.getMajorCount());
		}
		JSONArray results=new JSONArray();
		for(Map.Entry<String, Long> entry:cluster_map.entrySet()){
			System.out.println(entry.getKey()+" ==== "+entry.getValue());
			if(entry.getValue()>0){
		        
				JSONObject jobject=new JSONObject();
				jobject.put("file", ExtractThumbnail.imageDumpPath+"/img_"+entry.getKey()+".jpg");
			jobject.put("similarity", entry.getValue());
				
		        results.put(jobject);
			}
	
	        
	    }
		return results;
		
	}

	public static ArrayList<KeyPoint> keypoint_extract(String query_image) throws IOException{
//System.out.println("query=>"+query_image);
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
			
				feature++;
				keypoints.add(keys[i]);
				
			
			
		}
		Collections.sort(keypoints, new Comparator<KeyPoint>() {
	        @Override
	        public int compare(KeyPoint o1, KeyPoint o2) {
	        	
	        	if(o1.response<o2.response)return 0;
	        	else return -1;
	            
	        }
	    });
		
			
//		return  new ArrayList<KeyPoint>(keypoints);
		if(keypoints.size()>100)
		return  new ArrayList<KeyPoint>(keypoints.subList(0, 100));
		else return new ArrayList<KeyPoint>(keypoints);
		
	}
	
	static{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

}
