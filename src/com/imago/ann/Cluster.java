package com.imago.ann;

import java.util.HashMap;

public class Cluster{
	String name;
	HashMap <String,Long> instances=new HashMap<>();
	public String getMajorClass(){
		String max="";
		Long max_val=0l;
		for(String key:instances.keySet()){
	
			if(instances.get(key)>max_val){
				max_val=instances.get(key);
//				max=key;
			max=String.valueOf(Integer.parseInt(key)+1);
				
			}
		}
		return max;
	}
	
}