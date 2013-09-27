/*
 * @(#)Timecode.java   2012.04.27 at 08:56:36 PDT
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



package org.mbari.movie;

import org.mbari.util.IObservable;
import org.mbari.util.IObserver;
import org.mbari.util.ObservableSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * <p>
 * Represents video time code information based on frames. The format of time codes are
 * hh:mm:ss:ff where hh = hour, mm = minute, ss = second and ff = frame.
 * </p>
 *
 * <p>
 * <font color="ff3333">This class is not persisted; however the string contents
 * (hh:mm:ss:ff) are persisted from other classes</font>.
 * </p>
 * <hr>
 *
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: Timecode.java 332 2006-08-01 11:38:46 -0700 (Tue, 01 Aug 2006) hohonuuli $
 */
public class Timecode implements Comparable, IObservable {

    /**
     * Displayed when no valid timecode is available
     */
    public static final String EMPTY_TIMECODE_STRING = "--:--:--:--";
    private static final Logger log = LoggerFactory.getLogger(Timecode.class);
    private static final NumberFormat format = new DecimalFormat() {

        {
            setMaximumFractionDigits(0);
            setMinimumIntegerDigits(2);
            setMaximumIntegerDigits(2);
        }
    };

    /**
     * The number of frames per second. Default is 30.0
     */
    private double frameRate = 30D;

    /**
     * Indicates what the time code represents
     */
    private Representation representation = Representation.TIMECODE;
    private final ObservableSupport os = new ObservableSupport();

    /** Indicates if the timecode object contains valid values */
    protected boolean isValid = false;
    private int frame;

    /**
     * This is the primary value used for all time calculations
     */
    private double frames;

    /* time fields arerecalculated when the frames or frameRate members are changed */
    private int hour;
    private int minute;
    private int second;

    /**
     * Enum description
     *
     */
    public enum Representation { TIMECODE, RUNTIME }

    /**
     * Default constructor. Sets frameRate to 30 and frames to 0.
     */
    public Timecode() {

        // Do nothing
    }

    /**
     * Create timecode object using frames provided and frameRate = 30
     * @param frames The frame count to initialize with.
     */
    public Timecode(double frames) {
        setFrames(frames);
    }

    /**
     * Create a new video time code from a String.
     *
     * @param timecode
     *            A String of the format hh:mm:ss:ff representing the video time
     *            code.
     * @exception NumberFormatException
     *                Description of the Exception
     */
    public Timecode(String timecode) throws NumberFormatException {
        setTimecode(timecode);
    }

    /**
     * Constructor for the Timecode object
     *
     * @param timecode
     *            Description of the Parameter
     */
    public Timecode(Timecode timecode) {
        this(timecode.getFrames(), timecode.getFrameRate());
    }

    /**
     * Initialize a timecode object
     *
     * @param frames The initial frame cound
     * @param frameRate The initial frameRate
     */
    public Timecode(double frames, double frameRate) {
        setFrameRate(frameRate);
        setFrames(frames);
    }

    /**
     * Constructs ...
     *
     * @param timecode
     * @param frameRate
     *
     * @throws NumberFormatException
     */
    public Timecode(String timecode, double frameRate) throws NumberFormatException {
        setFrameRate(frameRate);
        setTimecode(timecode);
    }

    /**
     * Adjust the timecode values by adding frames
     * @param frames The number of frames to increment
     */
    public void addFrames(double frames) {
        this.frames += frames;
    }

    /**
     * Method description
     *
     *
     * @param observer
     */
    public void addObserver(final IObserver observer) {
        os.add(observer);
    }

    /**
     * Compare this <code>Timecode</code> with the passed handle of some other
     * <code>Timecode</code> object.
     *
     *
     *
     *
     * @param object
     *            The <code>Timecode</code> against which to compare.
     * @return An integer less than 0 if this
     *         <code>TTimecode/code> is less than the
     *  argument; an integer equal to 0 if this <code>TTimecode/code> is equal
     *  to the argument; an integer greater than 0 if this <code>TTimecode/code>
     *  is greater than the argument.
     */
    public int compareTo(final Object object) {
        return (int) diffFrames((Timecode) object);
    }

    /**
     * Calculate the difference in number of frames between this
     * <code>Timecode</code> and that of the passed handle of some other
     * <code>Timecode</code> object.
     *
     *
     *
     *
     * @param that
     *            The other timecode
     * @return The number of frames, which will be greater than 0 if this
     *         <code>TTimecode/code> is greater than the passed argument
     *  <code>TTimecode/code>, etc.
     */
    public double diffFrames(final Timecode that) {
        return this.getFrames() - that.getFrames();
    }

    /**
     * Compare this <code>Timecode</code> with an Object, either another
     * <code>Timecode</code> or a String.
     *
     *
     *
     *
     * @param object
     *            The comparison Object.
     * @return <code><code>true</code></code> if the passed Object is equal
     *         to this
     *         <code>TTimecode/code>, <code><code>false</code></code> otherwise.
     */
    public boolean equals(final Object object) {
        return diffFrames((Timecode) object) == 0D;
    }

    /**
     * Gets the time code frame number.
     * @return  The integer frame number in range 0-29, inclusive.
     */
    public int getFrame() {
        return frame;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public double getFrameRate() {
        return frameRate;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public double getFrames() {
        return frames;
    }

    /**
     * Gets the time code hour.
     * @return  The integer hour in range 0-23, inclusive.
     */
    public int getHour() {
        return hour;
    }

    /**
     * Gets the time code minute.
     * @return  The integer minute in range 0-59, inclusive.
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public Representation getRepresentation() {
        return representation;
    }

    /**
     * Gets the time code second.
     * @return  The integer second in range 0-59, inclusive.
     */
    public int getSecond() {
        return second;
    }

    /**
     * Hash code for this <code>Timecode</code> object. The hash code is
     * calculated via the hh:mm:ss:ff String representation.
     *
     * @return An integer hash code for this <code>Timecode</code> object.
     */
    public int hashCode() {
        return (int) (this.getFrames() / this.getFrameRate());
    }

    void notifyObservers() {
        os.notify(this, null);
    }

    /**
     * Method description
     *
     */
    public void removeAllObservers() {
        os.clear();
    }

    /**
     * Method description
     *
     *
     * @param observer
     */
    public void removeObserver(final IObserver observer) {
        os.remove(observer);
    }

    /**
     * Settign the frame rate helps to calculate time. For most VARS operations the framerate is 30 fps. Changing
     * the frameRate will trigger a recalculation of the 'frames'.
     * @param frameRate The rate in frames per second
     */
    public void setFrameRate(double frameRate) {
        if (frameRate != this.frameRate) {
            String timecode = toString();
            this.frameRate = frameRate;
            setTimecode(timecode);
        }
    }

    /**
     * Method description
     *
     *
     * @param f
     */
    public void setFrames(double f) {
        this.frames = f;
        double frameRate = getFrameRate();
        hour = (int) Math.floor((f / 60.0 / 60.0 / frameRate));
        f = f - (double) hour * 60.0 * 60.0 * frameRate;
        minute = (int) Math.floor(f / 60.0 / frameRate);
        f = f - (double) minute * 60.0 * frameRate;
        second = (int) Math.floor(f / frameRate);
        frame = (int) Math.floor(f - (double) second * frameRate);
        isValid = true;
        notifyObservers();
    }

    /**
     * Method description
     *
     * @param representation
     */
    public void setRepresentation(Representation representation) {
        this.representation = representation;
    }

    /**
     * Method description
     *
     *
     * @param h
     * @param m
     * @param s
     * @param f
     */
    public void setTime(int h, int m, int s, int f) {
        setFrames(((h * 60 + m) * 60 + s) * getFrameRate() + f);
    }

    /**
     * Set time code from a String.
     *
     * @param timecode
     *            A String of the format hh:mm:ss:ff representing the video time
     *            code.
     */
    public void setTimecode(final String timecode) {
        try {
            final int h = Integer.parseInt(timecode.substring(0, 2));
            final int m = Integer.parseInt(timecode.substring(3, 5));
            final int s = Integer.parseInt(timecode.substring(6, 8));
            final int f = Integer.parseInt(timecode.substring(9, 11));
            setTime(h, m, s, f);
        }
        catch (Exception e) {
            isValid = false;

            if (log.isWarnEnabled()) {
                log.warn("Illegal TimeCode format: '" + timecode + "'");
            }
        }
    }

    /**
     * String representation of this <code>Timecode</code> object. This
     * representation is fixed and will not change so a developer can depend on
     * this fomrat to remain constant.
     *
     * @return A String representation of this <code>Timecode</code> object.
     *         The format is <code>hh:mm:ss:ff</code>.
     */
    public String toString() {
        String out = EMPTY_TIMECODE_STRING;
        if (isValid) {
            final StringBuffer b = new StringBuffer();
            b.append(format.format(getHour())).append(':');
            b.append(format.format(getMinute())).append(':');
            b.append(format.format(getSecond())).append(':');
            b.append(format.format(getFrame()));
            out = b.toString();
        }

        return out;
    }
}
