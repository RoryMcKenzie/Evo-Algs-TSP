import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Gui extends JFrame implements Observer{

	private JPanel contentPane;
	Individual individual = null;
	private double zoom = 0.1;//0.4 for berlin (with 1 gui) 0.0004 for dsj1000 + add translate 200, 100
	private int generation;

	/**
	 * Create the frame.
	 * @param i 
	 */
	public Gui(int i) {
		int y = (i / 5 ) * 200;
		int x = i % 5 * 200;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(x, y, 200, 200);
		contentPane = new JPanel() {
			

			@Override
			public void paint(Graphics g) {	
				if (individual == null){
					return;
				}				
				super.paint(g);
				Graphics2D g2d = (Graphics2D)g;
				g2d.drawString(generation + "    " + individual.fitness, 10, 10);
				AffineTransform at = new AffineTransform();	            
				at.translate(10, 20);//0,0 for berlin
				at.scale(zoom, zoom);// i prefer WebEx than zoom		            
	            g2d.setTransform(at);	            

				g2d.drawLine((int)individual.depot.x, (int)individual.depot.y, (int)individual.chromosome.get(0).x, (int)individual.chromosome.get(0).y);
				for(int i = 0; i < individual.chromosome.size() - 1; i++) {
					int x1 = (int)individual.chromosome.get(i).x;
					int x2 = (int)individual.chromosome.get(i + 1).x;
					int y1 = (int)individual.chromosome.get(i).y;
					int y2 = (int)individual.chromosome.get(i + 1).y;					
					g2d.drawLine(x1, y1, x2, y2);
				}
				g2d.drawLine((int)individual.chromosome.get(individual.chromosome.size() - 1).x, (int)individual.chromosome.get(individual.chromosome.size() - 1).y, (int)individual.depot.x, (int)individual.depot.y);
				
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setVisible(true);
	}

	@Override
	public void update(Observable arg0, Object arg1) {		
		individual = (Individual) arg1;
		generation = ((EA)arg0).generation;
		repaint();
	
	}

}
