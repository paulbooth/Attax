package attax;

public class Strategy {
	public int playerNumber=0;
	public Strategy(){
		playerNumber=0+2;
	}
	public Strategy(int player){
		playerNumber=player+2;
	}
	public int[] getMove(AttaxBoard attaxBoard){
		java.util.Random rand=new java.util.Random();
		int x=rand.nextInt(AttaxPanel.GRIDLENGTH), y=rand.nextInt(AttaxPanel.GRIDHEIGHT);
		int[] move=attaxBoard.canGetTo(x,y,playerNumber);
		int maxNumTries=100;
    	int numTries=0;
		while (move==null&&numTries<maxNumTries)
		{	
			x=rand.nextInt(AttaxPanel.GRIDLENGTH);
			y=rand.nextInt(AttaxPanel.GRIDHEIGHT);
			move=attaxBoard.canGetTo(x,y,playerNumber);
    		numTries++;
//    		System.out.println(""+numTries+"\t"+(move==null));
		}
		if (numTries>=maxNumTries){
			int[][] moves=attaxBoard.availableMoves(playerNumber);
			if(moves.length==0) return null;
			int i=new java.util.Random().nextInt(moves.length);
			return new int[]{moves[i][0],moves[i][1],moves[i][2],moves[i][3],playerNumber};
		}else{
		if (move==null) return null;
		return new int[]{move[0],move[1],x,y,playerNumber};
		}
	}
}
