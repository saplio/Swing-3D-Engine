import java.awt.Point;

import javax.swing.JFrame;

/**
 * Class with the main method that creates a frame, a space, cameras, and a keyboard listener
 */

public class Main {

	public static void main(String[] args) {
		// create space
		Space space = new Space();
        space.startTimer();
		
		// create a field of octagons
		Model octagons = ModelReader.readModel("octagon grid");
		octagons.moveBy(-8.75, 2, 0);
		space.addModel(octagons);
		
		// create two cameras in the same space
		createDisplay(space, new Point(100, 200));
        // createDisplay(space, new Point(700, 200));
	}

	public static void createDisplay(Space space, Point pos) {
        // create container
        JFrame frame = new JFrame();
        frame.setTitle("3D space");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(pos);

        // add camera to container
        TimeStepCamera camera = space.createTimeStepCamera();
        frame.add(camera.getViewPanel());
        frame.setVisible(true);
        camera.moveTo(0, 0, 2);

        // add keyboard control
        frame.addKeyListener(new SmoothCameraController(camera));
    }
}
