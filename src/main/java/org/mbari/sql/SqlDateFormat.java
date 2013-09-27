/*
 * Copyright 2005 MBARI
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1 
 * (the "License"); you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/copyleft/lesser.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/*
 * The Monterey Bay Aquarium Research Institute (MBARI) provides this
 * documentation and code 'as is', with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from MBARI
 */
package org.mbari.sql;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

//~--- classes ----------------------------------------------------------------

/**
 * <p>Class for formatting and parsing dates in the format specifed by the
 * W3C XML Schema 2 standard</p>
 *
 *@author        <a href="http://www.mbari.org">MBARI</a>
 *@created       October 3, 2004
 *@version       $Id: SqlDateFormat.java 265 2006-06-20 05:30:09Z hohonuuli $
 *@stereotype    factory
 *@testcase      test.moos.ssds.model.TestObjectBuilder
 */
@Deprecated
public final class SqlDateFormat {

    private static Calendar calendar = new GregorianCalendar(
        TimeZone.getTimeZone("GMT"));
    private static NumberFormat numberFormat = new DecimalFormat("0000");


    static {
        numberFormat.setMaximumFractionDigits(0);
    }


    /**
     * Format a date as the standard used in XML
     *
     * @param  date  The date to convert to a string representation
     * @return       A compact GMT representation. Example 2003-05-05 16:11:44.250
     * @since        Apr 5, 2003
     */
    public static String format(Date date) {
        StringBuffer toAppendTo = new StringBuffer();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int msec = calendar.get(Calendar.MILLISECOND);
        numberFormat.setMinimumIntegerDigits(4);
        toAppendTo.append(numberFormat.format((long) year));
        numberFormat.setMinimumIntegerDigits(2);
        toAppendTo.append("-" + numberFormat.format((long) month));
        toAppendTo.append("-" + numberFormat.format((long) day));
        toAppendTo.append(" " + numberFormat.format((long) hour));
        toAppendTo.append(":" + numberFormat.format((long) minute));
        toAppendTo.append(":" + numberFormat.format((long) second));
        toAppendTo.append("." + numberFormat.format((long) msec));
        return toAppendTo.toString();
    }

    /**
     *  Description of the Method
     *
     * @param  source  Description of the Parameter
     * @return         Description of the Return Value
     */
    public Date parse(String source) {
        // Starting index of date source
        int index = 0;
        int year = 0;
        int month = 0;
        int day = 0;
        int hour = 0;
        int minute = 0;
        int second = 0;
        int msec = 0;

        // Parse out the time components
        try {
            year = Integer.parseInt(source.substring(index, index + 4));
            month = Integer.parseInt(source.substring(index + 5, index + 7)) -
                    1;
            day = Integer.parseInt(source.substring(index + 8, index + 10));
            hour = Integer.parseInt(source.substring(index + 11, index + 13));
            minute = Integer.parseInt(source.substring(index + 14,
                    index + 16));
            second = Integer.parseInt(source.substring(index + 17,
                    index + 19));
            msec = Integer.parseInt((source.substring(index + 20,
                    index + 24)));
        } catch (NumberFormatException e) {
            System.err.println(
                    getClass().getName() + ": Unable to parse '" + source +
                    "'. Format should be YYYY-MM-DD HH:MM:SS.mmmm");
        }

        // Set the calendar
        calendar.set(year, month, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, msec);
        return calendar.getTime();
    }
}
