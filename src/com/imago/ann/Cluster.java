package com.imago.ann;

import java.util.HashMap;

public class Cluster{
	String name;
	HashMap <Long,Long> image_instances=new HashMap<>();
	public Long getMajorClass(){
		Long max=-1l;
		Long max_val=0l;
//		System.out.println("instances=>"+image_instances);
		for(Long key:image_instances.keySet()){
	
			if(image_instances.get(key)>max_val){
				max_val=image_instances.get(key);
//				max=key;
			max=key;
			}
		}
//		System.out.println("max=>"+max);
		return max;
	}
	public Long getMajorCount(){
		Long max=-1l;
		Long max_val=0l;
//		System.out.println("instances=>"+image_instances);
		for(Long key:image_instances.keySet()){
	
			if(image_instances.get(key)>max_val){
				max_val=image_instances.get(key);
//				max=key;
			max=key;
			}
		}
		return max_val;
	}
	
}