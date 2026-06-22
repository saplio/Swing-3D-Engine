import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

/**
 * Class with static methods that handle all of the math related to perspective.
 */

public class PerspectiveMath {

    /**
     * Rotate a {@code Surface} on the XY plane.
     * 
     * @param surface {@code Surface} to rotate
     * @param radians Amount to rotate (positive is counterclockwise)
     * @param axisX Pivot axis X coordinate
     * @param axisY Pivot axis Y coordinate
     * @return New {@code Surface} with the rotation applied
     */
    public static Surface getRotatedSurfaceXY(Surface surface, double radians, double axisX, double axisY) {
        Surface rotatedSurface = new Surface(surface.getColor());
        for (Point3D point : surface.getPoints()) {
            rotatedSurface.addPoint(getRotatedPointXY(point, radians, axisX, axisY));
        }
        return rotatedSurface;
    }

    /**
     * Rotate a {@code Point3D} on the XY plane.
     * 
     * @param point {@code Point3D} to rotate
     * @param radians Amount to rotate (positive is counterclockwise)
     * @param axisX Pivot axis X coordinate
     * @param axisY Pivot axis Y coordinate
     * @return New {@code Point3D} with the rotation applied
     */
    public static Point3D getRotatedPointXY(Point3D point, double radians, double axisX, double axisY) {
        double newX = ((point.x - axisX) * Math.cos(radians) - (point.y - axisY) * Math.sin(radians)) + axisX;
        double newY = ((point.x - axisX) * Math.sin(radians) + (point.y - axisY) * Math.cos(radians)) + axisY;
        return new Point3D(newX, newY, point.z);
    }


    /**
     * Rotate a {@code Surface} on the YZ plane.
     * 
     * @param surface {@code Surface} to rotate
     * @param radians Amount to rotate (positive is counterclockwise)
     * @param axisY Pivot axis Y coordinate
     * @param axisZ Pivot axis Z coordinate
     * @return New {@code Surface} with the rotation applied
     */
    public static Surface getRotatedSurfaceYZ(Surface surface, double radians, double axisY, double axisZ) {
        Surface rotatedSurface = new Surface(surface.getColor());
        for (Point3D point : surface.getPoints()) {
            rotatedSurface.addPoint(getRotatedPointYZ(point, radians, axisY, axisZ));
        }
        return rotatedSurface;
    }

    /**
     * Rotate a {@code Point3D} on the YZ plane.
     * 
     * @param point {@code Point3D} to rotate
     * @param radians Amount to rotate (positive is counterclockwise)
     * @param axisY Pivot axis Y coordinate
     * @param axisZ Pivot axis Z coordinate
     * @return New {@code Point3D} with the rotation applied
     */
    public static Point3D getRotatedPointYZ(Point3D point, double radians, double axisY, double axisZ) {
        double newY = ((point.y - axisY) * Math.cos(radians) - (point.z - axisZ) * Math.sin(radians)) + axisY;
        double newZ = ((point.y - axisY) * Math.sin(radians) + (point.z - axisZ) * Math.cos(radians)) + axisZ;
        return new Point3D(point.x, newY, newZ);
    }

    /**
     * Rotate a {@code Surface} on the XZ plane.
     * 
     * @param surface {@code Surface} to rotate
     * @param radians Amount to rotate (positive is counterclockwise)
     * @param axisX Pivot axis X coordinate
     * @param axisZ Pivot axis Z coordinate
     * @return New {@code Surface} with the rotation applied
     */
    public static Surface getRotatedSurfaceXZ(Surface surface, double radians, double axisX, double axisZ) {
        Surface rotatedSurface = new Surface(surface.getColor());
        for (Point3D point : surface.getPoints()) {
            rotatedSurface.addPoint(getRotatedPointXZ(point, radians, axisX, axisZ));
        }
        return rotatedSurface;
    }

    /**
     * Rotate a {@code Point3D} on the XZ plane.
     * 
     * @param point {@code Point3D} to rotate
     * @param radians Amount to rotate (positive is counterclockwise)
     * @param axisX Pivot axis X coordinate
     * @param axisZ Pivot axis Z coordinate
     * @return New {@code Point3D} with the rotation applied
     */
    public static Point3D getRotatedPointXZ(Point3D point, double radians, double axisX, double axisZ) {
        double newX = ((point.x - axisX) * Math.cos(radians) - (point.z - axisZ) * Math.sin(radians)) + axisX;
        double newZ = ((point.x - axisX) * Math.sin(radians) + (point.z - axisZ) * Math.cos(radians)) + axisZ;
        return new Point3D(newX, point.y, newZ);
    }

    /**
     * General rotation method for a {@code Surface} for perspective calculations.
     * 
     * @param surface {@code Surface} to rotate
     * @param yaw XY rotation in radians
     * @param pitch YZ rotation in radians
     * @param roll XZ rotation in radians
     * @param pointOfRotation Pivot point
     * @return New {@code Surface} with the rotation applied
     */
    public static Surface getViewRotatedSurface(Surface surface, double yaw, double pitch, double roll, Point3D pointOfRotation) {
        Surface rotatedSurface = new Surface(surface.getColor());
        for (Point3D point : surface.getPoints()) {        
            rotatedSurface.addPoint(getViewRotatedPoint(point, yaw, pitch, roll, pointOfRotation));
        }
        return rotatedSurface;
    }

    /**
     * General rotation method for a {@code Point3D} for perspective calculations.
     * 
     * @param point {@code Point3D} to rotate
     * @param yaw XY rotation in radians
     * @param pitch YZ rotation in radians
     * @param roll XZ rotation in radians
     * @param pointOfRotation Pivot point
     * @return New {@code Point3D} with the rotation applied
     */
    public static Point3D getViewRotatedPoint(Point3D point, double yaw, double pitch, double roll, Point3D pointOfRotation) {
        Point3D rotatedPoint;
        rotatedPoint = getRotatedPointXY(point, yaw, pointOfRotation.x, pointOfRotation.y);
        rotatedPoint = getRotatedPointYZ(rotatedPoint, pitch, pointOfRotation.y, pointOfRotation.z);
        rotatedPoint = getRotatedPointXZ(rotatedPoint, roll, pointOfRotation.x, pointOfRotation.z);
        return rotatedPoint;
    }

    /**
     * Reverse order of the general rotation method for a {@code Surface} for perspective calculations.
     * 
     * @param surface {@code Surface} to rotate
     * @param yaw XY rotation in radians
     * @param pitch YZ rotation in radians
     * @param roll XZ rotation in radians
     * @param pointOfRotation Pivot point
     * @return New {@code Surface} with the rotation applied
     */
    public static Surface getReverseViewRotatedSurface(Surface surface, double yaw, double pitch, double roll, Point3D pointOfRotation) {
        Surface rotatedSurface = new Surface(surface.getColor());
        for (Point3D point : surface.getPoints()) {        
            rotatedSurface.addPoint(getReverseViewRotatedPoint(point, yaw, pitch, roll, pointOfRotation));
        }
        return rotatedSurface;
    }

    /**
     * Reverse order of the general rotation method for a {@code Point3D} for perspective calculations.
     * 
     * @param point {@code Point3D} to rotate
     * @param yaw XY rotation in radians
     * @param pitch YZ rotation in radians
     * @param roll XZ rotation in radians
     * @param pointOfRotation Pivot point
     * @return New {@code Point3D} with the rotation applied
     */
    public static Point3D getReverseViewRotatedPoint(Point3D point, double yaw, double pitch, double roll, Point3D pointOfRotation) {
        Point3D rotatedPoint;
        rotatedPoint = getRotatedPointXZ(point, roll, pointOfRotation.x, pointOfRotation.z);
        rotatedPoint = getRotatedPointYZ(rotatedPoint, pitch, pointOfRotation.y, pointOfRotation.z);
        rotatedPoint = getRotatedPointXY(rotatedPoint, yaw, pointOfRotation.y, pointOfRotation.z);
        return rotatedPoint;
    }

    /**
     * Slice a {@code Surface} along a specified plane parallel to the Y plane and get the part of it in front of the plane.
     * 
     * @param surface {@code Surface} to slice
     * @param slicePlaneY Y position of slicing plane
     * @return New {@code Surface} that represents the part of the given {@code Surface} in front of the slicing plane
     */
    public static Surface getSlicedSurfaceY(Surface surface, double slicePlaneY) {
        Surface slicedSurface = new Surface(surface.getColor());

        List<Point3D> surfacePoints = surface.getPoints();
        for (int i = 0; i < surfacePoints.size(); ++i) {
            Surface slicedEdge = getSlicedLineY(surfacePoints.get(i), surfacePoints.get((i + 1) % surfacePoints.size()), slicePlaneY);
            
            for (Point3D point : slicedEdge.getPoints()) {
                slicedSurface.addPoint(point);
            }
        }

        return slicedSurface;
    }

    /**
     * Slice a line along a specified plane parallel to the Y plane and get the part of it in front of the plane.
     * 
     * @param lineStart Start point of line to slice
     * @param lineEnd End point of line to slice
     * @param slicePlaneY Y position of slicing plane
     * @return New {@code Surface} that represents the part of the given line in front of the slicing plane.
     * 
     */
    public static Surface getSlicedLineY(Point3D lineStart, Point3D lineEnd, double slicePlaneY) {
        Surface slicedLine = new Surface();
        
        if ((lineStart.y - slicePlaneY > 0) && (lineEnd.y - slicePlaneY > 0)) {
            slicedLine.addPoint(new Point3D(lineStart));
            slicedLine.addPoint(new Point3D(lineEnd));
        }
        else if ((lineStart.y - slicePlaneY > 0) ^ (lineEnd.y - slicePlaneY > 0)){
            double ratio = (lineStart.y - slicePlaneY) / ((lineStart.y - slicePlaneY) - (lineEnd.y - slicePlaneY));
            double slicePointX = (lineEnd.x - lineStart.x) * ratio + lineStart.x;
			double slicePointZ = (lineEnd.z - lineStart.z) * ratio + lineStart.z;

            if (lineStart.y > lineEnd.y) {
                slicedLine.addPoint(new Point3D(lineStart));
                slicedLine.addPoint(new Point3D(slicePointX, slicePlaneY, slicePointZ));
            }
            else {
                slicedLine.addPoint(new Point3D(slicePointX, slicePlaneY, slicePointZ));
                slicedLine.addPoint(new Point3D(lineEnd));
            }
        }

        return slicedLine;
    }

    /**
     * Project a {@code Point3D} onto a given projection screen
     * 
     * @param point Point in space to perform projection on
     * @param cameraPoint Point where center of projection screen is located in space
     * @param viewDimension Dimensions of the projection screen
     * @param fov Field of view
     * @return {@code Point} that represents the position where the {@code Point3D} should be displayed on the projection screen
     */
    public static Point calcPointPerspective(Point3D point, Point3D cameraPoint, Dimension viewDimension, double fov) {
        int x = (viewDimension.width / 2) + (int)(fov * (point.x - cameraPoint.x)/(point.y - cameraPoint.y));
        int y = (viewDimension.height / 2) - (int)(fov * (point.z - cameraPoint.z)/(point.y - cameraPoint.y));
        return new Point(x, y);
    }

    /**
     * Convert camera relative position information to orthogonal position information. Currently Only does this along XY plane.
     * 
     * @param amtRight Camera relative rightward movement
     * @param amtForward Camera relative forward movement
     * @param amtUp Camera relative upward movement
     * @param yaw Camera XY plane orientation
     * @return New {@code Point3D} representing the orthogonal version of the movement
     */
    public static Point3D cameraRelativeToOrthogonalXY(double amtRight, double amtForward, double amtUp, double yaw) {
        // TODO: make this a more general method that can account for all rotation

        double x = amtForward * Math.sin(-yaw) + amtRight * Math.sin(-yaw + Math.PI / 2);
		double y = amtForward * Math.cos(-yaw) + amtRight * Math.cos(-yaw +  Math.PI / 2);
        return new Point3D(x, y, amtUp);
    }

    // The following are perspective calculation equations which produce warped perspective.

    /**
     * Project a {@code Point3D} onto a given projection screen with a warped pincushion-like perspective
     * 
     * @param point Point in space to perform projection on
     * @param cameraPoint Point where center of projection screen is located in space
     * @param viewDimension Dimensions of the projection screen
     * @param fov Field of view
     * @return {@code Point} that represents the position where the {@code Point3D} should be displayed on the projection screen
     */
    public static Point calcPointPerspectivePincushion(Point3D point, Point3D cameraPoint, Dimension viewDimension, double fov) {
        int x = (viewDimension.width / 2) + (int)(fov * Math.atan((point.x - cameraPoint.x)/(point.y - cameraPoint.y)));
        int y = (viewDimension.height / 2) - (int)(fov * Math.atan((point.z - cameraPoint.z)/(point.y - cameraPoint.y)));
        return new Point(x, y);
    }

    /**
     * Project a {@code Point3D} onto a given projection screen with a warped fisheye-like perspective
     * 
     * @param point Point in space to perform projection on
     * @param cameraPoint Point where center of projection screen is located in space
     * @param viewDimension Dimensions of the projection screen
     * @param fov Field of view
     * @return {@code Point} that represents the position where the {@code Point3D} should be displayed on the projection screen
     */
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
