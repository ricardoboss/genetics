package de.ricardoboss.ml.genetics;

public interface FitnessCalculator<G> {
    float calculate(DNA<G> dna);
}
