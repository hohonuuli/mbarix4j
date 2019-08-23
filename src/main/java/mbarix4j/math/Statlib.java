/*
 * @(#)StatisticsUtilities.java   2011.12.22 at 09:11:23 PST
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

import java.util.Arrays;

/**
 * @author Brian Schlining
 * @since 2011-12-22
 */
public class Statlib {

    /**
     *
     * @param I
     * @param R
     * @return
     */
    public static strictfp double correlationCoefficient(double[] I, double[] R) {


        double k = I.length;
        double meanI = mean(I);
        double meanR = mean(R);
        double varR = variance(R);

        double sumI2 = 0;
        double sumIR = 0;
        for (int i = 0; i < k; i++) {
            double id = I[i];
            sumI2 += id * id;
            sumIR += id * R[i];
        }

        return (sumIR - k * meanI * meanR) / (Math.sqrt(sumI2 - k * meanI * meanI) * varR);

    }

    /**
     *
     * @param v1
     * @param v2
     * @return
     */
    public static strictfp double crossCorrelation(double[] v1, double[] v2) {

        throw new UnsupportedOperationException("Still working on this");

    }

    /**
     *
     * @param values
     * @return
     */
    public static strictfp double[] deviationFromMean(double[] values) {
        double mean = mean(values);
        double[] deviation = new double[values.length];
        for (int i = 0; i < deviation.length; i++) {
            deviation[i] = values[i] - mean;

        }

        return deviation;
    }

    /**
     * Calculate the mean of an array of values
     *
     * @param values The values to calculate
     * @return The mean of the values
     */
    public static strictfp double mean(double[] values) {
        return Matlib.sum(values) / values.length;
    }

    /**
     *
     * @param values
     * @return
     */
    public static double median(double[] values) {
        double[] v = new double[values.length];
        double median = Double.NaN;
        if ((v == null) || (v.length == 0)) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        }
        else if (v.length == 1) {
            median = v[0];
        }
        else {
            System.arraycopy(values, 0, v, 0, values.length);
            Arrays.sort(v);
            if (DoubleMath.isEven(v.length)) {
                int i = (int) Math.ceil(v.length / 2D);
                double n1 = v[i];
                double n0 = v[i - 1];
                median = (n0 + n1) / 2;
            }
            else {
                median = v[v.length / 2];
            }
        }

        return median;

    }

    /**
     * Calculate Pearson's correlation coefficient. Stole the math from wikipedia,
     * hope it's right ;-) This is also known as the <strong>Correlation
     * Coefficient</strong> or the <strong>Pearson product-moment correlation
     * coefficient</strong>
     *
     * @param x
     * @param y
     * @return
     */
    public static strictfp double pearsonsCorrelation(double[] x, double[] y) {

        if (x.length != y.length) {
            throw new IllegalArgumentException("The double arrays must be the same length!!! " + x.length + " != " +
                    y.length);
        }

        double N = y.length;
        double sum_sq_x = 0;
        double sum_sq_y = 0;
        double sum_coproduct = 0;
        double mean_x = x[0];
        double mean_y = y[0];
        for (int i = 2; i <= N; i++) {

            // for i in 2 to N:
            double sweep = (i - 1.0) / i;
            double delta_x = x[i - 1] - mean_x;
            double delta_y = y[i - 1] - mean_y;
            sum_sq_x += delta_x * delta_x * sweep;
            sum_sq_y += delta_y * delta_y * sweep;
            sum_coproduct += delta_x * delta_y * sweep;
            mean_x += delta_x / i;
            mean_y += delta_y / i;
        }
        double pop_sd_x = Math.sqrt(sum_sq_x / N);
        double pop_sd_y = Math.sqrt(sum_sq_y / N);
        double cov_x_y = sum_coproduct / N;

        return cov_x_y / (pop_sd_x * pop_sd_y);

    }

    /**
     * Standard deviation is a statistical measure of spread or variability.The
     * standard deviation is the root mean square (RMS) deviation of the values
     * from their arithmetic mean.
     *
     * <b>populationStandardDeviation</b> normalizes values by N, where N is the sample size. This the
     * <i>Population Standard Deviation</i>
     * @param values
     * @return
     */
    public static strictfp double populationStandardDeviation(double[] values) {
        double mean = mean(values);
        double n = values.length;
        double dv = 0;
        for (double d : values) {
            double dm = d - mean;
            dv += dm * dm;
        }

        return Math.sqrt(dv / n);
    }

    /**
     * Variance: the square of the standard deviation of the population. A
     * measure of the degree of spread among a set of values; a measure of the
     * tendency of individual values to vary from the mean value. <b>populationVariance</b>
     * uses <b>populationStandardDeviation</b> for the standard deviation calculation.
     *
     * @param values
     * @return
     */
    public static double populationVariance(double[] values) {
        double stdP = populationStandardDeviation(values);

        return stdP * stdP;
    }

    /**
     * Retrieve the quartile value from an array
     * .
     * @param values THe array of data
     * @param lowerPercent The percent cut off. For the lower quartile use 25,
     *      for the upper-quartile use 75
     * @return
     * @deprecated  Use percentile instead
     */
    public static double quartile(double[] values, double lowerPercent) {
        return percentile(values, lowerPercent / 100);
    }

    /**
     * Retrive the value at some percent. e.g. percentile(a, .5) = median(a)
     * @param values
     * @param percent The percent cut off. For the lower quartile use 0.25,
     *      for the upper-quartile use 0.75
     * @return The value the occurs at some percentile
     */
    public static double percentile(double[] values, double percent) {
        if (percent < 0 || percent > 1) {
            throw new IllegalArgumentException("Percentile must be between 0 and 1, found was " +
                    percent);
        }

        if ((values == null) || (values.length == 0)) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        }


        // Rank order the values
        double[] v = new double[values.length];
        System.arraycopy(values, 0, v, 0, values.length);
        Arrays.sort(v);

        int n = (int) Math.round(v.length * percent) - 1;

        if (n < 0) {
            n = 0;
        }
        else if (n > values.length - 1) {
            n = values.length - 1;
        }


        return v[n];
    }

    /**
     * Standard deviation is a statistical measure of spread or variability.The
     * standard deviation is the root mean square (RMS) deviation of the values
     * from their arithmetic mean.
     *
     * <b>standardDeviation</b> normalizes values by (N-1), where N is the sample size.  This is the
     * sqrt of an unbiased estimator of the variance of the population from
     * which X is drawn, as long as X consists of independent, identically
     * distributed samples.
     *
     * @param values
     * @return
     */
    public static strictfp double standardDeviation(double[] values) {
        double mean = mean(values);
        double dv = 0D;
        for (double d : values) {
            double dm = d - mean;
            dv += dm * dm;
        }

        return Math.sqrt(dv / (values.length - 1));

        //        double[] deviation = deviationFromMean(values);
        //        double s = 0D;
        //        for (double d : deviation) {
        //            s += (d * d);
        //        }
        //        return Math.sqrt(s / values.length - 1);
    }

    /**
     * Variance: the square of the standard deviation. A measure of the degree
     * of spread among a set of values; a measure of the tendency of individual
     * values to vary from the mean value.
     *
     * @param values
     * @return
     */
    public static double variance(double[] values) {
        double std = standardDeviation(values);

        return std * std;
    }

    /**
     * Bins elements into bins with specified centers
     * @param data The data to bin
     * @param centers The bins where each value represents a center of a bin. They MUST
     *             be ordered or the data will not be valid.
     * @param inclusive
     * @return
     */
    public static double[] hist(double[] data, double[] centers, boolean inclusive) {
        double[] histogram = new double[centers.length];
        for (double datum : data) {
            int n = Matlib.near(centers, datum, inclusive);
            if (n >= 0) {
                histogram[n] = histogram[n] + 1;
            }
        }
        return histogram;
    }

    public static double[] histc(double[] data, double[] edges) {
        double[] histogram = new double[edges.length];
        for (double datum : data) {
            int idx = Arrays.binarySearch(edges, datum);
            if (idx < 0) {
                idx = -idx - 1;
                if (idx <= 0 || idx >= edges.length) {
                    idx = -1;
                }
                else if (idx > edges.length - 2) {
                    idx = edges.length - 2;
                }
                else if (idx <= 0) {
                    idx = 0;
                }
                else {
                    idx = idx - 1;
                }
            }

            if (idx >= 0) {
                histogram[idx] = histogram[idx] + 1;
            }
        }

        return histogram;
    }


}
