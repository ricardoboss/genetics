package de.ricardoboss.ml.genetics;

import java.util.function.Supplier;

public interface GeneGenerator<T> extends Supplier<Gene<T>> {
}
