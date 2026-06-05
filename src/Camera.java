import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 * Class that uses Swing to display the 3D environment from a camera view in a Swing container.
 * The camera can be moved around in the environment
 */

public class Camera extends JComponent {

    public static final double DEFAULT_FOV = 400;

    private double x;
    private double y;
    private double z;

    private double yaw;
    private double pitch;
    private double roll;

    private double fov;

    private Space space;

    private ArrayList<ScreenPolygon> shapes;

    public Camera(Space space) {
        this(space, 0, 0, 0);
    }

    public Camera(Space space, double x, double y, double z) {
        this.space = space;

        this.x = x;
        this.y = y;
        this.z = z;

        fov = DEFAULT_FOV;
    }

    public Space getSpace() {
        return space;
    }

    public double getXPos() {
        return x;
    }

    public double getYPos() {
        return y;
    }

    public double getZPos() {
        return z;
    }

    public Point3D getCameraPoint3D() {
        return new Point3D(x, y, z);
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }

    public double getFOV() {
        return fov;
    }

    public void setFOV(double fov) {
        this.fov = fov;

        refresh();
    }

    public void moveTo(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        refresh();
    }

    public void moveOrthogonal(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;

         refresh();
    }

    public void moveCameraRelative(double amtRight, double amtForward, double amtUp) {
        Point3D movement = PerspectiveMath.cameraRelativeToOrthogonalXY(amtRight, amtForward, amtUp, yaw);
        x += movement.x;
		y += movement.y;
		z += movement.z;

         refresh();
        //TODO: this type of movement currently doesn't account for pitch and roll
    }

    //TODO: make sure the fields wrap between 0 and 2 * PI in the future
    public void setRotation(double yaw, double pitch, double roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;

         refresh();
    }

    public void rotate(double yaw, double pitch, double roll) {
        this.yaw += yaw;
        this.pitch += pitch;
        this.roll += roll;

         refresh();
    }

    public void refresh() {
        shapes = new ArrayList<ScreenPolygon>();

        for (Surface surface : space.getSurfaces()) {
            shapes.add(calcSurfacePerspective(surface));
        }

        repaint();
    }

    private ScreenPolygon calcSurfacePerspective(Surface surface) {
        Surface rotatedSurface;
        rotatedSurface = PerspectiveMath.getRotatedSurfaceXY(surface, yaw, x, y);
        rotatedSurface = PerspectiveMath.getRotatedSurfaceYZ(rotatedSurface, pitch, y, z);
        rotatedSurface = PerspectiveMath.getRotatedSurfaceXZ(rotatedSurface, roll, x, z);

        //FIXME: slicing slightly in front of the camera fixes visual bugs. Maybe figure out why and implement a cleaner fix
        Surface slicedSurface = PerspectiveMath.getSlicedSurfaceY(rotatedSurface, y + 0.001);

        ScreenPolygon shape = new ScreenPolygon(slicedSurface.getColor());

        for (Point3D point3D : slicedSurface.getPoints()) {
            Point screenPoint = PerspectiveMath.calcPointPerspective(point3D, getCameraPoint3D(), getSize(), fov);
            shape.addPoint(screenPoint.x, screenPoint.y);
        }

        return shape;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D)(g.create());

        for (ScreenPolygon shape : new ArrayList<ScreenPolygon>(shapes)) {
            g2D.setColor(shape.color);
            g2D.fill(shape);

            g2D.setColor(Color.BLACK);
            g2D.draw(shape);
        }
        
        g2D.dispose();
    }

    /**
     * Extension of Polygon class that stores a color
     */

    protected class ScreenPolygon extends Polygon {
        public Color color;

        public ScreenPolygon(Color c) {
            color = c; 
        }
    }
}
