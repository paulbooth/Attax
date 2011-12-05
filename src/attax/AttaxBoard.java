package attax;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

public class AttaxBoard {
	protected int[][] grid=new int[AttaxPanel.GRIDLENGTH][AttaxPanel.GRIDHEIGHT];
	public AttaxBoard(){
		initGrid();
	}
	public AttaxBoard(int[][]grid){
		this.grid=new int[grid.length][grid[0].length];
		for (int i=0;i<grid.length;i++)
			for(int j=0;j<grid[0].length;j++)
			this.grid[i][j]=grid[i][j];
	}
	public void initGrid(){
        for(int i=0;i<grid.length;i++)
        java.util.Arrays.fill(grid[i],0);
        for(int i=0;i<Math.min(AttaxPanel.NUMOFUNPASSABLE, AttaxPanel.GRIDHEIGHT*AttaxPanel.GRIDLENGTH-2);i++)
        	grid[AttaxPanel.RNG.nextInt(AttaxPanel.GRIDLENGTH)][AttaxPanel.RNG.nextInt(AttaxPanel.GRIDHEIGHT)]=1;
        	       
        if (AttaxPanel.STANDARDSTART){
        	grid[0][0]=2;
        	grid[AttaxPanel.GRIDLENGTH-1][AttaxPanel.GRIDHEIGHT-1]=2;
        	grid[0][AttaxPanel.GRIDHEIGHT-1]=3;
        	grid[AttaxPanel.GRIDLENGTH-1][0]=3;
        }
        for(int j=0;j<AttaxPanel.NUMRANDOMSTARTSPOTS;j++)
        for(int i=2;i<AttaxPanel.numOfPlayers+2;i++){
        	int x=0,y=0;
        	do{
        	x=AttaxPanel.RNG.nextInt(AttaxPanel.GRIDLENGTH);
        	y=AttaxPanel.RNG.nextInt(AttaxPanel.GRIDHEIGHT);
        	}while(grid[x][y]!=0);
        	grid[x][y]=i;
        }
	}
	public Graphics drawBoard(Graphics g){
		g.setColor(AttaxPanel.BACKGROUNDCOLOR);
		g.fillRect(0, 0, AttaxPanel.SQUARESIZE*AttaxPanel.GRIDLENGTH, AttaxPanel.SQUARESIZE*AttaxPanel.GRIDHEIGHT);
		for (int c=0;c<grid.length; c++ )
			for (int r=0;r<grid[1].length; r++ ){
				drawShape(g, c*AttaxPanel.SQUARESIZE, r*AttaxPanel.SQUARESIZE,grid[c][r]);
			}
		//LINES
		g.setColor(Color.black);
		for (int c=0;c<=grid.length; c++ )
			g.drawLine(c*AttaxPanel.SQUARESIZE,0,c*AttaxPanel.SQUARESIZE,AttaxPanel.SQUARESIZE*AttaxPanel.GRIDHEIGHT);
		for (int r=0;r<=grid[1].length; r++ )
			g.drawLine(0,r*AttaxPanel.SQUARESIZE,AttaxPanel.SQUARESIZE*AttaxPanel.GRIDLENGTH,r*AttaxPanel.SQUARESIZE);
		for(int i=1;i<3;i++){
			g.drawLine(-i,-i,-i,AttaxPanel.GRIDHEIGHT*AttaxPanel.SQUARESIZE+i);
			g.drawLine(AttaxPanel.GRIDLENGTH*AttaxPanel.SQUARESIZE+i,-i,AttaxPanel.GRIDLENGTH*AttaxPanel.SQUARESIZE+i,AttaxPanel.GRIDHEIGHT*AttaxPanel.SQUARESIZE+i);
			g.drawLine(-i,-i,AttaxPanel.GRIDLENGTH*AttaxPanel.SQUARESIZE+i,-i);
			g.drawLine(-i,AttaxPanel.GRIDHEIGHT*AttaxPanel.SQUARESIZE+i,AttaxPanel.GRIDLENGTH*AttaxPanel.SQUARESIZE+i,AttaxPanel.GRIDHEIGHT*AttaxPanel.SQUARESIZE+i);
		}
//		
//		if (AttaxPanel.gameOver){
//			g.setColor(Color.yellow);
//			int[] scores=getScores();
//		g.drawString("1:"+scores[0]+" 2:"+scores[1], 100, 100);
//		}
	return g;
	}
	
	public Graphics drawShape(Graphics g, int x, int y, int choice){
		return drawShape(g,x,y,AttaxPanel.SQUARESIZE,choice);
	}
	public Graphics drawShape(Graphics g, int x, int y, int r, int choice){
		r++;
		g.setColor(AttaxPanel.getColor(0));
		int w=Math.max((int)(r*.9), 3);
		g.fillRect(x+(r-w)/2,y+(r-w)/2,w, w);
		g.setColor(AttaxPanel.getColor(choice));
		if (choice<=1){
			if (choice!=0)
			g.fillRect(x,y,r, r);
		}
		else
		{
			g.fillOval(x+(r-w)/2,y+(r-w)/2,w, w);
			
			int littleRadius=w*14/40;
			if(littleRadius>1){
				g.setColor(g.getColor().darker());
				g.fillOval(x+r/2-littleRadius,y+r/2-littleRadius,littleRadius*2,littleRadius*2);

				g.setColor(new Color(255-AttaxPanel.getColor(choice).getRed(), 
						255-AttaxPanel.getColor(choice).getGreen(),
						255-AttaxPanel.getColor(choice).getBlue()
						));
			g.drawOval(x+r/2-littleRadius,y+r/2-littleRadius,littleRadius*2,littleRadius*2);
			}
		}
		return g;
	}
	public int applyMove(int x1, int y1, int x2, int y2, int player){
//		System.out.println("trying to apply from x:"+x1+" y:"+y1+" to x:"+x2+" y:"+y2);
		if (!canMove(x1,y1,x2,y2,player)) return -1;
//		System.out.println("Can move...");
		int change=1;
		if (Math.abs(x2-x1)==2||Math.abs(y2-y1)==2){
			grid[x1][y1]=0;
			change--;
		}
		
		if(player>1)
		for (int i=Math.max(x2-1,0);i<=Math.min(x2+1,AttaxPanel.GRIDLENGTH-1);i++)
			for(int j=Math.max(y2-1,0);j<=Math.min(y2+1,AttaxPanel.GRIDHEIGHT-1);j++)
				if (grid[i][j]>1&& grid[i][j]!=player) {grid[i][j]=player;change++;}
		
		grid[x2][y2]=player;
		return change;
	}
	public int applyMove(int[] move){
		if(move==null||move.length<5) return -1;
		else return applyMove(move[0],move[1],move[2],move[3],move[4]);
	}
//	public AttaxBoard applyMoveToAnotherBoard(int x1, int y1, int x2, int y2, int player){
////		System.out.println("trying to apply from x:"+x1+" y:"+y1+" to x:"+x2+" y:"+y2);
//		
//		if (!canMove(x1,y1,x2,y2,player)) return new AttaxBoard(grid);
////		System.out.println("Can move...");
//		int[][] newgrid =new int[this.grid.length][this.grid[0].length];
//		for (int i=0;i<grid.length;i++)
//			for(int j=0;j<grid[0].length;j++)
//			newgrid[i][j]=grid[i][j];
//		
//		if (Math.abs(x2-x1)==2||Math.abs(y2-y1)==2) newgrid[x1][y1]=0;
//		
//		for (int i=Math.max(x2-1,0);i<=Math.min(x2+1,AttaxPanel.GRIDLENGTH-1);i++)
//			for(int j=Math.max(y2-1,0);j<=Math.min(y2+1,AttaxPanel.GRIDHEIGHT-1);j++)
//				if (newgrid[i][j]>1) newgrid[i][j]=player;
//		
//		newgrid[x2][y2]=player;
//		return new AttaxBoard(newgrid);
//	}
//	public AttaxBoard applyMoveToAnotherBoard(int[] move){
//		if(move==null||move.length<5) return new AttaxBoard(grid.clone());
//		else return applyMoveToAnotherBoard(move[0],move[1],move[2],move[3],move[4]);
//	}
//	
	public boolean canMove(int x1, int y1, int x2, int y2, int player){
		if (x1<x2-2 ||x1>x2+2||y1<y2-2||y1>y2+2||
				x2>=AttaxPanel.GRIDLENGTH||y2>=AttaxPanel.GRIDHEIGHT||x2<0||y2<0||
				x1>=AttaxPanel.GRIDLENGTH||y1>=AttaxPanel.GRIDHEIGHT||x1<0||y1<0)
			return false;
		if (grid[x2][y2]!=0||grid[x1][y1]!=player) return false;
		return true;
	}
	public int getSpot(int x, int y){ return grid[x][y];}
	public int[] canGetTo(int x,int y, int player){
		int[] f = null;
		f=canSplitTo(x,y,player);
		if (f!=null) return f;
		return canJumpTo(x,y,player);
	}
	public int[] canSplitTo(int x, int y, int player){
		for (int i=Math.max(x-1,0);i<=Math.min(x+1,AttaxPanel.GRIDLENGTH-1);i++)
			for(int j=Math.max(y-1,0);j<=Math.min(y+1,AttaxPanel.GRIDHEIGHT-1);j++)
				if ((i!=x||j!=y)&& grid[i][j]==player)
				{
					
					return new int[]{i,j};
				}
		return null;
	}
	
	public int[] canJumpTo(int x, int y, int player){
		for (int i=Math.max(x-2,0);i<=Math.min(x+2,AttaxPanel.GRIDLENGTH-1);i++)
			for(int j=Math.max(y-2,0);j<=Math.min(y+2,AttaxPanel.GRIDHEIGHT-1);j++)
				if ((Math.abs(x-i)>1||Math.abs(y-j)>1)&&grid[i][j]==player)
				{
					return new int[]{i,j};
				}
		return null;
	}
	
	public int[][] getGrid() {
		return grid;
	}
	public void setGrid(int[][] grid) {
		this.grid = grid;
	}
	public boolean checkGameOver(){
		boolean foundEmpty=false;
		for (int i=0;i<AttaxPanel.GRIDLENGTH;i++)
			for(int j=0;j<AttaxPanel.GRIDHEIGHT;j++){
				if (grid[i][j]==0){
					foundEmpty=true;
					break;
				}
			}
		return !foundEmpty;
	}
	public int[] getScores(){
		int[] scores = new int[AttaxPanel.numOfPlayers];
		for (int i=0;i<AttaxPanel.GRIDLENGTH;i++)
			for(int j=0;j<AttaxPanel.GRIDHEIGHT;j++){
				if (grid[i][j]-2>=0)
					scores[grid[i][j]-2]++;
			}
		return scores;
	}
	public int[][] availableMoves(int player){
		ArrayList<int[]> moves=new ArrayList<int[]>(10);
		for (int x=0;x<AttaxPanel.GRIDLENGTH;x++)
			for(int y=0;y<AttaxPanel.GRIDHEIGHT;y++){
				if (grid[x][y]==player){
					for (int i=Math.max(x-2,0);i<=Math.min(x+2,AttaxPanel.GRIDLENGTH-1);i++)
						for(int j=Math.max(y-2,0);j<=Math.min(y+2,AttaxPanel.GRIDHEIGHT-1);j++)
							if ( grid[i][j]==0)
							{
								moves.add(new int[]{x,y,i,j});
							}
				}
			}
		int[][] movesarray=new int[moves.size()][4];
		for(int i=0;i<moves.size();i++)
			movesarray[i]=moves.get(i);
		return movesarray;
	}
	public int valueAdded(int[] move, int player){
		int x=move[2], y=move[3];
		int plus=1;
		for (int i=Math.max(x-1,0);i<=Math.min(x+1,AttaxPanel.GRIDLENGTH-1);i++)
			for(int j=Math.max(y-1,0);j<=Math.min(y+1,AttaxPanel.GRIDHEIGHT-1);j++)
				if (grid[i][j]>1&&grid[i][j]!=player) plus++;
		if ((Math.abs(x-move[0])>1||Math.abs(y-move[1])>1))
			plus--;
		return plus;
	}
	public int friendlyAround(int x, int y, int player){
		int friends=0;
		for (int i=Math.max(x-1,0);i<=Math.min(x+1,AttaxPanel.GRIDLENGTH-1);i++)
			for(int j=Math.max(y-1,0);j<=Math.min(y+1,AttaxPanel.GRIDHEIGHT-1);j++)
				if (grid[i][j]==player) friends++;
		return friends;
	}
	public String toString(){
		String s="";
		for(int i=0;i<grid.length;i++){
			for(int j=0;j<grid[0].length;j++)
				s+=grid[i][j];
			s+="\n";
		}
		return s;
	}
	public AttaxBoard clone(){
		return new AttaxBoard(grid);
	}
	
}
