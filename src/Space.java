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

    private ArrayList<Model> models; // stores all Model objects
    private ArrayList<Camera> cameras; // stores all Camera objects
    private ArrayList<TimeStepCamera> timeStepCameras; // stores specifically references to TimeStepCamera objects

    private Timer timer;

    public Space() {
        models = new ArrayList<Model>();
        cameras = new ArrayList<Camera>();
        timeStepCameras = new ArrayList<TimeStepCamera>();

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

    public List<TimeStepCamera> getTimeStepCameras() {
        return Collections.unmodifiableList(timeStepCameras);
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
     * Create a new {@code TimeStepCamera} object in this {@code Space}. 
     * 
     * @return {@code TimeStepCamera} initialized at the origin facing the positive Y axis
     */
    public TimeStepCamera createTimeStepCamera() {
        TimeStepCamera camera = new TimeStepCamera(this);
        cameras.add(camera);
        timeStepCameras.add(camera);
        return camera;
    }

    private void updateTimeStepPositions() {
        for (TimeStepCamera c : timeStepCameras) {
                c.timeStepUpdate(REFRESH_RATE);
        }
    }

    public void refreshCameras() {
        for (Camera c : cameras) {
            if (c.getViewPanel().isShowing()) {
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
        updateTimeStepPositions();
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
