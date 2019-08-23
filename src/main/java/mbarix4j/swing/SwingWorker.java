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


package mbarix4j.swing;

import javax.swing.SwingUtilities;

//~--- classes ----------------------------------------------------------------

/**
 *
 * <p>This is the 3rd version of SwingWorker (also known as
 * SwingWorker 3), an abstract class that you subclass to
 * perform GUI-related work in a dedicated thread.  For
 * instructions on using this class, see:
 *
 * http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html
 *
 * Note that the API changed slightly in the 3rd version:
 * You must now invoke start() on the SwingWorker after
 * creating it.</p><hr>
 *
 * @stereotype thing
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: SwingWorker.java 332 2006-08-01 18:38:46Z hohonuuli $
 *
 */
public abstract class SwingWorker {

    /**
	 * @uml.property  name="threadVar"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private ThreadVar threadVar;
    /**
	 * @uml.property  name="value"
	 */
    private Object value;    // see getValue(), setValue()

    //~--- constructors -------------------------------------------------------

    /**
     * Start a thread that will call the <code>construct</code> method
     * and then exit.
     */
    public SwingWorker() {
        final Runnable doFinished = new Runnable() {

            public void run() {
                finished();
            }
        };
        Runnable doConstruct = new Runnable() {

            public void run() {
                try {
                    setValue(construct());
                } finally {
                    threadVar.clear();
                }

                SwingUtilities.invokeLater(doFinished);
            }
        };
        Thread t = new Thread(doConstruct);
        threadVar = new ThreadVar(t);
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Compute the value to be returned by the <code>get</code> method.
     *
     * @return
     */
    public abstract Object construct();

    /**
     * Called on the event dispatching thread (not on the worker thread)
     * after the <code>construct</code> method has returned.
     */
    public void finished() {
        // No implementation
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Return the value created by the <code>construct</code> method.
     * Returns null if either the constructing thread or the current
     * thread was interrupted before a value was produced.
     *
     * @return the value created by the <code>construct</code> method
     */
    public Object get() {
        while (true) {
            Thread t = threadVar.get();
            if (t == null) {
                return getValue();
            }

            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();    // propagate
                return null;
            }
        }
    }

    /**
	 * Get the value produced by the worker thread, or null if it hasn't been constructed yet.
	 * @return
	 * @uml.property  name="value"
	 */
    protected synchronized Object getValue() {
        return value;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * A new method that interrupts the worker thread.  Call this method
     * to force the worker to stop what it's doing.
     */
    public void interrupt() {
        Thread t = threadVar.get();
        if (t != null) {
            t.interrupt();
        }

        threadVar.clear();
    }

    //~--- set methods --------------------------------------------------------

    /**
	 * Set the value produced by worker thread
	 * @param  x
	 * @uml.property  name="value"
	 */
    protected synchronized void setValue(Object x) {
        value = x;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Start the worker thread.
     */
    public void start() {
        Thread t = threadVar.get();
        if (t != null) {
            t.start();
        }
    }

    //~--- inner classes ------------------------------------------------------

    /**
     * Class to maintain reference to current worker thread
     * under separate synchronization control.
     */
    private static class ThreadVar {

        private Thread thread;

        /**
         * Constructs ...
         *
         *
         * @param t
         */
        ThreadVar(Thread t) {
            thread = t;
        }

        /**
         * <p><!-- Method description --></p>
         *
         */
        synchronized void clear() {
            thread = null;
        }

        /**
         * <p><!-- Method description --></p>
         *
         *
         * @return
         */
        synchronized Thread get() {
            return thread;
        }
    }
}
