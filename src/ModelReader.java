import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * This class has a static method used to allow the user
 * to add new objects to the space, reading from text files
 * in the project
 */

public class ModelReader {

	public static final String OBJECTS_PATH = "src/resources/models/";
	public static final String FILE_EXTENSION = ".txt";
	public static final String ERROR_FILE = "error";
	public static final String REGEX = "(?:[^\\d.-]|-(?=\\D)|\\.(?=\\D))+";

	public static final double DEFAULT_SCALE = 1;
	public static final int DEFAULT_TRANSPARENCY = 255;
	public static final int DIMENSIONS = 3;
	public static final double PLACEMENT_DIST_FORWARD = 2;
	public static final double PLACEMENT_DIST_RIGHT = 0;
	public static final double PLACEMENT_DIST_UP = 0;

	// TODO: change these methods to be called promptUserForModel and make a different readModel method that takes a File as an argument
	// this would remove make the code more organized and remove the need for an "error" parameter

	public static void promptUserForModel(Space space, Camera camera) {
		promptUserForModel(space, camera, false);
	}

	public static void promptUserForModel(Space space, Camera camera, boolean error) {
		String selection = ERROR_FILE; // stores the chosen object to add
		double scale = DEFAULT_SCALE; // stores the chosen scaling factor of object (defaults to 1)

		if (!error) {
			File projectFile = new File(OBJECTS_PATH); // get directory path where object files should be located

			// get list of files in the project folder and filter out all files that are not
			// text files
			ArrayList<String> options = new ArrayList<String>();
			for (File f : projectFile.listFiles()) {
				if (f.getName().endsWith(FILE_EXTENSION)) {
					options.add(f.getName().replace(FILE_EXTENSION, ""));
				}
			}
			// exit the method if no text files are available
			if (options.size() == 0) {
				JOptionPane.showMessageDialog(null, "There are no available object files to use", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// prompt user to choose an object to add
			selection = (String) JOptionPane.showInputDialog(null, "Choose an object to add:",
					"Add an Object", JOptionPane.PLAIN_MESSAGE, null, options.toArray(), options.get(0));

			// exit method if the user presses X or cancel on the window
			if (selection == null) {
				return;
			}

			// prompt the user for the scaling factor of the object, trying again if the
			// input is invalid
			String errorMessage = "";
			while (true) {
				String strScale = JOptionPane.showInputDialog(null,
						errorMessage + "Choose scaling factor of object:\n(will set to default if left blank)",
						"Set Scale", JOptionPane.PLAIN_MESSAGE);

				// exit method if the user presses X or cancel on the window
				if (strScale == null) {
					return;
				}

				// check if the user entered a valid double or left the prompt blank, add an
				// error message to the next
				// instance of the prompt if not
				try {
					if (!strScale.isBlank()) {
						scale = Double.parseDouble(strScale);
					}
					break;
				}
				catch (NumberFormatException e) {
					errorMessage = "Invalid input! Try again:\n";
				}
			}
		}

		// begin reading from file of chosen object
		try (FileInputStream fileInputStream = new FileInputStream(OBJECTS_PATH + selection + FILE_EXTENSION);
				Scanner scnr = new Scanner(fileInputStream);) {
			// try to create an object based on color and coordinate information from the
			// files
			// if the file is formatted incorrectly a placeholder will be created instead
			try {
				ArrayList<Surface> surfaces = new ArrayList<Surface>();
				// iterate through each surface described in the text file
				while (scnr.hasNextLine()) {
					String colorLine;

					do {
						colorLine = scnr.nextLine();
					} while (colorLine.replaceAll(REGEX, "").isBlank() && scnr.hasNextLine());

					if (colorLine.replaceAll(REGEX, "").isBlank()) {
						break;
					}

					String[] colorInfo = colorLine.replaceAll(REGEX, " ").trim().split(REGEX, 4);

					int r = Integer.parseInt(colorInfo[0]);
					int g = Integer.parseInt(colorInfo[1]);
					int b = Integer.parseInt(colorInfo[2]);
					int a = DEFAULT_TRANSPARENCY;

					try {
						a = Integer.parseInt(colorInfo[3]);
					}
					catch (ArrayIndexOutOfBoundsException e) {
					}

					Surface surface = new Surface(new Color(r, g, b, a));

					while (scnr.hasNextLine()) {
						String pointLine = scnr.nextLine();

						if (pointLine.replaceAll(REGEX, "").isBlank()) {
							break;
						}

						String[] pos = pointLine.replaceAll(REGEX, " ").trim().split(REGEX, DIMENSIONS);

						Point3D orthogonalPlacementDist = PerspectiveMath.cameraRelativeToOrthogonalXY(
								PLACEMENT_DIST_RIGHT, PLACEMENT_DIST_FORWARD, PLACEMENT_DIST_UP, camera.getYaw());
						
						// this line of code treats the coordinate information as camera relative,
						// making the orientation of the model depend on the camera yaw
						Point3D placementCoords = PerspectiveMath.cameraRelativeToOrthogonalXY(
								(Double.parseDouble(pos[0]) * scale), (Double.parseDouble(pos[1]) * scale), (Double.parseDouble(pos[2]) * scale), camera.getYaw());

						double x = camera.getXPos() + placementCoords.x + orthogonalPlacementDist.x;
						double y = camera.getYPos() + placementCoords.y + orthogonalPlacementDist.y;
						double z = camera.getZPos() + placementCoords.z + orthogonalPlacementDist.z;
						surface.addPoint(new Point3D(x, y, z));
					}

					if (surface.getPoints().size() == 0) {
						throw new Exception("No points in surface read from .txt file");
					}

					surfaces.add(surface);
				}


				if (surfaces.size() == 0) {
					JOptionPane.showMessageDialog(null, "No surfaces read from .txt file",
					"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// add surfaces after scanning all of them so that no surfaces get placed if an
				// error occurs in scanning
				for (Surface s : surfaces) {
					space.addSurface(s);
				}
			}
			catch (Exception e) {
				// if something went wrong with the file scanning, create a placeholder object
				// instead
				// FIXME: add some sort of check to prevent a loop if the error model has been tampered with
				promptUserForModel(space, camera, true);
				e.printStackTrace();
			}

			// close scanner
			scnr.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException occurred",
					"Error", JOptionPane.ERROR_MESSAGE);

		}
	}

	// TODO: eventually make this return a "Model" object rather than an ArrayList of surfaces

	public ArrayList<Surface> readModel(File f) {
		// TODO: add content
		return null;
	}
	
}
