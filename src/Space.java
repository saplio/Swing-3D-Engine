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

    public static final double REFRESH_RATE = 1.0 / 29800;

    private ArrayList<Model> models; // stores all Model objects
    private ArrayList<Camera> cameras; // stores all Camera objects

    public Space() {
        models = new ArrayList<Model>();
        cameras = new ArrayList<Camera>();

        Timer timer = new Timer((int)(REFRESH_RATE * 1000), this);
        timer.start();
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
     * @return {@code Camera} initialized at the origin facing the positive Y axis.
     */
    public Camera createCamera() {
        Camera camera = new Camera(this);
        cameras.add(camera);
        return camera;
    }

    public SmoothCamera createSmoothCamera() {
        SmoothCamera camera = new SmoothCamera(this);
        cameras.add(camera);
        return camera;
    }

    public List<Model> getModels() {
        return Collections.unmodifiableList(models);
    }

    public List<Camera> getCameras() {
        return Collections.unmodifiableList(cameras);
    }

    public void updateCameraPositions() {
        for (Camera c : cameras) {
            if (c instanceof SmoothCamera smoothCamera) {
                smoothCamera.updateVelocity();
                smoothCamera.moveOrthogonal(smoothCamera.getVelocity().x, smoothCamera.getVelocity().y, smoothCamera.getVelocity().z);
            }
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
