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
			if (camera != null) {
				camera.moveCameraRelative(0.35, 0 , 0);
				return;
			}

			spaceManager.movePlayerRelative(0.35, 0, 0);
		}
		else if (e.getKeyChar() == 'a') {
			if (camera != null) {
				camera.moveCameraRelative(-0.35, 0 , 0);
				return;
			}

			spaceManager.movePlayerRelative(-0.35, 0, 0);
		}
		else if (e.getKeyChar() == 'w') {
			if (camera != null) {
				camera.moveCameraRelative(0, 0.35 , 0);
				return;
			}

			spaceManager.movePlayerRelative(0, 0.35, 0);
		}
		else if (e.getKeyChar() == 's') {
			if (camera != null) {
				camera.moveCameraRelative(0, -0.35 , 0);
				return;
			}

			spaceManager.movePlayerRelative(0, -0.35, 0);
		}
		else if (e.getKeyChar() == ' ') {
			if (camera != null) {
				camera.moveCameraRelative(0, 0 , 0.35);
				return;
			}

			spaceManager.movePlayerRelative(0, 0, 0.35);
		}
		else if (e.getKeyChar() == 'z') {
			if (camera != null) {
				camera.moveCameraRelative(0, 0 , -0.35);
				return;
			}

			spaceManager.movePlayerRelative(0, 0, -0.35);
		}
		else if (e.getKeyChar() == 'n') {
			// calls the object adder static method
			new Thread(() -> {
				try {
					ObjectAdder.addObject(spaceManager);
				}
				catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Something went wrong with the object adder", "Error", JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}).start();
		}
		else if (e.getKeyChar() == 'r') {
			spaceManager.movePlayerRelative(5, 0, 0);
		}
		else if (e.getKeyChar() == 'l') {
			spaceManager.movePlayerRelative(-5, 0, 0);
		}
		else if (e.getKeyChar() == 'q') {
			spaceManager.turnPlayer(-Math.PI / 32);
		}
		else if (e.getKeyChar() == 'e') {
			spaceManager.turnPlayer(Math.PI / 32);
		}
		else if (e.getKeyChar() == 't') {
			spaceManager.tiltPlayer(Math.PI / 32);
		}
		else if (e.getKeyChar() == 'g') {
			spaceManager.tiltPlayer(-Math.PI / 32);
		}
	}
}
