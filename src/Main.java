/**
 * Program with the main method that creates a frame, a space manager, and a keyboard listener
 */

import java.awt.Color;
import java.awt.Point;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		//create space
		Space space = new Space();
		
		//create a field of octagons
		double[] ShapeX = {0, 0.5, 1, 1, 0.5, 0, -0.5, -0.5};
		double[] ShapeY = {0, 0, 0.5, 1, 1.5, 1.5, 1, 0.5};
		double[] ShapeZ = {0, 0, 0.1, 0.2, 0.3, 0.3, 0.2, 0.1};
		
		double[] ShapeZ2 = {0, 0, 0, 0, 0, 0, 0, 0};

		for (int l = 0; l < 17; l += 4) {
			for (int j = -4; j < 5; ++j) {	
				if (Math.abs(j) % 2 == 1) {
					for (int i = 2; i < 9; i += 2) {
						Surface s = new Surface(Color.RED); 
						for (int k = 0; k < ShapeX.length; ++k) {

							s.addPoint(new Point3D(j * 2 + ShapeX[k] - 0.25,
									i + ShapeY[k],
									l + ShapeZ[k]));
						}
						space.addSurface(s);
					}
				} else {
					for (int i = 2; i < 9; i += 2) {
						Surface s = new Surface(Color.RED);
						for (int k = 0; k < ShapeX.length; ++k) {

							s.addPoint(new Point3D(j * 2 + ShapeX[k] - 0.25,
									i + ShapeY[k],
									l + ShapeZ2[k]));
						}
						space.addSurface(s);
					}
				}
			}
		}
		
		//create two cameras of the same space
		createDisplay(space, new Point(100, 200));
        createDisplay(space, new Point( 700, 200));
	}

	public static void createDisplay(Space space, Point pos) {
        //create container
        JFrame frame = new JFrame();
        frame.setTitle("3D space");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(pos);

        //create camera
        Camera camera = space.createCamera();

        //add camera to container
        frame.setVisible(true);
        //TODO: make it so camera can be added before making frame visible
        frame.add(camera);
        frame.revalidate();
        camera.moveTo(0, 0, 2);

        //add keyboard control
        frame.addKeyListener(new Keyboard(camera));
    }
}
