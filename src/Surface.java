/*
 * custom class that holds the 3D coordinates of each individual surface in space
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Surface {

	public static final Color DEFAULT_COLOR = new Color(0, 0, 0, 0);

	//TODO: remove
	private double[] x; //stores x coordinates of vertices
	private double[] y; //stores y coordinates of vertices
	private double[] z; //stores z coordinates of vertices

	private Color color;

	private ArrayList<Point3D> points;
	
	//TODO: remove
	//constructor that passes positions of vertices in space
	public Surface(double[] x, double[] y, double[] z) {
		this.x = new double[x.length];
		this.y = new double[y.length];
		this.z = new double[z.length];
		
		for (int i = 0; i < x.length; ++i) {
			this.x[i] = x[i];
			this.y[i] = y[i];
			this.z[i] = z[i];
		}
	}

	public Surface() {
		this(DEFAULT_COLOR);
	}

	public Surface(Color c) {
		points = new ArrayList<Point3D>();
		color = c;
	}

	public Surface(List<Point3D> points) {
		this(points, DEFAULT_COLOR);
	}

	public Surface(List<Point3D> points, Color c) {
		this.points = new ArrayList<Point3D>(points);
		color = c;
	}
	
	public List<Point3D> getPoints() {
		return Collections.unmodifiableList(points);
	}

	public void addPoint(Point3D point) {
		if (!(points.contains(point))) {
			points.add(point);
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		color = c;
	}

	//TODO: remove old methods
	//getter method that returns coordinates of one of the surface's vertices
	public double[] getPointCoords(int pointNum) {
		double[] coords = {x[pointNum], y[pointNum], z[pointNum]};
		return coords;
	}
	
	//get x coordinates of vertices
	public double[] getXArray() {
		return x;
	}
	
	//get y coordinates of vertices
	public double[] getYArray() {
		return y;
	}
	
	//get z coordinates of vertices
	public double[] getZArray() {
		return z;
	}
	
	//getter method that returns the amount of vertices
	public int getNumPoints() {
		return x.length;
	}
}
