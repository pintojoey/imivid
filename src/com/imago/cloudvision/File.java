package com.imago.cloudvision;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;


public class File {
public static void main(String[] args) {
	//System.out.println(readFile("megha.txt"));
	
}
public static String readFile(String filename){
	String config="";
	try{
		BufferedReader br=new BufferedReader(new FileReader(filename));
		String read=null;
		StringBuffer sb=new StringBuffer();
		while((read=br.readLine())!=null ){sb.append(read+"\n");}
		config=sb.toString();
		br.close();
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return config;


}

public static void writeFile(String text,String location){
	
    FileOutputStream out = null;
    StringReader data=null;
    try {
    	 data=new StringReader(text);
      
       out = new FileOutputStream(location);
       
       int c;
       while ((c = data.read()) != -1) {
          out.write(c);
       }
    }
       catch (Exception e){
    	   e.printStackTrace();
       }
    finally {
       if (data != null) {
          try {
			data.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
       }
       if (out != null) {
          try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
       }
    }
	
}
}
