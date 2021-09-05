package mbarix4j3.math;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Brian Schlining
 * @since 2012-05-15
 */
public class StatlibTest {

    private double tolerance = 0.0000000001;
    double[] a = new double[] {1, 2, 3, 4, 5, 6, 7, 8, 9};
    double[] b = {0.814723686393179, 0.905791937075619, 0.126986816293506, 0.913375856139019,
            0.632359246225410, 0.097540404999410, 0.278498218867048, 0.546881519204984,
            0.957506835434298, 0.964888535199277};

    @Test
    public void testMean() {
        assertEquals(5, Statlib.mean(a), tolerance);
        assertEquals(0.623855305583175, Statlib.mean(b), tolerance);
    }

    @Test
    public void testMedian() {
        assertEquals(5, Statlib.median(a), tolerance);
        assertEquals(0.723541466309294, Statlib.median(b), tolerance);
    }

    @Test
    public void testVariance() {
        assertEquals(7.5, Statlib.variance(a), tolerance);
        assertEquals(0.119613625289474, Statlib.variance(b), tolerance);
    }

    @Test
    public void testHist() {

        double[] ba = {1D, 4D, 7D, 10D};
        double[] ha = Statlib.hist(a, ba, true);
        double[] ea = {2D, 3D, 3D, 1D};
        assertArrayEquals(ea, ha, tolerance);

        double[] ba1 = {2D, 4D, 7D, 10D};
        double[] ha1 = Statlib.hist(a, ba1, false);
        double[] ea1 = {3D, 2D, 3D, 1D};
        assertArrayEquals(ea1, ha1, tolerance);
        double[] ha2 = Statlib.hist(a, ba1, true);
        double[] ea2 = {2D, 2D, 3D, 1D};
        assertArrayEquals(ea2, ha2, tolerance);

        double[] bb = {0, 0.33333333, 0.66666666, 1};
        double[] hb = Statlib.hist(b, bb, true);
        double[] eb = {2D, 1D, 3D, 4D};
        assertArrayEquals(eb, hb, tolerance);

    }

    @Test
    public void testHistc() {
        double[] ba = {1D, 4D, 7D, 10D};
        double[] ha = Statlib.histc(a, ba);
        double[] ea = {3D, 3D, 3D, 0D};
        assertArrayEquals(ea, ha, tolerance);

        double[] ba1 = {2D, 4D, 7D, 10D};
        double[] ha1 = Statlib.histc(a, ba1);
        double[] ea1 = {2D, 3D, 3D, 0D};
        assertArrayEquals(ea1, ha1, tolerance);


        double[] bb = {0, 0.33333333, 0.66666666, 1};
        double[] hb = Statlib.histc(b, bb);
        double[] eb = {3D, 2D, 5D, 0D};
        assertArrayEquals(eb, hb, tolerance);
    }

    @Test
    public void testPercentile() {
        double[] q = new double[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        assertEquals(1, Statlib.percentile(q, 0), tolerance);
        assertEquals(1, Statlib.percentile(q, 0.1), tolerance);
        assertEquals(2, Statlib.percentile(q, 0.2), tolerance);
        assertEquals(3, Statlib.percentile(q, 0.25), tolerance);
        assertEquals(5, Statlib.percentile(q, 0.5), tolerance);
        assertEquals(8, Statlib.percentile(q, 0.75), tolerance);
        assertEquals(8, Statlib.percentile(q, 0.8), tolerance);
        assertEquals(10, Statlib.percentile(q, 1), tolerance);
    }


}
