import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Class that extends the built in KeyAdapter class in order to detect keyboard input in the program frame
 */

public class Keyboard extends KeyAdapter {
	private Camera camera;
	
	public Keyboard (Camera c) {
		camera = c;
	}

	// move the player depending on the key they pressed, or add an object to the program
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
			// TODO: remake place where model added depend on camera
			camera.getSpace().addModel(ModelReader.readModel(ModelReader.promptUserForModel()));
		}
		else if (e.getKeyChar() == 'r') {
			camera.moveCameraRelativeXY(5, 0, 0);
		}
		else if (e.getKeyChar() == 'l') {
			camera.moveCameraRelativeXY(-5, 0, 0);
		}
		else if (e.getKeyChar() == 'q') {
			camera.rotate(-Math.PI / 32, 0, 0);
		}
		else if (e.getKeyChar() == 'e') {
			camera.rotate(Math.PI / 32, 0, 0);
		}
		else if (e.getKeyChar() == 't') {
			camera.rotate(0, -Math.PI / 32, 0);
		}
		else if (e.getKeyChar() == 'g') {
			camera.rotate(0, Math.PI / 32, 0);
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
		}
	}
}
