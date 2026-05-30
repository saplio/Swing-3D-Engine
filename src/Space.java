import java.util.ArrayList;

/**
 * Class that acts as the actual 3D environment, storing all surfaces in the environment
 */

public class Space {

    private ArrayList<Surface> surfaces; // stores all Surface objects
    private ArrayList<Camera> cameras; // stores all Camera objects

    public Space() {
        surfaces = new ArrayList<Surface>();
        cameras = new ArrayList<Camera>();
        
    }

    public void addSurface(Surface surface) {
        surfaces.add(surface);
    }

    public Camera createCamera() {
        Camera camera = new Camera(this);
        cameras.add(camera);
        return camera;
    }

    public ArrayList<Surface> getSurfaces() {
        return surfaces;
    }
}
