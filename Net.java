import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Net {
    public int[] layers;
    public List<double[][]> weights;
    public List<double[]> biases;

    // Default Constructor: Creates a random brain
    public Net() {
        this(new int[]{10, 32, 64, 16, 3}, null, null); //x, y, energy, dist to food, angle from food, (speed, vision, size, metabolism) -> movement vector (x, y), reproduce?
    }

    // Parametrized Constructor: Used for creating specific nets or copies
    public Net(int[] layers, List<double[][]> weights, List<double[]> biases) {
        this.layers = layers;
        this.weights = (weights != null) ? weights : new ArrayList<>();
        this.biases = (biases != null) ? biases : new ArrayList<>();

        if (weights == null) {
            // Initialize random weights between -1 and 1
            for (int i = 0; i < layers.length - 1; i++) {
                double[][] layerWeights = new double[layers[i]][layers[i+1]];
                for (int r = 0; r < layers[i]; r++) {
                    for (int c = 0; c < layers[i+1]; c++) {
                        layerWeights[r][c] = ThreadLocalRandom.current().nextDouble(-1, 1);
                    }
                }
                this.weights.add(layerWeights);
            }
        }

        if (biases == null) {
            // Initialize random biases
            for (int i = 1; i < layers.length; i++) {
                double[] layerBiases = new double[layers[i]];
                for (int j = 0; j < layers[i]; j++) {
                    layerBiases[j] = ThreadLocalRandom.current().nextDouble(-1, 1);
                }
                this.biases.add(layerBiases);
            }
        }
    }

    // Forward propagation: Input -> Output 
    public double[] forward(double[] input) {
        double[] current = input;

        for (int i = 0; i < weights.size(); i++) {
            boolean isOutputLayer = (i == weights.size() - 1);
            current = feedForwardLayer(current, weights.get(i), biases.get(i), isOutputLayer);
        }
        return current;
    }

    private double[] feedForwardLayer(double[] input, double[][] weightMap, double[] biasMap, boolean isOutputLayer) {
        double[] output = new double[weightMap[0].length];

        for (int col = 0; col < weightMap[0].length; col++) {
            double sum = 0;

            for (int row = 0; row < weightMap.length; row++) {
                sum += input[row] * weightMap[row][col];
            } 

            double value = sum + biasMap[col];

            if (!isOutputLayer) {
                // Hidden layers → tanh
                output[col] = tanh(value);
            } else { 
                // Output layer → mixed behavior
                if (col < 2) {
                    // movement (x, y)
                    output[col] = tanh(value);
                } else {
                    // reproduce
                    output[col] = sigmoid(value);
                }
            } 
        }
        return output; 
    } 

    

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    } 

    private double tanh(double x) {
        return Math.tanh(x); 
    }

    // Mutation: Returns a slightly modified version of this brain
    public Net mutate(double strength) {
        List<double[][]> newWeights = new ArrayList<>();
        List<double[]> newBiases = new ArrayList<>();

        for (double[][] weightLayer : weights) {
            double[][] newLayer = new double[weightLayer.length][weightLayer[0].length];
            for (int r = 0; r < weightLayer.length; r++) {
                for (int c = 0; c < weightLayer[0].length; c++) {
                    // Randomly nudge the weight based on mutation strength
                    if (ThreadLocalRandom.current().nextDouble() < 0.1) {
                        double nudge = ThreadLocalRandom.current().nextGaussian() * strength;
                        newLayer[r][c] = weightLayer[r][c] + nudge;
                    } else {
                        newLayer[r][c] = weightLayer[r][c];
                    }
                }
            }
            newWeights.add(newLayer);
        }

        for (double[] biasLayer : biases) {
            double[] newLayer = new double[biasLayer.length];
            for (int i = 0; i < biasLayer.length; i++) {
                double nudge = ThreadLocalRandom.current().nextGaussian() * strength;
                newLayer[i] = biasLayer[i] + nudge;
            }
            newBiases.add(newLayer);
        }

        return new Net(this.layers.clone(), newWeights, newBiases);
    }
}