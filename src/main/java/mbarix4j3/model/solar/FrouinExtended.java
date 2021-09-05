package mbarix4j3.model.solar;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import mbarix4j3.solar.SolarUtil;


/**
 *  Test program for subroutine to calculate clear-sky solar irradiance
 *  at the surface at the SeaWiFS wavelengths (412, 443, 490, 510, 555,
 *  670 nm).
 *
 *  Fortran source code can be found at: http://genius.ucsd.edu/~john/SeaWiFS_dir/ssi.f
 *
 */
@Deprecated
public class FrouinExtended {
   public static void main(String[] args) {

      int     year = 0,
              month = 0,
              day = 0;

      double  time = 0.0,
              lat = 0.0,
              lon = 0.0,
              TauA865 = 0.0,
              Angstrom = 0.0,
              Dobson = 0.0,
              SolZen = 0.0;

      double[] E      = new double[6],
               lambda = {412,0, 443.0, 490.0, 510.0, 555.0, 670.0};

      boolean flag = false;

      if (args.length == 0) {
         flag = false;
         FrouinExtended.testFrouinExtended();
         /*flag = true;
         double[] out;

         // Sample run:

         year     = 1998;
         month    = 3;
         day      = 16;
         time     = 23.0;
         lat      = 20.83;
         lon      = -157.19;
         TauA865  = 0.08;
         Angstrom = 0.5;
         Dobson   = 325;


         out = Frouin.posSol( month, day, time, lon, lat);
         SolZen = out[0];
         E      = calcSurfSolIrrad( year, month, day, time, lat, lon, TauA865, Angstrom, Dobson);*/
      } else if (args.length == 9) {
         flag = true;
         year  = Integer.parseInt(args[0]);
         month = Integer.parseInt(args[1]);
         day   = Integer.parseInt(args[2]);

         time  = Double.parseDouble(args[3]);
         lat   = Double.parseDouble(args[4]);
         lon   = Double.parseDouble(args[5]);

         TauA865 =  Double.parseDouble(args[6]);
         Angstrom =  Double.parseDouble(args[7]);
         Dobson =  Double.parseDouble(args[8]);

         E      = calcSurfSolIrrad(lambda, year, month, day, time, lat, lon, TauA865, Angstrom, Dobson);

      } else if (args.length == 6) {
         flag  = true;
         year  = Integer.parseInt(args[0]);
         month = Integer.parseInt(args[1]);
         day   = Integer.parseInt(args[2]);

         time  = Double.parseDouble(args[3]);
         lat   = Double.parseDouble(args[4]);
         lon   = Double.parseDouble(args[5]);

         E = FrouinExtended.calcSurfSolIrrad(lambda, year, month, day, time, lat, lon);

      } else if (args.length == 3) {
         flag = true;
         long   UTC = Long.parseLong(args[0]);
         lat = Double.parseDouble(args[1]);
         lon = Double.parseDouble(args[2]);
         E   = FrouinExtended.calcSurfSolIrrad(lambda, UTC, lat, lon);
      }

      if (flag) {
         System.out.println("        Date: " + month + "/" + day + "/" + year + " " + time);
         System.out.println("    Latitude: " + lat + "   Longitude: " + lon);
         System.out.println("Solar Zenith: " + SolZen);
         for (int i = 0; i < E.length; i++) {
            System.out.println("       " + lambda[i] + "[nm]: " + E[i]);
         }
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
    * time: decimal GMT hours, 24-hour clock
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
    public static double[] calcSurfSolIrrad(double[] wl, int year, int month, int day,
       double time, double lat, double lon, double TauA865, double Angstrom,
       double Dobson) {

       //Integer*4   year, month, day, i
       //Real*4   time, lat, lon, TauA865, Angstrom, Dobson, E(6)
       //Real*4   E0(6), ESfactor, SolZen, CosSolZen, SolAz
       //Real*4   TransG(6), TransA(6), Sa(6), As, kO3(6), wl(6)
       //Real*4   AsymFac, Beta, Tau, TauMol(6), TauAer(6), A, B, C
       //Real*4   ww, wl4, wl5, wl6, Gamma

       double[] E0 = FrouinExtended.getE0(wl),
               E      = new double[wl.length],
               TransG = new double[wl.length],
               TransA = new double[wl.length],
               Sa     = new double[wl.length],
               kO3    = FrouinExtended.getKO3(wl),
               //kO3    = {0.0, 3.0e-06, 2.2e-05, 4.1e-05, 9.5e-05, 4.6e-05},
               //kO3    = {0.0, 0.000003, 0.000022, 0.000041, 0.000095, 0.000046},
               //wl     = {412.0, 443.0, 490.0, 510.0, 555.0, 670.0}, //Default: wavelength in nm.  Note that microns used in TauMol calc.
               TauMol = new double[wl.length],
               TauAer = new double[wl.length];

       double  EsFactor,
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


         // Get Earth-Sun distance factor 'F':
         EsFactor = FrouinExtended.varSol(day, month);

         // Get Solar Zenith angle:
         double[] tmp;
         tmp    = FrouinExtended.posSol( month, day, time, lon, lat);
         SolZen = tmp[0];
         SolAz  = tmp[1];

         if ( (SolZen < 0.0) || (SolZen > 90.0) ) {
            //System.out.println("Crit. Error in FrouinExtended.posSol: SolZen = " + SolZen);
            //System.out.println("  should be in range 0 -- 90 degrees");
            for(int i = 0; i < 6; i++) {
               E[i] = -999.9;
            }
            return E;
         }

         // CosSolZen = COSD( SolZen )
         CosSolZen = Math.cos(SolZen * Math.PI / 180.0);

         /////////////////////////////////////////////////
         // Estimate key parameters if missing from input:

         // Get Dobson climatology if necessary:
         if (Dobson <= 0) {
            Dobson = estimDobson(month, lat);
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
         } else {
            Angstrom = -1.0 * Angstrom;
         }

         // Setup for transmittance & irradiance equations:
         A =  0.008435;  // A =  8.435e-03;
         B = -0.0001225; // B = -1.225e-04;
         C =  0.000140;  // C =  1.40e-04;

         AsymFac = 0.6666667;
         Beta    = 0.5 * ( 1.0 + AsymFac );
         Gamma   = 1.0 - AsymFac;

         // Calculate albedo of the ocean:
         As = 0.05 / ( 1.1 * Math.pow(CosSolZen, 1.4) + 0.15 );

         // Loop through wavelengths to estimate Surf.Sol.Irrad.:
         for (int i = 0; i < E.length; i++) {

            // Compute gaseous transmittance:
            TransG[i] = Math.exp( -kO3[i] * Dobson / CosSolZen);

            // Compute diffuse atmospheric transmittance:
            ww  = wl[i] / 1000.0;
            wl4 = Math.pow(ww, 4.0);
            wl5 = ww * wl4;
            wl6 = ww * wl5;

            TauMol[i] = (A / wl4) + (B / wl5) + (C / wl6);
            TauAer[i] = TauA865 * Math.pow((wl[i] / 865.0), Angstrom);
            Tau       = TauMol[i] + TauAer[i];
            TransA[i] = Math.exp( -Tau / CosSolZen ) * Math.exp( (0.52*TauMol[i] + Beta*TauAer[i]) / CosSolZen );

            // Compute spherical albedo of the atmosphere:
            Sa[i] = Math.exp( -Tau ) * (0.92 * TauMol[i] + Gamma * TauAer[i] );
            E[i] = E0[i] * EsFactor * CosSolZen * TransG[i] * TransA[i] / ( 1.0 - (Sa[i] * As) );
         }

         return E;
   }

   // Debug paramters:
   // 890089200000 20.83 -157.190
   public static double[] calcSurfSolIrrad(double[] wl, long UTC, double lat, double lon) {

      int year,
          month,
          day;

      double hour,
             fHour,
             time,
             zoneOffset,
             dstOffset;

      double[] E;

      Date     d   = new Date(UTC);
      Calendar cal = Calendar.getInstance();
      cal.setTime(d);
      cal.setTimeZone(TimeZone.getTimeZone("GMT"));
      zoneOffset   = cal.get(Calendar.ZONE_OFFSET); // Local offset in millisec.
      dstOffset    = cal.get(Calendar.DST_OFFSET);  // Daylight savings offset

      year         = cal.get(Calendar.YEAR);
      month        = cal.get(Calendar.MONTH) + 1;
      day          = cal.get(Calendar.DAY_OF_MONTH);
      hour         = cal.get(Calendar.HOUR_OF_DAY) - zoneOffset/1000.0/60.0/60.0 - dstOffset/1000.0/60.0/60.0;
      fHour        = cal.get(Calendar.MINUTE) / 60.0 + cal.get(Calendar.SECOND) / 60.0 / 60.0;

      if (hour < 0) {
         hour = 24 - hour;
         day  = day - 1;
      } else if (hour >= 24) {
         hour = hour - 24.0;
         day  = day + 1;
      }

      time = hour + fHour;

      E            = calcSurfSolIrrad(wl, year, month, day, time, lat, lon);

      d   = null;
      cal = null;

      return E;
   }

   // Debug paramters:
   // 1998 3 16 23.0 20.83 -157.190
   public static double[] calcSurfSolIrrad(double[] wl, int year, int month, int day,
      double time, double lat, double lon) {

      double TauA865  = -999D,
             Angstrom = -999D,
             Dobson   = -999D;

      double[] E;

      E = calcSurfSolIrrad(wl, year, month, day, time, lat, lon, TauA865,
         Angstrom, Dobson);

      return E;
   }

   /**
    * Subroutine from 6S package to get Earth-Sun distance correction
    *
    * calculation of the variability of the solar constant during the year.
    * jday is the number of the day in the month
    * dsol is a multiplicative factor to apply to the mean value of
    * solar constant
    */
    public static double varSol(int jday, int month) {

      //subroutine varsol (jday,month,dsol)

      double dsol,
             pi,
             om;

      int    j;

      if (month <= 2) {
         j = 31 * (month - 1) + jday;
      } else if (month > 8) {
         j = 31 * (month - 1) - ((month - 2) / 2) - 2 + jday;
      } else {
         j = 31 * (month - 1) - ((month - 1) / 2) - 2 + jday;
      }

      pi   = Math.PI;                                           // pi = 2.0 * Math.acos(0.0);
      om   =(0.9856 * ((double) j - 4.0D)) * pi / 180.0;
      dsol = 1.0 / Math.pow((1.0 - 0.01673 * Math.cos(om)), 2);

      return dsol;
   }

   /**
    * Subroutine from 6S package to get Sun position
    */
   public static double[] posSol(int month, int jday, double tu, double xlon, double xlat) {

      /*
        subroutine possol (month,jday,tu,xlon,xlat,asol,phi0)
       */

      double asol,
             phi0;

      int ia,
          nojour;

      double[] out;

      /*
         solar position (zenithal angle asol,azimuthal angle phi0 in degrees)
         jday is the number of the day in the month
       */

      ia     = 0;
      nojour = dayNumber(jday, month, ia);

      out = posFft (nojour, tu, xlon, xlat);
      asol = out[0];
      phi0 = out[1];

      if (asol > 90) {
         //System.err.println("The sun is not raised");
      }

      return out;
   }

   public static double mod(double x, double y) {
      double m = x;
      if (y != 0) {
         m = x - y * Math.floor(x / y);
      }
      return m;
   }

   public static int sign(double x) {
      int s = 0;
      if (x > 0) {
         s = 1;
      } else if (x < 0) {
         s = -1;
      }
      return s;
   }

   /**
    * subroutine day_number(jday,month,ia,j)
    */
   public static int dayNumber(int jday, int month, int ia) {

      int j;

      if (month <= 2) {
         j = 31 * (month - 1) + jday;
      } else if (month > 8) {
         j = 31 * (month - 1) - ((month - 2) / 2) - 2 + jday;
      } else {
         j = 31 * (month - 1) - ((month - 1) / 2) - 2 + jday;
      }

      if ((ia != 0) & (FrouinExtended.mod(ia, 4) == 0)) {
         j = j + 1;
      }
      return j;
   }

   /**
    *     subroutine pos_fft (j,tu,xlon,xlat,asol,phi0)
    *
    *     parameter (pi=3.14159265,fac=pi/180.)
    *     solar position (zenithal angle asol,azimuthal angle phi0
    *                     in degrees)
    *     j is the day number in the year
    *
    *    mean solar time (heure decimale)
    */
    public static double[] posFft(int j, double tu, double xlon, double xlat) {
       /*
        *     real    tu, xlat, asol,phi0, tsm, xlon,xla, xj, tet,
        *             a1, a2, a3, a4, a5, et, tsv, ah, b1, b2, b3, b4,
        *             b5, b6, b7, delta, amuzero, elev, az, caz, azim, pi2
        *     integer j
        */
       double asol,
              phi0,
              tsm,
              xla,
              xj,
              tet,
              a1,
              a2,
              a3,
              a4,
              a5,
              et,       // Time (mn.dec)
              tsv,      // True solar time
              ah,       // Hour Angle
              b1,
              b2,
              b3,
              b4,
              b5,
              b6,
              b7,
              delta,    // solar declination   (in radians)
              amuzero,
              elev,
              az,
              caz,
              azim,
              pi2,
              pi = Math.PI,
              fac = pi/180.0;

      tsm = tu + xlon / 15.0;
      xla = xlat * fac;
      xj  = (double) j;
      tet = 2.0 * pi * xj / 365.0;

      // time equation (in mn.dec)
      a1 = 0.000075;
      a2 = 0.001868;
      a3 = 0.032077;
      a4 = 0.014615;
      a5 = 0.040849;
      et = a1 + a2 * Math.cos(tet) - a3 * Math.sin(tet) - a4 * Math.cos(2.0 * tet) - a5 * Math.sin(2.0 * tet);
      et = et * 12.0 * 60.0 / pi;

      // true solar time
      tsv = tsm + et / 60.0;
      tsv = tsv - 12.0;

      // hour angle
      ah  = tsv * 15.0 * fac;

      // solar declination   (in radian)
      b1 = 0.006918;
      b2 = 0.399912;
      b3 = 0.070257;
      b4 = 0.006758;
      b5 = 0.000907;
      b6 = 0.002697;
      b7 = 0.001480;
      delta = b1 - b2 * Math.cos(tet) + b3 * Math.sin(tet) - b4 * Math.cos(2.0 * tet)
         + b5 * Math.sin(2.0 * tet)- b6 *  Math.cos(3.0 * tet) + b7 * Math.sin(3.0*tet);

      // elevation,azimuth
      amuzero = Math.sin(xla) * Math.sin(delta) + Math.cos(xla) * Math.cos(delta) * Math.cos(ah);
      elev    = Math.asin(amuzero);
      az      = Math.cos(delta) * Math.sin(ah) / Math.cos(elev);
      if (Math.abs(az) - 1.0 > 0) {
         az = (double) FrouinExtended.sign(az); //if ( (abs(az)-1.000).gt.0.00000) az = sign(1.,az)
      }

      caz  = (-Math.cos(xla) * Math.sin(delta) + Math.sin(xla) * Math.cos(delta) *
         Math.cos(ah)) / Math.cos(elev);
      azim = Math.asin(az);
      if (caz <= 0) {
         azim = pi - azim;
      }
      if ((caz > 0) & (az <= 0)) {
         azim = 2 * pi + azim;
      }

      azim = azim + pi;
      pi2  = 2.0 * pi;
      if (azim > pi2) {
         azim = azim - pi2;
      }
      elev = elev * 180.0 / pi;

      // conversion in degrees

      asol = 90.0 - elev;
      phi0 = azim / fac;

      double[] out = {asol, phi0};
      return out;
   }


   /**
    * Estimate Dobson units from climatology, given the month and
    * latitude.  Table has 12 columns, one per month, and 35 rows,
    * from 85 N to -85, in steps of 5 deg lat.
    *
    * Note: corrected two bad PAR values in table (eg, 396 -> 296)
    *
    * Subroutine EstimDobson( month, lat, Dobson )
    */
    public static double estimDobson(int month, double lat) {

       int        i1,
                  i2;

       double     Dobson,
                  fac,
                  diffLat;
       double[][] TabDobson = new double[35][12];
       double[]   tmpDobson =
          {395, 395, 395, 395, 395,     392,     390,     387,     376,
           354, 322, 292, 269, 254,     248,     246,     247,     251,
           255, 260, 266, 271, 277,     286,     295,     306,     319,
           334, 344, 344, 338, 331,     324,     320,     316,
           433, 433, 433, 436, 432,     428,     426,     418,     402,
           374, 338, 303, 278, 261,     251,     246,     248,     250,
           254, 258, 262, 265, 270,     278,     286,     294,     303,
           313, 322, 325, 324, 317,     306,     299,     294,
           467, 470, 460, 459, 451,     441,     433,     420,     401,
           377, 347, 316, 291, 271,     260,     254,     254,     255,
           257, 259, 261, 264, 269,     277,     284,     289,     296,
           305, 312, 315, 317, 312,     305,     299,     295,
           467, 465, 462, 455, 444,     431,     421,     410,     395,
           373, 348, 325, 304, 287,     275,     267,     261,     259,
           258, 259, 260, 263, 271,     278,     284,     289,     297,
           306, 314, 318, 319, 313,     302,     302,     302,
           411, 414, 416, 415, 410,     406,     402,     394,     382,
           363, 342, 324, 307, 291,     279,     271,     264,     260,
           258, 257, 258, 264, 271,     281,     291,     303,     312,
           318, 322, 323, 322, 322,     322,     322,     322,
           371, 371, 370, 368, 367,     372,     375,     372,     360,
           341, 323, 311, 301, 290,     282,     275,     268,     263,
           259, 256, 258, 264, 273,     289,     306,     319,     327,
           328, 328, 337, 337, 337,     337,     337,     337,
           333, 332, 332, 334, 338,     346,     350,     346,     335,
           321, 310, 302, 296, 289,     284,     280,     274,     268,
           262, 259, 261, 268, 279,     295,     315,     331,     340,
           342, 338, 344, 340, 340,     340,     340,     340,
           311, 308, 308, 313, 320,     327,     330,     326,     319,
           310, 303, 298, 291, 286,     283,     281,     277,     273,
           268, 264, 266, 274, 288,     306,     327,     343,     353,
           355, 351, 339, 325, 307,     294,     294,     294,
           283, 291, 302, 308, 312,     317,     318,     313,     307,
           300, 295, 290, 284, 279,     279,     279,     278,     276,
           272, 270, 273, 282, 295,     313,     333,     348,     360,
           367, 368, 353, 324, 291,     267,     253,     230,
           299, 299, 299, 309, 315,     317,     317,     312,     302,
           291, 283, 280, 275, 270,     268,     267,     263,     263,
           265, 269, 277, 287, 301,     317,     336,     354,     371,
           387, 402, 402, 374, 333,     294,     274,     259,
           314, 314, 314, 314, 332,     332,     327,     322,     311,
           297, 284, 276, 270, 263,     261,     260,     258,     259,
           264, 270, 278, 286, 298,     311,     323,     335,     350,
           366, 381, 390, 388, 376,     357,     346,     341,
           358, 358, 358, 358, 358,     358,     353,     349,     338,
           320, 299, 281, 267, 256,     252,     251,     251,     253,
           257, 264, 272, 279, 287,     297,     307,     318,     332,
           347, 358, 365, 366, 364,     358,     356,     353};

       int n = -1;
       for (int c = 0; c < 12; c++) {
          for (int r = 0; r < 35; r++) {
             n = n + 1;
             TabDobson[r][c] = tmpDobson[n];
          }
       }

       month = month - 1;  // Java indexs arrays starting at 0
       if (lat >= 85) {
          Dobson  = TabDobson[0][month];
       } else if (lat <= -85) {
          Dobson  = TabDobson[34][month];
       } else if (lat >= 0) {
          i1      = 17 - (int) (lat / 5.0);
          i2      = i1 + 1;
          fac     = (TabDobson[i2][month] - TabDobson[i1][month]) / (-5.0);
          diffLat = lat - (90.0 - (i1 * 5.0));
          Dobson  = TabDobson[i1][month] + fac * diffLat;
       } else {
          i1      = 18 - (int) (lat / 5.0);
          i2      = i1 + 1;
          fac     = (TabDobson[i2][month] - TabDobson[i1][month]) / (-5.0);
          diffLat = lat - ( 90.0 - (i1 * 5.0) );
          Dobson  = TabDobson[i1][month] + fac * diffLat;
       }

       return Dobson;
   }

   public static void testFrouinExtended() {
    /*
     *
     *     Test program for subroutine to calculate clear-sky solar irradiance
     *     at the surface at the SeaWiFS wavelengths (412, 443, 490, 510, 555,
     *     670 nm).
     *
     *     Compile and run:
     *     % f77 -o ssi_test ssi_test.f
     *     % ./ssi_test
     *     1998  1 10  9.000   0.000    0.000 50.6  85.17 100.45 108.21 106.23 105.05  92.75
     *     1998  1 10  9.000  40.000    0.000 75.7  22.93  29.49  34.08  33.80  33.40  32.12
     *     1998  1 10 12.000   0.000    0.000 22.0 138.07 159.31 168.25 164.50 162.04 139.59
     *     1998  1 10 12.000  40.000    0.000 62.0  59.43  71.21  77.61  76.26  75.18  67.31
     *     1998  1 20  9.000   0.000    0.000 50.8  87.01 102.23 109.60 107.41 105.85  92.88
     *     1998  1 20  9.000  40.000    0.000 74.7  25.59  32.59  37.35  37.00  36.54  34.80
     *     1998  1 20 12.000   0.000    0.000 20.3 139.83 161.26 170.25 166.44 163.95 141.17
     *     1998  1 20 12.000  40.000    0.000 60.1  64.04  76.41  82.98  81.49  80.33  71.60
     *     1998  7 10  9.000   0.000    0.000 50.3  82.57  96.95 103.90 101.81 100.33  87.98
     *     1998  7 10  9.000  40.000    0.000 42.9  97.65 113.84 121.26 118.71 116.97 101.82
     *     1998  7 10 12.000   0.000    0.000 22.3 128.89 148.72 157.08 153.57 151.28 130.33
     *     1998  7 10 12.000  40.000    0.000 17.8 133.22 153.55 162.04 158.40 156.03 134.27
     *     1998  7 20  9.000   0.000    0.000 50.0  83.25  97.71 104.68 102.58 101.08  88.61
     *     1998  7 20  9.000  40.000    0.000 44.0  95.52 111.45 118.82 116.34 114.63  99.88
     *     1998  7 20 12.000   0.000    0.000 20.7 130.64 150.67 159.09 155.53 153.20 131.93
     *     1998  7 20 12.000  40.000    0.000 19.4 131.89 152.07 160.52 156.92 154.58 133.07
     *
     */

     int   year  = 1998;

     int[] month = {1, 7},          // Data month / 1, 7 /
           day   = {10, 20};        // Data day / 10, 20 /

     double[] time = {9.0, 12.0},   // Data time / 9.0, 12.0 /
              lat  = {0.0, 40.0},   // Data lat / 0.0, 40.0 /
              E    = new double[6],
              out  = new double[2];

     double   SolZen,
              TauA865 = 0.1,
              Angstrom = 1.0,
              Dobson = 300.0,
              lon = 0.0;

     DecimalFormat df1 = new DecimalFormat();
     df1.setMaximumFractionDigits(3);
     df1.setMinimumFractionDigits(3);

     DecimalFormat df2 = new DecimalFormat();
     df2.setMaximumFractionDigits(2);
     df2.setMinimumFractionDigits(2);



        //Integer*4       year, month(2), day(2), i, m, d, t, L

        //Real*4  time(2), lat(2), lon, TauA865, Angstrom, Dobson
        //Real*4  E(6), SolZen

        // Sample run:
        double[] lambda = {412, 443, 490, 510, 555, 670};
        for(int m = 0; m < 2; m++) {
           for(int d = 0; d < 2; d++) {
              for(int t = 0; t < 2; t++) {
                 for(int L = 0; L < 2; L++) {
                    out = FrouinExtended.posSol( month[m], day[d], time[t], lon, lat[L]);
                    SolZen = out[0];
                    E = FrouinExtended.calcSurfSolIrrad(lambda, year, month[m], day[d], time[t], lat[L],
                       lon, TauA865, Angstrom, Dobson);

                    System.out.print(year + " " + month[m] + " " + day[d] + " " +
                       df1.format(time[t]) + " " + df1.format(lat[L]) + " " + df1.format(lon) +
                       " " + df2.format(SolZen) + " ");
                    for(int i = 0;i < 6; i++) {
                       System.out.print(df2.format(E[i]) + " ");
                    }
                    System.out.println();
                  }
               }
            }
         }
   }

   public static double[] getE0(double[] lambda) {
      return SolarUtil.getNeckelLabIrradiance(lambda);
   }

   public static double[] getKO3(double[] lambda) {
      double[] kO3 = null;
      try {
          kO3 = SolarUtil.getOzoneAbsorption(lambda);
          for(int i = 0; i < kO3.length; i++) {
             kO3[i] = kO3[i]/1000;
          }
      } catch (IOException e) {
      }
      return kO3;
   }

}
