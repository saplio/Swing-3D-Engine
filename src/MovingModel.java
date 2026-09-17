import java.util.ArrayList;

/**
 * Example of TimeStepActor used with a model
 */
public class MovingModel extends Model implements TimeStepActor {

    private ArrayList<Point3D> stops;
    private double speed;
    private int currentStop;

    public MovingModel() {
        stops = new ArrayList<Point3D>();
        currentStop = 0;
        speed = 10;
    }

    public void addStop(Point3D stop) {
        if (stops.size() == 0) {
            moveTo(stop.x, stop.y, stop.z);
        }

        stops.add(stop);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double s) {
        speed = s;
    }

    @Override
    public void timeStepUpdate(double timeUnit) {

        if (stops.size() < 2) {
            return;
        }


        Point3D distance = stops.get(currentStop).difference(getLocation());

        if (speed * timeUnit > distance.getHypot()) {
            Point3D stop = stops.get(currentStop);
            moveTo(stop.x, stop.y, stop.z);

            ++currentStop;
            if (currentStop >= stops.size()) {
                currentStop = 0;
            }
        }
        else {
            Point3D movement = distance.scale(speed * timeUnit / distance.getHypot());
            moveBy(movement.x, movement.y, movement.z);
        }
    }
    
}
