/*
 * class that manages all of the shape objects in 2D space on the screen
 */

import java.util.ArrayList;
import java.awt.*;

public class Paintbrush {
	
	private Container frame; //stores container
	private ArrayList<Shape> shapes = new ArrayList<Shape>(); //stores all shape objects
	
	//constructor that passes the frame to the private field
	public Paintbrush(Container frame) {
		this.frame = frame;
	}
	
	//adds a new shape object to the screen
	public void addPolygon(int[] x, int[] y, Color color) {
		shapes.add(new Shape(x, y, color));
		
		frame.add(shapes.get(shapes.size() - 1));
		
		//revalidate and repaint are built in methods that essentially update the screen
		frame.revalidate();
		frame.repaint();
	}
	
	//move a shape to a specified coordinate
	public void moveTo(int x, int y, int shapeNum) {
		shapes.get(shapeNum).moveTo(x, y);
		frame.revalidate();
		frame.repaint();
	}
	
	//calls the warp method of specified shape, passing coordinate information
	public void warp(int[] x, int[] y, int shapeNum) {
		shapes.get(shapeNum).warp(x, y);
//		revalidating + repainting should be done in the space manager since it only 
//		needs to be done once per perspective movement
	}
	
	//remove specified shape
	public void delete(int shapeNum) {
		frame.remove(shapes.get(shapeNum));
		shapes.remove(shapeNum);
		frame.revalidate();
		frame.repaint();
	}
	
	//return amount of shapes being drawn
	public int amtShapes() {
		return shapes.size();
	}
}
