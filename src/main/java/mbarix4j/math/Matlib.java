/*
 * @(#)Matlib.java   2013.06.04 at 03:25:48 PDT
 *
 * Copyright 2009 MBARI
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package mbarix4j.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.TreeMap;

/**
 * <p>Static methods for doing useful math</p><hr>
 *
 * @author  : $Author: brian $
 * @version : $Revision: 1.1 $
 */

public class Matlib {

    /**
     * Cumulatively sum a vector
     * Example: cumsum([1 1 1 1 2]) = [1 2 3 4 6]
     *
     * @param n
     * @return
     */
    public static double[] cumsum(double[] n) {
        double[] buf = new double[n.length];

        for (int i = 0; i < n.length; i++) {
            if (i == 0) {
                buf[i] = n[0];
            }
            else {
                buf[i] = buf[i - 1] + n[i];
            }
        }

        return buf;
    }

    /**
     *
     * @param x
     * @return
     */
    public static double[] diff(double[] x) {
        if (x.length < 2) {
            return new double[] {};
        }
        else {
            double[] y = new double[x.length - 1];
            for (int i = 0; i < x.length - 1; i++) {
                int j = i + 1;
                y[i] = x[j] - x[i];
            }

            return y;
        }
    }

    /**
     * @param array
     * @param valueToFind
     * @return
     */
    public static int find(double[] array, double valueToFind) {
        return Arrays.binarySearch(array, valueToFind);
    }

    /**
     * Rounds the x to the nearest integer towards zero.
     *
     * @param x
     * @return
     */
    public static double fix(double x) {
        int sign = DoubleMath.sign(x);
        double y = 0;
        if (sign == -1) {
            y = Math.ceil(x);
        }
        else if (sign == 1) {
            y = Math.floor(x);
        }

        return y;
    }

    /**
     *
     * @param x
     * @param y
     * @param xi
     * @return
     *
     * @throws IllegalArgumentException
     */
    public static double[] interpolate(double[] x, double[] y, double[] xi) throws IllegalArgumentException {

        if (x.length != y.length) {
            throw new IllegalArgumentException("X and Y must be the same length");
        }
        if (x.length == 1) {
            throw new IllegalArgumentException("X must contain more than one value");
        }
        double[] dx = new double[x.length - 1];
        double[] dy = new double[x.length - 1];
        double[] slope = new double[x.length - 1];
        double[] intercept = new double[x.length - 1];

        // Calculate the line equation (i.e. slope and intercept) between each point
        for (int i = 0; i < x.length - 1; i++) {
            dx[i] = x[i + 1] - x[i];
            if (dx[i] == 0) {
                throw new IllegalArgumentException("X must be montotonic. A duplicate " + "x-value was found");
            }
            if (dx[i] < 0) {
                throw new IllegalArgumentException("X must be sorted");
            }
            dy[i] = y[i + 1] - y[i];
            slope[i] = dy[i] / dx[i];
            intercept[i] = y[i] - x[i] * slope[i];
        }

        // Perform the interpolation here
        double[] yi = new double[xi.length];
        for (int i = 0; i < xi.length; i++) {
            if ((xi[i] > x[x.length - 1]) || (xi[i] < x[0])) {
                yi[i] = Double.NaN;
            }
            else {
                int loc = Arrays.binarySearch(x, xi[i]);
                if (loc < -1) {
                    loc = -loc - 2;
                    yi[i] = slope[loc] * xi[i] + intercept[loc];
                }
                else {
                    yi[i] = y[loc];
                }
            }
        }

        return yi;
    }

    /**
     *
     * @param x
     * @param y
     * @param xi
     * @return
     */
    public static BigDecimal[] interpolate(BigDecimal[] x, BigDecimal[] y, BigDecimal[] xi) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("X and Y must be the same length");
        }
        if (x.length == 1) {
            throw new IllegalArgumentException("X must contain more than one value");
        }
        BigDecimal[] dx = new BigDecimal[x.length - 1];
        BigDecimal[] dy = new BigDecimal[x.length - 1];
        BigDecimal[] slope = new BigDecimal[x.length - 1];
        BigDecimal[] intercept = new BigDecimal[x.length - 1];

        // Calculate the line equation (i.e. slope and intercept) between each point
        BigInteger zero = new BigInteger("0");
        BigDecimal minusOne = new BigDecimal(-1);

        for (int i = 0; i < x.length - 1; i++) {

            //dx[i] = x[i + 1] - x[i];
            dx[i] = x[i + 1].subtract(x[i]);
            if (dx[i].equals(new BigDecimal(zero, dx[i].scale()))) {
                throw new IllegalArgumentException("X must be montotonic. A duplicate " + "x-value was found");
            }
            if (dx[i].signum() < 0) {
                throw new IllegalArgumentException("X must be sorted");
            }

            //dy[i] = y[i + 1] - y[i];
            dy[i] = y[i + 1].subtract(y[i]);

            //slope[i] = dy[i] / dx[i];
            slope[i] = dy[i].divide(dx[i]);

            //intercept[i] = y[i] - x[i] * slope[i];
            intercept[i] = x[i].multiply(slope[i]).subtract(y[i]).multiply(minusOne);

            //intercept[i] = y[i].subtract(x[i]).multiply(slope[i]);
        }

        // Perform the interpolation here
        BigDecimal[] yi = new BigDecimal[xi.length];
        for (int i = 0; i < xi.length; i++) {

            //if ((xi[i] > x[x.length - 1]) || (xi[i] < x[0])) {
            if ((xi[i].compareTo(x[x.length - 1]) > 0) || (xi[i].compareTo(x[0]) < 0)) {
                yi[i] = null;    // same as NaN
            }
            else {
                int loc = Arrays.binarySearch(x, xi[i]);
                if (loc < -1) {
                    loc = -loc - 2;

                    //yi[i] = slope[loc] * xi[i] + intercept[loc];
                    yi[i] = slope[loc].multiply(xi[i]).add(intercept[loc]);
                }
                else {
                    yi[i] = y[loc];
                }
            }
        }

        return yi;
    }

    /**
     *
     * @param x
     * @param y
     * @param xi
     * @return
     *
     * @throws IllegalArgumentException
     */
    public static double[] interpolate(long[] x, double[] y, long[] xi) throws IllegalArgumentException {

        double[] xd = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            xd[i] = (double) x[i];
        }

        double[] xid = new double[xi.length];
        for (int i = 0; i < xi.length; i++) {
            xid[i] = (double) xi[i];
        }

        return interpolate(xd, y, xid);
    }

    /**
     * generates n linearly-spaced points between d1 and d2.
     * @param d1 The min value
     * @param d2 The max value
     * @param n The number of points to generated
     * @return an array of lineraly space points.
     */
    public static double[] linspace(double d1, double d2, int n) {

        double[] y = new double[n];
        double dy = (d2 - d1) / (n - 1);
        for (int i = 0; i < n; i++) {
            y[i] = d1 + (dy * i);
        }

        return y;

    }

    /**
     * generates n logarithmically-spaced points between d1 and d2.
     * @param d1 The min value
     * @param d2 The max value
     * @param n The number of points to generated
     * @return an array of lineraly space points.
     */
    public static double[] logspace(double d1, double d2, int n) {

        return logspace(d1, d2, n, 10D);

    }

    /**
     * generates n logarithmically-spaced points between d1 and d2 using the
     * provided base.
     *
     * @param d1 The min value
     * @param d2 The max value
     * @param n The number of points to generated
     * @param base the logarithmic base to use
     * @return an array of lineraly space points.
     */
    public static double[] logspace(double d1, double d2, int n, double base) {
        double[] y = new double[n];
        double[] p = linspace(d1, d2, n);
        for (int i = 0; i < y.length - 1; i++) {
            y[i] = Math.pow(base, p[i]);
        }
        y[y.length - 1] = Math.pow(base, d2);

        return y;
    }

    /**
     * Find the index of the value nearest to the key. The values array can
     * contain only unique values. If it doesn't the first occurence of a value
     * in the values array is the one used, subsequent duplicate are ignored.
     *
     * @param values Values to search through for the nearest point.
     * @param key The key to search for the nearest neighbor in values.
     * @return the index of the value nearest to the key.
     * @since 20011207
     */
    public static int near(final double[] values, final double key) {
        return near(values, key, false);
    }

    /**
     * Find the index of the array nearest to the value. The values array can
     * contain only unique values. If it doesn't the first occurence of a value
     * in the values array is the one used, subsequent duplicate are ignored. If
     * the value falls outside the bounds of the array, <b>null</b> is returned
     *
     * @param values Values to search through for the nearest point
     * @param key THe value to search for the nearest neighbor in the array
     * @param inclusive If true the key must be within the values array
     * @return The index of the array value nearest the value. -1 will be returned if the
     *  key is outside the array values
     */
    public static int near(final double[] values, final double key, boolean inclusive) {

        int n = -1;
        boolean outsideValues = false;
        if (!inclusive) {
            if (key <= values[0]) {
                n = 0;
                outsideValues = true;
            }
            else if (key >= values[values.length - 1]) {
                n = values.length - 1;
                outsideValues = true;
            }
        }

        if (!outsideValues) {
            n = Arrays.binarySearch(values, key);
            if (n < 0) {

                // when n is an insertion point
                n = (-n) - 1;                 // Convert n to an index

                // If n == 0 we don't need to find nearest neighbor. If n is
                // larger than the array, use the last point in the array.
                if (inclusive && ((n == 0) || (n >= values.length))) {
                    n = -1;
                }
                else if (n > values.length - 1) {
                    n = values.length - 1;    // If key is larger than any value in values
                }
                else if (n > 0) {

                    // find nearest neighbor
                    double d1 = Math.abs(values[n - 1] - key);
                    double d2 = Math.abs(values[n] - key);
                    n = (d1 <= d2) ? n - 1 : n;
                }
            }
        }


        return n;
    }

    /**
     * Find the index of the array nearest to the value. The values array can
     * contain only unique values. If it doesn't the first occurence of a value
     * in the values array is the one used, subsequent duplicate are ignored. If
     * the value falls outside the bounds of the array, <b>null</b> is returned
     *
     * @param array Values to search through for the nearest point
     * @param value THe value to search for the nearest neighbor in the array
     * @return The index of the array value nearest the value. null if the value
     *      is larger or smaller than any values in the array.
     * @deprecated Use near instead
     */
    public static Integer nearInclusive(final double[] array, final double value) {
        Integer i = null;
        int idx = Arrays.binarySearch(array, value);
        if (idx < 0) {
            idx = -(idx) - 1;
            if ((idx == 0) || (idx >= array.length)) {

                // Do nothing. This point is outside the array bounds return value will be null
            }
            else {

                // Find nearest point
                double d0 = Math.abs(array[idx - 1] - value);
                double d1 = Math.abs(array[idx] - value);
                i = (d0 <= d1) ? idx - 1 : idx;
            }
        }
        else {
            i = idx;
        }

        return i;
    }

    /**
     *  Useful method for ordering a 1-D array based on an array of indices
     *  @param values A 1-D array of data to be sorted based on an array of indices
     *  @param order A 1-D array of indices specifying the ordering of the data.
     * @return
     */
    public static double[] subset(double[] values, int[] order) {
        double[] out = new double[order.length];
        for (int i = 1; i < order.length; i++) {
            out[i] = values[order[i]];
        }

        return out;
    }

    /**
     *
     * @param values
     * @param order
     * @return
     */
    public static float[] subset(float[] values, int[] order) {
        float[] out = new float[order.length];
        for (int i = 1; i < order.length; i++) {
            out[i] = values[order[i]];
        }

        return out;
    }

    /**
     *
     * @param values
     * @param order
     * @return
     */
    public static long[] subset(long[] values, int[] order) {
        long[] out = new long[order.length];
        for (int i = 1; i < order.length; i++) {
            out[i] = values[order[i]];
        }

        return out;
    }

    /**
     * Sum up all the values in an array
     *
     * @param values an array of values
     * @return The sum of all values in the Array
     */
    public static double sum(double[] values) {
        if ((values == null) || (values.length == 0)) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        }
        else {
            double sum = 0;
            for (int i = 0; i < values.length; i++) {
                sum += values[i];
            }

            return sum;
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public static double trapz(double[] x, double[] y) {
        double[] dx = diff(x);
        double[] dy = diff(y);
        double[] z = new double[dy.length];
        for (int i = 0; i < dy.length; i++) {
            double yy = y[i] + (dy[i] / 2);
            z[i] = dx[i] * yy;
        }

        return sum(z);
    }

    /**
     * Returns an array of indices indicating the order the data should be sorted
     * in. Duplicate values are discarded with the first one being kept. This method
     * is useful when a number of data arrays have to be sorted based on the values in
     * some coordinate array, such as time.
     *
     * To convert a array of values to a sorted monooic array try: <br>
     *    double[] x;  // some 1-D array of data <br>
     *    int[] i = Matlib.uniqueSort(x); <br>
     *    double[] xSorted = Matlib.reorder(x, i);<br><br>
     *
     * @param x An array of data that is to be sorted.
     * @return order An array of indexes such that y = Array.sort(x) and
     * y = x(order) are the same.
     */
    public static synchronized int[] uniqueSort(double[] x) {
        TreeMap<Double, Integer> tm = new TreeMap<>();
        for (int i = 0; i < x.length; i++) {
            Double key = Double.valueOf(x[i]);
            boolean exists = tm.containsKey(key);
            if (exists) {

                // Do nothing. Ignore duplicate keys
            }
            else {
                tm.put(key, Integer.valueOf(i));
            }
        }
        Object[] values = tm.values().toArray();
        int[] order = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            Integer tmp = (Integer) values[i];
            order[i] = tmp.intValue();
        }

        return order;
    }

    /**
     * Returns an array of indices indicating the order the data should be sorted
     * in. Duplicate values are discarded with the first one being kept. This method
     * is useful when a number of data arrays have to be sorted based on the values in
     * some coordinate array, such as time.
     *
     * To convert a array of values to a sorted monooic array try: <br>
     *    double[] x;  // some 1-D array of data <br>
     *    int[] i = Matlib.uniqueSort(x); <br>
     *    double[] xSorted = Matlib.reorder(x, i);<br><br>
     *
     * @param x An array of data that is to be sorted.
     * @return order An array of indexes such that y = Array.sort(x) and
     * y = x(order) are the same.
     */
    public static synchronized int[] uniqueSort(long[] x) {
        TreeMap<Long, Integer> tm = new TreeMap<>();
        for (int i = 0; i < x.length; i++) {
            Long key = Long.valueOf(x[i]);
            boolean exists = tm.containsKey(key);
            if (exists) {

                // Do nothing. Ignore duplicate keys
            }
            else {
                tm.put(key, Integer.valueOf(i));
            }
        }
        Object[] values = tm.values().toArray();
        int[] order = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            Integer tmp = (Integer) values[i];
            order[i] = tmp.intValue();
        }

        return order;
    }
}
