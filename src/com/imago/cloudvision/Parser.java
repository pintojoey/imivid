package com.imago.cloudvision;

import java.util.ArrayList;

import org.json.*;

class Position{
	private int x,y,z;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
}

class Type{
	private String type,value,friendly_type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFriendly_type() {
		return friendly_type;
	}

	public void setFriendly_type(String friendly_type) {
		this.friendly_type = friendly_type;
	}

}

class LandMark{
	private String type;
	Position position;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}

}
class Vertex{
	private int x,y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}

public class Parser {
	private ArrayList<Vertex> boundingPloy;
	private ArrayList<Vertex> fdBoundingPoly;
	private ArrayList<LandMark> landmark;
	private int roll_angle,pan_angle,tilt_angle;
	private double detection_confidence,landmarking_confidence;
	private String joy,sorrow,anger,surprise,under_exposed,blurred,headwear;
	private ArrayList<Type> types;
	private boolean showHeader;
	private int num;
	private SafeSearch safeSearch;
	private ArrayList<ImageProperties> properties;

	public boolean isShowHeader() {
		return showHeader;
	}


	public void setShowHeader(boolean showHeader) {
		this.showHeader = showHeader;
	}


	public int getNum() {
		return num;
	}


	public void setNum(int num) {
		this.num = num;
	}


	public int getRoll_angle() {
		return roll_angle;
	}


	public void setRoll_angle(int roll_angle) {
		this.roll_angle = roll_angle;
	}


	public int getPan_angle() {
		return pan_angle;
	}


	public void setPan_angle(int pan_angle) {
		this.pan_angle = pan_angle;
	}


	public int getTilt_angle() {
		return tilt_angle;
	}


	public void setTilt_angle(int tilt_angle) {
		this.tilt_angle = tilt_angle;
	}


	public double getDetection_confidence() {
		return detection_confidence;
	}


	public void setDetection_confidence(double detection_confidence) {
		this.detection_confidence = detection_confidence;
	}


	public double getLandmarking_confidence() {
		return landmarking_confidence;
	}


	public void setLandmarking_confidence(double landmarking_confidence) {
		this.landmarking_confidence = landmarking_confidence;
	}


	public String getJoy() {
		return joy;
	}


	public void setJoy(String joy) {
		this.joy = joy;
	}


	public String getSorrow() {
		return sorrow;
	}


	public void setSorrow(String sorrow) {
		this.sorrow = sorrow;
	}


	public String getAnger() {
		return anger;
	}


	public void setAnger(String anger) {
		this.anger = anger;
	}


	public String getSurprise() {
		return surprise;
	}


	public void setSurprise(String surprise) {
		this.surprise = surprise;
	}


	public String getUnder_exposed() {
		return under_exposed;
	}


	public void setUnder_exposed(String under_exposed) {
		this.under_exposed = under_exposed;
	}


	public String getBlurred() {
		return blurred;
	}


	public void setBlurred(String blurred) {
		this.blurred = blurred;
	}


	public String getHeadwear() {
		return headwear;
	}


	public void setHeadwear(String headwear) {
		this.headwear = headwear;
	}

	public ArrayList<ImageProperties> getProperties() {
		return properties;
	}


	public void setProperties(ArrayList<ImageProperties> properties) {
		this.properties = properties;
	}


	public ArrayList<Vertex> getBoundingPloy() {
		return boundingPloy;
	}


	public void setBoundingPloy(ArrayList<Vertex> boundingPloy) {
		this.boundingPloy = boundingPloy;
	}


	public ArrayList<Vertex> getFdBoundingPoly() {
		return fdBoundingPoly;
	}


	public void setFdBoundingPoly(ArrayList<Vertex> fdBoundingPoly) {
		this.fdBoundingPoly = fdBoundingPoly;
	}


	public ArrayList<LandMark> getLandmark() {
		return landmark;
	}


	public void setLandmark(ArrayList<LandMark> landmark) {
		this.landmark = landmark;
	}


	public ArrayList<Type> getTypes() {
		return types;
	}


	public void setTypes(ArrayList<Type> types) {
		this.types = types;
	}


	public SafeSearch getSafeSearch() {
		return safeSearch;
	}


	public void setSafeSearch(SafeSearch safeSearch) {
		this.safeSearch = safeSearch;
	}


	public static void main(String[] args) throws JSONException {
		Parser p=new Parser();
		String input=File.readFile("input.txt");
		JSONObject obj=new JSONObject(input);

		JSONArray faceAnn=obj.getJSONArray("faceAnnotations");
		JSONObject object=faceAnn.getJSONObject(0);

		JSONObject boundingPoly=object.getJSONObject("boundingPoly");
		JSONArray boundVertices=boundingPoly.getJSONArray("vertices");
		ArrayList<Vertex> boundVerticeList=new ArrayList<Vertex>(); 
		for(int i=0;i<boundVertices.length();i++){
			JSONObject vertex=boundVertices.getJSONObject(i);	
			Vertex current=new Vertex();
			current.setX(vertex.getInt("x"));
			current.setY(vertex.getInt("y"));
			boundVerticeList.add(current);
		}
		p.setBoundingPloy(boundVerticeList);
		JSONObject fdBoundingPoly=object.getJSONObject("fdBoundingPoly");
		JSONArray fdVertices=fdBoundingPoly.getJSONArray("vertices");
		ArrayList<Vertex> fdVerticeList=new ArrayList<Vertex>(); 
		for(int i=0;i<fdVertices.length();i++){
			JSONObject vertex=fdVertices.getJSONObject(i);	
			Vertex current=new Vertex();
			current.setX(vertex.getInt("x"));
			current.setY(vertex.getInt("y"));
			fdVerticeList.add(current);
		}
		p.setFdBoundingPoly(fdVerticeList);
		JSONArray landMarks=object.getJSONArray("landmarks");
		ArrayList<LandMark> position_list=new ArrayList<LandMark>();
		for(int i=0;i<landMarks.length();i++){
			LandMark current=new LandMark();
			JSONObject marks=landMarks.getJSONObject(i);
			current.setType(marks.getString("type"));
			Position cur=new Position();
			JSONObject pos=marks.getJSONObject("position");
			cur.setX(pos.getInt("x"));
			cur.setY(pos.getInt("y"));
			cur.setZ(pos.getInt("z"));
			current.setPosition(cur);
			position_list.add(current);
		}
		p.setLandmark(position_list);
		p.setRoll_angle(object.getInt("rollAngle"));
		p.setPan_angle(object.getInt("panAngle"));
		p.setTilt_angle(object.getInt("tiltAngle"));
		p.setAnger(object.getString("angerLikelihood"));
		p.setJoy(object.getString("joyLikelihood"));
		p.setSorrow(object.getString("sorrowLikelihood"));
		p.setSurprise(object.getString("surpriseLikelihood"));
		p.setUnder_exposed(object.getString("underExposedLikelihood"));
		p.setBlurred(object.getString("blurredLikelihood"));
		p.setHeadwear(object.getString("headwearLikelihood"));
		if(object.has("types")){
			JSONArray types=object.getJSONArray("types");
			ArrayList<Type> type_list=new ArrayList<Type>();
			for(int i=0;i<types.length();i++){
				JSONObject t=types.getJSONObject(i);
				Type current=new Type();
				current.setType(t.getString("type"));
				current.setValue(t.getString("value"));
				current.setFriendly_type(t.getString("friendlyType"));
				type_list.add(current);
			}
			p.setTypes(type_list);
		}
		SafeSearch s=new SafeSearch();
		JSONObject  safe_search=obj.getJSONObject("safeSearchAnnotation");
		SafeSearch safe=s.safeSearch(safe_search);
		p.setSafeSearch(safe);

		ImageProperties ip=new ImageProperties();
		JSONObject  image=obj.getJSONObject("imagePropertiesAnnotation");
		p.setProperties(ip.imageproperties(image));
		
		JSONObject jobj = obj.getJSONObject("webDetection");
		JSONArray jarray=jobj.getJSONArray("webEntities");
		for(int i=0;i<jarray.length();i++){
			System.out.println(jarray.getJSONObject(i).getString("description"));
		}
	}




	public static void parse(String input){

	}
}
