import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Model {

    private ArrayList<Surface> surfaces;

	public Model() {
        surfaces = new ArrayList<Surface>();
	}

    public Model(Surface surface) {
        surfaces = new ArrayList<Surface>();
        surfaces.add(surface);
    }

	public Model(List<Surface> surfaces) {
		this.surfaces = new ArrayList<Surface>(surfaces);
	}
    	
	public List<Surface> getSurfaces() {
		return Collections.unmodifiableList(surfaces);
	}

	public boolean addSurface(Surface surface) {
		if (!(surfaces.contains(surface))) {
			surfaces.add(surface);
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Model)) {
            return false;
        }

        Model other = (Model)obj;

        if (other.surfaces.equals(surfaces)) {
			return true;
        }

        return false;
	}

	@Override
	public String toString() {
		return "Model[surfaces.size=" + surfaces.size() + "]";
	}
}
