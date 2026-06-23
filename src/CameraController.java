import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Class that extends the built in KeyAdapter class in order to detect keyboard input in the program frame
 */

public class CameraController extends KeyAdapter {

	public static final double RIGHT_PLACEMENT = 0;
	public static final double FORWARD_PLACEMENT = 3;
	public static final double UP_PLACEMENT = 0;

	private Camera camera;
	
	public CameraController(Camera c) {
		camera = c;
	}

	// perform an action with the camera depending on the key pressed
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'd') {
			camera.moveCameraRelativeXY(0.35, 0 , 0);
		}
		else if (e.getKeyChar() == 'a') {
			camera.moveCameraRelativeXY(-0.35, 0 , 0);
		}
		else if (e.getKeyChar() == 'w') {
			camera.moveCameraRelativeXY(0, 0.35 , 0);
		}
		else if (e.getKeyChar() == 's') {
			camera.moveCameraRelativeXY(0, -0.35 , 0);
		}
		else if (e.getKeyChar() == ' ') {
			camera.moveCameraRelativeXY(0, 0 , 0.35);
		}
		else if (e.getKeyChar() == 'z') {
			camera.moveCameraRelativeXY(0, 0 , -0.35);
		}
		else if (e.getKeyChar() == 'n') {
			Model m = ModelReader.readModel(ModelReader.promptUserForModel());
			Point3D p = PerspectiveMath.cameraRelativeToOrthogonalXY(RIGHT_PLACEMENT, FORWARD_PLACEMENT, UP_PLACEMENT, camera.getYaw()).sum(camera.getCameraPoint3D());

			if (!(m == null)) {
				m.scale(ModelReader.promptUserForScale());
				m.moveTo(p.x, p.y, p.z);
				m.rotateXY(camera.getYaw());
				camera.getSpace().addModel(m);
			}
		}
		else if (e.getKeyChar() == 'r') {
			camera.moveCameraRelativeXY(5, 0, 0);
		}
		else if (e.getKeyChar() == 'l') {
			camera.moveCameraRelativeXY(-5, 0, 0);
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
			// camera.getSpace().moveLastModel();
			camera.getSpace().scaleLastModel();
		}
	}
}
