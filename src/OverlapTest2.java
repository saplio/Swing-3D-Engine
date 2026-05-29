import java.awt.BorderLayout;
import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OverlapTest2 {

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
		
		double[] ShapeX = {-10, 10, 10, -10};
		double[] ShapeY = {10 , 10, -10, -10};
		double[] ShapeZ = {0, 0, 0, 0};
		
		spaceManager.addSurface(ShapeX, ShapeY, ShapeZ, Color.RED);
		
		double[] ShapeX2 = {-10, 10, 10, -10};
		double[] ShapeY2 = {10 , 10, 10, 10};
		double[] ShapeZ2 = {10, 10, 5, -4};
		
		spaceManager.addSurface(ShapeX2, ShapeY2, ShapeZ2, Color.BLUE);
		
		Keyboard kb = new Keyboard(spaceManager);
		frame.addKeyListener(kb);
	}

}
