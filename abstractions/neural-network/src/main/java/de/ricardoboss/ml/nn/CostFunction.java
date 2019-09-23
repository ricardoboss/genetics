package de.ricardoboss.ml.nn;

public interface CostFunction {
    Matrix cost(Matrix expected, Matrix calculated);
}
