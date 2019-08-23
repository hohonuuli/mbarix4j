package mbarix4j.solar;

import java.util.Calendar;
import java.util.Date;

import mbarix4j.math.DoubleMath;
import mbarix4j.util.GmtCalendar;
import mbarix4j.util.TimeUtilities;



/**
 * Checked values against MLML/OCEANS toolbox functions (which are considered truth)
 * distance came out EXACTLY as the matlab functions. Azimuth, zenith and altitude
 * did not match exactly. There was some variation in around the 7th decimal place
 * This could be due to different time handiling implementations.
 */
public class SolarPosition {
    
    private double altitude; // Solar alitude angle in radians
    private double zenith; // Solar Zenith angle in radians
    private double azimuth; // Sun azimuth in radians
    private double declination; // Declination in radians
    private double distance; // Earth-sun distance in A.U,
    private double greenwichHourAngle; //
    private final double latitude; // Observers latitude in radians
    private final double longitude; // Observers longitude in radians
    private double earthSunDistance; // in AU
    private long time;
    private double equationOfTime; // equation of time in radians
    
    

    /**
     * @param millis Time of observation in Epoch seconds (GMT)
     * @param latitude Location of observation in decimal degrees (+N/-S)
     * @param longitude Locaiton of observation in decimal degrees (-W/+E)
     */
    public SolarPosition(long millis, double latitude, double longitude) {
        this.time = millis;
        this.longitude = longitude * (Math.PI) / 180D;
        this.latitude = latitude * (Math.PI) / 180D;
        this.calculateSolarPosition();
    }

    public SolarPosition(Date date, double latitude, double longitude) {
        this(date.getTime(), latitude, longitude);
    }

    /** Adapted from PLOTephem.HPL and
     *  Internal parameter names (P10-P20) are identical to those in the
     *  HPL program, PLOTephem, from which this was extracted.
     *
     * This program agrees with Wilson (1980) and Duffet-Smith (1997)
     * W. Broenkow 21 Jul 1997
     *
     * Ported MLML/OCEANS matlab toolbox function almanac_.m
     * 17 Nov 2000; Debugged Get exact matchups with almanac_.m and altazm_.m
     */
    private void calculateSolarPosition() {

        GmtCalendar gc = new GmtCalendar(this.time);
        double hour = gc.get(Calendar.HOUR_OF_DAY);
        double minute = gc.get(Calendar.MINUTE);
        double second = gc.get(Calendar.SECOND);
        double decimalHour = hour + (minute + second / 60D) / 60D;

        double julianDate = TimeUtilities.getJulianDate(this.time) + decimalHour / 24.0; // Julian Date + hours
        double days2000 = julianDate - 2451545.0;
        double cent1900 = 1.0 + days2000 / 36525.0;
        double gmst = 6.6460656 + 2400.051262 * cent1900 + 0.0000258 * Math.pow(cent1900, 2);
        gmst = DoubleMath.rem(gmst, 24);

        double P10;
        double P11;
        double P12;
        double P13;
        double P14;
        double P15;
        double P16;
        double P17;
        double P18;
        double P19;
        double P20;
        P10 = 2 * Math.PI * (0.827362 + 0.03386319198 * days2000); // originally FRAC360*()
        P11 = 2 * Math.PI * (0.347343 - 0.00014709391 * days2000); // just convert to 2*pi radians
        P12 = 2 * Math.PI * (0.779072 + 0.00273790931 * days2000); // and make angle positive
        P13 = 2 * Math.PI * (0.993126 + 0.00273777850 * days2000); // Even without making angles
        P14 = 2 * Math.PI * (0.140023 + 0.00445036173 * days2000); // the answer should be correct.
        P15 = 2 * Math.PI * (0.053856 + 0.00145561327 * days2000);
        P16 = 2 * Math.PI * (0.056531 + 0.00023080893 * days2000);

        P17 = (6910.0 - 17.0 * cent1900) * Math.sin(P13); // all trig arguments are radians
        P17 = P17 + 72.0 * Math.sin(2.0 * P13);
        P17 = P17 - 7.0 * Math.cos(P13 - P16);
        P17 = P17 + 6.0 * Math.sin(P10);
        P17 = P17 + 5.0 * Math.sin(4.0 * P13 - 8.0 * P15 + 3.0 * P16);
        P17 = P17 - 5.0 * Math.cos(2.0 * P13 - 2.0 * P14);
        P17 = P17 - 4.0 * Math.sin(P13 - P14);
        P17 = P17 + 4.0 * Math.cos(4.0 * P13 - 8.0 * P15 + 3.0 * P16);
        P17 = P17 + 3.0 * Math.sin(2.0 * P13 - 2.0 * P14);
        P17 = P17 - 3.0 * Math.sin(P16);
        P17 = P17 - 3.0 * Math.sin(2.0 * P13 - 2.0 * P16);

        P18 = (0.39785 - 0.00021 * cent1900) * Math.sin(P12);
        P18 = P18 + (0.00003 * cent1900 - 0.01) * Math.sin(P12 - P13);
        P18 = P18 + 0.00333 * Math.sin(P12 + P13);
        P18 = P18 + 0.00004 * Math.sin(P12 + 2.0 * P13);
        P18 = P18 - 0.00004 * Math.cos(P12);
        P18 = P18 - 0.00004 * Math.sin(P11 - P12);

        P19 = (-3349.0 + 8.0 * cent1900) * Math.cos(P13);
        P19 = P19 - 14.0 * Math.cos(2.0 * P13);
        P19 = P19 - 3.0 * Math.sin(P13 - P16);
        P19 = 1.0 + P19 / 1e5;

        P20 = (-0.04129 + 0.00005 * cent1900) * Math.sin(2 * P12);
        P20 = P20 + (0.03211 - 0.00008 * cent1900) * Math.sin(P13);
        P20 = P20 + 0.00104 * Math.sin(2 * P12 - P13);
        P20 = P20 - 0.00035 * Math.sin(2 * P12 + P13) - 0.0001;
        P20 = P20 - 0.00008 * Math.sin(P11);
        P20 = P20 + 0.00007 * Math.sin(2 * P13);
        P20 = P20 + 0.00003 * Math.sin(P10);
        P20 = P20 - 0.00002 * Math.cos(P13 - P16);
        P20 = P20 + 0.00002 * Math.sin(4.0 * P13 - 8.0 * P15 + 3.0 * P16);
        P20 = P20 - 0.00002 * Math.sin(P13 - P14);
        P20 = P20 - 0.00002 * Math.cos(2.0 * P13 - 2.0 * P14);

        // I went to some trouble to discover all of this...
        // so keep the commented lines as a tutorial.
        double equationOfTime = Math.asin(P20 / Math.sqrt(P19 - P18 * P18)); // Equation of Time (radians)
        earthSunDistance = 1.00021 * Math.sqrt(P19); // Earth-Sun Distance (A.U.)
        double declination = Math.asin(P18 / Math.sqrt(P19)); // Declination (radians)
        double meanRightAscension = P12; // Mean Right Ascension (radians)
        double rightAscension = meanRightAscension + equationOfTime; // Right Ascension (radians)
        double ghaAries = (15D * (gmst + decimalHour)) * Math.PI / 180D; // Greenwich Hour Angle Aries (radians)
        double gha = ghaAries - rightAscension; // Greenwich Hour Angle Sun (radians)
        gha = DoubleMath.rem(gha, Math.PI * 2D);
        double sha = (2 * Math.PI) + rightAscension; //Siderial  Hour Angle Sun (radians)
        double sunLongitude = meanRightAscension + P17 / 3600D; // Sun Longitude
        double longitudeRadians = -this.longitude;
        double latitudeRadians = this.latitude;

        double lha = gha - longitudeRadians;
        double solarZenith = Math.acos(Math.sin(latitudeRadians) * Math.sin(declination) + Math.cos(latitudeRadians) * Math.cos(declination) * Math.cos(lha)); // THeta
        //double solarAzimuth = Math.asin(Math.sin(Math.abs(lha))) * Math.cos(declination) /
        // Math.sin(solarZenith); // Phi
        double solarAzimuth = Math.acos((-Math.sin(latitudeRadians) * Math.cos(lha) * Math.cos(declination) +
            Math.sin(declination) * Math.cos(latitudeRadians)) / Math.sin(solarZenith));

        int sign1 = DoubleMath.sign(-Math.cos(declination) * Math.sin(lha) / Math.sin(solarZenith));
        int sign2 = DoubleMath.sign(Math.sin(solarAzimuth));

        if (sign1 != sign2) {
            solarAzimuth = 2.0 * Math.PI - solarAzimuth;
        }

        double solarAltitude = Math.PI / 2D - solarZenith; // Degrees above the horizon
        this.altitude = solarAltitude;
        this.zenith = solarZenith;
        this.distance = earthSunDistance;
        this.azimuth = solarAzimuth;
        this.greenwichHourAngle = gha;
        this.declination = declination;
        this.equationOfTime = equationOfTime;
    }
    
     /**
     * @return Solar altitude angle in radians
     */
    public double getAltitude() {
        return altitude;
    }

    /**
     * @return The Sun azimuth in radians
     */
    public double getAzimuth() {
        return azimuth;
    }

    public double getDeclination() {
        return declination;
    }

    /**
     * @return The Earth-Sun distance in Astronimical Units (A.U)
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @return Greenwich Hour Angle in radians
     */
    public double getGreenwichHourAngle() {
        return greenwichHourAngle;
    }

    /**
     * @return Observers latitude in radians
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return Observers longitude in radians
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return Observation time in Epoch millisecs GMT
     */
    public long getTime() {
        return time;
    }
    
    /**
     * @return Solar zenith angle in radians
     */
    public double getZenith() {
        return zenith;
    }
    
    public double getEarthSunDistance() {
        return earthSunDistance;
    }

    public double getEquationOfTime() {
        return equationOfTime;
    }
    
}