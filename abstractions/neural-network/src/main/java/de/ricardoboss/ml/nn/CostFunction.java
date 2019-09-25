package de.ricardoboss.ml.nn;

public interface CostFunction {
    Matrix calculate(Matrix expected, Matrix calculated);
}
