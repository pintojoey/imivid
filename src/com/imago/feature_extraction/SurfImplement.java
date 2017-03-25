package com.imago.feature_extraction;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.imago.graphics.Swing;
import com.stromberglabs.jopensurf.SURFInterestPoint;
import com.stromberglabs.jopensurf.Surf;

public class SurfImplement {
	public static void main(String args[]){
		try {
			BufferedImage image = ImageIO.read(new File("SampleImages"+File.separator+"birds.jpg"));
			
		
			Surf board = new Surf(image);
			board.saveToFile(board,"surf_test.bin");
			Surf boarder = board.readFromFile("surf_test.bin");
			List<SURFInterestPoint> points = boarder.getFreeOrientedInterestPoints();

			Swing m=new Swing("SampleImages"+File.separator+"birds.jpg",points);
			JFrame f=new JFrame();
			f.add(m);
			f.setSize(image.getHeight(),image.getWidth());
			f.setVisible(true);
			System.out.println("Found " + points.size() + " interest points");
	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
//	public static void saveToFile(Surf surf,String file){
//		try {
//			ObjectOutputStream stream = new ObjectOutputStream(
//					new FileOutputStream(file));
//			stream.writeObject(surf);
//			stream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static Surf readFromFile(String location) throws Exception {
//		File file = new File(location);
//		if (file.exists()) {
//            return readInputStream(new FileInputStream(file));
//		}
//		return null;
//	}
}
