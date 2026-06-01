import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JFrame;

public class ReworkTest {
    public static void main(String[] args) {
        //create Space
        Space space = new Space();

        //add surfaces to space
        ArrayList<Point3D> surface = new ArrayList<Point3D>();
        surface.add(new Point3D(0, 6, 0));
        surface.add(new Point3D(1, 6, 0));
        surface.add(new Point3D(0.5, 6, 1));
        space.addSurface(new Surface(surface, Color.RED));

        surface = new ArrayList<Point3D>();
        surface.add(new Point3D(0.2, 4, 0));
        surface.add(new Point3D(1.2, 4, 0));
        surface.add(new Point3D(0.7, 4, 1));
        surface.add(new Point3D(0.2, 4, 1.7));
        space.addSurface(new Surface(surface, new Color(0, 100, 0, 150)));

        //make two different cameras in the same space
        createDisplay(space, new Point(100, 200));
        createDisplay(space, new Point( 500, 200));
    }

    public static void createDisplay(Space space, Point pos) {
        //create container
        JFrame frame = new JFrame();
        frame.setTitle("3D space");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(pos);

        //create camera
        Camera camera = space.createCamera();

        //add camera to container
        frame.setVisible(true);
        //TODO: make it so camera can be added before making frame visible
        frame.add(camera);
        frame.revalidate();
        camera.recalculatePerspective();

        //add keyboard control
        frame.addKeyListener(new Keyboard(camera));
    }
}
