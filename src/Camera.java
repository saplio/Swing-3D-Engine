import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.io.File;
import java.util.ArrayList;

/**
 * Class that uses Swing to display the 3D environment from a camera view in a Swing container.
 * The camera can be moved around in the environment
 */

public class Camera extends JComponent {

    public static final double DEFAULT_FOV_FACTOR = 400;

    private double x;
    private double y;
    private double z;

    private double yaw;
    private double pitch;
    private double roll;

    private double fovFactor;

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

        fovFactor = DEFAULT_FOV_FACTOR;

        refresh();
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

    public double getFovFactor() {
        return fovFactor;
    }

    public void setFovFactor(double fovFactor) {
        this.fovFactor = fovFactor;

        refresh();
    }

    /**
     * Move camera to a coordinate position in the space its in.
     * 
     * @param x X position to move to
     * @param y Y position to move to
     * @param z Z position to move to
     */
    public void moveTo(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        refresh();
    }

    /**
     * Move camera relative to its current position.
     * 
     * @param x Amount to move along X axis
     * @param y Amount to move along Y axis
     * @param z Amount to move along Z axis
     */
    public void moveOrthogonal(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;

         refresh();
    }

    /**
     * Move camera relative to its current position and XY plane direction.
     * 
     * @param amtRight Amount to move to the right
     * @param amtForward Amount to move to the left
     * @param amtUp Amount to move up
     */
    public void moveCameraRelativeXY(double amtRight, double amtForward, double amtUp) {
        Point3D movement = PerspectiveMath.cameraRelativeToOrthogonalXY(amtRight, amtForward, amtUp, yaw);
        x += movement.x;
		y += movement.y;
		z += movement.z;

        refresh();
    }

    // TODO: make a method that moves the camera fully camera relative

    // TODO: make sure the fields wrap between 0 and 2 * PI in the future

    /**
     * Set the rotation of the camera.
     * 
     * @param yaw Rotation along XY plane
     * @param pitch Rotation along YZ plane
     * @param roll Rotation along XZ plane
     */
    public void setRotation(double yaw, double pitch, double roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;

        refresh();
    }

    /**
     * Rotate the camera by a specified amount.
     * 
     * @param yaw Rotation along XY plane
     * @param pitch Rotation along YZ plane
     * @param roll Rotation along XZ plane
     */
    public void rotate(double yaw, double pitch, double roll) {
        this.yaw += yaw;
        this.pitch += pitch;
        this.roll += roll;

        refresh();
    }
    
    /**
     * Recalculate the perspective on all the surfaces in the space and repaint the Swing component.
     */
    public void refresh() {
        ArrayList<ScreenPolygon> newShapes = new ArrayList<ScreenPolygon>();

        for (Model m : space.getModels()) {
            for (Surface s : m.getSurfaces()) {
                newShapes.add(calcSurfacePerspective(s));
            }
        }

        // code to allow cameras to see other cameras

        for (Camera c : space.getCameras()) {
            if (!c.equals(this)) {
                Model cameraCube = ModelReader.readModel(new File(ModelReader.MODELS_PATH + "camera"));
                cameraCube.rotateLikeCameraBy(c.getYaw(), c.getPitch(), c.getRoll());
                Point3D cameraPoint = c.getCameraPoint3D();
                cameraCube.moveTo(cameraPoint.x, cameraPoint.y, cameraPoint.z);

                for (Surface s : cameraCube.getSurfaces()) {
                    newShapes.add(calcSurfacePerspective(s));
                }
            }
        }

        shapes = newShapes;

        repaint();
    }

    /**
     * Calculate the perspective projection for a given surface for this {@code Camera}.
     * 
     * @param surface {@code Surface} to calculate perspective for
     * @return A {@code ScreenPolygon} object that represents the {@code Surface} projected onto the 2D screen
     */
    private ScreenPolygon calcSurfacePerspective(Surface surface) {
        Surface rotatedSurface = PerspectiveMath.getViewRotatedSurface(surface, -yaw, -pitch, -roll, getCameraPoint3D());

        // FIXME: slicing slightly in front of the camera fixes visual bugs. Maybe figure out why and implement a cleaner fix
        Surface slicedSurface = PerspectiveMath.getSlicedSurfaceY(rotatedSurface, y + 0.001);

        ScreenPolygon shape = new ScreenPolygon(slicedSurface.getColor());

        for (Point3D point3D : slicedSurface.getPoints()) {
            Point screenPoint = PerspectiveMath.calcPointPerspective(point3D, getCameraPoint3D(), getSize(), fovFactor);
            shape.addPoint(screenPoint.x, screenPoint.y);
        }

        return shape;
    }

    @Override
    protected void paintComponent(Graphics g) {
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Camera)) {
            return false;
        }

        Camera other = (Camera)obj;

        if (other.space.equals(space) && (other.fovFactor == fovFactor) &&
                (other.x == x) && (other.y == y) && (other.z == z) &&
                (other.yaw == yaw) && (other.pitch == pitch) && (other.roll == roll)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Camera[x=" + x + ",y=" + y + ",z=" + z + ",yaw=" + yaw + ",pitch=" + pitch + ",roll=" + roll + ",fov factor=" + fovFactor + "]";
    }

    /**
     * Extension of Polygon class that stores a color.
     */

    protected class ScreenPolygon extends Polygon {
        public Color color;

        public ScreenPolygon(Color c) {
            color = c;
        }
    }
}
