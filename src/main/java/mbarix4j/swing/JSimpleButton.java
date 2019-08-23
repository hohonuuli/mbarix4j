package mbarix4j.swing;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;


public class JSimpleButton extends JButton {
	
    public JSimpleButton() {
        super();
        initialize();
    }

    /**
     * @param a
     */
    public JSimpleButton(Action a) {
        super(a);
        initialize();
    }

    /**
     * @param icon
     */
    public JSimpleButton(Icon icon) {
        super(icon);
        initialize();
    }

    /**
     * @param text
     */
    public JSimpleButton(String text) {
        super(text);
        initialize();
    }

    /**
     * @param text
     * @param icon
     */
    public JSimpleButton(String text, Icon icon) {
        super(text, icon);
        initialize();
    }

    //~--- methods ------------------------------------------------------------

    /**
     * <p><!-- Method description --></p>
     *
     */
    private void initialize() {
        setBorderPainted(false);
        setFocusPainted(true);
        setContentAreaFilled(false);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setBorderPainted(isEnabled());
                setContentAreaFilled(isEnabled());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setBorderPainted(false);
                setContentAreaFilled(false);
            }

        });
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                setBorderPainted(true && isEnabled());
                setContentAreaFilled(isEnabled());
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                setBorderPainted(false);
                setContentAreaFilled(false);
            }
        });
    }

}
