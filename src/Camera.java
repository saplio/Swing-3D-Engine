import javax.swing.JPanel;

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

public class Camera {

    // TODO: maybe rewrite this and the model class to extend an abstract class called "Moveable" to organize code better

    public static final double DEFAULT_FOV_FACTOR = 400;
    
    private static final Model CAMERA_MODEL = ModelReader.readModel("camera");

    private Point3D location;

    private double yaw;
    private double pitch;
    private double roll;

    private double fovFactor;

    private Space space;

    private ArrayList<ScreenPolygon> shapes;

    private JPanel viewPanel;

    public Camera(Space space) {
        this(space, new Point3D());
    }

    public Camera(Space space, Point3D location) {
        this.space = space;

        this.location = new Point3D(location);

        fovFactor = DEFAULT_FOV_FACTOR;

        viewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2D = (Graphics2D)(g.create());

                // Here is where shape information is used to draw on the screen
                for (ScreenPolygon shape : new ArrayList<ScreenPolygon>(shapes)) {
                    g2D.setColor(shape.color);
                    g2D.fill(shape);

                    g2D.setColor(Color.BLACK);
                    g2D.draw(shape);
                }
        
                g2D.dispose();
            }
        };

        refresh();
    }

    public Space getSpace() {
        return space;
    }

    public JPanel getViewPanel() {
        return viewPanel;
    }

    public double getXPos() {
        return location.x;
    }

    public double getYPos() {
        return location.y;
    }

    public double getZPos() {
        return location.z;
    }

    public Point3D getCameraLocation() {
        return new Point3D(location);
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
        location = new Point3D(x, y, z);

        refresh();
    }

    /**
     * Move camera to a coordinate position in the space its in.
     * 
     * @param newLocation location to move to
     */
    public void moveTo(Point3D newLocation) {
        location = new Point3D(newLocation);

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
        location = location.sum(new Point3D(x, y, z));

         refresh();
    }

    /**
     * Move camera relative to its current position.
     * 
     * @param movement Amount to move
     */
    public void moveOrthogonal(Point3D movement) {
        location = location.sum(movement);

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
        Point3D movement = PerspectiveMath.cameraRelativeToOrthogonalXY(new Point3D(amtRight, amtForward, amtUp), yaw);
        location = location.sum(movement);

        refresh();
    }

    /**
     * Move camera relative to its current position and XY plane direction.
     * 
     * @param relativeMovement Amount to move
     */
    public void moveCameraRelativeXY(Point3D relativeMovement) {
        Point3D movement = PerspectiveMath.cameraRelativeToOrthogonalXY(relativeMovement, yaw);
        location = location.sum(movement);

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
                Model cameraCube = new Model(CAMERA_MODEL);
                cameraCube.rotateLikeCameraBy(c.getYaw(), c.getPitch(), c.getRoll());
                Point3D cameraPoint = c.getCameraLocation();
                cameraCube.moveTo(cameraPoint.x, cameraPoint.y, cameraPoint.z);

                for (Surface s : cameraCube.getSurfaces()) {
                    newShapes.add(calcSurfacePerspective(s));
                }
            }
        }

        shapes = newShapes;

        viewPanel.repaint();
    }

    /**
     * Calculate the perspective projection for a given surface for this {@code Camera}.
     * 
     * @param surface {@code Surface} to calculate perspective for
     * @return A {@code ScreenPolygon} object that represents the {@code Surface} projected onto the 2D screen
     */
    private ScreenPolygon calcSurfacePerspective(Surface surface) {
        Surface rotatedSurface = PerspectiveMath.getViewRotatedSurface(surface, -yaw, -pitch, -roll, location);

        // FIXME: slicing slightly in front of the camera fixes visual bugs. Maybe figure out why and implement a cleaner fix
        Surface slicedSurface = PerspectiveMath.getSlicedSurfaceY(rotatedSurface, location.y + 0.001);

        ScreenPolygon shape = new ScreenPolygon(slicedSurface.getColor());

        for (Point3D point3D : slicedSurface.getPoints()) {
            Point screenPoint = PerspectiveMath.calcPointPerspective(point3D, location, viewPanel.getSize(), fovFactor);
            shape.addPoint(screenPoint.x, screenPoint.y);
        }

        return shape;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Camera)) {
            return false;
        }

        Camera other = (Camera)obj;

        if (other.space.equals(space) && (other.fovFactor == fovFactor) && (other.location == location) &&
                (other.yaw == yaw) && (other.pitch == pitch) && (other.roll == roll)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Camera[x=" + location.x + ",y=" + location.y + ",z=" + location.z + ",yaw=" + yaw + ",pitch=" + pitch + ",roll=" + roll + ",fov factor=" + fovFactor + "]";
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
