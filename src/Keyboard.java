/*
 * class that extends the built in KeyAdapter class in order to detect keyboard input in the program frame
 */

import java.awt.event.*;
import javax.swing.*;

public class Keyboard extends KeyAdapter {
	
	private SpaceManager spaceManager; //stores space manager to call when moving or adding objects

	//TODO: rewrite this class to use only the camera attribute
	private Camera camera;
	
	//constructor to pass the space manager to the private field
	public Keyboard(SpaceManager spaceManager) {
		this.spaceManager = spaceManager;
	}

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
			// calls the object adder static method
			//TODO: make it work with rework
			// new Thread(() -> {
			// 	try {
			// 		ObjectAdder.addObject(spaceManager);
			// 	}
			// 	catch (Exception ex) {
			// 		JOptionPane.showMessageDialog(null, "Something went wrong with the object adder", "Error", JOptionPane.ERROR_MESSAGE);
			// 		ex.printStackTrace();
			// 	}
			// }).start();
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
