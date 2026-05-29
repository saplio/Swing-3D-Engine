/*
 * custom class that extends built in JComponent class for each individual 2D shape on the screen,
 * stores 2D coordinates of vertices on the screen and overrides built in paintComponent method to
 * add them to the screen
 */

import java.awt.*;
import javax.swing.*;

public class Shape extends JComponent {
	
	private int[] x; //stores x coordinates of all vertices
	private int[] y; //stores y coordinates of all vertices
	private Color c; //stores color of shape
	private int xpos; //x coordinate of shape, corresponds to the first vertex
	private int ypos; //y coordinate of shape, corresponds to the first vertex
	
	//constructor that passes a set of vertices and a color with which to paint the shape
	public Shape(int[] x, int[] y, Color color) {
		
		c = color;
		
		this.x = new int[x.length];
		this.y = new int[y.length];
		
		for (int i = 0; i < x.length; ++i) {
			this.x[i] = x[i];
			this.y[i] = y[i];
		}
		
		xpos = x[0];
		ypos = y[0];
	}
	
	//move shape's first vertex to the specified coordinates
	public void moveTo(int x, int y) {
		
		for (int i = 0; i < this.x.length; ++i) {
			this.x[i] += x - xpos;
			this.y[i] += y - ypos;
		}
		
		xpos = x;
		ypos = y;
	}
	
	//called during player movement, gives a new set of vertices for the shape to be painted at
	public void warp(int[] x, int[] y) {		
		this.x = new int[x.length];
		this.y = new int[y.length];
		
		for (int i = 0; i < this.x.length; ++i) {
			this.x[i] = x[i];
			this.y[i] = y[i];
		}
	}
	
	//overrides the paintComponent method of the JComponent class, which gets called internally
	//when needed to properly paint the component on the screen. Uses stored color and coordinates to
	//draw the shape
	public void paintComponent(Graphics g) { 
		g.setColor(c);
		g.fillPolygon(x, y, x.length);
		
		//add a black border
		g.setColor(Color.BLACK);
		g.drawPolygon(x, y, x.length);
	}
}
