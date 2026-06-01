/*
 * program with the main method that creates a frame, a space manager, and a keyboard listener
 */

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class Main {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		//set up a frame in which to draw the program
		JFrame frame = new JFrame();
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
		
		frame.setTitle("3D space");
		frame.setSize(1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(p);
		frame.setVisible(true);
		
		//make a space manager
		SpaceManager spaceManager = new SpaceManager(p);
		
		//create a field of octagons
		double[] ShapeX = {0, 0.5, 1, 1, 0.5, 0, -0.5, -0.5};
		double[] ShapeY = {0, 0, 0.5, 1, 1.5, 1.5, 1, 0.5};
		double[] ShapeZ = {0, 0, 0.1, 0.2, 0.3, 0.3, 0.2, 0.1};
		
		double[] ShapeZ2 = {0, 0, 0, 0, 0, 0, 0, 0};
		
		double[][] coords = new double[3][ShapeX.length];
	
		for (int l = 0; l < 17; l += 4) {
			for (int j = -4; j < 5; ++j) {
				
				if (Math.abs(j) % 2 == 1) {
					for (int i = 2; i < 9; i += 2) {
						for (int k = 0; k < ShapeX.length; ++k) {

							coords[0][k] = j * 2 + ShapeX[k] - 0.25;
							coords[1][k] = i + ShapeY[k];
							coords[2][k] = l + ShapeZ[k];
						}
						spaceManager.addSurface(coords[0], coords[1], coords[2], Color.RED);
					}
				} else {
					for (int i = 2; i < 9; i += 2) {
						for (int k = 0; k < ShapeX.length; ++k) {
							coords[0][k] = j * 2 + ShapeX[k] - 0.25;
							coords[1][k] = i + ShapeY[k];
							coords[2][k] = l + ShapeZ2[k];
						}
						spaceManager.addSurface(coords[0], coords[1], coords[2], Color.RED);
					}
				}
			}
		}
		
		// add a keyboard listener
		Keyboard kb = new Keyboard(spaceManager);
		frame.addKeyListener(kb);
	}
}
