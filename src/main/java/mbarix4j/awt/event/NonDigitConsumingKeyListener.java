/*
 * @(#)NonDigitConsumingKeyListener.java   2011.12.10 at 08:52:18 PST
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



package mbarix4j.awt.event;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * <p>KeyAdapter that consumes keys that are not digits, enter, backspace
 * or deletes. This class is useful for Swing widgets that should only except
 * digits as imput (like a calculator). Example of use:</p>
 *
 * <pre>
 * JTextField tf = new JTextField();
 * tf.addKeyListener(new NonDigitConsumingKeyListener());
 * </pre>
 *
 * @author     <a href="http://www.mbari.org">MBARI</a>
 * @created    October 3, 2004
 * @version    $Id: NonDigitConsumingKeyListener.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class NonDigitConsumingKeyListener extends KeyAdapter {

    private Component component;

    /**
     * Default constructor
     */
    public NonDigitConsumingKeyListener() {
        super();
    }

    /**
     * Suppling a component allows this KeyAdapter to beep if the
     * wrong character is used.
     *
     * @param  c  A Component object.
     */
    public NonDigitConsumingKeyListener(Component c) {
        super();
        this.component = c;
    }

    /**
     *  Traps for keystrokes that are characters and consumes them.
     *
     * @param  e  A KeyEvent
     */
    public void keyTyped(KeyEvent e) {
        final char c = e.getKeyChar();
        if (c == KeyEvent.VK_ENTER) {
            // Do nothing. Enter is normally handled by ActionListeners
        }
        else if (!((Character.isDigit(c)) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            if (component != null) {
                component.getToolkit().beep();
            }
            e.consume();
        }
    }
}
