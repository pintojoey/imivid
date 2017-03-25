package com.imago.cloudvision;

import org.json.*;
public class SafeSearch {
private String adult,spoof,medical,violence;

public String getAdult() {
	return adult;
}

public void setAdult(String adult) {
	this.adult = adult;
}

public String getSpoof() {
	return spoof;
}

public void setSpoof(String spoof) {
	this.spoof = spoof;
}

public String getMedical() {
	return medical;
}

public void setMedical(String medical) {
	this.medical = medical;
}

public String getViolence() {
	return violence;
}

public void setViolence(String violence) {
	this.violence = violence;
}
public static void main(String[] args) {
}
public static SafeSearch safeSearch(JSONObject safeSearch){
	SafeSearch obj=new SafeSearch();
	try {
		
		
		obj.setAdult(safeSearch.getString("adult"));
		obj.setMedical(safeSearch.getString("medical"));
		obj.setSpoof(safeSearch.getString("spoof"));
		obj.setViolence(safeSearch.getString("violence"));
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	return obj;
}
}

