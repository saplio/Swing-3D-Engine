/**
 * Class that uses Swing to display the 3D environment from a camera view in a Swing container.
 * The camera can be moved around in the environment
 */

import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

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
    }

    public void moveTo(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        recalculatePerspective();
    }

    public void moveOrthogonal(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;

         recalculatePerspective();
    }

    public void moveCameraRelative(double amtRight, double amtForward, double amtUp) {
        x += amtForward * Math.sin(yaw) + amtRight * Math.sin(yaw + Math.PI / 2);
		y += amtForward * Math.cos(yaw) + amtRight * Math.cos(yaw +  Math.PI / 2);
		z += amtUp;

         recalculatePerspective();
        //TODO: this type of movement currently doesn't account for pitch and roll
    }

    //TODO: make sure the fields wrap between 0 and 2 * PI in the future
    public void setCameraAngle(double yaw, double pitch, double roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;

         recalculatePerspective();
    }

    public void turnCameraAngle(double yaw, double pitch, double roll) {
        this.yaw += yaw;
        this.pitch += pitch;
        this.roll += roll;

         recalculatePerspective();
    }

    public void recalculatePerspective() {
        shapes = new ArrayList<ScreenPolygon>();

        for (Surface surface : space.getSurfaces()) {
            shapes.add(calcSurfacePerspective(surface));
        }

        repaint();
    }

    private ScreenPolygon calcSurfacePerspective(Surface surface) {
        ScreenPolygon shape = new ScreenPolygon(surface.getColor());

        for (Point3D point3D : surface.getPoints()) {
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

    protected class ScreenPolygon extends Polygon {
        public Color color;

        public ScreenPolygon(Color c) {
            color = c; 
        }
    }
}
