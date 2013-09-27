package org.mbari.solar;

import static org.junit.Assert.*;
import org.junit.Test;


public class SolarUtilTest {

    @Test
    public void testOzoneRead() {
        try {
            double[] ozone = SolarUtil.getOzoneWavelengths();
            assertNotNull(ozone);
            assertTrue(ozone.length > 10);
        }
        catch (Exception e) {
            fail("Failed to read ozone.txt");
        }
    }
    
}

