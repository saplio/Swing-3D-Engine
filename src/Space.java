import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that acts as the actual 3D environment, storing all surfaces in the environment
 */

// TODO: make this class serializable

public class Space {

    private ArrayList<Surface> surfaces; // stores all Surface objects
    private ArrayList<Camera> cameras; // stores all Camera objects

    public Space() {
        surfaces = new ArrayList<Surface>();
        cameras = new ArrayList<Camera>();
    }

    public boolean addSurface(Surface surface) {
        if (surfaces.contains(surface)) {
            return false;
        }

        surfaces.add(surface);

        for (Camera c : cameras) {
            c.refresh();
        }

        return true;
    }

    public boolean addModel(List<Surface> modelSurfaces) {
        if (surfaces.containsAll(modelSurfaces)) {
            return false;
        }
        
        for (Surface s : modelSurfaces) {
            surfaces.add(s);
        }
        

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

    public List<Surface> getSurfaces() {
        return Collections.unmodifiableList(surfaces);
    }

    // TODO: add toString and equals methods
}
