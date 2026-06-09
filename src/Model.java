import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that stores a collection of surfaces that behave as a unit
 */

public class Model {

    private ArrayList<Surface> surfaces;
	private Point3D location;

	public Model() {
        surfaces = new ArrayList<Surface>();
		location = new Point3D();
	}

    public Model(Surface surface) {
        surfaces = new ArrayList<Surface>();
        surfaces.add(surface);
		location = new Point3D();
    }

	public Model(List<Surface> surfaces) {
		this.surfaces = new ArrayList<Surface>(surfaces);
		location = new Point3D();
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

	public boolean addModel(Model otherModel) {
		boolean anyAdded = false;

		for (Surface s : otherModel.surfaces) {
			if (!(surfaces.contains(s))) {
				surfaces.add(s);
				anyAdded = true;
			}
		}

		return anyAdded;
	}

	public Point3D getLocation() {
		return new Point3D(location);
	}

	public void moveTo(double x, double y, double z) {
		Point3D movement = new Point3D(x, y, z).difference(getLocation());
		for (Surface s : surfaces) {
			s.moveBy(movement.x, movement.y, movement.z);
		}
		location = new Point3D(x, y, z);
	}

	public void moveBy(double x, double y, double z) {
		for (Surface s : surfaces) {
			s.moveBy(x, y, z);
		}
		location = location.sum(new Point3D(x, y, z));
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
		return "Model[location=" + location +",surfaces.size=" + surfaces.size() + "]";
	}
}
