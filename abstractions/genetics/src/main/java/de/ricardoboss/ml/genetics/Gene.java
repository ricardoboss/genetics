package de.ricardoboss.ml.genetics;

public interface Gene<T> {
    void mutate();
    T getValue();
    Gene<T> clone();
}
