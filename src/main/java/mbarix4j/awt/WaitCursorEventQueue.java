/*
 * @(#)WaitCursorEventQueue.java   2011.12.10 at 08:50:06 PST
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



package mbarix4j.awt;

import javax.swing.SwingUtilities;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.MenuComponent;
import java.awt.MenuContainer;

/**
 * Changes the cursor to a wait cursor automatically if a task is taking a
 * long time. Use as:
 *
 * <pre>
 * int delay = 500; // milliseconds
 * Toolkit.getDefaultToolkit().getSystemEventQueue().push(new WaitCursorEventQueue(delay));
 * </pre>
 *
 * See
 * http://www.javaworld.com/javaworld/javatips/jw-javatip87.html for details.
 *
 * @author brian
 * @version $Id: WaitCursorEventQueue.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class WaitCursorEventQueue extends EventQueue {

    private int delay;

    /**
         * @uml.property  name="waitTimer"
         * @uml.associationEnd  multiplicity="(1 1)" inverse="this$0:mbarix4j.awt.WaitCursorEventQueue$WaitCursorTimer"
         */
    private WaitCursorTimer waitTimer;

    /**
     * Constructs ...
     *
     *
     * @param delay TIme in milliseconds when waiting before the cursor changes
     *  to a wait cursor.
     */
    public WaitCursorEventQueue(int delay) {
        this.delay = delay;
        waitTimer = new WaitCursorTimer();
        waitTimer.setDaemon(true);
        waitTimer.start();
    }

    @Override
    protected void dispatchEvent(AWTEvent event) {
        waitTimer.startTimer(event.getSource());

        try {
            super.dispatchEvent(event);
        }
        finally {
            waitTimer.stopTimer();
        }
    }

    private class WaitCursorTimer extends Thread {

        private Component parent;
        private Object source;

        /**
         */
        public synchronized void run() {
            while (true) {
                try {

                    // wait for notification from startTimer()
                    wait();

                    // wait for event processing to reach the threshold, or
                    // interruption from stopTimer()
                    wait(delay);

                    if (source instanceof Component) {
                        parent = SwingUtilities.getRoot((Component) source);
                    }
                    else if (source instanceof MenuComponent) {
                        MenuContainer mParent = ((MenuComponent) source).getParent();
                        if (mParent instanceof Component) {
                            parent = SwingUtilities.getRoot((Component) mParent);
                        }
                    }

                    if ((parent != null) && parent.isShowing()) {
                        parent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    }
                }
                catch (InterruptedException ie) {}
            }
        }

        synchronized void startTimer(Object source) {
            this.source = source;
            notify();
        }

        synchronized void stopTimer() {
            if (parent == null) {
                interrupt();
            }
            else {
                parent.setCursor(null);
                parent = null;
            }
        }
    }
}
