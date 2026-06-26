/**
 * Camera with speed and acceleration attributes for more organic movement.
 */

public class SmoothCamera extends Camera {

    private Point3D velocity;
    private Point3D acceleration;

    public SmoothCamera(Space space) {
        super(space);
        
        velocity = new Point3D();
        acceleration = new Point3D();
    }

    public Point3D getVelocity() {
        return new Point3D(velocity);
    }

    public Point3D getAcceleration() {
        return new Point3D(acceleration);
    }

    public void setVelocity(Point3D v) {
        velocity = new Point3D(v);
    }

    public void setAcceleration(Point3D a) {
        acceleration = new Point3D(a);
    }

    public void updateVelocity() {
        velocity = velocity.sum(acceleration);
    }
}
