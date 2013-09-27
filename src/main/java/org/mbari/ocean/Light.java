package org.mbari.ocean;

/**
 * @author Brian Schlining
 * @since 2011-12-17
 */
public class Light {

    public static double c2t(double c) {
        return c2t(c, 1.0);
    }

    /**
     * Convert beam attenuation (/m) to percent beam transmission
     *
     * @param c          beam attenuation coefficient (/m)
     * @param pathLength m (default = 1.0 m)
     * @return beam transmission (percentage per pathlength)
     */
    public static double c2t(double c, double pathLength) {
        return 100D * Math.exp(-c * pathLength);
    }

    /**
     * Convert beam transmission to beam attenuation
     *
     * @param t          beam transmission (percentage per pathlength)
     * @param pathLength m (default = 1.0 m)
     * @return c beam attenuation coefficient (/m)
     */
    public static double t2c(double t, double pathLength) {
        return -(Math.log(t / 100D) / pathLength);
    }
}
