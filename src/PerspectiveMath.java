import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

/**
 * Class with static methods that handle all of the math related to perspective.
 */

public class PerspectiveMath {

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

    public static Surface getSlicedSurface(Surface surface, double slicePlaneY) {
        Surface slicedSurface = new Surface(surface.getColor());

        List<Point3D> surfacePoints = surface.getPoints();
        for (int i = 0; i < surfacePoints.size(); ++i) {
            Surface slicedEdge = getSlicedLine(surfacePoints.get(i), surfacePoints.get((i + 1) % surfacePoints.size()), slicePlaneY);
            
            for (Point3D point : slicedEdge.getPoints()) {
                slicedSurface.addPoint(point);

            }
        }

        return slicedSurface;
    }

    public static Surface getSlicedLine(Point3D edgeStart, Point3D edgeEnd, double slicePlaneY) {
        Surface slicedLine = new Surface();
        
        if ((edgeStart.y - slicePlaneY > 0) && (edgeEnd.y - slicePlaneY > 0)) {
            slicedLine.addPoint(new Point3D(edgeStart));
            slicedLine.addPoint(new Point3D(edgeEnd));
        }
        else if ((edgeStart.y - slicePlaneY > 0) ^ (edgeEnd.y - slicePlaneY > 0)){
            double ratio = (edgeStart.y - slicePlaneY) / ((edgeStart.y - slicePlaneY) - (edgeEnd.y - slicePlaneY));
            double slicePointX = (edgeEnd.x - edgeStart.x) * ratio + edgeStart.x;
			double slicePointZ = (edgeEnd.z - edgeStart.z) * ratio + edgeStart.z;

            if (edgeStart.y > edgeEnd.y) {
                slicedLine.addPoint(new Point3D(edgeStart));
                slicedLine.addPoint(new Point3D(slicePointX, slicePlaneY, slicePointZ));
            }
            else {
                slicedLine.addPoint(new Point3D(slicePointX, slicePlaneY, slicePointZ));
                slicedLine.addPoint(new Point3D(edgeEnd));
            }
        }

        return slicedLine;
    }

    public static Point calcPointPerspective(Point3D point, Point3D cameraPoint, Dimension viewDimension, double fov) {
        int x = (viewDimension.width / 2) + (int)(fov * (point.x - cameraPoint.x)/(point.y - cameraPoint.y));
        int y = (viewDimension.height / 2) - (int)(fov * (point.z - cameraPoint.z)/(point.y - cameraPoint.y));
        return new Point(x, y);
    }

    //The following are perspective calculation equations which produce warped perspective.

    public static Point calcPointPerspectivePincushion(Point3D point, Point3D cameraPoint, Dimension viewDimension, double fov) {
        int x = (viewDimension.width / 2) + (int)(fov * Math.atan((point.x - cameraPoint.x)/(point.y - cameraPoint.y)));
        int y = (viewDimension.height / 2) - (int)(fov * Math.atan((point.z - cameraPoint.z)/(point.y - cameraPoint.y)));
        return new Point(x, y);
    }

    public static Point calcPointPerspectiveFisheye(Point3D point, Point3D cameraPoint, Dimension viewDimension, double fov) {
        double theta = Math.atan((point.z - cameraPoint.z) / (point.x - cameraPoint.x));
		
		if ((point.x - cameraPoint.x < 0) && (point.z - cameraPoint.z >= 0)) {
			theta += Math.PI;
		}
		else if ((point.x - cameraPoint.x < 0) && (point.z - cameraPoint.z < 0)) {
			theta += Math.PI;
		}
		else if ((point.x - cameraPoint.x >= 0) && (point.z - cameraPoint.z < 0)) {
			theta += Math.PI * 2;
		}
		
		double r = (fov * (Math.atan(Math.hypot((point.x - cameraPoint.x), (point.z - cameraPoint.z)) / (point.y - cameraPoint.y)) / (Math.PI * 0.45)));
		
		int x = (int)((viewDimension.width / 2) + (r * Math.cos(theta)));	
        int y = (int)((viewDimension.height / 2) - (r * Math.sin(theta)));

        return new Point(x, y);
    }
}
