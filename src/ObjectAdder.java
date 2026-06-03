/*
 * This class has a static method used to allow the user
 * to add new objects to the space, reading from text files
 * in the project
 */

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

public class ObjectAdder {
	
	public static final String OBJECTS_PATH = "src/resources/objects/";

	//TODO: update with rework
	//static method for adding objects
	public static void addObject(SpaceManager spaceManager) throws IOException, InvocationTargetException, InterruptedException {
		String selection; //stores the chosen object to add
		double scale = 1; // stores the chosen scaling factor of object (defaults to 1)
		
		File projectFile = new File(OBJECTS_PATH); // get directory path where object files should be located

		//get list of files in the project folder and filter out all files that are not text files
		ArrayList<String> options = new ArrayList<String>();
		for (int i = 0; i < projectFile.listFiles().length; ++i) {
			if (projectFile.listFiles()[i].getName().endsWith(".txt")) {
				options.add(projectFile.listFiles()[i].getName().replace(".txt", ""));
			}
		}
		//exit the method if no text files are available
		if (options.size() == 0) {
			JOptionPane.showMessageDialog(null, "There are no available object files to use", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//prompt user to choose an object to add
		selection = (String)JOptionPane.showInputDialog(null, "Choose an object to add:",
				"Add an Object", JOptionPane.PLAIN_MESSAGE, null, options.toArray(), options.get(0));
		
		//exit method if the user presses X or cancel on the window
		if (selection == null) {
			return;
		}
		
		//prompt the user for the scaling factor of the object, trying again if the input is invalid
		String errorMessage = "";
		while (true) {
			String strScale = JOptionPane.showInputDialog(null, errorMessage + "Choose scaling factor of object:\n(will set to default if left blank)",
					"Set Scale", JOptionPane.PLAIN_MESSAGE);
			
			//exit method if the user presses X or cancel on the window
			if (strScale == null) {
				return;
			}
			
			//check if the user entered a valid double or left the prompt blank, add an error message to the next
			//instance of the prompt if not
			try {
				if (!strScale.isBlank()) {
					scale = Double.parseDouble(strScale);
				}
				break;
			}
			catch(Exception e) {
				errorMessage = "Invalid input! Try again:\n";
			}
		}

		//begin reading from file of chosen object
		FileInputStream fileInputStream = new FileInputStream(OBJECTS_PATH + selection + ".txt");
		Scanner scnr = new Scanner(fileInputStream);

		//try to create an object based on color and coordinate information from the files
		//if the file is formatted incorrectly a placeholder will be created instead
		try {
			ArrayList<double[][]> surfaceCoords = new ArrayList<double[][]>(); //stores all of the coordinate information of the object
			ArrayList<Color> surfaceColors = new ArrayList<Color>(); //stores all of the color information of the object
			
			//iterate through each surface described in the text file
			while (scnr.hasNext()) {
				//read color of surface
				int r = scnr.nextInt();
				int g = scnr.nextInt();
				int b = scnr.nextInt();
				surfaceColors.add(new Color(r, g, b));
				
				String surface = "";
				int amtPoints = 0;
				scnr.nextLine();
				String tempLine = scnr.nextLine().trim();
				
				while (!tempLine.isBlank()) {
					++amtPoints;
					surface += tempLine + " ";
					
					if (scnr.hasNext()) {
						tempLine = scnr.nextLine().trim();
					}
					else {
						break;
					}
				}
				
				double[] objX = new double[amtPoints];
				double[] objY = new double[amtPoints];
				double[] objZ = new double[amtPoints];
				
				//read coordinates of vertices of surface
				Scanner surfaceScnr = new Scanner(surface);
				for (int i = 0; i < amtPoints; ++i) {
					objX[i] = spaceManager.getPlayerCoords()[0] + (surfaceScnr.nextDouble() * scale);
					objY[i] = spaceManager.getPlayerCoords()[1] + (surfaceScnr.nextDouble() * scale) + 1;
					objZ[i] = spaceManager.getPlayerCoords()[2] + (surfaceScnr.nextDouble() * scale);
				}
				
				double[][] objXYZ = {objX, objY, objZ};
				surfaceCoords.add(objXYZ);
				
				surfaceScnr.close();
			}
			
			//add surfaces using stored data; doing this upon reading data for each surface
			//instead of storing all the data and doing it at the end like this causes 
			//the adder to create some surfaces for the object and then also create the error object
			//once it encounters the error rather than just creating the error object
			for (int i = 0; i < surfaceCoords.size(); ++i) {
				spaceManager.addSurface(surfaceCoords.get(i)[0], surfaceCoords.get(i)[1],surfaceCoords.get(i)[2], surfaceColors.get(i));
			}
		}
		catch (Exception e) {
			//if something went wrong with the file scanning, create a placeholder object instead
			double[] squareX = {spaceManager.getPlayerCoords()[0], spaceManager.getPlayerCoords()[0] - 1, spaceManager.getPlayerCoords()[0] - 1, spaceManager.getPlayerCoords()[0]};
			double[] squareY = {spaceManager.getPlayerCoords()[1] + 1, spaceManager.getPlayerCoords()[1] + 1, spaceManager.getPlayerCoords()[1] + 1, spaceManager.getPlayerCoords()[1] + 1};
			double[] squareZ = {spaceManager.getPlayerCoords()[2], spaceManager.getPlayerCoords()[2], spaceManager.getPlayerCoords()[2] + 1, spaceManager.getPlayerCoords()[2] + 1};	
			spaceManager.addSurface(squareX, squareY, squareZ, Color.MAGENTA);
			
			for (int i = 0; i < squareX.length; ++i) {
				squareX[i] += 1;
			}
			spaceManager.addSurface(squareX, squareY, squareZ, Color.BLACK);
			
			for (int i = 0; i < squareZ.length; ++i) {
				squareZ[i] -= 1;
			}
			spaceManager.addSurface(squareX, squareY, squareZ, Color.MAGENTA);
			
			for (int i = 0; i < squareX.length; ++i) {
				squareX[i] -= 1;
			}
			spaceManager.addSurface(squareX, squareY, squareZ, Color.BLACK);
		}
		
		//close scanner and file input stream
		scnr.close();
		fileInputStream.close();
	}
}
