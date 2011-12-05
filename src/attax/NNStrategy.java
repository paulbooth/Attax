package attax;

import java.util.ArrayList;

public class NNStrategy extends Strategy {
	InputNeuron[] inputNeurons;
	Neuron[][] hiddenNeurons;
	Neuron[] outputNeurons;
	Connection[] connections;
	double learningConstant=.001;
	public NNStrategy() {
	}

	public NNStrategy(int player) {
		super(player);
		inputNeurons=new InputNeuron[AttaxPanel.GRIDHEIGHT*AttaxPanel.GRIDLENGTH];
		hiddenNeurons=new Neuron[1][3];
		outputNeurons=new Neuron[AttaxPanel.GRIDHEIGHT*AttaxPanel.GRIDLENGTH];
		for(int i=0;i<inputNeurons.length;i++)
			inputNeurons[i]=new InputNeuron();
		for(int i=0;i<hiddenNeurons.length;i++)
			for(int j=0;j<hiddenNeurons[0].length;j++)
				hiddenNeurons[i][j]=new Neuron();
		for (int i=0;i<outputNeurons.length;i++)
			outputNeurons[i]=new Neuron();
		connections=new Connection[inputNeurons.length];

	}
	public NNStrategy(int player, int MNeurons){
		super(player);
	}
	public NNStrategy(int player, int MNeurons, int MNeuronRows){
		super(player);
	}

	@Override
	public int[] getMove(AttaxBoard attaxBoard){
		return null;
	}
	public int[] feedForward(AttaxBoard attaxBoard){
		for (int x=0;x<AttaxPanel.GRIDLENGTH;x++)
			for(int y=0;y<AttaxPanel.GRIDHEIGHT;y++)
				inputNeurons[x+y*AttaxPanel.GRIDLENGTH].giveInput(attaxBoard.getSpot(x, y));
		for (int i=0;i<hiddenNeurons.length;i++)
			for(int j=0;j<hiddenNeurons[0].length;j++){
				hiddenNeurons[i][j].calcOutput();
			}
		double[][] finalOutputs=new double[outputNeurons.length][3];
		double[] bestOut=new double[]{-1,-1,-1};
		for (int i=0;i<outputNeurons.length;i++) {
			finalOutputs[i][0]=i %AttaxPanel.GRIDLENGTH;
			finalOutputs[i][1]=i/AttaxPanel.GRIDLENGTH;
			finalOutputs[i][2]=outputNeurons[i].calcOutput();
			if (finalOutputs[i][2]>bestOut[2]){
				bestOut=finalOutputs[i].clone();
			}
		}
		
		return null;
	}
	private class Neuron{
		public double output=1, biasWeight=1;
		public ArrayList<Connection> froms=new ArrayList<Connection>(3), tos=new ArrayList<Connection>(4);
		public void addFromConnection(Neuron from){
			froms.add(new Connection(from,this));
		}
		public void addToConnection(Neuron to){
			tos.add(new Connection(this,to));
		}
		public double  calcOutput(){
			double sum=0;
			for (Connection fromc:froms)
				sum+=fromc.from.getOutput()*fromc.weight;
			sum+=biasWeight;
			output=g(sum);
			return output;
		}
		public double getOutput(){ return output;}
		public double g(double x){ return 1/(1+Math.exp(-x));}
	}
	private class InputNeuron extends Neuron{
		double[] outmap=new double[AttaxPanel.numOfPlayers+2];
		public InputNeuron(){
			for (int i=0;i<outmap.length;i++)
				outmap[i]=AttaxPanel.RNG.nextDouble();

		}
		@Override
		public double calcOutput(){
			return output;
		}
		public void giveInput(int input ){
			output=outmap[input];
		}
	}
	private class Connection{
		private Neuron from=null, to=null;
		double weight=1;
		public Connection(Neuron from, Neuron to){
			this.from=from;
			this.to=to;
			weight=AttaxPanel.RNG.nextDouble()*2-1;
		}
		public Connection(Neuron from, Neuron to, double weight){
			this.from=from;
			this.to=to;
			this.weight=weight;
		}

		public Neuron getFrom() {return from;}
		public void setFrom(Neuron from) {this.from = from;}
		public Neuron getTo() {	return to;}
		public void setTo(Neuron to) {this.to = to;}
		public double getWeight() {	return weight;}
		public void setWeight(double weight) {this.weight = weight;}

	}
}
