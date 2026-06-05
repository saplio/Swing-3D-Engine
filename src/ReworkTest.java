import java.awt.Color;
import java.awt.Point;

import javax.swing.JFrame;

public class ReworkTest {
    public static void main(String[] args) {
        // create Space
        Space space = new Space();

        // add surfaces to space
        Surface surface;
        surface = new Surface(Color.RED);
        surface.addPoint(new Point3D(0, 6, 0));
        surface.addPoint(new Point3D(1, 6, 0));
        surface.addPoint(new Point3D(0.5, 6, 1));
        space.addSurface(surface);

        surface = new Surface(new Color(0, 100, 0 ,150));
        surface.addPoint(new Point3D(0.2, 4, 0));
        surface.addPoint(new Point3D(1.2, 4, 0));
        surface.addPoint(new Point3D(0.7, 4, 1));
        surface.addPoint(new Point3D(0.2, 4, 1.7));
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

        // create camera
        Camera camera = space.createCamera();

        // add camera to container
        frame.setVisible(true);
        // TODO: make it so camera can be added before making frame visible
        frame.add(camera);
        frame.revalidate();
        camera.refresh();

        // add keyboard control
        frame.addKeyListener(new Keyboard(camera));
    }
}
