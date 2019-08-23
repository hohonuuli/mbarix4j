package mbarix4j.util;


import java.util.*;


/**
 *  GmtCalendar is a useful class for working with times that are based using
 *  GMT time. For example, if a data loger collects info and returns it as GMT
 *  using GregorianCalendar is a pain because it will assumes that user supplied
 *  times are from the default TimeZone. This will cause an ofset in the time of
 *  each data sample. To avoid this GmtCalendar uses the GMT as it's default
 *  TimeZone. Note that it will return time in milliseconds UTC but any dates
 *  produced by using DateFormat will give the sample time based on the local
 *  time.
 *
 * @author  Brian Schlining
 * @version  30 Nov 1999
 * @created  October 31, 2000
 */
public class GmtCalendar extends GregorianCalendar {


    /**
     *  Constructs a GmtCalendar with the given date set in the GMT time zone
     *  with the default locale.
     *
     * @param  year the value used to set the YEAR time field in the calendar.
     * @param  month the value used to set the MONTH time field in the calendar.
     *      Month value is 0-based. e.g., 0 for January.
     * @param  date the value used to set the DATE time field in the calendar.
     */

    public GmtCalendar(int year, int month, int date) {

        super(TimeZone.getTimeZone("GMT"), Locale.getDefault());

        this.set(ERA, AD);

        this.set(YEAR, year);

        this.set(MONTH, month);

        this.set(DATE, date);

    }




    /**
     *  Constructs a GregorianCalendar with the given date and time set for the
     *  GMT time zone with the default locale.
     *
     * @param  year the value used to set the YEAR time field in the calendar.
     * @param  month the value used to set the MONTH time field in the calendar.
     *      Month value is 0-based. e.g., 0 for January.
     * @param  date the value used to set the DATE time field in the calendar.
     * @param  hour the value used to set the HOUR_OF_DAY time field in the
     *      calendar.
     * @param  minute the value used to set the MINUTE time field in the
     *      calendar.
     */

    public GmtCalendar(int year, int month, int date, int hour,
            int minute) {

        super(TimeZone.getTimeZone("GMT"), Locale.getDefault());

        this.set(ERA, AD);

        this.set(YEAR, year);

        this.set(MONTH, month);

        this.set(DATE, date);

        this.set(HOUR_OF_DAY, hour);

        this.set(MINUTE, minute);

    }




    /**
     *  Constructs a GregorianCalendar with the given date and time set for the
     *  GMT time zone with the default locale.
     *
     * @param  year the value used to set the YEAR time field in the calendar.
     * @param  month the value used to set the MONTH time field in the calendar.
     *      Month value is 0-based. e.g., 0 for January.
     * @param  date the value used to set the DATE time field in the calendar.
     * @param  hour the value used to set the HOUR_OF_DAY time field in the
     *      calendar.
     * @param  minute the value used to set the MINUTE time field in the
     *      calendar.
     * @param  second the value used to set the SECOND time field in the
     *      calendar.
     */

    public GmtCalendar(int year, int month, int date, int hour,
            int minute, int second) {

        super(TimeZone.getTimeZone("GMT"), Locale.getDefault());

        this.set(ERA, AD);

        this.set(YEAR, year);

        this.set(MONTH, month);

        this.set(DATE, date);

        this.set(HOUR_OF_DAY, hour);

        this.set(MINUTE, minute);

        this.set(SECOND, second);

    }


    /**
     *  Constructor for the GmtCalendar object
     *
     * @param  date Description of the Parameter
     */
    public GmtCalendar(Date date) {
        super(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        this.clear();
        this.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.setTime(date);
        this.setTime(this.getTime());
    }


    /**
     *  Constructor for the GmtCalendar object
     *
     * @param  millisec Description of the Parameter
     */
    public GmtCalendar(long millisec) {
        super(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        this.clear();
        this.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.setTimeInMillis(millisec);
        this.setTime(this.getTime());
    }


}


