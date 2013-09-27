/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mbari.gis;

import org.mbari.geometry.Envelope;
import org.mbari.geometry.Point2D;
import org.mbari.math.Matlib;

/**
 *
 * @author brian
 */
public class DEMRasterGrid implements DEMAccess {
    
        /**
     * Defines the eastings or longitude of the grid
     */
    private double[] x;

    /**
     * Defines the northings or latitude of the grid.
     */
    private double[] y;

    /**
     * Defines the height or other z data.
     */
    private float[][] z;

    private Envelope<Double> envelope;


     /**
     * @param x X-coordinates (i.e East-West) for each column of z
     * @param y Y-coordinates (i.e. North-South) for each row of z
     * @param z data values whos positions are defined by the x and y arrays. z
     *      should be z[y.length][x.length]
     * @throws RuntimeException thrown if the x.length != z[0].length and y.length != z.length
     */
    public DEMRasterGrid( double[] y, double[] x, float[][] z)  {

        if ((x.length == z[0].length) && (y.length == z.length)) {
            this.x = x;
            this.y = y;
            this.z = z;
            setBounds();
        } else {
            throw new RuntimeException("Dimensions of z do not match the " +
                    "length of the x and y axes!");
        }
    }

    /**
     * Used to define the boundaries of the grid. Required for contains method.
     */
    private void setBounds() {
        double xMin = 0;
        double xMax = 0;
        double yMin = 0;
        double yMax = 0;

        if (x[0] < x[x.length - 1]) {
            xMin = x[0];
            xMax = x[x.length - 1];
        } else {
            xMin = x[x.length - 1];
            xMax = x[0];
        }

        if (y[0] < y[y.length - 1]) {
            yMin = y[0];
            yMax = y[y.length - 1];
        } else {
            yMin = y[y.length - 1];
            yMax = y[0];
        }

        Point2D<Double> lowerLeftCorner = new Point2D<Double>(xMin, yMin);
        Point2D<Double> upperRightCorner = new Point2D<Double>(xMax, yMax);


        envelope = new Envelope<Double>(lowerLeftCorner, upperRightCorner);
    }

    public Envelope<Double> getEnvelope() {
        return envelope;
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    /**
     * Retrieve the elevation at the given x and y coordinate.
     * @param xa
     * @param ya
     * @return The elevation value nearest the given coordinate. Float.NaN is
     *      returned if the coordinates are outside the grid boundaries.
     */
    public float getZ(double xa, double ya) {
        Integer ix = Matlib.nearInclusive(x, xa);
        Integer iy = null;
        float za = Float.NaN;
        if (ix != null) {
            iy = Matlib.nearInclusive(y, ya);
            if (iy != null) {
                za = z[iy][ix];
            }
        }
        return za;
    }

    public float getZ(int ix, int iy) {
        return z[iy][ix];
    }

}
