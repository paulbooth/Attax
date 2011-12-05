package attax;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class AttaxPanel extends JPanel{
	static public AttaxBoard attaxBoard;
	static public boolean gameOver=false;

	static public Strategy[] strategies={new GreedyStrategy(0), new SimpleGreedyStrategy(1), new SimpleGreedyStrategy(2), new RandomStrategy(3)};
	static protected int turn=0, numOfPlayers=2;

	static protected int offsetx=50,offsety=50;
	static final private int TURNWAIT=0, GAMEENDWAIT=0;
	static final public int NUMOFUNPASSABLE=0, NUMRANDOMSTARTSPOTS=0;
	static final public boolean STANDARDSTART=true;

	static public java.util.Random RNG=new java.util.Random();
	static public int[] totalScores=new int[numOfPlayers];
	final static int GRIDHEIGHT=100, GRIDLENGTH=100;
	final static int SQUARESIZE=3;

	final static boolean DRAWBOARD=true;
	final static Color BACKGROUNDCOLOR=new Color(10,90,10,100);
	ImageIcon backgroundImage=new ImageIcon("src/res/Ataxx.JPG");
	static Color[] PLAYERCOLORS={new Color(20,110,30,100),Color.black, 
		new Color(10,200,60),
		new Color(0,0,0),
		new Color(RNG.nextInt(255),RNG.nextInt(255),RNG.nextInt(255))
	,
	new Color(RNG.nextInt(255),200,80), 
	new Color(RNG.nextInt(255),RNG.nextInt(255),RNG.nextInt(255))
	};

	public AttaxPanel(){
		super();
		if(AttaxPanel.DRAWBOARD){
			setPreferredSize( new Dimension(
					AttaxPanel.GRIDLENGTH*AttaxPanel.SQUARESIZE+100,AttaxPanel.GRIDHEIGHT*AttaxPanel.SQUARESIZE+100) );
			requestFocus();
			repaint();
		}
		attaxBoard=new AttaxBoard();
		java.util.Arrays.fill(totalScores,0);
		if(numOfPlayers>strategies.length){
			Strategy[] newstrats=new Strategy[numOfPlayers];
			for(int i=0;i<strategies.length;i++){
				newstrats[i]=strategies[i];
			}
			for(int j=strategies.length;j<numOfPlayers;j++)
				newstrats[j]=new RandomOtherStrategy(j);
			strategies=newstrats;
		}

		boolean foundPlayerControlled=false;
		for(int i=0;i<numOfPlayers;i++)
			if (strategies[i] instanceof PlayerStrategy)
			{
				foundPlayerControlled=true;
				break;
			}
		if(foundPlayerControlled)
			addMouseListener(new MouseAdapter(){
				public void mousePressed(MouseEvent me){
					for(int i=0;i<numOfPlayers;i++)
						if (strategies[i] instanceof PlayerStrategy)
							((PlayerStrategy)strategies[i]).clicked(me);
				}
			});
		//        final long start=System.currentTimeMillis();
		//        System.out.println(start);
		Thread updater= new Thread()
		{ 	        
			public void run(){
				turn=AttaxPanel.RNG.nextInt(numOfPlayers);
				while(true){

					if( !gameOver) {
						//	while (!turnOver);

						//        	        	System.out.println( Runtime.getRuntime().freeMemory());
						attaxBoard.applyMove(strategies[turn].getMove(AttaxPanel.attaxBoard));
						//        	        		System.out.println("NOPPE"+turn);


						repaint();
						turn++;
						if (turn==numOfPlayers) turn=0;
						gameOver=attaxBoard.checkGameOver();        	        
						//        	            
						if (TURNWAIT>0){
							try{Thread.sleep(TURNWAIT);}catch(Exception ex){};
						}
					}
					else{
						int[] singleRoundScores=attaxBoard.getScores();
						System.out.println();
						for(int i=0;i<totalScores.length;i++){
							totalScores[i]+=singleRoundScores[i];
							System.out.print("t"+i+":"+totalScores[i]+"    s"+i+":"+singleRoundScores[i]+"   ");
						}
						System.out.print(""+((double)totalScores[0]/totalScores[1]));

						System.out.println("\n");
						//        	        	System.out.println(System.currentTimeMillis()-start);
						repaint();
						gameOver=false;

						try{Thread.sleep(GAMEENDWAIT); 	}catch(Exception ex){}
						attaxBoard=new AttaxBoard();
					}}
			}
		}; updater.start();
		repaint();

	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (!AttaxPanel.DRAWBOARD) return;
		int w=getSize().width, h=getSize().height;
		//		System.out.println(""+w+getSize().width+" h"+h);
		offsetx=(w-AttaxPanel.SQUARESIZE*AttaxPanel.GRIDLENGTH)/2;
		offsety=(h-AttaxPanel.SQUARESIZE*AttaxPanel.GRIDHEIGHT)/2;
		g.drawImage(backgroundImage.getImage(),0,0,w,h,null);


		g.translate(w/2+85, 0);
		g.setColor(AttaxPanel.BACKGROUNDCOLOR);
		g.fillRect(0, 0, 46,46);
		g.setColor(Color.black);
		g.drawRect(0,0,46,46);
		g.drawLine(0, 16, 46, 16);
		g.setColor(new Color(255-AttaxPanel.BACKGROUNDCOLOR.getRed(), 
				255-AttaxPanel.BACKGROUNDCOLOR.getGreen(),
				255-AttaxPanel.BACKGROUNDCOLOR.getBlue()
		));		
		g.drawString("TURN:"+(turn+1),1,14);
		attaxBoard.drawShape(g, 7, 15,32, turn+2);
		g.translate(-w/2-85+offsetx, offsety);

		attaxBoard.drawBoard(g);
		g.translate(-offsetx+w/2-75,-offsety);
		g.setColor(new Color(RNG.nextInt(50),200+RNG.nextInt(50),RNG.nextInt(50)));
		g.setFont(new Font("sansserif",Font.BOLD,60));
		g.drawString("Ataxx",0,45);
		g.translate(-w/2+75,0);
		g.setColor(new Color(255,255,255,200));
		g.fillRect(0,0,w/2-75,47);
		g.setColor(Color.black);
		g.drawRect(0,0,w/2-75,47);
		g.setFont(new Font("timesnewroman",Font.PLAIN,14));
		int[] singleRoundScores=attaxBoard.getScores();
		for (int i=0;i<numOfPlayers;i++){
			g.drawString("Player #"+(i+1),i*65,14);
			g.drawString(""+singleRoundScores[i],20+65*i,30);
			//if(gameOver)
			g.drawString(""+totalScores[i],20+65*i,45);
		}

	}

	static Color getColor(int choice){
		if (choice<PLAYERCOLORS.length)
			return PLAYERCOLORS[choice];
		else{
			Color[] newcolors=new Color[PLAYERCOLORS.length];
			for(int i=0;i<PLAYERCOLORS.length;i++){
				newcolors[i]=PLAYERCOLORS[i];
			}
			for(int i=PLAYERCOLORS.length;i<newcolors.length;i++)
				newcolors[i]=new Color(RNG.nextInt(255),RNG.nextInt(255),RNG.nextInt(255));
			PLAYERCOLORS=newcolors;
		}
		return 	new Color(RNG.nextInt(255),RNG.nextInt(255),RNG.nextInt(255));
	}
}
