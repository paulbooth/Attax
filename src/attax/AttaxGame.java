package attax;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class AttaxGame extends JFrame{
//	JScrollPane scrollPane;
//	Image image;
	@SuppressWarnings("deprecation")
	public AttaxGame(){
		super("Ataxx Attack!!");
//		icon = new ImageIcon("src/res/captain_canada.gif");
		 
//		JPanel panel = new JPanel()
//		{
//			protected void paintComponent(Graphics g)
//			{
//				//  Dispaly image at at full size
//				g.drawImage(icon.getImage(), 0, 0, null);
// 
//				//  Scale image to size of component
//				Dimension d = getSize();
//				g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
// 
//				//  Fix the image position in the scroll pane
//				Point p = scrollPane.getViewport().getViewPosition();
//				g.drawImage(icon.getImage(), p.x, p.y+55, null);
// 
//				super.paintComponent(g);
//			}
//		};
//		panel.setOpaque( false );
//		panel.setPreferredSize( new Dimension(AttaxGame.GRIDLENGTH*AttaxGame.SQUARESIZE,AttaxGame.GRIDHEIGHT*AttaxGame.SQUARESIZE) );
		
		JScrollPane scrollPane = new JScrollPane( new AttaxPanel() );
		if(AttaxPanel.DRAWBOARD){
		getContentPane().add( scrollPane );
		
		setSize(AttaxPanel.GRIDLENGTH*AttaxPanel.SQUARESIZE+100,AttaxPanel.GRIDHEIGHT*AttaxPanel.SQUARESIZE+100);
		setLocation((int)(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth()-getWidth())/2,
				(int)(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight()-getHeight())/2);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		toFront();
		setAlwaysOnTop(AttaxPanel.DRAWBOARD);
		setVisible(AttaxPanel.DRAWBOARD);
//		setResizable(false);
		pack();show();}
	}
	public static void main(String[] args) {
		new AttaxGame();
	}

}
