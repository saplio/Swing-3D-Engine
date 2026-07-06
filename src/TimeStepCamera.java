/**
 * Camera with speed and acceleration attributes for more organic movement.
 */

public class TimeStepCamera extends Camera implements TimeStepActor {

    public static final double DEFAULT_DECELERATION = 8;
    public static final double DEFAULT_TOP_SPEED = 15;

    private Point3D velocity;
    private Point3D acceleration;

    private double deceleration;
    private double topSpeed;

    private boolean beingMoved;

    public TimeStepCamera(Space space) {
        super(space);
        
        velocity = new Point3D();
        acceleration = new Point3D();

        beingMoved = false;

        deceleration = DEFAULT_DECELERATION;
        topSpeed = DEFAULT_TOP_SPEED;
    }

    public boolean getBeingMoved() {
        return beingMoved;
    }

    public void setBeingMoved(boolean b) {
        beingMoved = b;
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

    public double getDeceleration() {
        return deceleration;
    }

    public void setDeceleration(double decel) {
        deceleration = decel;
    }

    public double getTopSpeed() {
        return topSpeed;
    }

    public void setTopSpeed(double speed) {
        topSpeed = speed;
    }

    // TODO: make it so acceleration and deceleration is independent on each axis
    @Override
    public void timeStepUpdate(double timeUnit) {
        if (beingMoved == false && !velocity.equals(new Point3D())) {
            acceleration = velocity.negative().scale(deceleration / velocity.getHypot());
            if (acceleration.scale(timeUnit).getHypot() >= velocity.getHypot()) {
                acceleration = new Point3D();
                velocity = new Point3D();
            }
        }

        velocity = velocity.sum(acceleration.scale(timeUnit));

        // cap velocity at top speed
        if (velocity.getHypot() >= topSpeed) {
            velocity = velocity.scale(topSpeed / velocity.getHypot());
        }

        moveOrthogonal(velocity.scale(timeUnit));
    }

    @Override
    public String toString() {
        return "SmoothMovementCamera[x=" + getXPos() + ",y=" + getYPos() + ",z=" + getZPos() + 
                ",yaw=" + getYaw() + ",pitch=" + getPitch() + ",roll=" + getRoll() + 
                ",fov factor=" + getFovFactor() + 
                ",velocity=" + velocity + ",acceleration=" + acceleration + "]";
    }
}
