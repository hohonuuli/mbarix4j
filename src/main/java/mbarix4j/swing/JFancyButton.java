/*
 * @(#)JFancyButton.java   2010.03.18 at 09:33:49 PDT
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

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * <p>A pretty button. Border decorations are turned off unless it has focus
 * or a mouseover event occurs. This button makes for a cleaner looking
 * interface. Use it as you would a <code>JButton</code></p>
 *
 * <h2><u>License</u></h2>
 * <p><font size="-1" color="#336699"><a href="http://www.mbari.org">
 * The Monterey Bay Aquarium Research Institute (MBARI)</a> provides this
 * documentation and code &quot;as is&quot;, with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from
 * MBARI.</font></p>
 *
 * <p><font size="-1" color="#336699">Copyright 2003 MBARI.
 * MBARI Proprietary Information. All rights reserved.</font></p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: JFancyButton.java 314 2006-07-10 02:38:46Z hohonuuli $
 */
public class JFancyButton extends JButton {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public JFancyButton() {
        super();
        initialize();
    }

    /**
     * @param a
     */
    public JFancyButton(Action a) {
        super(a);
        initialize();
    }

    /**
     * @param icon
     */
    public JFancyButton(Icon icon) {
        super(icon);
        initialize();
    }

    /**
     * @param text
     */
    public JFancyButton(String text) {
        super(text);
        initialize();
    }

    /**
     * @param text
     * @param icon
     */
    public JFancyButton(String text, Icon icon) {
        super(text, icon);
        initialize();
    }

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
                setBorderPainted(true && isEnabled());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setBorderPainted(false);
            }

        });

        addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                setBorderPainted(true && isEnabled());
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                setBorderPainted(false);
            }

        });
    }



}
