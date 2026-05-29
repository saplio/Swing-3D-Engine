/*
 * class that manages the player position and all of the surfaces in space,
 * and does perspective calculations
 */

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class SpaceManager {
	
	private Container frame; //stores frame
	private Paintbrush brush; //stores paintbrush for updating the screen
	private ArrayList<Surface> surfaces = new ArrayList<Surface>(); //stores all surface objects
	
	private double playerX; //player X position
	private double playerY; //player Y position
	private double playerZ; //player Z position
	private double playerHorizontalDirection; //direction player is facing in the XY plane
	private double playerVerticalDirection; //direction playing is facing in the YZ plane
	
	//constructor that passes frame and declares and initializes paintbrush and player positions
	public SpaceManager(Container frame) {
		this.frame = frame;
		brush = new Paintbrush(frame);
		playerX = 0;
		playerY = 0;
		playerZ = 2;
		playerHorizontalDirection = 0;
		playerVerticalDirection = 0;
	}
	
	//private helper method that takes in a point in 3D space and returns its X position on the screen
	private int perspectiveCalcX(double[] coords3D) { 
		return (frame.getSize().width / 2) + (int)(400 * (coords3D[0] - playerX)/(coords3D[1] - playerY));
	}
	
	//private helper method that takes in a point in 3D space and returns its Y position on the screen
	private int perspectiveCalcY(double[] coords3D) {
		return (frame.getSize().height / 2) - (int)(400 * (coords3D[2] - playerZ)/(coords3D[1] - playerY));
	}
	
	//old private helper methods that return a warped pincushion perspective
	public int pincushionPerspectiveCalcX(double[] coords3D) { 
		return (frame.getSize().width / 2) + (int)(400 * ((Math.atan((coords3D[0] - playerX)/(coords3D[1] - playerY))) / (2 * 0.45)));
	}
	
	public int pincushionPerspectiveCalcY(double[] coords3D) {
		return (frame.getSize().height / 2) - (int)(400 * ((Math.atan((coords3D[2] - playerZ)/(coords3D[1] - playerY))) / (2 * 0.45)));
	}
	
	//old private helper methods that return a warped fisheye perspective
	public int fisheyePerspectiveCalcX(double[] coords3D) { 
		double theta = 0;
		
		if ((coords3D[0] - playerX >= 0) && (coords3D[2] - playerZ >= 0)) {
			theta = Math.atan((coords3D[2] - playerZ) / (coords3D[0] - playerX));
		}
		else if ((coords3D[0] - playerX <= 0) && (coords3D[2] - playerZ >= 0)) {
			theta = Math.PI + Math.atan((coords3D[2] - playerZ) / (coords3D[0] - playerX));
		}
		else if ((coords3D[0] - playerX < 0) && (coords3D[2] - playerZ < 0)) {
			theta = Math.PI + Math.atan((coords3D[2] - playerZ) / (coords3D[0] - playerX));
		}
		else if ((coords3D[0] - playerX >= 0) && (coords3D[2] - playerZ <= 0)) {
			theta = (Math.PI * 2) + Math.atan((coords3D[2] - playerZ) / (coords3D[0] - playerX));
		}
		
		double r = (500 * (Math.atan(Math.hypot((coords3D[0] - playerX), (coords3D[2] - playerZ)) / (coords3D[1] - playerY)) / (Math.PI * 0.45)));
		
		return (int)((frame.getSize().width / 2) + (r * Math.cos(theta)));	
	}
	
	public int fisheyePerspectiveCalcY(double[] coords3D) {
		double theta = 0;
		
		if ((coords3D[0] - playerX >= 0) && (coords3D[2] - playerZ >= 0)) {
			theta = Math.atan((coords3D[2] - playerZ) / (coords3D[0] - playerX));
		}
		else if ((coords3D[0] - playerX <= 0) && (coords3D[2] - playerZ >= 0)) {
			theta = 2 + Math.atan((coords3D[2] - playerZ) / (coords3D[0] - playerX));
		}
		else if ((coords3D[0] - playerX < 0) && (coords3D[2] - playerZ < 0)) {
			theta = 2 + Math.atan((coords3D[2] - playerZ) / (coords3D[0] - playerX));
		}
		else if ((coords3D[0] - playerX >= 0) && (coords3D[2] - playerZ <= 0)) {
			theta = (2 * 2) + Math.atan((coords3D[2] - playerZ) / (coords3D[0] - playerX));
		}
		
		double r = (500 * (Math.atan(Math.hypot((coords3D[0] - playerX), (coords3D[2] - playerZ)) / (coords3D[1] - playerY)) / (2 * 0.45)));
		
		return (int)((frame.getSize().height / 2) - (r * Math.sin(theta)));	
	}
	
	//return a version of the specified surface rotated according to the player direction
	private Surface rotate(int index) {
		//initialize the arrays of new sets of points
		double[] newX = new double[surfaces.get(index).getNumPoints()];
		double[] newY = new double[surfaces.get(index).getNumPoints()];
		double[] newZ = new double[surfaces.get(index).getNumPoints()];
		
		//rotate along the XY plane
		for (int i = 0; i < surfaces.get(index).getNumPoints(); ++i) {
			double oldX = surfaces.get(index).getXArray()[i] - playerX;
			double oldY = surfaces.get(index).getYArray()[i] - playerY;
			newX[i] = (oldX * Math.cos(playerHorizontalDirection) - oldY * Math.sin(playerHorizontalDirection)) + playerX;
			newY[i] = (oldX * Math.sin(playerHorizontalDirection) + oldY * Math.cos(playerHorizontalDirection)) + playerY;
		}
		
		//rotate along the YZ plane
		for (int i = 0; i < surfaces.get(index).getNumPoints(); ++i) {
			double oldY = newY[i] - playerY;
			double oldZ = surfaces.get(index).getZArray()[i] - playerZ;
			newY[i] = (oldY * Math.cos(playerVerticalDirection) - oldZ * Math.sin(playerVerticalDirection)) + playerY;
			newZ[i] = (oldY * Math.sin(playerVerticalDirection) + oldZ * Math.cos(playerVerticalDirection)) + playerZ;
		}
		//FIXME: not as efficient as it could be
		
		//return a new surface using the new points
		return new Surface(newX, newY, newZ);
	}
	
	//private helper method that rotates a specified surface and finds the part of it that is in front of the player
	private double[][] rotateAndFindPointsInFront(int surfaceNum) {
		//rotate surface
		Surface rotatedSurface = rotate(surfaceNum);
		
		ArrayList<double[]> pointsInFront = new ArrayList<double[]>(); //stores vertices of part of surface that is in front
		int j;
		
		//iterate through all edges except the last one
		for (j = 0; j < rotatedSurface.getNumPoints() - 1; ++j) {
			//if both points of current edge are in front, just add the beginning point
			if (rotatedSurface.getPointCoords(j)[1] > playerY + 0.01 && rotatedSurface.getPointCoords(j + 1)[1] > playerY + 0.01) {
				pointsInFront.add(rotatedSurface.getPointCoords(j));
			}
			//if only the beginning point of current edge is in front, add the beginning point
			//and add another point where the edge goes from being in front to being behind
			else if (rotatedSurface.getPointCoords(j)[1] > playerY + 0.01) {
				pointsInFront.add(rotatedSurface.getPointCoords(j));
				
				double ratio = (rotatedSurface.getPointCoords(j)[1] - playerY) / ((rotatedSurface.getPointCoords(j)[1] - playerY) - (rotatedSurface.getPointCoords(j + 1)[1] - playerY));
				double edgePointX = (rotatedSurface.getPointCoords(j + 1)[0] - rotatedSurface.getPointCoords(j)[0]) * ratio + rotatedSurface.getPointCoords(j)[0];
				double edgePointZ = (rotatedSurface.getPointCoords(j + 1)[2] - rotatedSurface.getPointCoords(j)[2]) * ratio + rotatedSurface.getPointCoords(j)[2];
				double[] edgePoint = {edgePointX, playerY + 0.01, edgePointZ};
				
				pointsInFront.add(edgePoint);
			}
			//if only the end point of current edge is in front, add only the
			//point where the edge goes from being in behind to being in front
			else if (rotatedSurface.getPointCoords(j + 1)[1] > playerY + 0.01) {
				double ratio = (rotatedSurface.getPointCoords(j)[1] - playerY) / ((rotatedSurface.getPointCoords(j)[1] - playerY) - (rotatedSurface.getPointCoords(j + 1)[1] - playerY));
				double edgePointX = (rotatedSurface.getPointCoords(j + 1)[0] - rotatedSurface.getPointCoords(j)[0]) * ratio + rotatedSurface.getPointCoords(j)[0];
				double edgePointZ = (rotatedSurface.getPointCoords(j + 1)[2] - rotatedSurface.getPointCoords(j)[2]) * ratio + rotatedSurface.getPointCoords(j)[2];
				double[] edgePoint = {edgePointX, playerY + 0.01, edgePointZ};
				
				pointsInFront.add(edgePoint);
			}
		}
		
		//use slightly different code for last edge, replacing 'j + 1' with '0'
		j = rotatedSurface.getNumPoints() - 1;
		
		//if both points of current edge are in front, just add the beginning point
		if (rotatedSurface.getPointCoords(j)[1] > playerY + 0.01 && rotatedSurface.getPointCoords(0)[1] > playerY + 0.01) {
			pointsInFront.add(rotatedSurface.getPointCoords(j));
		}
		//if only the beginning point of current edge is in front, add the beginning point
		//and add another point where the edge goes from being in front to being behind
		else if (rotatedSurface.getPointCoords(j)[1] > playerY + 0.01) {
			pointsInFront.add(rotatedSurface.getPointCoords(j));
			
			double ratio = (rotatedSurface.getPointCoords(j)[1] - playerY) / ((rotatedSurface.getPointCoords(j)[1] - playerY) - (rotatedSurface.getPointCoords(0)[1] - playerY));
			double edgePointX = (rotatedSurface.getPointCoords(0)[0] - rotatedSurface.getPointCoords(j)[0]) * ratio + rotatedSurface.getPointCoords(j)[0];
			double edgePointZ = (rotatedSurface.getPointCoords(0)[2] - rotatedSurface.getPointCoords(j)[2]) * ratio + rotatedSurface.getPointCoords(j)[2];
			double[] edgePoint = {edgePointX, playerY + 0.01, edgePointZ};
			
			pointsInFront.add(edgePoint);
		}
		//if only the end point of current edge is in front, add only the
		//point where the edge goes from being in front to being behind
		else if (rotatedSurface.getPointCoords(0)[1] > playerY + 0.01) {
			double ratio = (rotatedSurface.getPointCoords(j)[1] - playerY) / ((rotatedSurface.getPointCoords(j)[1] - playerY) - (rotatedSurface.getPointCoords(0)[1] - playerY));
			double edgePointX = (rotatedSurface.getPointCoords(0)[0] - rotatedSurface.getPointCoords(j)[0]) * ratio + rotatedSurface.getPointCoords(j)[0];
			double edgePointZ = (rotatedSurface.getPointCoords(0)[2] - rotatedSurface.getPointCoords(j)[2]) * ratio + rotatedSurface.getPointCoords(j)[2];
			double[] edgePoint = {edgePointX, playerY + 0.01, edgePointZ};
			
			pointsInFront.add(edgePoint);
		}
		
		double[][] temp = new double[pointsInFront.size()][3];
		return pointsInFront.toArray(temp); //convert array list to two dimensional array and return it
	}
	
	//create a new surface
	public void addSurface(double[] x, double[] y, double[] z, Color color) throws InvocationTargetException, InterruptedException {
		surfaces.add(new Surface(x, y, z));
		
		int[] screenX = new int[x.length];
		int[] screenY = new int[x.length];
		
		//perform perspective calculations and pass them to the paintbrush
		for (int i = 0; i < x.length; ++i) {
			
			double[] pointCoords = {x[i], y[i], z[i]};
			
			screenX[i] = perspectiveCalcX(pointCoords);
			screenY[i] = perspectiveCalcY(pointCoords);
		}
		
		SwingUtilities.invokeAndWait(() -> {
			brush.addPolygon(screenX, screenY, color);
			movePlayer(0, 0, 0); //FIXME: quick dirty fix of moving the player to the same place to make sure the shape renders correctly
		});
	}
	
	//move player to a specified position in space
	public void movePlayerTo(double x, double y, double z) {
		playerX = x;
		playerY = y;
		playerZ = z;
		
		//perform perspective calculations on each surface and pass them to the paintbrush
		for (int i = 0; i < surfaces.size(); ++i) {
			double[][] properPoints = rotateAndFindPointsInFront(i); 
			
			int[] screenX = new int[properPoints.length];
			int[] screenY = new int[properPoints.length];
			
			if (properPoints.length > 0) {
				for (int j = 0; j < properPoints.length; ++j) {
					screenX[j] = perspectiveCalcX(properPoints[j]);
					screenY[j] = perspectiveCalcY(properPoints[j]);
				}
			}
			
			brush.warp(screenX, screenY, i);
		}
		
		//revalidate and repaint here instead of in the paintbrush so that it only happens once per perspective movement 
		frame.revalidate();
		frame.repaint();
	}
	
	//move the player by a specified amount
	public void movePlayer(double x, double y, double z) {
		playerX += x;
		playerY += y;
		playerZ += z;
		
		//perform perspective calculations on each surface and pass them to the paintbrush
		for (int i = 0; i < surfaces.size(); ++i) {
			double[][] properPoints = rotateAndFindPointsInFront(i); 
			
			int[] screenX = new int[properPoints.length];
			int[] screenY = new int[properPoints.length];
			
			if (properPoints.length > 0) {
				for (int j = 0; j < properPoints.length; ++j) {
					screenX[j] = perspectiveCalcX(properPoints[j]);
					screenY[j] = perspectiveCalcY(properPoints[j]);
				}
			}
			
			brush.warp(screenX, screenY, i);
		}
		
		//revalidate and repaint here instead of in the paintbrush so that it only happens once per perspective movement 
		frame.revalidate();
		frame.repaint();
	}
	
	//move the player by a specified amount relative to the direction they are facing
	public void movePlayerRelative(double amtRight, double amtForward, double amtUp) {
		
		playerX += amtForward * Math.sin(playerHorizontalDirection) + amtRight * Math.sin(playerHorizontalDirection + Math.PI / 2);
		playerY += amtForward * Math.cos(playerHorizontalDirection) + amtRight * Math.cos(playerHorizontalDirection +  Math.PI / 2);
		playerZ += amtUp;
		
		//perform perspective calculations on each surface and pass them to the paintbrush
		for (int i = 0; i < surfaces.size(); ++i) {
			double[][] properPoints = rotateAndFindPointsInFront(i); 
			
			int[] screenX = new int[properPoints.length];
			int[] screenY = new int[properPoints.length];
			
			if (properPoints.length > 0) {
				for (int j = 0; j < properPoints.length; ++j) {
					screenX[j] = perspectiveCalcX(properPoints[j]);
					screenY[j] = perspectiveCalcY(properPoints[j]);
				}
			}
			
			brush.warp(screenX, screenY, i);
		}
		
		//revalidate and repaint here instead of in the paintbrush so that it only happens once per perspective movement 
		frame.revalidate();
		frame.repaint();
	}
	
	//rotate player in the XY plane
	public void turnPlayer(double addedAngle) {
		playerHorizontalDirection += addedAngle;
		playerHorizontalDirection += (playerHorizontalDirection < 0) ? Math.PI * 2 : 0;
		playerHorizontalDirection -= (playerHorizontalDirection > Math.PI * 2) ? Math.PI * 2 : 0;
		
		//perform perspective calculations on each surface and pass them to the paintbrush
		for (int i = 0; i < surfaces.size(); ++i) {
			double[][] properPoints = rotateAndFindPointsInFront(i);
			
			int[] screenX = new int[properPoints.length];
			int[] screenY = new int[properPoints.length];
			
			if (properPoints.length > 0) {
				for (int j = 0; j < properPoints.length; ++j) {
					screenX[j] = perspectiveCalcX(properPoints[j]);
					screenY[j] = perspectiveCalcY(properPoints[j]);
				}
			}
			
			brush.warp(screenX, screenY, i);
		}
		
		//revalidate and repaint here instead of in the paintbrush so that it only happens once per perspective movement 
		frame.revalidate();
		frame.repaint();
	}
	
	//rotate player in the YZ plane
	public void tiltPlayer(double addedAngle) {
		playerVerticalDirection -= addedAngle;
		playerVerticalDirection += (playerVerticalDirection < 0) ? Math.PI * 2 : 0;
		playerVerticalDirection -= (playerVerticalDirection > Math.PI * 2) ? Math.PI * 2 : 0;
		
		//perform perspective calculations on each surface and pass them to the paintbrush
		for (int i = 0; i < surfaces.size(); ++i) {
			double[][] properPoints = rotateAndFindPointsInFront(i);
			
			int[] screenX = new int[properPoints.length];
			int[] screenY = new int[properPoints.length];
			
			if (properPoints.length > 0) {
				for (int j = 0; j < properPoints.length; ++j) {
					screenX[j] = perspectiveCalcX(properPoints[j]);
					screenY[j] = perspectiveCalcY(properPoints[j]);
				}
			}
			
			brush.warp(screenX, screenY, i);
		}
		
		//revalidate and repaint here instead of in the paintbrush so that it only happens once per perspective movement 
		frame.revalidate();
		frame.repaint();
	}
	
	//getter method that returns player coordinates
	public double[] getPlayerCoords() {
		double[] coords = {playerX, playerY, playerZ};
		return coords;
	}
}
