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

	public static final String MODELS_PATH = "src/resources/models/";
	public static final String FILE_EXTENSION = ".txt";
	public static final String ERROR_FILE = "error";
	public static final String REGEX = "(?:[^\\d.-]|-(?=\\D)|\\.(?=\\D))+";

	public static final double DEFAULT_SCALE = 1;
	public static final int DEFAULT_TRANSPARENCY = 255;
	public static final int DIMENSIONS = 3;
	public static final double PLACEMENT_DIST_FORWARD = 2;
	public static final double PLACEMENT_DIST_RIGHT = 0;
	public static final double PLACEMENT_DIST_UP = 0;

	// TODO: polish

	public static File promptUserForModel() {
		// stores the chosen object to add
		// double scale = DEFAULT_SCALE; // stores the chosen scaling factor of object (defaults to 1)

		File projectFile = new File(MODELS_PATH); // get directory path where object files should be located

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
			return null;
		}

		// prompt user to choose an object to add
		String selection = (String) JOptionPane.showInputDialog(null, "Choose an object to add:",
				"Add an Object", JOptionPane.PLAIN_MESSAGE, null, options.toArray(), options.get(0));

		// exit method if the user presses X or cancel on the window
		if (selection == null) {
			return null;
		}

		return new File(MODELS_PATH + selection + FILE_EXTENSION);
	}

	// TODO: polish
	// TODO: make use of this method

	public static double promptUserForScale() {
		double scale = DEFAULT_SCALE;

		// prompt the user for the scaling factor of the object, trying again if the
		// input is invalid
		String errorMessage = "";
		while (true) {
			String strScale = JOptionPane.showInputDialog(null,
					errorMessage + "Choose scaling factor of object:\n(will set to default if left blank)",
					"Set Scale", JOptionPane.PLAIN_MESSAGE);

			// exit method if the user presses X or cancel on the window
			if (strScale == null) {
				return scale;
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

		return scale;
	}

	// TODO: eventually make this return a "Model" object rather than an ArrayList of surfaces
	// TODO: allow including other model files in a model file
	// TODO: polish


	public static Model readModel(File f) {
		if (f == null) {
			return null;
		}
		
		Model model = new Model();

		try (FileInputStream fileInputStream = new FileInputStream(f.getPath());
				Scanner scnr = new Scanner(fileInputStream);) {
			// try to create an object based on color and coordinate information from the
			// files
			try {
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
						
						double x = Double.parseDouble(pos[0]);
						double y = Double.parseDouble(pos[1]);
						double z = Double.parseDouble(pos[2]);
						surface.addPoint(new Point3D(x, y, z));
					}

					if (surface.getPoints().size() == 0) {
						throw new Exception("No points in surface read from .txt file");
					}

					model.addSurface(surface);
				}

				if (model.getSurfaces().size() == 0) {
					JOptionPane.showMessageDialog(null, "No surfaces read from .txt file",
					"Error", JOptionPane.ERROR_MESSAGE);
					return null;
				}
			}
			catch (Exception e) {
				// TODO: add some sort of system to prevent infinite loops if the error file has been tampered with
				e.printStackTrace();
				return readModel(new File(MODELS_PATH + ERROR_FILE + FILE_EXTENSION));
			}

			// close scanner
			scnr.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException occurred",
					"Error", JOptionPane.ERROR_MESSAGE);

		}

		return model;
	}
	
}
