package com.imago.hadoop;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

import com.imago.ann.Weka;
import com.imago.graphics.Image;
import com.imago.graphics.Utilities;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class MapReduce extends Configured implements Tool {
	 public static FastVector attributes=null;
  public static void main(String args[]) throws Exception {
    
   
    int res = ToolRunner.run(new MapReduce(), args);
    System.exit(res);
  }



  public int run(String[] args) throws Exception {
    
	  
	  ArrayList<Double> classes=new ArrayList<Double>();
	  
	 for(double i=0;i<=1;i++){
		 classes.add(i);
	 }
	  
	attributes=Weka.getAttributeSet(256, classes);
	  
	  
	  
	Path inputPath = new Path("/imago_serialized_text/");
    Path outputPath = new Path("/imago_output_"+new Date().getTime()+"/");

    Configuration conf = getConf();
    conf.set("fs.default.name","hdfs://localhost:9000");
    conf.set("mapred.job.tracker","hdfs://localhost:8088");
    Job job = new Job(conf, this.getClass().toString());

    FileInputFormat.setInputPaths(job, inputPath);
    FileOutputFormat.setOutputPath(job, outputPath);

    
    
    
    job.setJobName("Imago");
    job.setJarByClass(MapReduce.class);
    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    job.setMapperClass(Map.class);
    job.setCombinerClass(Reduce.class);
    job.setReducerClass(Reduce.class);

    return job.waitForCompletion(true) ? 0 : 1;
    
    
    
    
  }

  public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
  
    private Text word = new Text();

    @Override
    public void map(LongWritable key, Text value,
                    Mapper.Context context) throws IOException, InterruptedException {
    	
      String line = value.toString();
      System.out.println("reached1");
      
      Image image=Utilities.getImage(line.substring(line.indexOf(",")+1));
        word.set(String.valueOf(image.getHeight()));
        Image blank_image=new Image(576,768);
        
        ArrayList<Image> images=new ArrayList<Image>();
        images.add(image);
        images.add(blank_image);
        
        ArrayList<Double>classes=new ArrayList<Double>();
        classes.add(1.0);
        classes.add(0.0);
        
      Instances  instances=  Weka.getTrainingSet(images, classes, attributes);
      System.out.println("reached2");
  	File mlp_file =null;
      try {
mlp_file = Weka.getMLPFile("neural", Weka.trainMLP(instances, attributes));
new FileUpload(mlp_file,"/imago_serialized_ann"+ line.substring(0, line.indexOf(","))+".ann").uploadToHDFS();

	 System.out.println("reached3");
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      final IntWritable one = new IntWritable(1);
        
      context.write(mlp_file,null);
       
        
     
    
    }
  }

  public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
    	
    	//new FileUpload(mlp_file,"/imago_serialized_ann/"+ line.substring(0, line.indexOf(","))+".ann").uploadToHDFS();
    	
//      int sum = 0;
//     
//
//      for (IntWritable value : values) {
//    	 
//        sum +=value.get();
//      }
//     
//      context.write(key, new IntWritable(sum));
    }
  }

}