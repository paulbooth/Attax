package attax;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PlayerStrategy extends Strategy {
	AttaxBoard attaxBoard;
	int[] click1=null,click2=null;
	boolean myTurn=false;

	public PlayerStrategy() {
		super();
	}
	public PlayerStrategy(int player){
		super(player);
	}
	@Override
	public int[] getMove(AttaxBoard attaxBoard){
		this.attaxBoard=attaxBoard;
		if(attaxBoard.availableMoves(playerNumber).length==0) return null;
		boolean othersCanMove=false;
		for(int i=2;i<AttaxPanel.numOfPlayers+2;i++){
			if (i!=playerNumber&&attaxBoard.availableMoves(i).length>0) othersCanMove=true;
		}
		if (!othersCanMove){
			do{
				attaxBoard.applyMove(new GreedyStrategy(playerNumber-2).getMove(attaxBoard));
			}while(attaxBoard.availableMoves(playerNumber).length!=0);
			return null;
		}
		click1=null;
		click2=null;
		myTurn=true;
		while(click2==null){
			try{
				Thread.sleep(10);
			}catch(Exception e){};
		}
		myTurn=false;
		return new int[]{click1[0],click1[1],click2[0],click2[1],playerNumber};
	}
	public void clicked(MouseEvent me){
		if (!myTurn) return;
		int x=(int)((me.getX()-AttaxPanel.offsetx)/AttaxPanel.SQUARESIZE);
		int y=(int)((me.getY()-AttaxPanel.offsety)/AttaxPanel.SQUARESIZE);
//		System.out.println("clicked x:"+x+" y:"+y);
		if (attaxBoard.getSpot(x, y)==playerNumber)
		{
			click1=new int[]{x,y};
			return;
		}
		if(attaxBoard.getSpot(x, y)==0){
			if(click1!=null)
			click2=new int[]{x,y};
			else{
				
				click1=attaxBoard.canSplitTo(x, y, playerNumber);
				if(click1!=null)
					click2=new int[]{x,y};
					
			}
			if (click2!=null&&!attaxBoard.canMove(click1[0],click1[1],click2[0],click2[1],playerNumber)){
				click2=null;
				click1=null;
			}
		}
		
	}

}
