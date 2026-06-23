import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	public static final String MAIN_FILE_EXTENSION = ".main";
	public static final String SECONDARY_FILE_EXTENSION = ".txt";

	public static final File ERROR_FILE = new File(MODELS_PATH + "error");

	public static final String REGEX = "(?:[^\\d.-]|-(?=\\.\\D)|\\.(?=\\D))+";
	public static final String FILE_INCLUDE_CHAR = ";";

	public static final double DEFAULT_SCALE = 1;
	public static final int DEFAULT_TRANSPARENCY = 255;
	public static final int DIMENSIONS = 3;

	/**
	 * Prompt the user to choose a model directory from the models folder.
	 * 
	 * @return {@code File} object of the chosen model folder, or {@code null} if the prompt was canceled
	 */
	public static File promptUserForModel() {
		File modelFiles = new File(MODELS_PATH); // get directory where model files should be located

		if (!modelFiles.isDirectory()) {
			JOptionPane.showMessageDialog(null, "Could not find directory " + MODELS_PATH, "Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}

		// get list of files in the project folder and add only directories that contain a valid main file
		// to the options
		ArrayList<String> options = new ArrayList<String>();
		for (File modelFolder : modelFiles.listFiles()) {
			if (modelFolder.isDirectory()) {
				for (File f : modelFolder.listFiles()) {
					if (f.getName().endsWith(MAIN_FILE_EXTENSION)) {
						options.add(modelFolder.getName());
						break;
					}
				}
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

		return new File(MODELS_PATH + selection);
	}
	
	/**
	 * Prompt the user to enter a float to scale models.
	 * 
	 * @return The user's entered float, or the default scale if the prompt was canceled or left blank
	 */
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

	/**
	 * Attempt to read a model from the given file.
	 * 
	 * @param f File or directory to read from
	 * @return The {@code Model} read, the error model if a model could not properly be read from the file, or {@code null}
	 */
	public static Model readModel(File f) {
		ArrayList<File> fileList = new ArrayList<File>();
		fileList.add(f);
		return readModel(fileList);
	}

	// This private method exists to make sure an object file can't include itself indirectly

	private static Model readModel(ArrayList<File> fileChain) {
		File f = fileChain.getLast();

		// return null if null was passed to this method via the promptUserForScale method
		if (f == null) {
			return null;
		}

		// find the main file in the directory if the File is a directory
		if (f.isDirectory()) {
			boolean containsMain = false;
			for (File innerFile : f.listFiles()) {
				containsMain = true;
				if (innerFile.getName().endsWith(MAIN_FILE_EXTENSION)) {
					f = innerFile;
					break;
				}
				containsMain = false;
			}
			
			if (!containsMain) {
				new Exception("Could not find " + MAIN_FILE_EXTENSION + " file").printStackTrace();

				if (fileChain.getLast().equals(ERROR_FILE)) {
					return null;
				}

				return readModel(ERROR_FILE);
			}
		}

		Model model = new Model();

		try (FileInputStream fileInputStream = new FileInputStream(f.getPath());
				Scanner scnr = new Scanner(fileInputStream);) {
			// try to create an object based on color and coordinate information from the
			// files
			try {
				// read through the file
				while (scnr.hasNextLine()) {
					String firstLine;

					do {
						firstLine = scnr.nextLine();
					} while (!firstLine.contains(FILE_INCLUDE_CHAR) && firstLine.replaceAll(REGEX, "").isBlank() && scnr.hasNextLine());

					if (firstLine.contains(FILE_INCLUDE_CHAR)) {
						String directoryPath = f.getParent() + "/" + firstLine.substring(firstLine.indexOf(FILE_INCLUDE_CHAR) + 1, firstLine.lastIndexOf(FILE_INCLUDE_CHAR));
						String filePath = directoryPath + SECONDARY_FILE_EXTENSION;

						File directoryToInclude = new File(directoryPath);
						File fileToInclude = new File(filePath);

						if (!directoryToInclude.exists()) {
							if (!fileToInclude.exists()) {
								throw new FileNotFoundException("Could not find file " + fileToInclude);
							}
						}
						else {
							fileToInclude = directoryToInclude;
						}

						if (fileChain.contains(fileToInclude)) {
							throw new Exception("Model file cannot include itself or other files that include it");
						}

						fileChain.add(fileToInclude);
						Model modelToInclude = readModel(fileChain);
						fileChain.removeLast();

						String scaleString = firstLine.substring(firstLine.lastIndexOf(FILE_INCLUDE_CHAR)).replaceAll(REGEX, " ");
						
						if (!scaleString.isBlank()) {
							double scale = Double.parseDouble(scaleString);
							modelToInclude.scale(scale);
						}
						
						String[] pos = scnr.nextLine().replaceAll(REGEX, " ").trim().split(REGEX, DIMENSIONS);
						
						double x = Double.parseDouble(pos[0]);
						double y = Double.parseDouble(pos[1]);
						double z = Double.parseDouble(pos[2]);

						modelToInclude.moveTo(x, y, z);
						model.addModel(modelToInclude);
						continue;
					}

					// this accounts for if the very last line is blank
					if (firstLine.replaceAll(REGEX, "").isBlank()) {
						break;
					}

					String[] colorInfo = firstLine.replaceAll(REGEX, " ").trim().split(REGEX, 4);

					int r = Integer.parseInt(colorInfo[0]);
					int g = Integer.parseInt(colorInfo[1]);
					int b = Integer.parseInt(colorInfo[2]);
					int a = DEFAULT_TRANSPARENCY;

					// attempt to find an alpha value, use default transparency if one was not written
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
				e.printStackTrace();

				if (fileChain.getLast().equals(ERROR_FILE)) {
					return null;
				}
				
				return readModel(ERROR_FILE);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "IOException occurred",
					"Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return model;
	}
}
