import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class that acts as the actual 3D environment, storing all models in the environment
 */

// TODO: make this class serializable

public class Space implements ActionListener {

    public static final double REFRESH_RATE = 1.0 / 60.0;
    public static final double DECELERATION = 4;

    private ArrayList<Model> models; // stores all Model objects
    private ArrayList<Camera> cameras; // stores all Camera objects
    private ArrayList<SmoothMovementCamera> smoothMovementCameras; // stores specifically references to SmoothMovementCamera objects

    private Timer timer;

    public Space() {
        models = new ArrayList<Model>();
        cameras = new ArrayList<Camera>();
        smoothMovementCameras = new ArrayList<SmoothMovementCamera>();

        timer = new Timer((int)(REFRESH_RATE * 1000), this);
    }

    public void startTimer() {
        timer.start();
    }

    public void stopTimer() {
        timer.stop();
    }

    public List<Model> getModels() {
        return Collections.unmodifiableList(models);
    }

    public List<Camera> getCameras() {
        return Collections.unmodifiableList(cameras);
    }

    public List<SmoothMovementCamera> getSmoothMovementCameras() {
        return Collections.unmodifiableList(smoothMovementCameras);
    }

    public boolean addModel(Model model) {
        if (models.contains(model)) {
            return false;
        }

        models.add(model);

        return true;
    }

    public boolean addSurface(Surface surface) {
        if (surface == null) {
            return false;
        }
        
        Model m = new Model(surface);

        if (models.contains(m)) {
            return false;
        }

        models.add(m);

        return true;
    }

    /**
     * Create a new {@code Camera} object in this {@code Space}. 
     * 
     * @return {@code Camera} initialized at the origin facing the positive Y axis
     */
    public Camera createCamera() {
        Camera camera = new Camera(this);
        cameras.add(camera);
        return camera;
    }

    /**
     * Create a new {@code SmoothMovementCamera} object in this {@code Space}. 
     * 
     * @return {@code SmoothMovementCamera} initialized at the origin facing the positive Y axis
     */
    public SmoothMovementCamera createSmoothMovementCamera() {
        SmoothMovementCamera camera = new SmoothMovementCamera(this);
        cameras.add(camera);
        smoothMovementCameras.add(camera);
        return camera;
    }

    private void updateCameraPositions() {
        // TODO: could make an interface SmoothMovement that could be implemented by models as well and have them all updated by this method
        // TODO: above should be done so that all movement is described in the interface's implementation rather than deceleration movement described in the space
        // FIXME: this system causes some jitter at the moment
        for (SmoothMovementCamera c : smoothMovementCameras) {
                boolean isDecelerating = false;
                // FIXME: top speed is not fully consistent
                // FIXME: remove print statements
                System.out.println(c.getAcceleration());
                if (c.getAcceleration().equals(new Point3D()) && !c.getVelocity().equals(new Point3D())) {
                    // set acceleration to have the magnitude of the deceleration constant in the opposite direction of the current speed
                    c.setAcceleration(c.getVelocity().negative().scale(DECELERATION / c.getVelocity().getHypot()));  
                    // System.out.println(c.getAcceleration().getHypot());
                    isDecelerating = true;
                }
                // FIXME: using just the x axis for these likely causes some weird movement bugs in certain cases like getting stopped when strafing while facing either direction of the y axis 
                if (Math.signum(c.getVelocity().x) != Math.signum(c.getAcceleration().x) && !c.getVelocity().equals(new Point3D())) {
                    // System.out.println("decelerating");
                    isDecelerating = true;
                }

                c.updateVelocity(REFRESH_RATE);

                if (isDecelerating && !c.getVelocity().equals(new Point3D()) && Math.signum(c.getVelocity().x) == Math.signum(c.getAcceleration().x)) {
                    // System.out.println("stopping");
                    c.setVelocity(new Point3D());
                    c.setAcceleration(new Point3D());
                }

                c.updatePosition(REFRESH_RATE);
        }
    }

    public void refreshCameras() {
        for (Camera c : cameras) {
            if (c.isShowing()) {
                c.refresh();
            }
        }
    }

    // FIXME: temporary methods for testing
    public void moveLastModel() {
        models.getLast().moveBy(0, 1, 0);
    }

    public void scaleLastModel() {
        models.getLast().scale(1.1);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateCameraPositions();
        refreshCameras();
    }

    @Override
    public boolean equals(Object obj) {
		if (!(obj instanceof Space)) {
            return false;
        }

        Space other = (Space)obj;

        if (other.models.equals(models) && other.cameras.equals(cameras)) {
			return true;
        }

        return false;
	}

    @Override
    public String toString() {
        return "Space[models.size=" + models.size() + ",cameras.size=" + cameras.size() + "]";
    } 
}
