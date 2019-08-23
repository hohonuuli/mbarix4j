/*
 * @(#)DoubleMath.java   2011.12.27 at 08:33:51 PST
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

/**
 * @author Brian Schlining
 * @since 2011-12-27
 */
public class DoubleMath {

    /**  */
    public static final double TWO_PI = Math.PI * 2;
    public static final double TAU = TWO_PI;

    /**
     *
     * @param x
     * @return
     */
    public static boolean isEven(double x) {
        return rem(x, 2.0) == 0.0;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public static double mod(double x, double y) {
        double m = x;
        if (y != 0) {
            m = x - y * Math.floor(x / y);
        }

        return m;
    }

    /**
     * Adjusts an angle in radians to fall between 0 and 2 * PI
     *
     * @param angleRadians
     * @return
     */
    public static double normalizeRadianAngle(double angleRadians) {

        if (angleRadians < 0) {
            while (angleRadians < -TWO_PI) {
                angleRadians += TWO_PI;
            }
            angleRadians = TWO_PI + angleRadians;
        }
        else {
            while (angleRadians > TWO_PI) {
                angleRadians -= TWO_PI;
            }
        }

        return angleRadians;

    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    public static double rem(double x, double y) {
        double m = x;
        if (y != 0) {
            m = x - y * Matlib.fix(x / y);
        }

        return m;
    }

    /**
     *
     * @param x
     * @return
     */
    public static int sign(double x) {
        int s = 0;
        if (x > 0) {
            s = 1;
        }
        else if (x < 0) {
            s = -1;
        }

        return s;
    }
}
