/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mbari.movie;

import java.util.Date;

/**
 *
 * @author brian
 */
public class VideoTimeBean implements VideoTime {

    private Date date;
    private String timecode;

    public VideoTimeBean() { /* Default constructor */ }

    public VideoTimeBean(Date date, String timecode) {
        this.date = date;
        this.timecode = timecode;
    }

    public Date getDate() {
        return date;
    }

    public String getTimecode() {
        return timecode;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @param timecode Should be 'hh:mm:ss:ff' format. This class does not
     * check the format though
     */
    public void setTimecode(String timecode) {
        this.timecode = timecode;
    }

}
