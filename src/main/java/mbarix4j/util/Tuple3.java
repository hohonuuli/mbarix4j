package mbarix4j.util;

/**
 * @author Brian Schlining
 * @since Sep 27, 2010
 */
public class Tuple3<A, B, C> extends Tuple2<A, B> {

    private final C c;

    /**
     * Constructs ...
     *
     * @param a
     * @param b
     * @param c
     */
    public Tuple3(A a, B b, C c) {
        super(a, b);
        this.c = c;
    }

    /**
     * @return
     */
    public C getC() {
        return c;
    }
}