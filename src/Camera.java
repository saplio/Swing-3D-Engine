/**
 * Class that uses Swing to display the 3D environment from a camera view in a Swing container.
 * The camera can be moved around in the environment
 */

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;

public class Camera extends JComponent {

    private double x;
    private double y;
    private double z;

    private double yaw;
    private double pitch;
    private double roll;

    private Space space;

    private ArrayList<Surface> surfaces;
    private ArrayList<Polygon> shapes;

    public Camera(Space space) {
        this(space, 0, 0, 0);
    }

    public Camera(Space space, double x, double y, double z) {
        this.space = space;
        surfaces = space.getSurfaces();

        this.x = x;
        this.y = y;
        this.z = z;
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
    }

    public void moveCameraRelative(double amtRight, double amtForward, double amtUp) {
        x += amtForward * Math.sin(yaw) + amtRight * Math.sin(yaw + Math.PI / 2);
		y += amtForward * Math.cos(yaw) + amtRight * Math.cos(yaw +  Math.PI / 2);
		z += amtUp;

        //TODO: this type of movement currently doesn't account for pitch and roll
    }

    //TODO: make sure the fields wrap between 0 and 2 * PI in the future
    public void setCameraAngle(double yaw, double pitch, double roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    public void turnCameraAngle(double yaw, double pitch, double roll) {
        this.yaw += yaw;
        this.pitch += pitch;
        this.roll += roll;
    }

    public void recalculatePerspective() {
        //TODO: add content
    }

    @Override
    public void paintComponent(Graphics g) {
        //TODO: add content
    }
}
