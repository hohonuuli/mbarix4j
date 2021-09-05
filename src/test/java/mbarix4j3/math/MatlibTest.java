/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mbarix4j3.math;

import java.math.BigDecimal;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author brian
 */
public class MatlibTest {

    private double tol = 0.0000000001;

    @Test
    public void testCumSum() {
        double[] a = new double[] {1, 1, 1, 1, 2};
        double[] expected = new double[] {1, 2, 3, 4, 6};
        double[] actual = Matlib.cumsum(a);
        for (int i = 0; i < a.length; i++) {
            assertEquals(expected[i], actual[i], tol);
        }
    }

    @Test
    public void testDiff() {
        double[] x = new double[] {1, 2, 4};
        double[] e = new double[] {1, 2};
        double[] a = Matlib.diff(x);
        for (int i = 0; i < e.length; i++) {
            assertEquals(e[i], a[i], tol);
        }
    }

    @Test
    public void testFix() {
        assertEquals(-1, Matlib.fix(-1.9), tol);
        assertEquals(-1, Matlib.fix(-1.2), tol);
        assertEquals(1, Matlib.fix(1.2), tol);
        assertEquals(1, Matlib.fix(1.9), tol);
    }
    
    @Test
    public void nearInclusiveTest() {
        double[] a = new double[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        
        assertTrue("Value was outside the array and should have been null.", Matlib.near(a, 10, true) == -1);
        assertTrue("Value was outside the array and should have been null.", Matlib.near(a, -1, true) == -1);
        assertTrue(Matlib.near(a, 3D, true) == 3);
        assertTrue(Matlib.near(a, 2.9, true) == 3);
        assertTrue(Matlib.near(a, 3.1, true) == 3);
        assertTrue(Matlib.near(a, 0, true) == 0);
        assertTrue(Matlib.near(a, 9, true) == 9);
        
//        a = new double[] {9d, 8d, 7d, 6d, 5d, 4d, 3d, 2d, 1d, 0d};
//        assertNull("Value was outside the array and should have been null.", Matlib.nearInclusive(a, 10));
//        assertNull("Value was outside the array and should have been null.", Matlib.nearInclusive(a, -1));
//        assertTrue(Matlib.near(a, 3D, true) == 6);
//        assertTrue(Matlib.nearInclusive(a, 2.9).equals(6));
//        assertTrue(Matlib.nearInclusive(a, 3.1).equals(6));
//        assertTrue(Matlib.nearInclusive(a, 0).equals(9));
//        assertTrue(Matlib.nearInclusive(a, 9).equals(0));
        
    }
    
    @Test

    public void testInterpDouble() {
        double[] x = {0D, 1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D};
        double[] y = {0D, 1D, 2D, 6D, 8D, 11D, 20D, 10D, 9D};
        double[] xi = {1D, 1.5D, 4.5D, 5D, 7D, 7.5D};
        double[] yi = Matlib.interpolate(x, y, xi);
        double[] expectedYi = {1D, 1.5D, 9.5D, 11D, 10D, 9.5D};
        
        for (int i = 0; i < expectedYi.length; i++) {
            double d = expectedYi[i];
            assertEquals( expectedYi[i], yi[i], 0.0000001);
        }
    }
    
    @Test
    public void testInterpBigDecimal() {
        BigDecimal[] x = toBigDecimalArray(new double[] {0D, 1D, 2D, 3D, 4D, 5D, 6D, 7D, 8D});
        BigDecimal[] y = toBigDecimalArray(new double[] {0D, 1D, 2D, 6D, 8D, 11D, 20D, 10D, 9D});
        BigDecimal[] xi = toBigDecimalArray(new double[] {1D, 1.5D, 4.5D, 5D, 7D, 7.5D});
        BigDecimal[] yi = Matlib.interpolate(x, y, xi);
        BigDecimal[] expectedYi = toBigDecimalArray(new double[] {1D, 1.5D, 9.5D, 11D, 10D, 9.5D});
        
        for (int i = 0; i < expectedYi.length; i++) {
            BigDecimal d = expectedYi[i];
            assertEquals(expectedYi[i], yi[i]);
        }
    }
    
    private BigDecimal[] toBigDecimalArray(double[] d) {
        BigDecimal[] out = new BigDecimal[d.length];
        for (int i = 0; i < d.length; i++) {
            out[i] = new BigDecimal(d[i]);
        }
        return out;
    }

    @Test
    public void testNear() {
        double[] a = new double[] {3, 6, 9, 12, 15, 18, 21, 81};
        assertEquals(0, Matlib.near(a, 3));
        //assertEquals(0, Matlib.near(a, 0));
        assertEquals(0, Matlib.near(a, 4));
        assertEquals(1, Matlib.near(a, 5));
        assertEquals(6, Matlib.near(a, 21.1));
        assertEquals(7, Matlib.near(a, 80));
        assertEquals(7, Matlib.near(a, 1000));
    }

    @Test
    public void testSum() {
        double[] a = new double[] {1, 1, 1, 1, 1, 2};
        assertEquals(7, Matlib.sum(a), tol);
    }

    @Test
    public void testLogspace() {

        /* From Matlab: logspace(log10(1), log10(400), 10) */
        double[] expectedResult = new double[] {1.0000000000000D, 1.9458877175764D, 3.7864790094146D,
                7.3680629972808D, 14.3374232887377D, 27.8990158792484D,
                54.2883523318981D, 105.6390380101001D, 205.5617065604391D,
                400.0000000000001D};
        double[] actualResult = Matlib.logspace(Math.log10(1), Math.log10(400), 10);
        for (int i = 0; i < actualResult.length; i++) {
            assertEquals("Value is not within expected tolerance", expectedResult[i], actualResult[i], 0.0000000001D);

        }

    }

    @Test
    public void testLinspace() {
        double[] expectedResult = new double[] {1.0000000000000D, 45.3333333333333D,
                89.6666666666667D, 134.0000000000000D, 178.3333333333333D,
                222.6666666666667D, 267.0000000000000D, 311.3333333333333D,
                355.6666666666667D, 400.0000000000000D };
        double[] actualResult = Matlib.linspace(1, 400, 10);
        for (int i = 0; i < actualResult.length; i++) {
            assertEquals("Values is not within expected tolerance", expectedResult[i], actualResult[i], 0.0000000001D);
        }
    }

    @Test
    public void testMedian() {

        double[] values1 = new double[] {1.0000000000000D, 45.3333333333333D,
                89.6666666666667D, 134.0000000000000D, 178.3333333333333D,
                222.6666666666667D, 267.0000000000000D, 311.3333333333333D,
                355.6666666666667D, 400.0000000000000D };
        double expectedMedian1 = 200.5D;
        double[] values2 = new double[] {1.0000000000000D, 45.3333333333333D,
                89.6666666666667D, 134.0000000000000D, 178.3333333333333D,
                222.6666666666667D, 267.0000000000000D, 311.3333333333333D,
                355.6666666666667D, 400.0000000000000D, 234.5D};
        double expectedMedian2 = 222.6666666666667D;

        double median1 = Statlib.median(values1);
        assertEquals("Median is not what we expected", expectedMedian1, median1, 0.0000000001D);
        double median2 = Statlib.median(values2);
        assertEquals("Median is not what we expected", expectedMedian2, median2, 0.0000000001D);

    }

    @Test
    public void testVariance() {
        double[] values = new double[] {44,   50,   38,   96,   42,   47,   40,   39,   46,   50};
        double expectedValue = 288.8444444444444D;
        double var = Statlib.variance(values);
        assertEquals("Variance is not what we expected", expectedValue, var, 0.000000001D);

        values = new double[] {1.0000000000000D, 45.3333333333333D,
                89.6666666666667D, 134.0000000000000D, 178.3333333333333D,
                222.6666666666667D, 267.0000000000000D, 311.3333333333333D,
                355.6666666666667D, 400.0000000000000D, 234.5D};
        expectedValue = 1.632000757575758e+04;
        var = Statlib.variance(values);
        assertEquals("Variance is not what we expected", expectedValue, var, 0.000000001D);
    }

    @Test
    //@Ignore
    public void testStd() {

        double[] values = new double[] {4, 9, 11, 12, 17, 5, 8, 12, 14};
        double expectedValue = 4.18;
        double std = Statlib.standardDeviation(values);
        assertEquals("Standard Deviation is not what we expected", expectedValue, std, 0.01D);

        values = new double[]{1, 2, 3, 4, 5};
        expectedValue = 1.58113;
        std = Statlib.standardDeviation(values);
        assertEquals("Standard Deviation is not what we expected", expectedValue, std, 0.00001D);


        values = new double[] {1.0000000000000D, 45.3333333333333D,
                89.6666666666667D, 134.0000000000000D, 178.3333333333333D,
                222.6666666666667D, 267.0000000000000D, 311.3333333333333D,
                355.6666666666667D, 400.0000000000000D, 234.5D};
        expectedValue = 127.7497850321384D;
        std = Statlib.standardDeviation(values);
        assertEquals("Standard Deviation is not what we expected", expectedValue, std, 0.0000000001D);

        
    }

    @Test
    public void testTrapz() {
        double[] x = new double[] {1, 2, 4, 8, 16};
        double[] y = new double[] {2, 3, 2, 4, 8};
        double a = Matlib.trapz(x, y);
        double e = 67.5;
        assertEquals(e, a, tol);
    }


    @Test
    public void testPearsonsCorrelation() {
        double[] values2 = new double[] {1.0000000000000D, 45.3333333333333D,
                89.6666666666667D, 134.0000000000000D, 178.3333333333333D,
                222.6666666666667D, 267.0000000000000D, 311.3333333333333D,
                355.6666666666667D, 400.0000000000000D, 1D};
        double[] values = new double[] {1.0000000000000D, 45.3333333333333D,
                89.6666666666667D, 134.0000000000000D, 178.3333333333333D,
                222.6666666666667D, 267.0000000000000D, 311.3333333333333D,
                355.6666666666667D, 400.0000000000000D, 234.5D};
        
        double cc = Statlib.pearsonsCorrelation(values, values2);
        double expectedValue = 0.867003816722222;
        assertEquals("Correlaction Coefficient was not 1", expectedValue, cc, 0.0000001D);

        cc = Statlib.pearsonsCorrelation(values, values);
        assertEquals("Correlaction Coefficient was not 1", 1D, cc, 0.0000001D);
        assertTrue("Correlation Coefficient is not in the expected range: -1 <= " +
                cc + " <= 1",(cc <= 1) && (cc >= -1));


    }

}
