/*
 * custom class that holds the 3D coordinates of each individual surface in space
 */

public class Surface {

	private double[] x; //stores x coordinates of vertices
	private double[] y; //stores y coordinates of vertices
	private double[] z; //stores z coordinates of vertices
	
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
