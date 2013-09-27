/*
 * @(#)Seawater.java   2012.11.15 at 06:07:05 PST
 *
 * Copyright 2011 MBARI
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package org.mbari.ocean;

/**
 * @author Brian Schlining
 * @since 2011-12-17
 */
public class Seawater {

    /**
     * Conductivity of 42.914 [mmho cm :sup:`-1` == mS cm :sup:`-1`] at Salinity
     * 35 psu, Temperature 15 :math:`^\\circ` C [ITPS 68] and Pressure 0 db.
     *
     *References
     * ----------
     * .. [1] R.C. Millard and K. Yang 1992. "CTD Calibration and Processing Methods
     * used by Woods Hole Oceanographic Institution" Draft April 14, 1992
     * (Personal communication).
     *
     * See also (Culkin and Smith, 1980; UNESCO, 1983.)
     */
    private static double C3515 = 42.9140;

    /** Mean radius of earth  A.E. Gill. */
    private static double EARTH_RADIUS = 6371000;

    /**
     * :math:`\\Omega = \\frac{2\\pi}{\\textrm{sidereal day}}` = 7.292e-5.radians sec :sup:`-1`
     *
     * 1 sidereal day = 23.9344696 hours
     *
     * Changed to a more precise value at Groten 2004
     *
     * References
     * ----------
     * .. [1] A.E. Gill 1982. p.54  eqn 3.7.15 "Atmosphere-Ocean Dynamics" Academic
     * Press: New York. ISBN: 0-12-283522-0. page: 597
     * .. [2] Groten, E., 2004: Fundamental Parameters and Current (2004) Best
     * Estimates of the Parameters of Common Relevance to Astronomy, Geodesy, and
     * Geodynamics. Journal of Geodesy, 77, pp. 724-797.
     */
    private static double OMEGA = 7.292115e-5;

    /**
     * Acceleration of gravity [m s :sup:`2`] used by sw.swvel and bfrq without lat
     * info.
     */
    private static double GDEF = 9.8;

    /**
     * The Celcius zero point; 273.15 K.  That is T = t + T0 where T is the
     * Absolute Temperature (in degrees K) and t is temperature in degrees C.
     */
    private static double T0 = 273.15;

    /**
     * Mole-weighted average atomic weight of the elements of
     * Reference-Composition sea salt, in units of kg mol :sup:`-1`. Strictly
     * speaking, the formula below applies only to seawater of Reference Composition.
     * If molality is required to an accuracy of better than 0.1% we suggest you
     * contact the authors for further guidance.
     */
    private static double MS = 0.0314038218;

    /**
     * The "specific heat" for use with Conservative Temperature. cp0 is the ratio
     * of potential enthalpy to Conservative Temperature.
     * See Eqn. (3.3.3) and Table D.5 from IOC et al. (2010).
     */
    private static double CP0 = 3991.86795711963;

    /**
     * SSO is the Standard Ocean Reference Salinity (35.16504 g/kg.)
     *
     * SSO is the best estimate of the Absolute Salinity of Standard Seawater
     * when the seawater sample has a Practical Salinity, SP, of 35
     * (Millero et al., 2008), and this number is a fundamental part of the
     * TEOS-10 definition of seawater.
     *
     * References:
     * -----------
     * .. [1] IOC, SCOR and IAPSO, 2010: The international thermodynamic equation of
     * seawater - 2010: Calculation and use of thermodynamic properties.
     * Intergovernmental Oceanographic Commission, Manuals and Guides No. 56,
     * UNESCO (English), 196 pp. See appendices A.3, A.5 and Table D.4.
     *
     * .. [2] Millero, F. J., R. Feistel, D. G. Wright, and T. J. McDougall, 2008:
     * The composition of Standard Seawater and the definition of the
     * Reference-Composition Salinity Scale, Deep-Sea Res. I, 55, 50-72.
     * See Table 4 and section 5.
     */
    private static double SSO = 35.16504;

    /**
     * The molar gas constant = 8.314472 m :sup:`2` kg s:sup:`-21 K :sup:`-1`
     * mol :sup:`-1`.
     */
    private static double R = 8.314472;

    /**
     * The ratio of Practical Salinity, SP, to Chlorinity, 1.80655 kg/g for
     * Reference Seawater (Millero et al., 2008). This is the ratio that was used by
     * the JPOTS committee in their construction of the 1978 Practical Salinity Scale
     * (PSS-78) to convert between the laboratory measurements of seawater samples
     * (which were measured in Chlorinity) to Practical Salinity.
     *
     * References:
     * -----------
     * .. [1] Millero, F. J., R. Feistel, D. G. Wright, and T. J. McDougall, 2008:
     * The composition of Standard Seawater and the definition of the
     * Reference-Composition Salinity Scale, Deep-Sea Res. I, 55, 50-72. See section
     * 5 below Eqn. (5.5).
     */
    private static double SON_CL = 1.80655;

    /**
     * Absolute Pressure of one standard atmosphere in Pa, 101325 Pa.
     */
    private static double P0 = 101325.0;

    /**
     * This function returns the mole-weighted atomic weight of sea salt of
     * Reference Composition, which is 31.4038218 g/mol.  This has been
     * defined as part of the Reference-Composition Salinity Scale of 2008
     * (Millero et al., 2008).
     *
     * References:
     * -----------
     * .. [1] IOC, SCOR and IAPSO, 2010: The international thermodynamic equation of
     * seawater - 2010: Calculation and use of thermodynamic properties.
     * Intergovernmental Oceanographic Commission, Manuals and Guides No. 56, UNESCO
     * (English), 196 pp. See Table D.4 of this TEOS-10 Manual.
     *
     * .. [2] Millero, F. J., R. Feistel, D. G. Wright, and T. J. McDougall, 2008:
     * The composition of Standard Seawater and the definition of the
     * Reference-Composition Salinity Scale, Deep-Sea Res. I, 55, 50-72.
     * See Eqn. (5.3)
     */
    private static double ATOMIC_WEIGHT = 31.4038218;

    /**
     * This function returns the valence factor of sea salt of Reference
     * Composition, 1.2452898.  This valence factor is exact, and follows from
     * the definition of the Reference-Composition Salinity Scale 2008 of
     * Millero et al. (2008).  The valence factor is the mole-weighted square
     * of the charges, Z, of the ions comprising Reference Composition sea salt.
     *
     * References:
     * -----------
     * .. [1] IOC, SCOR and IAPSO, 2010: The international thermodynamic equation of
     * seawater - 2010: Calculation and use of thermodynamic properties.
     * Intergovernmental Oceanographic Commission, Manuals and Guides No. 56,
     * UNESCO (English), 196 pp. See Table D.4 of this TEOS-10 Manual.
     *
     * .. [2] Millero, F. J., R. Feistel, D. G. Wright, and T. J. McDougall, 2008:
     * The composition of Standard Seawater and the definition of the
     * Reference-Composition Salinity Scale, Deep-Sea Res. I, 55, 50-72.
     * See Eqn. (5.9).
     */
    private static double VALENCE_FACTOR = 1.2452898;


    private Seawater() {

        // No instantiation
    }

    /**
     * Adiabatic temperature gradient (salinity,temperature,pressure)
     * <p/>
     * From: UNESCO Tech Paper Mar Sci 44 (1983)
     *
     * @param salinity    (psu)
     * @param temperature (celsius)
     * @param pressure    (decibar)
     * @return Adiabatic temperature gradient (C/dbar)
     */
    public static double atg(double salinity, double temperature, double pressure) {
        double A0 = 3.5803E-5;
        double A1 = 8.5258E-6;
        double A2 = -6.8360E-8;
        double A3 = 6.6228E-10;
        double B0 = 1.8932E-6;
        double B1 = -4.2393E-8;
        double C0 = 1.8741E-8;
        double C1 = -6.7795E-10;
        double C2 = 8.7330E-12;
        double C3 = -5.4481E-14;
        double D0 = -1.1351E-10;
        double D1 = 2.7759E-12;
        double E0 = -4.6206E-13;
        double E1 = 1.8676E-14;
        double E2 = -2.1687E-16;

        return A0 + (A1 + (A2 + A3 * temperature) * temperature) * temperature +
               (B0 + B1 * temperature) * (salinity - (35D)) +
               (C0 + (C1 + (C2 + C3 * temperature) * temperature) * temperature +
                (D0 + D1 * temperature) *
                (salinity - (35D))) * pressure + ((E0 + (E1 + E2 * temperature) * temperature)) * pressure * pressure;
    }

    /**
     * Seawater bulk modulus
     *
     * @param salinity    (psu)
     * @param temperature (celsius)
     * @param pressure    (dbar)
     * @return Seawater bulk modulus in BARS!! (not dbar)
     */
    public static double bulkmod(double salinity, double temperature, double pressure) {

        // use constant names exactly as in UNESCO 1983

        double E0 = 19652.21;
        double E1 = 148.4206;
        double E2 = -2.327105;
        double E3 = 1.360477E-2;
        double E4 = -5.155288E-5;

        double F0 = 54.6746;
        double F1 = -0.603459;
        double F2 = 1.09987E-2;
        double F3 = -6.1670E-5;

        double G0 = 7.944E-2;
        double G1 = 1.6483E-2;
        double G2 = -5.3009E-4;

        double H0 = 3.239908;
        double H1 = 1.43713E-3;
        double H2 = 1.16092E-4;
        double H3 = -5.77905E-7;

        double I0 = 2.2838E-3;
        double I1 = -1.0981E-5;
        double I2 = -1.6078E-6;

        double J0 = 1.91075E-4;    // ADDED LAST DIGIT 18 FEB 88 WWB

        double K0 = 8.50935E-5;
        double K1 = -6.12293E-6;
        double K2 = 5.2787E-8;

        double M0 = -9.9348E-7;
        double M1 = 2.0816E-8;
        double M2 = 9.1697E-10;

        double PRESS = pressure / 10;    // do not change input doubleues; use pressure in bars
        double KW = E0 + (E1 + (E2 + (E3 + E4 * temperature) * temperature) * temperature) * temperature;    // eq 19

        double AW = H0 + (H1 + (H2 + H3 * temperature) * temperature) * temperature;
        double BW = K0 + (K1 + K2 * temperature) * temperature;
        double A = AW + (I0 + (I1 + I2 * temperature) * temperature) * salinity + J0 * salinity * Math.sqrt(salinity);    // eq 17
        double B = BW + (M0 + (M1 + M2 * temperature) * temperature) * salinity;    // eq 18
        double F = (F0 + (F1 + (F2 + F3 * temperature) * temperature) * temperature) * salinity;
        double G = (G0 + (G1 + G2 * temperature) * temperature) * salinity * Math.sqrt(salinity);

        double KST0 = KW + F + G;                                                   // eq 16

        return KST0 + (A + B * PRESS) * PRESS;    // eq 15

    }

    /**
     * Specific volume anomaly = f(S,T,P)
     * International Equation of State of Seawater (1980)
     *
     * @param salinity    psu
     * @param temperature Celsius
     * @param pressure    decibar (Supply P = 0 to obtain delta-t)
     * @return Specific Volume Anomaly (centiliters/ton or 1E-8 m^3/kg)
     */
    public static double delta(double salinity, double temperature, double pressure) {
        return 1.0E5 * (1 / density(salinity, temperature, pressure) - 1 / density(35, 0, pressure));
    }

    /**
     *
     * @param salinity
     * @param temperature
     * @return
     */
    public static double delta(double salinity, double temperature) {
        return delta(salinity, temperature, 0D);
    }

    /**
     * DENSITY - Seawater density (S,T,P)
     * <p/>
     * International Equation of State of Seawater (1980)
     * UNESCO Tech Paper Mar Sci 44 (1983)
     * <p/>
     * Example:  density(34.567,5.00,2000) -> 1.036409
     * density(35,25,10000)      -> 1.06253817  UNESCO 44 p19
     *
     * @param S = Salinity (psu)
     * @param T = Temperature (C)
     * @param P = Pressure (dbar)
     * @return density (kg/liter)
     */
    public static double density(double S, double T, double P) {
        double A0 = 999.842594;
        double A1 = 6.793952E-2;
        double A2 = -9.095290E-3;
        double A3 = 1.001685E-4;
        double A4 = -1.120083E-6;
        double A5 = 6.536332E-9;
        double B0 = 8.24493E-1;
        double B1 = -4.0899E-3;
        double B2 = 7.6438E-5;
        double B3 = -8.2467E-7;
        double B4 = 5.3875E-9;
        double C0 = -5.72466E-3;
        double C1 = 1.0227E-4;
        double C2 = -1.6546E-6;
        double D0 = 4.8314E-4;


        double RHOW = A0 + (A1 + (A2 + (A3 + (A4 + A5 * T) * T) * T) * T) * T;    // eq 4
        double B = (B0 + (B1 + (B2 + (B3 + B4 * T) * T) * T) * T) * S;
        double C = (C0 + (C1 + C2 * T) * T) * S * Math.sqrt(S);
        double D = D0 * S * S;
        double RHO = RHOW + B + C + D;                                            // rho(S,T,0), eq 13

        // secant bulk modulus
        double KSTP = bulkmod(S, T, P);    // separate subroutine
        RHO = RHO / (1 - P / (10 * KSTP));    // eq 07

        // NOTE: 10 since K in bars; P in dbars

        return RHO / 1000;    // scale to kg/liter

    }

    /**
     * Seawater density (S, T, 0)
     * International Equation of State of Seawater (1980)
     * UNESCO Tech Paper Mar Sci 44 (1983)
     *
     * @param S = salinity    psu
     * @param T = temperature C
     * @return density (kg/liter)
     */
    public static double density(double S, double T) {
        return density(S, T, 0);
    }

    /**
     * Calculate ocean depth from measured pressure and latitude.<br>
     * <p/>
     * Example: depth_(5000,36) -> 4906.08<br>
     * depth_(1000,90) -> 9674.23  UNESCO 44 p28<br><br>
     * <p/>
     * Note:    For more accurate results an additional factor of the
     * ratio of the actual geopotential anomaly/gravity must
     * be added. This correction will be less than 2 m.<br>
     *
     * @param pressure dbar
     * @param latitude decimal degrees
     * @return
     */
    public static double depth(double pressure, double latitude) {
        double C1 = 9.72659;
        double C2 = -2.2512E-5;
        double C3 = 2.279E-10;
        double C4 = -1.82E-15;

        double G0 = 9.780318;
        double G1 = 5.2788E-3;
        double G2 = 2.36E-5;

        double GAMMA = 2.184E-6;

        double X = Math.pow(Math.sin(Math.PI * latitude / 180D), 2D);
        double GRAVITY = G0 * (1.0D + (G1 + G2 * X) * X) + (GAMMA / 2D) * pressure;
        double Z = (C1 + (C2 + (C3 + C4 * pressure) * pressure) * pressure) * pressure;

        return Z / GRAVITY;

    }


    /**
     * Freezing point of seawater. Ref: UNESCO Tech Paper Mar Sci 44 (1983)
     *
     * TODO: Create unit test
     * freeze(33, 0)  -> -1.808
     * freeze(35,500) -> -2.299  UNESCO 44 p30
     *
     * @param salinity Salinity (psu)
     * @param pressure Pressure (dbar)
     * @return Freezing point (Celsius)
     */
    public static double freeze(double salinity, double pressure) {
        double A0  =  -0.0575;
        double A1  =   1.710523E-3;
        double A2  =  -2.154996E-4;
        double B   = -7.53E-4;

        return (A0 + A1 * Math.sqrt(salinity) + A2 * salinity) * salinity + B * pressure;
    }

    /**
     * Conversion of conductivity ratio to practical salinity (R, T, P)
     * UNESCO Tech Paper Mar Sci 44 (1983)
     *
     * @param conductivity Conductivity ratio
     * @param temperature  C
     * @param pressure     (dbar)
     * @return Practical salinity (psu or ~g/kg)
     */
    public static double salinity(double conductivity, double temperature, double pressure) {
        double[] a = {
            0.0080, -0.1692, 25.3851, 14.0941, -7.0261, 2.7081
        };
        double[] b = {
            0.0005, -0.0056, -0.0066, -0.0375, 0.0636, -0.0144
        };
        double[] c = { 0.6766097, 2.00564E-2, 1.104259E-4, -6.9698E-7, 1.0031E-9 };
        double[] d = { 3.426E-2, 4.464E-4, 4.215E-1, -3.107E-3 };
        double[] e = { 2.070E-5, -6.370E-10, 3.989E-15 };
        double k = 0.0162;

        double rt = c[0] + (c[1] + (c[2] + (c[3] + c[4] * temperature) * temperature) * temperature) * temperature;    // eq 3
        double dd = 1 + (d[0] + d[1] * temperature) * temperature + (d[2] + d[3] * temperature) * conductivity;
        double ee = (e[0] + (e[1] + e[2] * pressure) * pressure) * pressure;
        double rp = 1 + ee / dd;                                                                            // eq 4
        double rr = conductivity / (rp * rt);
        double rrt = (rr > 0) ? Math.sqrt(rr) : 0D;

        double bb = b[0] + (b[1] + (b[2] + (b[3] + (b[4] + b[5] * rrt) * rrt) * rrt) * rrt) * rrt;
        double delS = (temperature - 15) * bb / (1 + k * (temperature - 15));                               // eq 2

        double s = a[0] + (a[1] + (a[2] + (a[3] + (a[4] + a[5] * rrt) * rrt) * rrt) * rrt) * rrt + delS;    // eq 1

        return (s < 0D) ? 0D : s;
    }

    /**
     *
     * @param conductivity
     * @param temperature
     * @return
     */
    public static double salinity(double conductivity, double temperature) {
        return salinity(conductivity, temperature, 0);
    }

    /**
     * Computes the potential density anomaly sigma-t
     * International Equation of State of Seawater (1980)
     * UNESCO Tech Paper Mar Sci 44 (1983)
     *
     * @param salinity    psu
     * @param temperature Celsius
     * @return potential depth anomaly (g/liter)
     */
    public static double sigmat(double salinity, double temperature) {
        return 1000D * (density(salinity, temperature, 0) - 1);
    }

    /**
     * 'Spiciness', an oceanographic variable for characterization
     * of intrusions and water masses.
     * <p/>
     * Spiceness is orthogonal to isopycnals of potential density
     * <p/>
     * Algorithmn from P. Flament, Subduction and finestructure associated
     * with upwelling filaments. Ph. D. Dissertation. University of
     * California, San Diego. Vol 32. No.10. pp.1195 to 1207. 1985
     *
     * @param salinity psu
     * @param theta    Potential temperature Celsius (see theta)
     * @param pressure decibar
     * @return
     */
    public static double spiciness(double salinity, double theta, double pressure) {
        double a = 3.1171e-1 - 2.1029e-6 * pressure - 2.7917e-9 * Math.pow(pressure, 2);
        double b = -1.0925e-2 + 7.5657e-7 * pressure + 4.9133e-11 * Math.pow(pressure, 2);
        double c = -7.3732e-2 - 4.8778e-6 * pressure + 1.0850e-9 * Math.pow(pressure, 2);

        double s0 = 35;

        return (density(s0, 0, pressure) - 1) * 1000 -
               (density((s0 - (1 + c) * (salinity - s0)), (theta + (salinity - s0) * (a + b * theta)), pressure) - 1) *
               1000;

    }

    /**
     * Sound velocity in seawater = f(S, T, P)
     * UNESCO Tech Paper Mar Sci 44 (1983)
     *
     * @param salinity    psu
     * @param temperature Celsius
     * @param pressure    dbar
     * @return Speed of sound in seawater (m/s)
     */
    public static double svel(double salinity, double temperature, double pressure) {
        double a00 = 1.389;
        double a01 = -1.262E-2;
        double a02 = 7.164E-5;
        double a03 = 2.006E-6;
        double a04 = -3.210E-8;
        double a10 = 9.4742E-5;
        double a11 = -1.2580E-5;
        double a12 = -6.4885E-8;
        double a13 = 1.0507E-8;
        double a14 = -2.0122E-10;

        double a20 = -3.9064E-7;
        double a21 = 9.1041E-9;
        double a22 = -1.6002E-10;
        double a23 = 7.9880E-12;

        double a30 = 1.100E-10;
        double a31 = 6.649E-12;
        double a32 = -3.389E-13;

        double b00 = -1.922E-2;
        double b01 = -4.42E-5;

        double b10 = 7.3637E-5;
        double b11 = 1.7945E-7;

        double c00 = 1402.388;
        double c01 = 5.03711;
        double c02 = -5.80852E-2;
        double c03 = 3.3420E-4;
        double c04 = -1.4780E-6;
        double c05 = 3.1464E-9;

        double c10 = 0.153563;
        double c11 = 6.8982E-4;
        double c12 = -8.1788E-6;
        double c13 = 1.3621E-7;
        double c14 = -6.1185E-10;

        double c20 = 3.1260E-5;
        double c21 = -1.7107E-6;
        double c22 = 2.5974E-8;
        double c23 = -2.5335E-10;
        double c24 = 1.0405E-12;

        double c30 = -9.7729E-9;
        double c31 = 3.8504E-10;
        double c32 = -2.3643E-12;

        double d00 = 1.727E-3;
        double d10 = -7.9836E-6;

        double p2 = pressure / 10;    // use pressure in bars

        double cw = c00 +
            (c01 + (c02 + (c03 + (c04 + c05 * temperature) * temperature) * temperature) * temperature) * temperature +
            ((c10 + (c11 + (c12 + (c13 + c14 * temperature) * temperature) * temperature) * temperature) +
             ((c20 + (c21 + (c22 + (c23 + c24 * temperature) * temperature) * temperature) * temperature) +
              (c30 + (c31 + c32 * temperature) * temperature) * p2) * p2) * p2;

        double a = a00 + (a01 + (a02 + (a03 + a04 * temperature) * temperature) * temperature) * temperature +
            ((a10 + (a11 + (a12 + (a13 + a14 * temperature) * temperature) * temperature) * temperature) +
             ((a20 + (a21 + (a22 + a23 * temperature) * temperature) * temperature) +
              (a30 + (a31 + a32 * temperature) * temperature) * p2) * p2) * p2;

        double b = b00 + b01 * temperature + (b10 + b11 * temperature) * p2;
        double d = d00 + d10 * p2;

        return cw + (a + b * Math.sqrt(salinity) + d * salinity) * salinity;

    }

    /**
     * Local potential temperature at the reference pressure = f(S, T, P)
     * UNESCO Tech Paper Mar Sci 44 (1983)
     *
     * @param salinity    psu
     * @param temperature Celsius
     * @param p0          (decibar)
     * @param pr          Reference pressure (decibar)
     * @return Potential Temperature at the reference pressure (Celsius)
     */
    public static double theta(double salinity, double temperature, double p0, double pr) {
        double p = p0;
        double t2 = temperature;    // the input temperature in changed iteratively
        double h = pr - p;
        double xk = h * atg(salinity, t2, p);
        t2 = t2 + 0.5 * xk;
        double q = xk;
        p = p + 0.5 * h;
        xk = h * atg(salinity, t2, p);
        t2 = t2 + 0.29289322 * (xk - q);
        q = 0.58578644 * xk + 0.121320344 * q;
        xk = h * atg(salinity, t2, p);
        t2 = t2 + 1.70710678 * (xk - q);
        q = 3.414213562 * xk - 4.121320344 * q;
        p = p + 0.5 * h;
        xk = h * atg(salinity, t2, p);

        return t2 + (xk - 2D * q) / 6D;
    }

    /**
     *
     * @param salinity
     * @param temperature
     * @param p0
     * @return
     */
    public static double theta(double salinity, double temperature, double p0) {
        return theta(salinity, temperature, p0, 0D);
    }
}
