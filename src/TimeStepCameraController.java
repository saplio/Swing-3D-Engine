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

    public static final double DEFAULT_ACCELERATION = 35; // FIXME: add ability to change acceleration
	public static final double DEFAULT_SENSITIVITY = 0.002;

    private TimeStepCamera camera;
    private LinkedList<String> movementKeysPressed;
    private double sensitivity;
    private boolean mouseMotionOn;

    public TimeStepCameraController(TimeStepCamera camera) {
        sensitivity = DEFAULT_SENSITIVITY;
        mouseMotionOn = true;

        movementKeysPressed = new LinkedList<String>();
        this.camera = camera;

        mapMovementAction("A", "move left", -35, 0,0);
        mapMovementAction("D", "move right", 35, 0, 0);
        mapMovementAction("W", "move forward", 0, 35, 0);
        mapMovementAction("S", "move backward", 0, -35, 0);
        mapMovementAction("SPACE", "move up", 0, 0, 35);
        mapMovementAction("Z", "move down", 0, 0, -35);

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
            }

            if (!movementKeysPressed.contains(key)) {
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

    @Override
	public void mouseMoved(MouseEvent e) {
		if (!mouseMotionOn || !SwingUtilities.getWindowAncestor(e.getComponent()).isFocusOwner()) {
			return;
		}

		Point viewLocation = e.getComponent().getLocationOnScreen();
		Dimension viewSize = e.getComponent().getSize();
		Point absoluteCenter = new Point((viewLocation.x + viewSize.width / 2), (viewLocation.y + viewSize.height / 2));
		Point relativeCenter = new Point(viewSize.width / 2, viewSize.height / 2);
		Point movement = new Point(e.getX() - relativeCenter.x, e.getY() - relativeCenter.y);

		camera.rotate(-movement.x * sensitivity,-movement.y * sensitivity, 0);

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
