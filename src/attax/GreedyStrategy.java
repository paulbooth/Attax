package attax;

import java.util.ArrayList;

public class GreedyStrategy extends Strategy {

	public GreedyStrategy() {
	}

	public GreedyStrategy(int player) {
		super(player);
	}
	@Override
	public int[] getMove(AttaxBoard attaxBoard){
		int[][] moves=attaxBoard.availableMoves(playerNumber);
		if (moves.length==0) return null;
		ArrayList<int[]> bestmoves=new ArrayList<int[]>(5);
		float bestplus=-900;
		for (int mi=0;mi<moves.length;mi++)
		{
//			System.out.println("move #"+mi+" from x:"+moves[mi][0]+" y:"+moves[mi][1]+" to x:"+moves[mi][2]+" y:"+moves[mi][3]);
			float tempplus=attaxBoard.valueAdded(moves[mi], playerNumber)
			-((Math.abs(moves[mi][0]-moves[mi][2])>1||Math.abs(moves[mi][1]-moves[mi][3])>1)?attaxBoard.friendlyAround(moves[mi][0], moves[mi][1], playerNumber)/5f:0);
			if(tempplus>bestplus)
			{
				bestmoves.clear();
				bestmoves.add(moves[mi]);
				bestplus=tempplus;
			}else if(tempplus==bestplus){
				bestmoves.add(moves[mi]);
			}
		}
		if(bestmoves.size()==1)
			return new int[]{
					bestmoves.get(0)[0],
					bestmoves.get(0)[1],
					bestmoves.get(0)[2],
					bestmoves.get(0)[3],
					playerNumber};
		
		int bestfriends=attaxBoard.friendlyAround(bestmoves.get(0)[2], bestmoves.get(0)[3], playerNumber);
		
		ArrayList<int[]> superbestmoves=new ArrayList<int[]>(bestmoves.size());
		superbestmoves.add(bestmoves.get(0));
		for (int i=1;i<bestmoves.size();i++){
			int tempfriends=attaxBoard.friendlyAround(bestmoves.get(i)[2],bestmoves.get(i)[3], playerNumber);
			if(tempfriends>bestfriends)
			{
				superbestmoves.clear();
				superbestmoves.add(bestmoves.get(i));
				bestfriends=tempfriends;
			}else if(tempfriends==bestfriends){
				superbestmoves.add(bestmoves.get(i));
			}
		}
		
		int randIndex=AttaxPanel.RNG.nextInt(superbestmoves.size());
		return new int[]{
				superbestmoves.get(randIndex)[0],
				superbestmoves.get(randIndex)[1],
				superbestmoves.get(randIndex)[2],
				superbestmoves.get(randIndex)[3],
				playerNumber};
	}

}
