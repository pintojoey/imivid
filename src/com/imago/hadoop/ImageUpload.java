package com.imago.hadoop;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.imago.graphics.Image;
import com.imago.graphics.Utilities;

public class ImageUpload extends Configured implements Tool {

	public static void main(String args[]) throws Exception {


		int res = ToolRunner.run(new ImageUpload(), args);
		System.exit(res);
	}



	public int run(String[] arg0) throws Exception {


		Configuration conf = getConf();
		conf.set("fs.default.name","hdfs://localhost:9000");
		conf.set("mapred.job.tracker","hdfs://localhost:8088");
		FileSystem fs = FileSystem.get(conf);
	
		ArrayList<String>files=getFiles("Fruits");
		Iterator<String> iterator = files.iterator();
		while(iterator.hasNext()){
			File file=new File(iterator.next());
			Image image=Utilities.readImage(file.getAbsolutePath());
       
			File temp=Utilities.writeFile(image.getName()+","+image.serialize(),"temp.txt");
			fs.copyFromLocalFile(new Path(temp.getAbsolutePath()), new Path("/imago_serialized_text/"+file.getName().split("\\.")[0]+".txt"));
			
		}
		
		return 0;
	}
	
	public static ArrayList<String> getFiles(String folder){
		ArrayList<String> files=new ArrayList<String>();
		
			for(java.io.File file: new File(folder).listFiles()){
				
				files.add(file.getPath());
			}
			return files;
	}
}