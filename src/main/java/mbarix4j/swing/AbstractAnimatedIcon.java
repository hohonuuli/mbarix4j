/*
 * @(#)AbstractAnimatedIcon.java   2010.01.21 at 10:07:32 PST
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



package mbarix4j.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import javax.swing.Timer;

/** Provide animation of auto-generated animations.  Makes use of the repaint
 * tracking structure established by {@link AnimatedIcon}.
 *
 */
public abstract class AbstractAnimatedIcon extends AnimatedIcon {

    private static final int DEFAULT_INTERVAL = 1000 / 24;
    private int frame;
    private int frameCount;
    private int repaintInterval;
    private Timer timer;

    protected AbstractAnimatedIcon() {
        this(0);
    }

    protected AbstractAnimatedIcon(int frameCount) {
        this(frameCount, DEFAULT_INTERVAL);
    }

    protected AbstractAnimatedIcon(int frameCount, int interval) {
        this.frameCount = frameCount;
        setFrameInterval(interval);

    }

    /** Ensure the timer stops running, so it, too can be GC'd. */
    @Override
    protected void finalize() {
        timer.stop();
    }

    /**
     * Returns the current animation frame number.
     * @return
     */
    public int getFrame() {
        return frame;
    }

    /**
     * Returns the total number of frames.
     * @return
     */
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * @return
     */
    public int getFrameInterval() {
        return repaintInterval;
    }

    /**
     * @return
     */
    public abstract int getIconHeight();

    /**
     * @return
     */
    public abstract int getIconWidth();

    /** Advance to the next animation frame. */
    public void nextFrame() {
        setFrame(getFrame() + 1);
    }

    /** Implement this method to paint the icon. */
    protected abstract void paintFrame(Component c, Graphics g, int x, int y);

    protected synchronized void registerRepaintArea(Component c, int x, int y, int w, int h) {
        if ((timer != null) && !timer.isRunning()) {
            timer.start();
        }

        super.registerRepaintArea(c, x, y, w, h);
    }

    /**
     * Set the current animation frame number.
     *
     * @param f
     */
    public void setFrame(int f) {
        this.frame = f;

        if (frameCount != 0) {
            frame = frame % frameCount;
        }

        repaint();
    }

    /**
     * Setting a frame interval of zero stops automatic animation.
     *
     * @param interval
     */
    public void setFrameInterval(int interval) {
        repaintInterval = interval;

        if (interval != 0) {
            if (timer == null) {
                timer = new Timer(interval, new AnimationUpdater(this));
                timer.setRepeats(true);
            }
            else {
                timer.setDelay(interval);
            }
        }
        else if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    private static class AnimationUpdater implements ActionListener {

        private WeakReference ref;

        /**
         * Constructs ...
         *
         * @param icon
         */
        public AnimationUpdater(AbstractAnimatedIcon icon) {
            this.ref = new WeakReference(icon);
        }

        /**
         *
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            AbstractAnimatedIcon icon = (AbstractAnimatedIcon) ref.get();
            if (icon != null) {
                icon.nextFrame();
            }
        }
    }
}
