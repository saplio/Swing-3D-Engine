import java.awt.BorderLayout;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OverlapTest1 {

	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		JFrame frame = new JFrame();
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
		
		frame.setTitle("3D space");
		frame.setSize(1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(p);
		frame.setVisible(true);
		
		SpaceManager spaceManager = new SpaceManager(p);
		
		int y = 5;
		
		double[] ShapeX = {0, 0.75, 1, 0.75, 0, -0.75, -1, -0.75};
		double[] ShapeY = {y, y, y, y, y, y, y, y};
		double[] ShapeZ = {-1, -0.75, 0, 0.75, 1, 0.75, 0, -0.75};
		
		spaceManager.addSurface(ShapeX, ShapeY, ShapeZ, Color.RED);
		
		int diff = -2;
		
		double[] ShapeX2 = {3, 0, -2};
		double[] ShapeY2 = {y + diff - 1, y + diff, y + diff};
		double[] ShapeZ2 = {-1, 0.5, -1};
		
		spaceManager.addSurface(ShapeX2, ShapeY2, ShapeZ2, Color.BLUE);
		
		Keyboard kb = new Keyboard(spaceManager);
		frame.addKeyListener(kb);
	}

}
