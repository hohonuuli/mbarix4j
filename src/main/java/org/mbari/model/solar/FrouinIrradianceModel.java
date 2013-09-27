package org.mbari.model.solar;

import java.io.IOException;
import java.util.Calendar;
import org.mbari.util.GmtCalendar;
import org.mbari.solar.SolarPosition;
import org.mbari.solar.SolarUtil;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c)
 * Company:
 * @author
 * @version 1.0
 */
public class FrouinIrradianceModel {

    public FrouinIrradianceModel(double[] lambda) {
        this.wavelength = lambda;
        this.E0 = SolarUtil.getNeckelLabIrradiance(lambda);
        try {
            this.kO3 = SolarUtil.getOzoneAbsorption(lambda);
        }
        catch (IOException e) {
        }
    }

    /**
     *   This subroutine calculates the incoming clear-sky solar irradiance
     * just above the surface at SeaWiFS wavelengths (412, 443, 490, 510,
     * 555, 670 nm), using this equation:
     *
     * E = E0 * ESfactor * COS(SolZen) * TransG * TransA / (1 - (Sa * As))
     *
     *   where:
     * E0 = extra-terrestrial solar irradiance (mW/cm^2/um)
     * ESfactor = Earth-Sun distance factor
     * SolZen = solar zenith angle (deg).
     * TransG = gaseous transmittance
     * TransA = diffuse atmospheric transmittance
     * Sa = spherical albedo of the atmosphere
     * As = albedo of the ocean
     * [note: Routine estimates SolZen from input parameters.
     *  Valid for 0 -- 90 deg, but for SolZen > 80 deg,
     *  the above formula and the approximations used for the various
     *  atmospheric functions are not very accurate.]
     *
     * Routine Inputs:
     * year: 4-digit integer (y2k compliant ;-)
     * month: 2-digit integer
     * day: 2-digit integer
     * hour: decimal GMT hours, 24-hour clock
     * lat: latitude (deg)
     * lon: longitude (deg)
     * TauA865 *: aerosol optical thickness, 865 nm
     * Angstrom *: Angstrom coefficient ( convention: Angstrom > 0 )
     * Dobson *: ozone amount in Dobson units
     * [note: inputs labelled with '*' may be missing.  If so, they
     *  should be given values of -999 or less.  The subroutine will
     *  fill in appropriate default values.]
     *
     * Output:
     * E: surface solar irradiance (mW/cm^2/um)
     * [note: if the SolZen is found to be > 90 deg (sun below horizon),
     *  then routine fails cleanly.  Output 'E' values set to missing
     *  value -999 or less.  This should be checked for in the calling
     *  program.]
     *
     *
     * Sample run input (from a MOBY site):
     * 1998 3 16 23.0 20.83 -157.19 0.08 0.5 325.
     * Output:
     * year, month, day, GMT time, lat, lon, sun zenith angle,
     *   and the 6 E's.
     * 1998  3 16 23.000  20.830 -157.190 23.1
     *   133.52 154.19 162.95 159.29 156.78 135.45
     *
     *
     *      Authors: Robert Frouin <RFrouin@ucsd.edu>, scientific algorithms
     *              John McPherson <JMcPherson@ucsd.edu>, program structure
     *
     * References:
     *
     * Frouin, R., D. W. Ligner, and C. Gautier, 1989: A Simple analytical formula
     * to compute clear sky total and photosynthetically available solar irradiance
     * at the ocean surface. J. Geophys. Res., 94, 9731-9742.
     *
     * Tanre, D., M. Herman, P.-Y. Deschamps, and A. De Leffe, 1979: Atmospheric
     * modeling for Space measurements of ground reflectances, including
     * bi-directional properties. Appl. Optics, 18, 21,3587-21,3597.
     *
     */
    public double[] calcIrradiance(long millis, double lat, double lon,
            double TauA865, double Angstrom, double Dobson) {

        double[] wl = this.wavelength;
        double[] E = new double[this.wavelength.length],
                TransG = new double[this.wavelength.length],
                TransA = new double[this.wavelength.length],
                Sa = new double[this.wavelength.length],
                TauMol = new double[this.wavelength.length],
                TauAer = new double[this.wavelength.length];

        double EsFactor,
                SolZen,
                CosSolZen,
                SolAz,
                As,
                AsymFac,
                Beta,
                Tau,
                A,
                B,
                C,
                ww,
                wl4,
                wl5,
                wl6,
                Gamma;

        int month = new GmtCalendar(millis).get(Calendar.MONTH) + 1; // Java months are 0 - 11
        SolarPosition sp = new SolarPosition(millis, lat, lon);

        // Get Earth-Sun distance factor 'F':
        EsFactor = 1 / sp.getDistance();

        // Get Solar Zenith angle:
        SolZen = sp.getZenith() * 180 / Math.PI;
        SolAz = sp.getAzimuth() * 180 / Math.PI;

        if ((SolZen < 0.0) || (SolZen > 90.0)) {
            //System.out.println("Crit. Error in FrouinExtended.posSol: SolZen = " + SolZen);
            //System.out.println("  should be in range 0 -- 90 degrees");
            for (int i = 0; i < E.length; i++) {
                E[i] = -999.9;
            }
            return E;
        }

        CosSolZen = Math.cos(SolZen * Math.PI / 180.0);

        /////////////////////////////////////////////////
        // Estimate key parameters if missing from input:

        // Get Dobson climatology if necessary:
        if (Dobson <= 0) {
            Dobson = SolarUtil.estimateDobson(month, lat);
        }

        // Get default TauA865 if necessary:
        if (TauA865 <= 0) {
            TauA865 = 0.1;
        }

        /*
        Get default Angstrom coefficient (correspondinng to maritime aerosol)
        if necessary.  Also, convention is for Angstrom > 0 as input, so
        we need to switch signs in that case.
         */
        if (Angstrom <= 0) {
            Angstrom = -0.07;
        }
        else {
            Angstrom = -1.0 * Angstrom;
        }

        // Setup for transmittance & irradiance equations:
        A = 0.008435;  // A =  8.435e-03;
        B = -0.0001225; // B = -1.225e-04;
        C = 0.000140;  // C =  1.40e-04;

        AsymFac = 0.6666667;
        Beta = 0.5 * (1.0 + AsymFac);
        Gamma = 1.0 - AsymFac;

        // Calculate albedo of the ocean:
        As = 0.05 / (1.1 * Math.pow(CosSolZen, 1.4) + 0.15);

        // Loop through wavelengths to estimate Surf.Sol.Irrad.:
        for (int i = 0; i < E.length; i++) {

            // Compute gaseous transmittance:
            TransG[i] = Math.exp(-this.kO3[i] / 1000 * Dobson / CosSolZen);

            // Compute diffuse atmospheric transmittance:
            ww = wl[i] / 1000.0;
            wl4 = Math.pow(ww, 4.0);
            wl5 = ww * wl4;
            wl6 = ww * wl5;

            TauMol[i] = (A / wl4) + (B / wl5) + (C / wl6);
            TauAer[i] = TauA865 * Math.pow((wl[i] / 865.0), Angstrom);
            Tau = TauMol[i] + TauAer[i];
            TransA[i] = Math.exp(-Tau / CosSolZen) * Math.exp((0.52 * TauMol[i] + Beta * TauAer[i]) / CosSolZen);

            // Compute spherical albedo of the atmosphere:
            Sa[i] = Math.exp(-Tau) * (0.92 * TauMol[i] + Gamma * TauAer[i]);
            E[i] = E0[i] * EsFactor * CosSolZen * TransG[i] * TransA[i] / (1.0 - (Sa[i] * As));
        }

        return E;
    }

    public double[] calcIrradiance(long millis, double lat, double lon) {

        double TauA865 = -999D,
                Angstrom = -999D,
                Dobson = -999D;

        return calcIrradiance(millis, lat, lon, TauA865,
                Angstrom, Dobson);

    }

    /**
     * Haven't checked this yet
     */
    public double[] calcIrradiance(int year, int month, int day,
            double time, double lat, double lon) {

        GmtCalendar gmt = new GmtCalendar(year, month - 1, day, (int) time, 0);

        double TauA865 = -999D,
                Angstrom = -999D,
                Dobson = -999D;

        return calcIrradiance(gmt.getTime().getTime(), lat, lon, TauA865,
                Angstrom, Dobson);
    }

    public double[] getWavelength() {
        return this.wavelength;
    }
    private double[] wavelength;
    private double[] kO3;
    private double[] E0;
}
