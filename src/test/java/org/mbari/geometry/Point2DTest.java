package org.mbari.geometry;

import org.junit.Test;
import org.junit.Assert;

/**
 * @author brian
 * @since Apr 27, 2009 11:29:14 AM
 */
public class Point2DTest {

    @Test
    public void equalsTest() {
        Point2D<Integer> p0 = new Point2D<Integer>(10, 12);
        Point2D<Integer> p1 = new Point2D<Integer>(12, 10);
        Point2D<Integer> p2 = new Point2D<Integer>(10, 12);
        Assert.assertFalse("Whoops, equals is implemented wrong", p0.equals(p1));
        Assert.assertTrue("Simitry failed", p0.equals(p0));
        Assert.assertEquals("Equals failed", p0, p2);
    }

    public void distanceTest() {
        Point2D<Integer> p0 = new Point2D<Integer>(10, 12);
        Point2D<Integer> p1 = new Point2D<Integer>(12, 10);
        double dp = p0.distance(p1);

        java.awt.geom.Point2D j0 = p0.toJavaPoint2D();
        java.awt.geom.Point2D j1 = p1.toJavaPoint2D();
        double dj = j0.distance(j1);

        Assert.assertEquals("Distance was not correct", dj, dp);

    }


}
