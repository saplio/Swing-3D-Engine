import java.awt.Dimension;
import java.awt.Point;

/**
 * Class with static methods that handle all of the math related to perspective.
 */

public class PerspectiveMath {
    //TODO: add static methods for perspective calculations and make other classes use these static methods for all perspective math

    public static Surface getRotatedSurfaceXY(Surface surface, double radians, double axisX, double axisY) {
        Surface rotatedSurface = new Surface(surface.getColor());
        for (Point3D point : surface.getPoints()) {
            rotatedSurface.addPoint(getRotatedPointXY(point, radians, axisX, axisY));

        }
        return rotatedSurface;
    }

    public static Point3D getRotatedPointXY(Point3D point, double radians, double axisX, double axisY) {
        double newX = ((point.x - axisX) * Math.cos(radians) - (point.y - axisY) * Math.sin(radians)) + axisX;
        double newY = ((point.x - axisX) * Math.sin(radians) + (point.y - axisY) * Math.cos(radians)) + axisY;
        return new Point3D(newX, newY, point.z);
    }

    public static Surface getRotatedSurfaceYZ(Surface surface, double radians, double axisY, double axisZ) {
        Surface rotatedSurface = new Surface(surface.getColor());
        for (Point3D point : surface.getPoints()) {
            rotatedSurface.addPoint(getRotatedPointYZ(point, radians, axisY, axisZ));

        }
        return rotatedSurface;
    }

    public static Point3D getRotatedPointYZ(Point3D point, double radians, double axisY, double axisZ) {
        double newY = ((point.y - axisY) * Math.cos(radians) - (point.z - axisZ) * Math.sin(radians)) + axisY;
        double newZ = ((point.y - axisY) * Math.sin(radians) + (point.z - axisZ) * Math.cos(radians)) + axisZ;
        return new Point3D(point.x, newY, newZ);
    }

    public static Surface getRotatedSurfaceXZ(Surface surface, double radians, double axisX, double axisZ) {
        Surface rotatedSurface = new Surface(surface.getColor());
        for (Point3D point : surface.getPoints()) {
            rotatedSurface.addPoint(getRotatedPointXZ(point, radians, axisX, axisZ));

        }
        return rotatedSurface;
    }

    public static Point3D getRotatedPointXZ(Point3D point, double radians, double axisX, double axisZ) {
        double newX = ((point.x - axisX) * Math.cos(radians) - (point.z - axisZ) * Math.sin(radians)) + axisX;
        double newZ = ((point.x - axisX) * Math.sin(radians) + (point.z - axisZ) * Math.cos(radians)) + axisZ;
        return new Point3D(newX, point.y, newZ);
    }

    public static Surface getSlicedSurface(Surface surface, double y) {
        //TODO: add content
        return null;
    }

    public static Point calcPointPerspective(Point3D point, Point3D cameraPoint, Dimension viewDimension, double fov) {
        int x = (viewDimension.width / 2) + (int)(fov * (point.x - cameraPoint.x)/(point.y - cameraPoint.y));
        int y = (viewDimension.height / 2) - (int)(fov * (point.z - cameraPoint.z)/(point.y - cameraPoint.y));
        return new Point(x, y);
    }
}
