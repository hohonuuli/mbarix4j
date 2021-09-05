package mbarix4j3.gis.util;

import org.junit.Assert;
import org.junit.Test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author brian
 */
public class GisUtilTest {
    
    @Test
    public void geoToMathTest() {
        //Example:  geo2mth_([0:30:360]) ->
        //90 60 30  0 -30 -60 -90 -120 -150 180 150 120 90
        double[] geo = {0D, 30D, 60D, 90D, 120D, 150D, 180D, 210D, 240D, 270D, 
                300D, 330D, 360D};
        double[] math = {90D, 60D, 30D,  0D, -30D, -60D, -90D, -120D, -150D, 180D, 150D, 120D, 90D};
        for (int i = 0; i < geo.length; i++) {
            double a = GISUtilities.geoToMath(Math.toRadians(geo[i]));
            Assert.assertEquals(math[i], Math.toDegrees(a), 0.0001);
        }
    }
    
    @Test
    public void mathToGeoTest() {
        //Example:  geo2mth_([0:30:360]) ->
        //90 60 30  0 -30 -60 -90 -120 -150 180 150 120 90
        double[] geo = {0D, 30D, 60D, 90D, 120D, 150D, 180D, 210D, 240D, 270D, 
                300D, 330D, 0D};
        double[] math = {90D, 60D, 30D,  0D, -30D, -60D, -90D, -120D, -150D, 180D, 150D, 120D, 90D};
        for (int i = 0; i < geo.length; i++) {
            double a = GISUtilities.mathToGeo(Math.toRadians(math[i]));
            Assert.assertEquals(geo[i], Math.toDegrees(a), 0.0001);
        }
    }

}
