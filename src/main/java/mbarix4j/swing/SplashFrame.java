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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//~--- classes ----------------------------------------------------------------

/**
 * <p>Splash screen for the query application. Use as:</p>
 * <pre>
 * SplashFrame f = new SplashFrame();
 * f.setMessage("Starting application");
 * f.show();
 * f.setMessage("Doing something else now");
 * f.dispose();
 * </pre>
 *
 * @author Brian Schlining
 * @version $Id: SplashFrame.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class SplashFrame extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -4993476492124824111L;
    /**
	 * @uml.property  name="imageIcon"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private final ImageIcon imageIcon;
    /**
	 * @uml.property  name="messageLabel"
	 * @uml.associationEnd  
	 */
    private JLabel messageLabel;
    /**
	 * @uml.property  name="splashPanel"
	 * @uml.associationEnd  
	 */
    private JPanel splashPanel;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs ...
     *
     *
     * @param imageIcon
     */
    public SplashFrame(ImageIcon imageIcon) {
        super();
        this.imageIcon = imageIcon;
        initialize();
    }

    //~--- get methods --------------------------------------------------------

    /**
	 * <p><!-- Method description --></p>
	 * @return
	 * @uml.property  name="messageLabel"
	 */
    private JLabel getMessageLabel() {
        if (messageLabel == null) {
            messageLabel = new JLabel(" ");
        }

        return messageLabel;
    }

    /**
	 * <p><!-- Method description --></p>
	 * @return
	 * @uml.property  name="splashPanel"
	 */
    private JPanel getSplashPanel() {
        if (splashPanel == null) {
            splashPanel = new JPanel(false);
            splashPanel.setLayout(
                    new BoxLayout(splashPanel, BoxLayout.Y_AXIS));
            splashPanel.add(new JLabel(imageIcon));
            splashPanel.add(getMessageLabel());
        }

        return splashPanel;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * <p><!-- Method description --></p>
     *
     */
    private void initialize() {
        setResizable(false);
        setUndecorated(true);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        getContentPane().add("Center", getSplashPanel());
        pack();

        /*
         * This centers the frame on the screen
         */
        setLocationRelativeTo(null);
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the message displayed at the bottom of the splash frame.
     *
     * @param msg  The message to display
     */
    public void setMessage(String msg) {
        getMessageLabel().setText(msg);
        repaint();
    }
}
