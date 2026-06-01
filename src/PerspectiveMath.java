import java.awt.Dimension;
import java.awt.Point;

/**
 * Class with static methods that handle all of the math related to perspective.
 */

public class PerspectiveMath {
    //TODO: add static methods for perspective calculations and make other classes use these static methods for all perspective math

    public static Surface rotateSurfaceXY(Surface surface, double radians, double x, double y) {
        //TODO: add content
        return null;
    }

    public static Surface rotateSurfaceYZ(Surface surface, double radians, double y, double z) {
        //TODO: add content
        return null;
    }

    public static Surface rotateSurfaceXZ(Surface surface, double radians, double x, double z) {
        //TODO: add content
        return null;
    }

    public static Surface sliceSurface(Surface surface, double y) {
        //TODO: add content
        return null;
    }

    public static Point calcPointPerspective(Point3D point, Point3D cameraPoint, Dimension viewDimension, double fov) {
        int x = (viewDimension.width / 2) + (int)(fov * (point.x - cameraPoint.x)/(point.y - cameraPoint.y));
        int y = (viewDimension.height / 2) - (int)(fov * (point.z - cameraPoint.z)/(point.y - cameraPoint.y));
        return new Point(x, y);
    }
}
