package de.ricardoboss.ml.genetics.test;

import de.ricardoboss.ml.genetics.DNA;
import de.ricardoboss.ml.genetics.Pool;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        final var populationSize = 1000;

        final var fitnessThreshold = 0.1f;
        final var mutationProbability = 0.03f;

        final var survivorRatio = 0.4f;
        final var pairingRatio = 0.5f;

        final var targetFitness = 1f;
        final var target = "To be or not to be, that is the question.";

        var pool = new Pool<>(
                () -> new DNA<>(new CharGene.Generator(), target.length()),
                populationSize,
                dna -> {
                    var fitness = 0f;
                    for (int i = 0; i < dna.getGenes().size(); i++)
                        if (dna.getGenes().get(i).getValue().compareTo(target.charAt(i)) == 0)
                            fitness++;

                    return fitness / (float) target.length();
                });

        final var start = System.currentTimeMillis();
        var fitness = pool.calculateFitness();
        Map.Entry<DNA<Character>, Float> max;

        do {
            final var generationStart = System.currentTimeMillis();
            pool.advanceGeneration(fitnessThreshold, survivorRatio, pairingRatio, mutationProbability);
            fitness = pool.calculateFitness();

            max = null;
            for (final var e : fitness.entrySet())
                if (max == null || max.getValue() < e.getValue()) {
                    max = e;
                }

            assert max != null;

            final var avg = fitness.values().stream().mapToDouble(v -> v).average().orElse(0);
            final var generationEnd = System.currentTimeMillis();
            System.out.printf("\rgen = %d\tgen_time = %sms\tavg_fit = %.2f\tmax_fit = %.2f\tbest: %s", pool.getGeneration(), generationEnd - generationStart, avg, max.getValue(), max.getKey().toString());
        } while (max.getValue() < targetFitness);

        final var end = System.currentTimeMillis();

        System.out.println("\r\nTook: " + (end - start) + "ms");
    }
}
