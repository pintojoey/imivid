package com.imago.hadoop;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class FileUpload extends Configured implements Tool{
	public File file;
	public String remote_location;
	public FileUpload(File file, String remote_location) {
	this.file=file;
	this.remote_location=remote_location;

	}

	public void uploadToHDFS() {
		int res = 0;
		try {
			res = ToolRunner.run(new FileUpload(file,remote_location), null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public int run(String[] arg0) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = getConf();
		conf.set("fs.default.name","hdfs://localhost:9000");
		conf.set("mapred.job.tracker","hdfs://localhost:8088");
		FileSystem fs = FileSystem.get(conf);
		fs.copyFromLocalFile(new Path(file.getAbsolutePath()), new Path(remote_location));
		return 0;
	}

}
