package attax;

public class RandomStrategy extends Strategy {
	
	
	public RandomStrategy() {
		super();
	}

	public RandomStrategy(int player) {
		super(player);
	}
	@Override
	public int[] getMove(AttaxBoard attaxBoard){
		int[][] moves=attaxBoard.availableMoves(playerNumber);
		if (moves.length==0)return null;
		else{
			int i=new java.util.Random().nextInt(moves.length);
			return new int[]{moves[i][0],moves[i][1],moves[i][2],moves[i][3],playerNumber};
		}
	}
}
