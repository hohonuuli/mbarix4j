package mbarix4j.swing;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/** Prevents mouse and key input to a {@link JComponent} or {@link JFrame},
 * while dimming the component and displaying a wait cursor.
 */
public class WaitIndicator extends AbstractComponentDecorator implements KeyEventDispatcher {

    /** Place the wait indicator over the entire frame. */
    public WaitIndicator(JFrame frame) {
        this(frame.getLayeredPane());
    }
    
    /** Place the wait indicator over the given component. */
    public WaitIndicator(JComponent target) {
        super(target);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        getPainter().addMouseListener(new MouseAdapter() { });
        getPainter().addMouseMotionListener(new MouseMotionAdapter() { });
        getPainter().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

    /** Remove the wait indicator. */
    @Override
    public void dispose() {
        super.dispose();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
    }

    /** Consume events targeted at our target component.  Return true to
     * consume the event.
     */
    public boolean dispatchKeyEvent(KeyEvent e) {
        return SwingUtilities.isDescendingFrom(e.getComponent(), getComponent());
    }
    
    /** The default dims the blocked component. */
    public void paint(Graphics g) {
        Color bg = getComponent().getBackground();
        Color c = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 128);
        Rectangle r = getDecorationBounds();
        g = g.create();
        g.setColor(c);
        g.fillRect(r.x, r.y, r.width, r.height);
        g.dispose();
    }
}
