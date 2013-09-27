package org.mbari.math;

/**
 * NUmber representing a fraction
 *
 * @author brian
 * @since May 27, 2009 8:39:14 PM
 */
@Deprecated
public class Fraction extends Number {

    private int numerator;
    private int denominator;

    public Fraction(int denominator, int numerator) {
        this.denominator = denominator;
        this.numerator = numerator;
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public int intValue() {
        return (int) doubleValue();
    }

    public long longValue() {
        return (long) doubleValue();
    }

    public float floatValue() {
        return (float) doubleValue();
    }

    public double doubleValue() {
        return (double) numerator / denominator;
    }

    public Fraction add(Fraction f) {
        int n = numerator * f.denominator + denominator * f.numerator;
        int d = denominator * f.denominator;
        Fraction sum = new Fraction (n, d);
        sum.reduce();
        return sum;
    }

    public Fraction substract(Fraction f) {
        int n = numerator * f.denominator - denominator * f.numerator;
        int d = denominator * f.denominator;
        Fraction diff = new Fraction (n, d);
        diff.reduce();
        return diff;
    }

    public Fraction multiply(Fraction f) {
        int n = numerator * f.numerator;
        int d = denominator * f.denominator;
        Fraction m = new Fraction(n, d);
        m.reduce();
        return m;
    }

    public Fraction divide(Fraction f) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void reduce() {
        int u = numerator;
        int v = denominator;
        int temp;

        while( v != 0) {
            temp = u % v;
            u = v;
            v = temp;
        }

        numerator /= u;
        denominator /= u;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (obj instanceof Fraction) {
            Fraction that = (Fraction) obj;
            Double thisD = doubleValue();
            Double thatD = that.doubleValue();
            isEqual = thisD.equals(thatD);
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return ((Double) doubleValue()).hashCode();
    }

    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }
}
