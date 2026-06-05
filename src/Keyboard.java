import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Class that extends the built in KeyAdapter class in order to detect keyboard input in the program frame
 */

public class Keyboard extends KeyAdapter {
	private Camera camera;
	
	public Keyboard (Camera c) {
		camera = c;
	}

	//move the player depending on the key they pressed, or add an object to the program
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == 'd') {
			camera.moveCameraRelative(0.35, 0 , 0);
		}
		else if (e.getKeyChar() == 'a') {
			camera.moveCameraRelative(-0.35, 0 , 0);
		}
		else if (e.getKeyChar() == 'w') {
			camera.moveCameraRelative(0, 0.35 , 0);
		}
		else if (e.getKeyChar() == 's') {
			camera.moveCameraRelative(0, -0.35 , 0);
		}
		else if (e.getKeyChar() == ' ') {
			camera.moveCameraRelative(0, 0 , 0.35);
		}
		else if (e.getKeyChar() == 'z') {
			camera.moveCameraRelative(0, 0 , -0.35);
		}
		else if (e.getKeyChar() == 'n') {
			ModelAdder.addModel(camera.getSpace(), camera);
		}
		else if (e.getKeyChar() == 'r') {
			camera.moveCameraRelative(5, 0, 0);
		}
		else if (e.getKeyChar() == 'l') {
			camera.moveCameraRelative(-5, 0, 0);
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
	}
}
