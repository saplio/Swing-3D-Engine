import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom class that holds the 3D coordinates of each individual surface in space
 */

public class Surface {

	public static final Color DEFAULT_COLOR = new Color(0, 0, 0, 0);

	private Color color;

	private ArrayList<Point3D> points;

	public Surface() {
		this(DEFAULT_COLOR);
	}

	public Surface(Color c) {
		points = new ArrayList<Point3D>();
		color = c;
	}

	public Surface(List<Point3D> points) {
		this(points, DEFAULT_COLOR);
	}

	public Surface(List<Point3D> points, Color c) {
		this.points = new ArrayList<Point3D>(points);
		color = c;
	}
	
	public List<Point3D> getPoints() {
		return Collections.unmodifiableList(points);
	}

	public boolean addPoint(Point3D point) {
		if (point == null) {
			return false;
		}

		if (points.size() == 0 || !(points.getLast().equals(point))) {
			points.add(point);
			return true;
		}

		return false;
	}

	public Point3D getLocationPoint3D() {
		return new Point3D(points.getFirst());
	}

	public void moveTo(double x, double y, double z) {
		Point3D movement = new Point3D(x, y, z).difference(getLocationPoint3D());
		for (Point3D p : points) {
			p.moveBy(movement.x, movement.y, movement.z);
		}
	}

	public void moveBy(double x, double y, double z) {
		for (Point3D p : points) {
			p.moveBy(x, y, z);
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		color = c;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Surface)) {
            return false;
        }

        Surface other = (Surface)obj;

        if (other.color.equals(color) && other.points.equals(points)) {
			return true;
        }

        return false;
	}

	@Override
	public String toString() {
		return "Surface[color=" + color + ",points.size=" + points.size() + "]";
	}
}
