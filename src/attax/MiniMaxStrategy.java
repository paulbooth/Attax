package attax;

import java.util.ArrayList;

public class MiniMaxStrategy extends Strategy {
	int maxSearchDepth=1;
	public MiniMaxStrategy() {
	}

	public MiniMaxStrategy(int player) {
		super(player);
	}
	public MiniMaxStrategy(int player, int maxSearchDepth){
		super(player);
		this.maxSearchDepth=maxSearchDepth;
	}
	@Override
	public int[] getMove(AttaxBoard attaxBoard){
		int scoreGain=0;
		int[][] moves=attaxBoard.availableMoves(playerNumber);
		if (moves.length==0) return null;
		boolean othersCanMove=false;
		for(int i=2;i<AttaxPanel.numOfPlayers+2;i++){
			if (i!=playerNumber&&attaxBoard.availableMoves(i).length>0) othersCanMove=true;
		}
		if (!othersCanMove){
			do{
				attaxBoard.applyMove(new SimpleGreedyStrategy(playerNumber-2).getMove(attaxBoard));
			}while(attaxBoard.availableMoves(playerNumber).length!=0);
			return null;
		}
		int bestScore=-500;
		ArrayList<int[]> bestmoves=new ArrayList<int[]>(5);
		for (int mi=0;mi<moves.length;mi++)
		{
			//			System.out.println(""+mi+"/"+moves.length);
			//			System.out.println(attaxBoard);
			AttaxBoard newBoard=attaxBoard.clone();
			scoreGain=newBoard.applyMove(moves[mi][0],moves[mi][1],moves[mi][2],moves[mi][3], playerNumber);
			//			System.out.println("\n"+attaxBoard);
			int searchDepth=0;
			for(int i=playerNumber+1;searchDepth<maxSearchDepth&&!newBoard.checkGameOver();i++){
				if (i>=AttaxPanel.numOfPlayers+2) i=2;
				if (newBoard.availableMoves(i)==null) continue;

				//				System.out.println(""+i+"p:"+playerNumber);
				
				if (i==playerNumber) {
					searchDepth++;
					if (maxSearchDepth>1)
						scoreGain+=newBoard.applyMove(new MiniMaxStrategy(i-2,maxSearchDepth-1).getMove(newBoard));
					else
						scoreGain+=newBoard.applyMove(new GreedyStrategy(i-2).getMove(newBoard));
				}else{
					if (i==playerNumber-1||(playerNumber==2&&i==AttaxPanel.numOfPlayers+1))
						searchDepth++;
					scoreGain-=newBoard.applyMove(new GreedyStrategy(i-2).getMove(newBoard));
				}

			}
			if (scoreGain>bestScore){
				bestScore=scoreGain;
				bestmoves.clear();
				bestmoves.add(moves[mi]);
			}else if (scoreGain==bestScore) bestmoves.add(moves[mi]);

		}
		int randIndex=new java.util.Random().nextInt(bestmoves.size());
		//		System.out.println("b:"+bestmoves.get(randIndex)[0]+" "
		//				+bestmoves.get(randIndex)[1]+" "+bestmoves.get(randIndex)[2]+" "+bestmoves.get(randIndex)[3]);
		return 		new int[]{
				bestmoves.get(randIndex)[0],
				bestmoves.get(randIndex)[1],
				bestmoves.get(randIndex)[2],
				bestmoves.get(randIndex)[3],
				playerNumber};
	}


}
