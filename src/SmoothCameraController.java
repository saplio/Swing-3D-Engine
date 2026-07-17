import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class SmoothCameraController extends KeyAdapter implements MouseMotionListener, MouseListener {

    public static final double RIGHT_PLACEMENT = 0;
	public static final double FORWARD_PLACEMENT = 3;
	public static final double UP_PLACEMENT = 0;

	public static final double DEFAULT_ACCELERATION = 35;

	public static final double DEFAULT_SENSITIVITY = 0.002;
	
    private TimeStepCamera camera;

	private double acceleration;
	private double sensitivity;

	private boolean mouseMotion;

	private final Cursor BLANK_CURSOR;

// TODO: use Key Bindings instead of a KeyAdapter

    public SmoothCameraController(TimeStepCamera c) {
        camera = c;

		acceleration = DEFAULT_ACCELERATION;
		sensitivity = DEFAULT_SENSITIVITY;

		mouseMotion = true;

		// TODO: add link to where code was obtained
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
    	cursorImg, new Point(0, 0), "blank cursor");

		camera.getViewPanel().setCursor(BLANK_CURSOR);
    }

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double a) {
		acceleration = a;
	}

	public double getSensitivity() {
		return sensitivity;
	}

	public void setSensitivity(double s) {
		sensitivity = s;
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

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			mouseMotion = !mouseMotion;
			if (mouseMotion) {
				camera.getViewPanel().setCursor(BLANK_CURSOR);
			}
			else {
				camera.getViewPanel().setCursor(Cursor.getDefaultCursor());
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		camera.setBeingMoved(false);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// FIXME: mouse can escape windowed mode if moved fast enough
		if (!mouseMotion || !e.getComponent().isFocusOwner()) {
			return;
		}

		Point viewLocation = e.getComponent().getLocationOnScreen();
		Dimension viewSize = e.getComponent().getSize();
		Point absoluteCenter = new Point((viewLocation.x + viewSize.width / 2), (viewLocation.y + viewSize.height / 2));
		Point relativeCenter = new Point(viewSize.width / 2, viewSize.height / 2);
		Point movement = new Point(e.getX() - relativeCenter.x, e.getY() - relativeCenter.y);

		camera.rotate(-movement.x * sensitivity,-movement.y * sensitivity, 0);

		try {
			new Robot().mouseMove(absoluteCenter.x, absoluteCenter.y);;
		}
		catch(AWTException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// empty method to satisfy interface
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// empty method to satisfy interface
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// empty method to satisfy interface
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// empty method to satisfy interface
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// empty method to satisfy interface
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// empty method to satisfy interface
	}
}
