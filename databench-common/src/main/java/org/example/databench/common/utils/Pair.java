package org.example.databench.common.utils;

public class Pair<T, U> {
    public final T left;
    public final U right;

    public Pair(T left, U right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" :: { left: %s, right: %s }", left, right);
    }
}
