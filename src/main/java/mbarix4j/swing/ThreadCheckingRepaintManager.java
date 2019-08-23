/*
 * ThreadCheckingRepaintManager.java
 * 
 * Created on Sep 7, 2007, 9:04:09 AM
 */

package mbarix4j.swing;

import java.util.Map;
import javax.swing.JComponent;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>From http://www.clientjava.com/blog/2004/08/20/1093059428000.html.</p>
 * 
 * <p>This class checks to see if you are modifying Swing components on nonswing threads.
 * It's very simple to use:</p>
 * <code>
 * javax.swing.RepaintManager.setCurrentManager(new ThreadCheckingRepaintManager());
 * </code>
 */ 
public class ThreadCheckingRepaintManager extends RepaintManager {
    
    private static final Logger log = LoggerFactory.getLogger(ThreadCheckingRepaintManager.class);
    
    @Override
    public synchronized void addInvalidComponent(JComponent jComponent) {
        checkThread();
        super.addInvalidComponent(jComponent);
    }

    private void checkThread() {
        if (!SwingUtilities.isEventDispatchThread()) {
            if (log.isWarnEnabled()) {
                Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
                StackTraceElement[] elements = map.get(Thread.currentThread());
                StringBuilder b = new StringBuilder("A swing component was modified on the Thread:");
                b.append(Thread.currentThread().getName()).append("\n");
                for (StackTraceElement element : elements) {
                    b.append("\t").append(element.toString()).append("\n");
                }
                b.append("The code should be changed so that Swing components are always modified on the swing EventDispatchThread");
            }
        }
    }

    @Override
    public synchronized void addDirtyRegion(JComponent jComponent, int i,
                                            int i1, int i2, int i3) {
        checkThread();
        super.addDirtyRegion(jComponent, i, i1, i2, i3);
    }
}
