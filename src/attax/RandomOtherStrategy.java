package attax;

public class RandomOtherStrategy extends Strategy {

	public RandomOtherStrategy() {
	}

	public RandomOtherStrategy(int player) {
		super(player);
	}
	@Override
	public int[] getMove(AttaxBoard attaxBoard){
		switch(AttaxPanel.RNG.nextInt(5)){
		case 0: return new RandomStrategy(playerNumber-2).getMove(attaxBoard);
		case 1: return new SimpleGreedyStrategy(playerNumber-2).getMove(attaxBoard);
		case 2: return new GreedyStrategy(playerNumber-2).getMove(attaxBoard);
		case 3: return new MiniMaxStrategy(playerNumber-2,AttaxPanel.RNG.nextInt(1)+1).getMove(attaxBoard); 
		default: return new Strategy(playerNumber-2).getMove(attaxBoard);
		}
	}

}
