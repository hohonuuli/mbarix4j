/*
 * JSimpleToggleButton.java
 * 
 * Created on Aug 28, 2007, 11:46:12 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mbarix4j.swing;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JToggleButton;

/**
 * A ToggleButton that is undecorated except when it has focus.
 * @author brian
 */
public class JSimpleToggleButton extends JToggleButton {
    
    // This block will be called be all the constructors
    {
        initialize();
    }

    public JSimpleToggleButton() {
        super();
    }

    public JSimpleToggleButton(Icon icon) {
        super(icon);
    }

    public JSimpleToggleButton(Icon icon, boolean selected) {
        super(icon, selected);
    }

    public JSimpleToggleButton(String text) {
        super(text);
    }

    public JSimpleToggleButton(String text, boolean selected) {
        super(text, selected);
    }

    public JSimpleToggleButton(Action a) {
        super(a);
    }

    public JSimpleToggleButton(String text, Icon icon) {
        super(text, icon);
    }

    public JSimpleToggleButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
    }
    
    
    
    /**
     * Setup the button so that it toggles the border on mose over.
     *
     */
    private void initialize() {
        setBorderPainted(false);
        setFocusPainted(true);
        setContentAreaFilled(false);
        addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setBorderPainted(isEnabled());
                setContentAreaFilled(isEnabled());
            }
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setBorderPainted(false);
                setContentAreaFilled(false);
            }

        });
        
        addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                setBorderPainted(true && isEnabled());
                setContentAreaFilled(isEnabled());
            }
            
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                setBorderPainted(false);
                setContentAreaFilled(false);
            }
        });
    }

}
