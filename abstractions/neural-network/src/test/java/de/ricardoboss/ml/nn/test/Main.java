package de.ricardoboss.ml.nn.test;

import de.ricardoboss.ml.nn.Matrix;
import de.ricardoboss.ml.nn.NeuralNetwork;

public class Main {
    public static void main(String[] args) {
        var nn = new NeuralNetwork(2, 1, 4, 1);
        var x = new Matrix(new double[][]{
                new double[]{ 1d, 2d },
                new double[]{ 4d, 5d },
                new double[]{ 7d, 8d },
                new double[]{ 3d, 9d },
        });

        System.out.println("X =");
        System.out.println(x);

        var y = new Matrix(new double[][]{
                new double[]{ 3d },
                new double[]{ 6d },
                new double[]{ 9d },
                new double[]{ 15d },
        });

        System.out.println("Y = ");
        System.out.println(y);


        var y_hat = nn.forward(x);

        System.out.println("y_hat =");
        System.out.println(y_hat);

        var e = nn.cost(x, y);

        System.out.println("e = ");
        System.out.println(e);

        // TODO: calculate list of matrices which correspond to the relative error of each weight in a given matrix of synapses
    }
}
