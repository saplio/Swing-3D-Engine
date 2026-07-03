/**
 * Camera with speed and acceleration attributes for more organic movement.
 */

public class SmoothMovementCamera extends Camera {

    private Point3D velocity;
    private Point3D acceleration;

    public SmoothMovementCamera(Space space) {
        super(space);
        
        velocity = new Point3D();
        acceleration = new Point3D();
    }

    public Point3D getVelocity() {
        return new Point3D(velocity);
    }

    public void setVelocity(Point3D v) {
        velocity = new Point3D(v);
    }

    public Point3D getAcceleration() {
        return new Point3D(acceleration);
    }

    public void setAcceleration(Point3D a) {
        acceleration = new Point3D(a);
    }

    public void updateVelocity(double timeUnit) {
        velocity = velocity.sum(acceleration.scale(timeUnit));
    }

    public void updatePosition(double timeUnit) {
        Point3D movement = velocity.scale(timeUnit);
        moveOrthogonal(movement.x, movement.y, movement.z);
    }

    @Override
    public String toString() {
        return "SmoothMovementCamera[x=" + getXPos() + ",y=" + getYPos() + ",z=" + getZPos() + 
                ",yaw=" + getYaw() + ",pitch=" + getPitch() + ",roll=" + getRoll() + 
                ",fov factor=" + getFovFactor() + 
                ",velocity=" + velocity + ",acceleration=" + acceleration + "]";
    }
}
