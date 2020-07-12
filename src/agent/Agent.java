package agent;

// Each agent has its own neural network, their duty is to
// get a decision from them and train them according to the
// expected output. Each frame, a new agent is going to be initialized
// under the simulation package.
public class Agent {
	private NeuralNetwork net;
	private int decision;
	private final double constant = 0.3;    // Constant may be changed.
	
	// Initializes a new agent with neural network and output array
	public Agent(double[] input, int[] layers, double[] expected) {
		net = new NeuralNetwork(layers);
		decision = getDecision(net.output(input));
		// Instead of calling a new method for training,
		// I thought it would be more efficient to call training
		// method under constructor. (So every time an agent is created
		// it's going to train its neural network automatically)
		net.train(input, expected, constant);
	}
	// Returns the decision (index of the neuron who returned the
	// highest output)
	private int getDecision(double[] output) {
		int decision = 0;
		for(int i=0; i<output.length; i++) {
			if(output[decision]<output[i]) {
				decision = i;
			}
		}
		// It can be modified so that instead of returning index,
		// it can return the movement (left,right etc.) directly.
		// In order to do that, movements should be declared under 
		// a class.
		return decision;
	}
	// getter for decision, will be used in simulation.
	public int decision() {
		return decision;
	}
	
}
