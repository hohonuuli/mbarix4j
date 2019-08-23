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
 * Created on Dec 4, 2003
 * @author achase
 */
package mbarix4j.swing;

import java.text.DateFormat;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JPanel;

//~--- classes ----------------------------------------------------------------

/**
 * A panel that contains a clock whose time is updated every 1000 milliseconds
 *
 * @author achase
 */
public class ClockDisplay extends JPanel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
	 * @uml.property  name="timeLabel"
	 * @uml.associationEnd  
	 */
    private javax.swing.JLabel timeLabel = null;
    /**
	 * @uml.property  name="stopClock"
	 */
    private boolean stopClock = false;
    /**
	 * @uml.property  name="dateFormat"
	 */
    private DateFormat dateFormat = null;

    //~--- constructors -------------------------------------------------------

    /**
     * This is the default constructor
     */
    public ClockDisplay() {
        super();
        initialize();
    }

    //~--- get methods --------------------------------------------------------

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    private String getCurrentTime() {
        return getDateFormat().format(new Date());
    }

    /**
	 * The <tt>DateFormat</tt> being used to format the time.
	 * @return  The <tt>DateFormat</tt> object being used to format. The default  object returend uses <tt>DateFormat.MEDIUM</tt> for both date and time, and  uses <tt>DateFormat</tt> object returned by a call to <tt>DateFormat.getDateTimeInstance</tt>
	 * @uml.property  name="dateFormat"
	 */
    public DateFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                    DateFormat.MEDIUM);
        }

        return dateFormat;
    }

    /**
	 * This method initializes timeLabel
	 * @return  javax.swing.JLabel
	 * @uml.property  name="timeLabel"
	 */
    private javax.swing.JLabel getTimeLabel() {
        if (timeLabel == null) {
            timeLabel = new javax.swing.JLabel();
            timeLabel.setText("JLabel");
            SwingWorker worker = new SwingWorker() {

                public Object construct() {
                    // let this worker run forever
                    while (!stopClock) {
                        timeLabel.setText(getCurrentTime());
                        timeLabel.repaint();

                        try {
                            Thread.sleep(1000);
                        }
                        // don't sweat it if the thread is interrupted
                        catch (InterruptedException ie) {
                            System.out.println("time thread interruped");
                        }
                    }

                    return null;
                }
            };
            worker.start();
        }

        return timeLabel;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        this.setLayout(new java.awt.BorderLayout());
        this.add(getTimeLabel(), java.awt.BorderLayout.CENTER);
        this.setSize(204, 28);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Is the clock stopped?
     * @return true if the clock is stopped, false if it is running
     */
    public boolean isStopClock() {
        return stopClock;
    }

    //~--- methods ------------------------------------------------------------

    /* oo-la-la, glamorous test code! */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Clock Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 500, 500);
        frame.getContentPane().add(new ClockDisplay());
        frame.setVisible(true);
    }

    //~--- set methods --------------------------------------------------------

    /**
	 * Set the <tt>DateFormat</tt> object to be used in formatting the time shown in the clock.
	 * @param format  The format to use for the date/time displayed.
	 * @uml.property  name="dateFormat"
	 */
    public void setDateFormat(DateFormat format) {
        dateFormat = format;
    }

    /**
	 * Set the clock to stopped or running
	 * @param  b
	 * @uml.property  name="stopClock"
	 */
    public void setStopClock(boolean b) {
        stopClock = b;
    }

}    // @jve:visual-info  decl-index=0 visual-constraint="199,194"

