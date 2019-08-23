/*
 * @(#)DateConverter.java   2011.12.27 at 07:58:15 PST
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



package mbarix4j.util;

import mbarix4j.math.Matlib;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utilities for converting between diferent types of commonly used
 * scientific date formats
 *
 * @author Brian Schlining
 * @revision 1.0.0, 19 Jul 1999
 */
public class DateConverter {

    /**
     *
     * @param date
     * @return
     */
    public static int dateToDayOfYear(Date date) {
        GmtCalendar gmt = new GmtCalendar(date);

        return gmt.get(Calendar.DAY_OF_YEAR);
    }

    /**
     *
     * @param millis
     * @return
     */
    public static int dateToDayOfYear(long millis) {
        GmtCalendar gmt = new GmtCalendar(millis);

        return gmt.get(Calendar.DAY_OF_YEAR);
    }

    // ///////////////////////////////////////
    // Public Static Methods

    /**
     * Convert the Day of a Year to a Date object. This method can handle days of
     * the year greater than 365 (or 366 for leap years). It simple wraps the day
     * around to the next year. For example: doyToDate(367, 1999) will return a
     * Date object for Jan 2, 2000. All times must be in GMT.
     *
     * @param dayOfYear  The decimal day of the year. Jan 01, 00:00:00 = 1
     * @param year       The year
     * @return A Date object for the dayOfYear specified
     */
    public static Date doyToDate(double dayOfYear, double year) {

        // Determine if the year is a leap year. Specify the number of days in Feb
        double feb = 28.0d;
        double max = 365d;

        if (DateConverter.isLeap(year)) {
            feb = 29.0d;
            max = 366d;
        }

        double[] daysPerMonth = {
            0d, 31d, feb, 31d, 30d, 31d, 30d, 31d, 31d, 30d, 31d, 30d, 31d
        };
        double[] days = Matlib.cumsum(daysPerMonth);

        // Handle days outside a normal year
        while (Math.floor(dayOfYear) > max) {
            dayOfYear -= max;
            year++;

            if (DateConverter.isLeap(year)) {
                max = 366d;
            }
            else {
                max = 365d;
            }
        }

        // Get the month
        int month;

        loop:
        {
            for (month = 0; month < Array.getLength(days); month++) {
                if (dayOfYear < days[month]) {
                    break loop;
                }
            }
        }

        // Get the day
        double day = dayOfYear - days[month - 1];

        // Get the hour
        double hour = (day - Math.floor(day)) * 24.0d;

        // Get the minute
        double minute = (hour - Math.floor(hour)) * 60.0d;

        // Get the second
        double second = (minute - Math.floor(minute)) * 60.0d;
        GmtCalendar gc = new GmtCalendar((int) year, (month - 1),    // In Java months are incremented 0 to 11
                                         (int) Math.floor(day), (int) Math.floor(hour), (int) Math.floor(minute),
                                         (int) Math.rint(second));

        /*
         * int tz = gc.getTimeZone().getRawOffset();
         * gc.add(Calendar.MILLISECOND, tz);
         *
         * gc.setTimeZone(TimeZone.getTimeZone("GMT"));
         */
        return gc.getTime();
    }

    /**
     * Convert the Day of a Year to a Date object
     *
     * @param dayOfYear  The decimal day of the year. Jan 01, 00:00:00 = 1
     * @param year       The year
     * @return A Date object for the dayOfYear specified
     */
    public static Date doyToDate(int dayOfYear, int year) {
        return DateConverter.doyToDate((double) dayOfYear, (double) year);
    }

    /**
     * Convert the Day of a Year to a Date object
     *
     * @param dayOfYear  The decimal day of the year. Jan 01, 00:00:00 = 1
     * @param year       The year
     * @return A Date object for the dayOfYear specified
     */
    public static Date doyToDate(float dayOfYear, int year) {
        return DateConverter.doyToDate((double) dayOfYear, (double) year);
    }

    /**
     * Convert the Day of a Year to a Date object
     *
     * @param dayOfYear  The decimal day of the year. Jan 01, 00:00:00 = 1
     * @param year       The year
     * @return A Date object for the dayOfYear specified
     */
    public static Date doyToDate(double dayOfYear, int year) {
        return DateConverter.doyToDate(dayOfYear, (double) year);
    }

    /**
     * Check to see if a year is a leap year
     *
     * @param year  The year in question
     * @return True if the year is a leap year, false otherwise
     */
    public static boolean isLeap(double year) {
        GregorianCalendar gc = new GregorianCalendar();

        return gc.isLeapYear((int) year);
    }

    /**
     * Check to see if a year is a leap year
     *
     * @param year  The year in question
     * @return True if the year is a leap year, false otherwise
     */
    public static boolean isLeap(int year) {
        return DateConverter.isLeap((double) year);
    }

    // Debugging method

    /**
     * Method declaration
     *
     *
     * @param args
     *
     * @see
     */
    public static void main(String[] args) {
        Date t = DateConverter.doyToDate(3.122d, 1999);
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss zzz");

        System.out.println(df.format(t));

        Date x = DateConverter.doyToDate(322.4444d, 1999);

        System.out.println(df.format(x));

        long y = DateConverter.serialDaysToUtc(7.303306456040278e+005);

        System.out.println(df.format(new Date(y)));
        System.out.println("pause");
    }

    /**
     * Convert from serial days, the format used by matlab,
     * (Serial days.01 jan 0000 00:00:00 = Day 1) to UTC
     * (milliseconds since 01 Jan 1970 00:00:00).
     *
     * @param serialDay The date value to be conerted
     * @return UTC time
     */
    public static long serialDaysToUtc(double serialDay) {
        return (long) ((serialDay - 719529D) * 1000D * 60D * 60D * 24D);
    }

    /**
     * Convert a date object ot serial days
     *
     * @param d The data oject to be converted
     * @return Serial date corresponding to the <i>d</i>
     */
    public static double toSerialDays(Date d) {
        return utcToSerialDays(d.getTime());
    }

    /**
     * Convert from UTC (milliseconds since 01 Jan 1970 00:00:00) to serial days,
     * the format used by matlab (Serial days. 01 jan 0000 00:00:00 = Day 1).
     *
     * @param utc time in UTC
     * @return Serial day format utilized by matlab
     */
    public static double utcToSerialDays(long utc) {
        return utcToSerialDays((double) utc);
    }

    /**
     * Convert from UTC (milliseconds since 01 Jan 1970 00:00:00) to serial days,
     * the format used by matlab (Serial days. 01 jan 0000 00:00:00 = Day 1).
     *
     * @param utc time in UTC
     * @return Serial day format utilized by matlab
     */
    public static double utcToSerialDays(double utc) {
        return utc / 1000D / 60D / 60D / 24D + 719529D;
    }
}
