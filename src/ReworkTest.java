import javax.swing.JFrame;

public class ReworkTest {
    public static void main(String[] args) {
        //create Space
        Space space = new Space();

        //add surfaces to space

        //create container
        JFrame frame = new JFrame();
        frame.setTitle("3D space");
		frame.setSize(1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //camera = Space.createCamera
        Camera camera = space.createCamera();

        //container.add(camera);
        frame.add(camera);

        frame.setVisible(true);

        //camera.addKeyboard(new Keyboard)
    }
}
