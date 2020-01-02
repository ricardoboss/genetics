package de.ricardoboss.ml.genetics.test;

import de.ricardoboss.ml.genetics.Gene;
import de.ricardoboss.ml.genetics.GeneGenerator;

import java.util.Random;

public class CharGene implements Gene<Character> {
    private static final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz .,;!?0123456789";
    private static final Random r = new Random();

    public static final class Generator implements GeneGenerator<Character> {
        @Override
        public CharGene get() {
            return new CharGene(alphabet.charAt(r.nextInt(alphabet.length())));
        }
    }

    private char value;

    private CharGene(char value) {
        this.value = value;
    }

    @Override
    public void mutate() {
        value = alphabet.charAt(r.nextInt(alphabet.length()));
    }

    public Character getValue() {
        return value;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public Gene<Character> clone() {
        return new CharGene(value);
    }
}
