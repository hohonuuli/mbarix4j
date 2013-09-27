package org.mbari.ocean;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mbari.util.Tuple3;
import static org.mbari.ocean.Waves.*;
/**
 * @author Brian Schlining
 * @since 2011-12-26
 */
public class WavesTest {
    
    @Test
        public void testCelerity() {
            Tuple3<Double, Double, Double> a = celerity(8, 15);
            assertEquals(10.7129, a.getA(), 0.0001);
            assertEquals(99.8220, a.getB(), 0.0001);
        }
}
