package com.imago.graphics;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.File;
import java.util.List;
import java.util.Iterator;

import javax.swing.JFrame;

import com.stromberglabs.jopensurf.SURFInterestPoint;

public class Swing extends Canvas {
	public String image;
	public Graphics g;
	List<SURFInterestPoint> points;
	public Swing(String image,List<SURFInterestPoint> points){
		this.image=image;
		this.points=points;
	}

public void paint(Graphics g){
	this.g=g;
	Toolkit t=Toolkit.getDefaultToolkit();
	java.awt.Image i=t.getImage(image);
	g.drawImage(i,0,0,this);
	Iterator<SURFInterestPoint> points_iterator = points.iterator();
	while(points_iterator.hasNext()){
		SURFInterestPoint current = points_iterator.next();
		System.out.println(current.getX()+" "+current.getY());
		g.fillOval((int)current.getX()-5, (int)current.getY()-5, 10, 10);
	}
}
}
