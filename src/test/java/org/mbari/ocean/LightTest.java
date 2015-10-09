package org.mbari.ocean;

import static org.junit.Assert.*;

import org.junit.Test;

import static org.mbari.ocean.Light.*;

/**
 * @author Brian Schlining
 * @since 2011-12-26
 */
public class LightTest {

    @Test
    public void testC2T() {
        double a = c2t(0.045);
        assertEquals(95.5997, a, 0.0001);
        double b = c2t(0.045, 0.25);
        assertEquals(98.8813, b, 0.0001);
    }
}
