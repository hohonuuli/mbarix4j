package org.mbari.ocean;

import static org.junit.Assert.*;
import static org.mbari.ocean.Seawater.*;
import org.junit.Test;


/**
 * @author Brian Schlining
 * @since 2011-12-26
 */
public class SeawaterTest {
    
    @Test
    public void testDensity() {
        double d = density(34.567, 5.0, 2000);
        assertEquals(1.036409, d, 0.000001);
    }

    @Test
    public void testBulkModulus() {
        double b = bulkmod(35D, 10D, 4000D);
        assertEquals(24046.04869, b, 0.00001);
        double c = bulkmod(35D, 25D, 10000);
        assertEquals(27108.94504, c, 0.00001);
    }

    @Test
    public void testAtg() {
        double a = atg(35, 10, 4000);
        assertEquals(1.612567e-004, a, 0.0000000001);
        double b = atg(40, 40, 10000);
        assertEquals(3.255976e-004, b, 0.0000000001);
    }

    @Test
    public void testSalinity() {
        double s = salinity(0.87654, 12.345, 1234);
        assertEquals(31.9077, s, 0.0001);
        double ss = salinity(0.65, 5, 1500); // UNESCO 44 p9
        assertEquals(27.9953479, ss, 0.001);
    }

    @Test
    public void testSigmat() {
        assertEquals(26.1870, sigmat(34.567, 12.345), 0.0001);
        assertEquals(24.7630, sigmat(35, 20), 0.0001); // UNESCO 44 p23
    }

    @Test
    public void testSvel() {
        assertEquals(1522.96, svel(35, 10, 2000), 0.01);
        assertEquals(1731.995, svel(40, 40, 10000), 0.001);
    }

    @Test
    public void testTheta() {
        assertEquals(9.2906, theta(35, 10, 5000), 0.0001);
        assertEquals(9.8341, theta(35, 10, 5000, 4000), 0.0001);
        assertEquals(8.3121, theta(40, 10, 10000, 0), 0.0001);
    }

    @Test
    public void testDelta() {
        assertEquals(94.884, delta(34.567, 5.00, 2000), 0.001);
        assertEquals(981.302, delta(40, 40, 10000), 0.001); //  UNESCO 44 p22
    }

    @Test
    public void testDepth() {
        assertEquals(4906.08, depth(5000, 36), 0.01);
        assertEquals(9674.23, depth(10000, 90), 0.01); //UNESCO 44 p28
    }
}
