import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SmoothCameraController extends KeyAdapter {

    public static final double RIGHT_PLACEMENT = 0;
	public static final double FORWARD_PLACEMENT = 3;
	public static final double UP_PLACEMENT = 0;

	public static final double DEFAULT_ACCELERATION = 20;

    private TimeStepCamera camera;
	private double acceleration;

	// TODO: use Key Bindings instead of a KeyAdapter

    public SmoothCameraController(TimeStepCamera c) {
        camera = c;

		acceleration = DEFAULT_ACCELERATION;
    }

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double a) {
		acceleration = a;
	}

    // perform an action with the camera depending on the key pressed
	@Override
	public void keyPressed(KeyEvent e) {
		if ("daws z".contains(String.valueOf(e.getKeyChar()))) {
			camera.setBeingMoved(true);
			if (e.getKeyChar() == 'd') {
				Point3D cameraAccel = PerspectiveMath.cameraRelativeToOrthogonalXY(new Point3D(acceleration, 0, 0), camera.getYaw());
				camera.setAcceleration(cameraAccel);
			}
			else if (e.getKeyChar() == 'a') {
				Point3D cameraAccel = PerspectiveMath.cameraRelativeToOrthogonalXY(new Point3D(-acceleration, 0, 0), camera.getYaw());
				camera.setAcceleration(cameraAccel);
			}
			else if (e.getKeyChar() == 'w') {
				Point3D cameraAccel = PerspectiveMath.cameraRelativeToOrthogonalXY(new Point3D(0, acceleration, 0), camera.getYaw());
				camera.setAcceleration(cameraAccel);
			}
			else if (e.getKeyChar() == 's') {
				Point3D cameraAccel = PerspectiveMath.cameraRelativeToOrthogonalXY(new Point3D(0, -acceleration, 0), camera.getYaw());
				camera.setAcceleration(cameraAccel);
			}
			else if (e.getKeyChar() == ' ') {
				Point3D cameraAccel = PerspectiveMath.cameraRelativeToOrthogonalXY(new Point3D(0, 0, acceleration), camera.getYaw());
				camera.setAcceleration(cameraAccel);
			}
			else if (e.getKeyChar() == 'z') {
				Point3D cameraAccel = PerspectiveMath.cameraRelativeToOrthogonalXY(new Point3D(0, 0, -acceleration), camera.getYaw());
				camera.setAcceleration(cameraAccel);
			}
		}

		if (e.getKeyChar() == 'n') {
			Model m = ModelReader.readModel(ModelReader.promptUserForModel());
			Point3D p = PerspectiveMath.cameraRelativeToOrthogonalXY(new Point3D(RIGHT_PLACEMENT, FORWARD_PLACEMENT, UP_PLACEMENT), camera.getYaw()).sum(camera.getCameraLocation());

			if (!(m == null)) {
				m.scale(ModelReader.promptUserForScale());
				m.moveTo(p.x, p.y, p.z);
				m.rotateXY(camera.getYaw());
				camera.getSpace().addModel(m);
			}
		}
		else if (e.getKeyChar() == 'q') {
			camera.rotate(Math.PI / 32, 0, 0);
		}
		else if (e.getKeyChar() == 'e') {
			camera.rotate(-Math.PI / 32, 0, 0);
		}
		else if (e.getKeyChar() == 't') {
			camera.rotate(0, Math.PI / 32, 0);
		}
		else if (e.getKeyChar() == 'g') {
			camera.rotate(0, -Math.PI / 32, 0);
		}
		else if (e.getKeyChar() == 'x') {
			camera.rotate(0, 0, Math.PI / 32);
		}
		else if (e.getKeyChar() == 'c') {
			camera.rotate(0, 0, -Math.PI / 32);
		}
		else if (e.getKeyChar() == 'i') {
			System.out.println();
			System.out.println("Position: " + camera.getXPos() + ", " + camera.getYPos() + ", " + camera.getZPos());
			System.out.println("Rotation: " + camera.getYaw() + ", " + camera.getPitch() + ", " + camera.getRoll());
			System.out.println(camera.getSpace().toString());
			int s = 0;
			for (Model m: camera.getSpace().getModels()) {
				s += m.getSurfaces().size();
			}
			System.out.println("Amount of surfaces: " + s);
		}
		else if (e.getKeyChar() == 'm') {
			camera.getSpace().scaleLastModel();
		}
		else if (e.getKeyChar() == 'h') {
			camera.setFovFactor(camera.getFovFactor() + 10);
		}
		else if (e.getKeyChar() == 'j') {
			camera.setFovFactor(camera.getFovFactor() - 10);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		camera.setBeingMoved(false);
	}
}
