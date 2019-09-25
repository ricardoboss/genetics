package de.ricardoboss.ml.nn;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class NeuralNetwork {
    private static final ActivationFunction SIGMOID = z -> z.apply(val -> 1d / (1d + Math.exp(-val)));
    private static final CostFunction HALF_SQUARES = (expected, calculated) ->
            expected.sub(calculated).apply(val -> 0.5d * Math.pow(val, 2));
    private static final CostFunctionPrime SIGMOID_PRIME = z ->
            z.apply(val -> Math.exp(-val) / Math.pow(1 + Math.exp(-val), 2));

    private final List<Matrix> weights;
    private final ActivationFunction activationFunction;
    private final CostFunction costFunction = HALF_SQUARES;

    public NeuralNetwork(List<Matrix> weights)
    {
        this(weights, SIGMOID);
    }

    public NeuralNetwork(List<Matrix> weights, ActivationFunction f)
    {
        this.weights = weights;
        this.activationFunction = f;
    }

    public NeuralNetwork(int numInputNeurons, int numOutputNeurons, int numHiddenNeurons, int numHiddenLayers)
    {
        this(numInputNeurons, numOutputNeurons, numHiddenNeurons, numHiddenLayers, SIGMOID);
    }

    public NeuralNetwork(int numInputNeurons, int numOutputNeurons, int numHiddenNeurons, int numHiddenLayers, ActivationFunction f)
    {
        this.activationFunction = f;

        weights = new ArrayList<>(numHiddenLayers + 1);
        weights.add(Matrix.random(numInputNeurons, numHiddenNeurons));
        for (int i = 0; i < numHiddenLayers - 1; i++)
            weights.add(Matrix.random(numHiddenNeurons, numHiddenNeurons));
        weights.add(Matrix.random(numHiddenNeurons, numOutputNeurons));
    }

    public Matrix forward(Matrix x)
    {
        if (x.cols != weights.get(0).rows)
            throw new IllegalArgumentException("Invalid number of inputs for x!");

        Matrix a = x;
        for (var w : weights)
            a = activationFunction.apply(a.dot(w));

        return a;
    }

    public Matrix cost(Matrix x, Matrix y)
    {
        var y_hat = forward(x);

        return costFunction.calculate(y, y_hat);
    }

    public List<Matrix> export()
    {
        var copy = new ArrayList<Matrix>();

        weights.forEach(w -> copy.add(w.clone()));

        return copy;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public NeuralNetwork clone()
    {
        return new NeuralNetwork(this.export(), activationFunction);
    }
}
