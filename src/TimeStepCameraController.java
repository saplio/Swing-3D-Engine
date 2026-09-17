import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

/**
 * Class that sets keyboard controls for the view panel of a {@code TimeStepCamera}.
 */

public class TimeStepCameraController implements MouseInputListener {

    public static final double DEFAULT_ACCELERATION = 35;
	public static final double DEFAULT_SENSITIVITY = 0.002;

    private double acceleration;
    private double sensitivity;

    private TimeStepCamera camera;
    private LinkedList<String> movementKeysPressed;
    private boolean mouseMotionOn;

    // TODO: implement external changing of keybinds

    public TimeStepCameraController(TimeStepCamera camera) {
        acceleration = DEFAULT_ACCELERATION;
        sensitivity = DEFAULT_SENSITIVITY;
        mouseMotionOn = true;

        movementKeysPressed = new LinkedList<String>();
        this.camera = camera;

        mapAllMovementActions(acceleration);

        mapAction("ESCAPE", "toggle mouse motion", new ToggleMouseMotion());
        mapAction("N", "add model", new AddModel());

        camera.getViewPanel().addMouseMotionListener(this);
    }

    private void mapAction(String key, String actionMapKey, AbstractAction action) {
        JPanel panel = camera.getViewPanel();
        panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), actionMapKey);
        panel.getActionMap().put(actionMapKey, action);
    }

    private void mapMovementAction(String key, String action, double right, double forward, double up) {
        mapAction(key, action, new AddAcceleration(key, right, forward, up));
        mapAction("released " + key, "released " + action, new RemoveAcceleration(key, right, forward, up));
    }

    private void mapAllMovementActions(double a) {
        mapMovementAction("A", "move left", -a, 0,0);
        mapMovementAction("D", "move right", a, 0, 0);
        mapMovementAction("W", "move forward", 0, a, 0);
        mapMovementAction("S", "move backward", 0, -a, 0);
        mapMovementAction("SPACE", "move up", 0, 0, a);
        mapMovementAction("Z", "move down", 0, 0, -a);
    }

    private class AddAcceleration extends AbstractAction {
        private Point3D relativeAccel;
        private String key;

        public AddAcceleration(String key, double right, double forward, double up) {
            relativeAccel = new Point3D(right, forward, up);
            this.key = key;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (movementKeysPressed.size() == 0) {
                camera.setBeingMoved(true);
                movementKeysPressed.add(key);
                camera.setAcceleration(PerspectiveMath.cameraRelativeToOrthogonalXY(relativeAccel, camera.getYaw()));
            }
            else if (!movementKeysPressed.contains(key)) {
                movementKeysPressed.add(key);
                camera.setAcceleration(camera.getAcceleration().sum(PerspectiveMath.cameraRelativeToOrthogonalXY(relativeAccel, camera.getYaw())));
            }
        }
    }

    private class RemoveAcceleration extends AbstractAction {
        private Point3D relativeAccel;
        private String key;

        public RemoveAcceleration(String key, double right, double forward, double up) {
            relativeAccel = new Point3D(right, forward, up);
            this.key = key;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            camera.setAcceleration(camera.getAcceleration().sum(PerspectiveMath.cameraRelativeToOrthogonalXY(relativeAccel.negative(), camera.getYaw())));

            movementKeysPressed.remove(key);
            if (movementKeysPressed.size() == 0) {
                camera.setBeingMoved(false);
            } 
        }
    }

    private class ToggleMouseMotion extends AbstractAction {
        @Override 
        public void actionPerformed(ActionEvent e) {
            mouseMotionOn = !mouseMotionOn;
        }
    }

    private class AddModel extends AbstractAction {
        @Override 
        public void actionPerformed(ActionEvent e) {
            Model m = ModelReader.readModel(ModelReader.promptUserForModel());
			Point3D p = PerspectiveMath.cameraRelativeToOrthogonalXY(new Point3D(0, 3, 0), camera.getYaw()).sum(camera.getCameraLocation());

			if (!(m == null)) {
				m.scale(ModelReader.promptUserForScale());
				m.moveTo(p.x, p.y, p.z);
				m.rotateXY(camera.getYaw());
				camera.getSpace().addModel(m);
			}
        }
    }

    public double getSensitivity() {
		return sensitivity;
	}

	public void setSensitivity(double s) {
		sensitivity = s;
	}

    public double getAcceleration() {
        return acceleration;
    }

    // FIXME: ideally the AbstractAction classes directly read from acceleration rather than remapping the keybinds
    public void setAcceleration(double a) {
        acceleration = a;
        mapAllMovementActions(a);
    }

    @Override
	public void mouseMoved(MouseEvent e) {
		if (!mouseMotionOn || !SwingUtilities.getWindowAncestor(e.getComponent()).isFocusOwner()) {
			return;
		}

		Point viewLocation = e.getComponent().getLocationOnScreen();
		Dimension viewSize = e.getComponent().getSize();

		Point relativeCenter = new Point(viewSize.width / 2, viewSize.height / 2);
		Point movement = new Point(e.getX() - relativeCenter.x, e.getY() - relativeCenter.y);

		camera.rotate(-movement.x * sensitivity,-movement.y * sensitivity, 0);

        Point absoluteCenter = new Point((viewLocation.x + viewSize.width / 2), (viewLocation.y + viewSize.height / 2));
		try {
			new Robot().mouseMove(absoluteCenter.x, absoluteCenter.y);
		}
		catch(AWTException ex) {
			ex.printStackTrace();
		}
	}

    // Unused interface methods
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
}
