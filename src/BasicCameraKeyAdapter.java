import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Class that extends the built in KeyAdapter class in order to detect keyboard input in the program frame
 */

public class BasicCameraKeyAdapter extends KeyAdapter {

	public static final double RIGHT_PLACEMENT = 0;
	public static final double FORWARD_PLACEMENT = 3;
	public static final double UP_PLACEMENT = 0;

	public static final double DEFAULT_STEP_SIZE = 0.35;

	private Camera camera;
	private double stepSize;
	
	public BasicCameraKeyAdapter(Camera c) {
		camera = c;
		stepSize = DEFAULT_STEP_SIZE;
	}

	public double getStepSize() {
		return stepSize;
	}

	public void setStepSize(double newSize) {
		stepSize = newSize;
	}

	// perform an action with the camera depending on the key pressed
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'd') {
			camera.moveCameraRelativeXY(stepSize, 0 , 0);
		}
		else if (e.getKeyChar() == 'a') {
			camera.moveCameraRelativeXY(-stepSize, 0 , 0);
		}
		else if (e.getKeyChar() == 'w') {
			camera.moveCameraRelativeXY(0, stepSize, 0);
		}
		else if (e.getKeyChar() == 's') {
			camera.moveCameraRelativeXY(0, -stepSize, 0);
		}
		else if (e.getKeyChar() == ' ') {
			camera.moveCameraRelativeXY(0, 0 , stepSize);
		}
		else if (e.getKeyChar() == 'z') {
			camera.moveCameraRelativeXY(0, 0 , -stepSize);
		}
		else if (e.getKeyChar() == 'n') {
			Model m = ModelReader.readModel(ModelReader.promptUserForModel());
			Point3D p = PerspectiveMath.cameraRelativeToOrthogonalXY(new Point3D(RIGHT_PLACEMENT, FORWARD_PLACEMENT, UP_PLACEMENT), camera.getYaw()).sum(camera.getCameraLocation());

			if (!(m == null)) {
				m.scale(ModelReader.promptUserForScale());
				m.moveTo(p.x, p.y, p.z);
				m.rotateXY(camera.getYaw());
				camera.getSpace().addModel(m);
			}
		}
		else if (e.getKeyChar() == 'r') {
			camera.moveCameraRelativeXY(stepSize * 20, 0, 0);
		}
		else if (e.getKeyChar() == 'l') {
			camera.moveCameraRelativeXY(-stepSize * 20, 0, 0);
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
}
