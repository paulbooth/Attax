package attax;

import java.util.ArrayList;

public class SimpleGreedyStrategy extends Strategy {

	public SimpleGreedyStrategy() {
	}

	public SimpleGreedyStrategy(int player) {
		super(player);
	}
	@Override
	public int[] getMove(AttaxBoard attaxBoard){
		int[][] moves=attaxBoard.availableMoves(playerNumber);
		if (moves.length==0) return null;
		ArrayList<int[]> bestmoves=new ArrayList<int[]>(5);
		int bestplus=0;
		for (int mi=0;mi<moves.length;mi++)
		{
//			System.out.println("move #"+mi+" from x:"+moves[mi][0]+" y:"+moves[mi][1]+" to x:"+moves[mi][2]+" y:"+moves[mi][3]);
			int tempplus=attaxBoard.valueAdded(moves[mi], playerNumber);
			if(tempplus>bestplus)
			{
				bestmoves.clear();
				bestmoves.add(moves[mi]);
				bestplus=tempplus;
			}else if(tempplus==bestplus){
				bestmoves.add(moves[mi]);
			}
		}
		int randIndex=new java.util.Random().nextInt(bestmoves.size());
		return new int[]{
				bestmoves.get(randIndex)[0],
				bestmoves.get(randIndex)[1],
				bestmoves.get(randIndex)[2],
				bestmoves.get(randIndex)[3],
				playerNumber};
	}

}
