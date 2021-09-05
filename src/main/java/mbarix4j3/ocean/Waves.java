package mbarix4j3.ocean;

import mbarix4j3.util.Tuple3;

import static java.lang.Math.*;

/**
 * @author Brian Schlining
 * @since 2011-12-26
 */
public class Waves {

    /**
     * Ideal wave phase speed = f(Period, Depth)
     *
     * @param t = ideal wave period (seconds)
     * @param z = water depth (meters)
     * @return (c, Ld, L) where: C = wave phase speed (m/s), Ld = deepwater wavelength (M),
     *         and L = wavelength in water depth, Z
     */
    public static Tuple3<Double, Double, Double> celerity(double t, double z) {
        double ld = 9.8 * pow(t, 2) / (2 * PI);    // deep water wavelength
        double c = sqrt(9.8 * ld / (2 * PI) * tanh(2 * PI * z / ld));
        double ll = c * t;
        return new Tuple3<Double, Double, Double>(c, ld, ll);
    }

    /**
     * Ideal wave phase speed = f(Period), Deep water approximation
     *
     * @param t = ideal wave period (seconds)
     * @return (c, Ld, L) where: C = wave phase speed (m/s), Ld = deepwater wavelength (M),
     *         and L = wavelength in deep water
     */
    public static Tuple3<Double, Double, Double> celerity(double t) {
        double ld = 9.8 * pow(t, 2) / (2 * PI);     // deep water wavelength
        double c = 9.8 * t / (2 * PI);
        double ll = c * t;
        return new Tuple3<Double, Double, Double>(c, ld, ll);
    }
}
