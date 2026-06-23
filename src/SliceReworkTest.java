import java.awt.Color;
import java.awt.Point;

import javax.swing.JFrame;

public class SliceReworkTest {
    public static void main(String[] args) {
        // create space
        Space space = new Space();

        Surface surface;
        surface = new Surface(Color.RED);
        surface.addPoint(new Point3D(-1, 5, 0));
        surface.addPoint(new Point3D(-1, 5, 3));
        surface.addPoint(new Point3D(-1, 15, 3));
        surface.addPoint(new Point3D(-1, 15, 0));
        space.addSurface(surface);

        // make two different cameras in the same space
        createDisplay(space, new Point(100, 200));
        createDisplay(space, new Point( 700, 200));
    }

    public static void createDisplay(Space space, Point pos) {
        // create container
        JFrame frame = new JFrame();
        frame.setTitle("3D space");
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(pos);

        // add camera to container
        Camera camera = space.createCamera();
        frame.add(camera);
        frame.setVisible(true);
        camera.refresh();

        // add keyboard control
        frame.addKeyListener(new CameraController(camera));
    }
}
