package org.mbari.util;

/*
 * Holder for a pair of values. Useful for when you need to return multiple
 * values from a method.
 */
@Deprecated
public class Pair<A, B> {
    
    private final A a;
    private final B b;
    
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }
    
    public A getA() {
        return a;
    }
    
    public B getB() {
        return b;
    }
    
}