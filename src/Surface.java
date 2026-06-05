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

	public void addPoint(Point3D point) {
		if (!(points.contains(point))) {
			points.add(point);
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color c) {
		color = c;
	}
}
