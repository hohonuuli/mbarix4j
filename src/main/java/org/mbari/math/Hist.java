package org.mbari.math;

/**
 * @author Brian Schlining
 * @since 2011-12-22
 */
public class Hist {

    private final double[] bins;
    private final double[] values;
    private final boolean inclusive;

    public Hist(double[] bins, boolean inclusive) {
        this.bins = bins;
        this.values = new double[bins.length];
        this.inclusive = inclusive;
    }
    
    public void fill(double[] data) {
        for (double datum : data) {
            fill(datum);
        }
    }
    
    public void fill(double datum) {
        int n = Matlib.near(bins, datum, inclusive);
        if (n > 0) {
            values[n] = values[n] + 1;
        }
    }
}
