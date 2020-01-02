package de.ricardoboss.ml.genetics;

import java.util.*;
import java.util.stream.Collectors;

public class DNA<G> {
    public final GeneGenerator<G> generator;
    private final List<Gene<G>> genes = new ArrayList<>();

    private DNA(GeneGenerator<G> generator) {
        this.generator = generator;
    }

    public DNA(DNA<G> dna) {
        this(dna.generator, dna.genes.stream().map(Gene::clone).collect(Collectors.toList()));
    }

    public DNA(GeneGenerator<G> generator, List<Gene<G>> genes) {
        this(generator);

        this.genes.addAll(genes);
    }

    public DNA(GeneGenerator<G> generator, int numGenes) {
        this(generator);

        for (int i = 0; i < numGenes; i++)
            genes.add(this.generator.get());
    }

    public DNA<G> mutate(float mutationProbability) {
        if (mutationProbability < 0 || mutationProbability > 1)
            throw new IllegalArgumentException("Mutation probability must be between 0 and 1!");

        var clone = this.clone();
        var r = new Random();
        for (var gene : clone.genes) {
            if (r.nextFloat() < mutationProbability)
                gene.mutate();
        }

        return clone;
    }

    public DNA<G> combine(DNA<G> partner, GeneGenerator<G> generator, float ratio) {
        var count = (int) Math.ceil((this.genes.size() + partner.genes.size()) / 2f);
        var resultGenes = new ArrayList<Gene<G>>(count);
        var aStack = new Stack<Gene<G>>();
        var bStack = new Stack<Gene<G>>();
        var r = new Random();

        aStack.addAll(this.genes);
        bStack.addAll(partner.genes);

        for (int i = 0; i < count; i++) {
            Gene<G> gene;

            if (aStack.empty()) {
                gene = bStack.pop();
            } else if (bStack.empty()) {
                gene = aStack.pop();
            } else {
                if (r.nextFloat() < ratio) {
                    gene = aStack.pop();
                    bStack.pop();
                } else {
                    aStack.pop();
                    gene = bStack.pop();
                }
            }

            resultGenes.add(gene);
        }

        Collections.reverse(resultGenes);

        return new DNA<>(generator, resultGenes);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public DNA<G> clone() {
        return new DNA<>(this);
    }

    public List<Gene<G>> getGenes() {
        return genes;
    }

    @Override
    public String toString() {
        return this.genes.stream().map(Gene::getValue).map(Object::toString).collect(Collectors.joining());
    }
}
