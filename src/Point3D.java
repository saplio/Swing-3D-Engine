/**
 * Stores coordinates for a point in 3D space
 */

public class Point3D {

    public double x;
    public double y;
    public double z;

    public Point3D() {
        this(0, 0, 0);
    }

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(Point3D point) {
        x = point.x;
        y = point.y;
        z = point.z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void movePoint(double newX, double newY, double newZ) {
        x = newX;
        y = newY;
        z = newZ;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point3D)) {
            return false;
        }

        Point3D other = (Point3D)obj;

        if ((other.x == x) && (other.y == y) && (other.z == z)) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Point3D[x=" + x + ",y=" + y + ",z=" + z + "]";
    }
}
