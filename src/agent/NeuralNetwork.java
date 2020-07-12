package agent;

import java.util.Arrays;

// Would it be better if we have a neuron class?
public class NeuralNetwork {
	private int[] layers;
	private double[][][] weights;
	private double[][] bias;
	private double[][] output;
	private double[][] errors;

	// NeuralNetwork can be initialized with different set of layers.
	// An input such as [10,5,3] means there are three layers with
	// 10, 5 and 3 neurons.
	public NeuralNetwork(int[] layers) {
		this.layers = layers;
		// For now, I wrote three different nested loops to construct weights, bias and
		// output
		// respectively, later on we can delete some of the loops to reduce the code.

		// [layer][neuron][weight]
		weights = new double[layers.length - 1][][];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = new double[layers[i]][];
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] = new double[layers[i + 1]];
				for (int n = 0; n < weights[i][j].length; n++) {
					weights[i][j][n] = Math.random(); // For now, assign all weights to double values between 0 and 1
				}
			}
		}
		bias = new double[layers.length - 1][];
		for (int i = 0; i < bias.length; i++) {
			bias[i] = new double[layers[i + 1]];
			for (int j = 0; j < bias[i].length; j++) {
				bias[i][j] = Math.random(); // For now, assign all biases to double values between 0 and 1
			}
		}
		errors = new double[layers.length][];
		output = new double[layers.length][];
		for (int i = 0; i < output.length; i++) {
			output[i] = new double[layers[i]]; // First layer outputs are going to be inputs
			errors[i] = new double[layers[i]]; // We are not going to assign first layer errors (since there cannot be).
		}
	}

	// Getting the overall output
	public double[] output(double[] input) {
		// First - Output[0] takes the input
		for (int i = 0; i < input.length; i++) {
			output[0][i] = input[i];
		}

		// Output array is going to be filled with the outputs of the neurons
		for (int i = 1; i < output.length; i++) {
			for (int j = 0; j < output[i].length; j++) {
				output[i][j] = sum(i, j);
			}
		}
		// returns the last layer's outputs
		return output[output.length - 1];

	}

	// Sigmoid function that is going to be used to shrink the output values
	private double sigmoid(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

	// Calculates the output of an individual neuron
	private double sum(int layer, int neuron) {
		double sum = bias[layer - 1][neuron];
		for (int i = 0; i < layers[layer - 1]; i++) {
			sum += (weights[layer - 1][i][neuron] * output[layer - 1][i]);
		}
		return sigmoid(sum);
	}

	// Constant is called "eta", learning rate that will optimize the
	// training phase.
	public void train(double[] input, double[] expected, double constant) {
		output(input); // So, outputs are ready to evaluate.
		error(expected);
		adjustment(constant);
		
	}

	private void error(double[] expected) {
		// If neuron is an output neuron
		for (int i = 0; i < layers[layers.length - 1]; i++) {
			// This is the function we need to use for outer layer neurons.
			errors[layers.length - 1][i] = (output[layers.length - 1][i] - expected[i]) * output[layers.length - 1][i]
					* (1 - output[layers.length - 1][i]);
		}
		// If neuron is an inner (hidden) neuron
		for (int i = layers.length - 2; i > 0; i--) {
			// neurons
			for (int j = 0; j < layers[i]; j++) {
				double err = 0;
				// next neurons
				for (int next = 0; next < layers[i + 1]; next++) {
					err += weights[i][j][next] * errors[i + 1][next];
				}
				errors[i][j] = err * output[i][j] * (1 - output[i][j]);
			}
		}
	}

	private void adjustment(double constant) {
		// layers
		for (int i = 1; i < layers.length; i++) {
			for (int j = 0; j < layers[i]; j++) {
				// Previous neurons
				for(int back=0; back<layers[i-1]; back++) {
					double difference = -constant * output[i-1][back] * errors[i][j];
					// update the weight
					weights[i-1][back][j] += difference; 
				}
				double diff_bias = -constant * errors[i][j];
				bias[i-1][j] += diff_bias;
			}
		}
	}

	// Code Testing - Debug
	public static void main(String[] args) {
		NeuralNetwork net = new NeuralNetwork(new int[] {4,3,3,6});
		
		// Training phase
		double[] input = new double[] {0.2, 0.4, 0.3, 0.9};
		double[] target = new double[] {1,0,0,0,0,0};
		
		long SetTime = System.nanoTime();
		for(int i=0; i<100; i++) {
			net.train(input, target, 0.3);
		}

		long EndTime = System.nanoTime();
		System.out.println("Execution time in milliseconds --> "+(EndTime-SetTime)/1000000);
		System.out.println(Arrays.toString(net.output(input)));
	}
}
