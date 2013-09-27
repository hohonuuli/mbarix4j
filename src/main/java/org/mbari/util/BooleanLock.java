/*
 * @(#)BooleanLock.java   2011.12.10 at 09:15:58 PST
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



package org.mbari.util;

/**
 * <p>The BooleanLock class provides a useful encapsulation of a boolean variable
 * that is easily and safely accessed from multiple theads. These threads can
 * test and set an internal value and wait for it to change. The wait/notify
 * mechanism is used internally to support waiting for the value to change, and
 * frees external classes from the error-prone complexity of properly implementing this mechanism.</p>
 *
 *@author     <a href="http://www.mbari.org">MBARI</a>
 *@version    $Id: BooleanLock.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
@Deprecated
public class BooleanLock {

    /**
         * @uml.property  name="value"
         */
    private boolean value;

    /**
     * Constructor. Assigns the inital value to false.
     */
    public BooleanLock() {
        this(false);
    }

    /**
     * Constructor
     *
     * @param  initialValue  The intial value to assign to the BooleanLock
     */
    public BooleanLock(boolean initialValue) {
        value = initialValue;
    }

    /**
     * @return    true if the lock value is false
     */
    public synchronized boolean isFalse() {
        return !value;
    }

    /**
     * @return    true if the lock value is true
     */
    public synchronized boolean isTrue() {
        return value;
    }

    /**
         * Sets the value of this lock. Issues a notifyAll() call if the value is changed. (i.e the new value does not equal the current value.
         * @param  newValue
         * @uml.property  name="value"
         */
    public synchronized void setValue(boolean newValue) {
        if (newValue != value) {
            value = newValue;
            notifyAll();
        }
    }

    /**
     * Attempts to change the value of the lock to false. This method will wait
     * for the specified duration until attempting the change.
     *
     * @param  msTimeout              The time (in milliseconds) to wait before changing the value to true
     * @return                        True is the change is a success
     * @throws  InterruptedException
     */
    public synchronized boolean waitToSetFalse(long msTimeout) throws InterruptedException {
        boolean success = waitUntilTrue(msTimeout);
        if (success) {
            setValue(false);
        }

        return success;
    }

    /**
     * Attempts to change the value of the lock to true. This method will wait
     * for the specified duration until attempting the change.
     *
     * @param  msTimeout              The time (in milliseconds) to wait before changing the value to true
     * @return                        True is the change is a success
     * @throws  InterruptedException
     */
    public synchronized boolean waitToSetTrue(long msTimeout) throws InterruptedException {
        boolean success = waitUntilFalse(msTimeout);
        if (success) {
            setValue(true);
        }

        return success;
    }

    /**
     * @param  msTimeout
     * @return
     * @throws  InterruptedException
     */
    public synchronized boolean waitUntilFalse(long msTimeout) throws InterruptedException {
        return waitUntilStateIs(false, msTimeout);
    }

    /**
     * @param  state
     * @param  msTimeout
     * @return
     * @throws  InterruptedException
     */
    public synchronized boolean waitUntilStateIs(boolean state, long msTimeout) throws InterruptedException {
        if (msTimeout == 0L) {
            while (value != state) {
                wait();

                // wait indefinitely until notified
            }

            // condition has finally been met
            return true;
        }

        // only wait for the specified amount of time
        long endTime = System.currentTimeMillis() + msTimeout;
        long msRemaining = msTimeout;
        while ((value != state) && (msRemaining > 0L)) {
            wait(msRemaining);
            msRemaining = endTime - System.currentTimeMillis();
        }

        // May have timed out, or value may have met value, calculate return
        // value
        return (value == state);
    }

    /**
     * @param  msTimeout
     * @return
     * @throws  InterruptedException
     */
    public synchronized boolean waitUntilTrue(long msTimeout) throws InterruptedException {
        return waitUntilStateIs(true, msTimeout);
    }
}
