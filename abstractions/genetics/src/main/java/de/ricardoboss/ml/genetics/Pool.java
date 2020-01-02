package de.ricardoboss.ml.genetics;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Pool<T> {
    private final DnaGenerator<T> generator;
    private final FitnessCalculator<T> fitnessCalculator;
    private final Collection<DNA<T>> pool;
    private final Map<DNA<T>, Float> fitnessMap = new HashMap<>();
    private int fitnessMapGeneration = 0;
    private int generation = 0;

    public Pool(DnaGenerator<T> generator, int size, FitnessCalculator<T> fitnessCalculator) {
        this.generator = generator;
        this.fitnessCalculator = fitnessCalculator;
        this.pool = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            pool.add(this.generator.get());
        }
    }

    public void advanceGeneration(float fitnessThreshold, float survivorRatio, float pairingRatio, float mutationProbability) {
        generation++;

        var sortedFitness = calculateFitness()
                .entrySet()
                .stream()
                .filter(e -> e.getValue() >= fitnessThreshold)
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());

        Function<Integer, DNA<T>> dnaSupplier;
        final var originalSize = pool.size();
        final var dnaPool = new LinkedList<DNA<T>>();
        final var r = new Random();

        if (sortedFitness.size() > 0) {
            for (int i = sortedFitness.size() - 1; i > 0; i--) {
                for (int j = 0; j < i; j++) {
                    dnaPool.add(sortedFitness.get(i).getKey());
                }
            }

            if (dnaPool.size() == 0)
                dnaSupplier = i -> generator.get();
            else {
                if (survivorRatio + pairingRatio > 1f)
                    throw new IllegalArgumentException("Thresholds are out of bounds! Must be smaller than or equal to 1 in sum.");

                dnaSupplier = i -> {
                    var action = r.nextFloat();
                    var a = dnaPool.get(r.nextInt(dnaPool.size()));

                    if (action < survivorRatio) // get one original dna
                        return a;
                    else if (action < survivorRatio + pairingRatio) { // combine two dna
                        var b = dnaPool.get(r.nextInt(dnaPool.size()));

                        return a.combine(b, a.generator, 0.5f).mutate(mutationProbability);
                    } else // generate new dna
                        return generator.get();
                };
            }
        } else {
            dnaSupplier = i -> generator.get();
        }

        pool.clear();
        for (var i = 0; i < originalSize; i++) {
            pool.add(dnaSupplier.apply(i));
        }
    }

    public Map<DNA<T>, Float> calculateFitness() {
        if (fitnessMapGeneration != generation) {
            fitnessMap.clear();
            fitnessMap.putAll(
                    pool.stream()
                            .distinct()
                            .map(dna -> new HashMap.SimpleEntry<>(dna, fitnessCalculator.calculate(dna)))
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
            );

            fitnessMapGeneration = generation;
        }

        return fitnessMap;
    }

    public int getGeneration() {
        return generation;
    }
}
