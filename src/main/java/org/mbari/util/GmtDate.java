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


package org.mbari.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//~--- classes ----------------------------------------------------------------

/**
 * <p>Provides preset formatting for Dates. All dates are returned as GMT</p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: GmtDate.java 94 2005-12-19 17:43:29Z hohonuuli $
 */
@Deprecated
public abstract class GmtDate {

    private final static SimpleDateFormat date2dyFormatter = new SimpleDateFormat(
        "MM/dd/yy");
    private final static SimpleDateFormat dateFormatter = new SimpleDateFormat(
        "MM/dd/yyyy");
    private final static SimpleDateFormat formatter = new SimpleDateFormat(
        "EEE MMM dd yyyy DDD HH:mm:ss z");
    private final static SimpleDateFormat shortFormatter = new SimpleDateFormat(
        "MM/dd/yyyy HH:mm:ss");
    private final static SimpleDateFormat yyyydddFormatter = new SimpleDateFormat(
        "yyyyDDD");
    private static String fullString;

    //~--- methods ------------------------------------------------------------

    /**
     *  Description of the Method
     *
     * @param  date  Description of the Parameter
     * @return       Description of the Return Value
     */
    public final static String format(Date date) {
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        fullString = formatter.format(date);
        return fullString.substring(0, 32);
    }

    /**
     *  Description of the Method
     *
     * @param  epoch  Description of the Parameter
     * @return        Description of the Return Value
     */
    public final static String format(long epoch) {
        return format(new Date(epoch));
    }

    /**
     *  Description of the Method
     *
     * @param  strDate                       Description of the Parameter
     * @return                               Description of the Return Value
     * @exception  java.text.ParseException  Description of the Exception
     */
    public final static Date parseDateFormat(String strDate)
            throws java.text.ParseException {
        Date result = null;
        if (strDate.length() == 10) {
            dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            result = dateFormatter.parse(strDate);
        } else if (strDate.length() == 8) {
            date2dyFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            result = date2dyFormatter.parse(strDate);
        }

        return result;
    }

    /**
     *  Description of the Method
     *
     * @param  date  Description of the Parameter
     * @return       Description of the Return Value
     */
    public final static String shortFormat(Date date) {
        shortFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        fullString = shortFormatter.format(date);
        return fullString.substring(0, 19);
        // makes new string
    }

    /**
     *  Description of the Method
     *
     * @param  epoch  Description of the Parameter
     * @return        Description of the Return Value
     */
    public final static String shortFormat(long epoch) {
        return shortFormat(new Date(epoch));
    }

    /**
     *  Description of the Method
     *
     * @param  date  Description of the Parameter
     * @return       Description of the Return Value
     */
    public final static String yyyydddFormat(Date date) {
        yyyydddFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        fullString = yyyydddFormatter.format(date);
        return fullString.substring(0, 7);
        // makes new string
    }

    /**
     *  Description of the Method
     *
     * @param  epoch  Description of the Parameter
     * @return        Description of the Return Value
     */
    public final static String yyyydddFormat(long epoch) {
        return yyyydddFormat(new Date(epoch));
    }
}
