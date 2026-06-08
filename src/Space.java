import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that acts as the actual 3D environment, storing all models in the environment
 */

// TODO: make this class serializable

public class Space {

    private ArrayList<Model> models; // stores all Model objects
    private ArrayList<Camera> cameras; // stores all Camera objects

    public Space() {
        models = new ArrayList<Model>();
        cameras = new ArrayList<Camera>();
    }

    public boolean addModel(Model model) {
        if (model == null || models.contains(model)) {
            return false;
        }

        models.add(model);
        
        for (Camera c : cameras) {
            c.refresh();
        }

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

        for (Camera c : cameras) {
            c.refresh();
        }

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

    public List<Model> getModels() {
        return Collections.unmodifiableList(models);
    }

    public List<Camera> getCameras() {
        return Collections.unmodifiableList(cameras);
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
