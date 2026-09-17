import java.awt.Point;

import javax.swing.JFrame;

/**
 * Class with the main method that creates a space, cameras, and a controller for the cameras
 */

public class Main {

	public static void main(String[] args) {
		// create space
		Space space = new Space();
		
		// create a field of octagons
		Model octagons = ModelReader.readModel("octagon grid");
		octagons.moveBy(-8.75, 2, 0);
		space.addModel(octagons);

        //create a moving pyramid
        MovingModel movingPyramid = new MovingModel();
        movingPyramid.addModel(ModelReader.readModel("pyramid"));
        movingPyramid.scale(3);
        movingPyramid.addStop(new Point3D(12, 5, 0));
        movingPyramid.addStop(new Point3D(12, 15, 0));
        movingPyramid.addStop(new Point3D(12, 10, 10));
        space.addModel(movingPyramid);

		// create two cameras in the same space
		createDisplay(space, new Point(100, 200));
        createDisplay(space, new Point(700, 200));

        // start space tick timer
        space.startTimer();
	}

	public static void createDisplay(Space space, Point pos) {
        // create container
        JFrame frame = new JFrame();
        frame.setTitle("3D space");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(pos);

        // add camera to container
        TimeStepCamera camera = space.createTimeStepCamera(new Point3D(0, 0, 2));
        frame.add(camera.getViewPanel());
        frame.setVisible(true);

        // add keyboard and mouse control
        TimeStepCameraController controller = new TimeStepCameraController(camera);
    }
}
