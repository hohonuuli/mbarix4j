/*
 * @(#)FancyComboBox.java   2009.12.16 at 02:55:41 PST
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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import mbarix4j.text.IgnoreCaseToStringComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <div>This is a custom combo box that does the following.
 * <ol>
 *  <li>Maintains items in order (using a SortedArrayList as a  backingstore)</li>
 *  <li>Allows the developer to set the Comparator used to order the items.</li>
 *  <li>Selects the correct item as a user types</li>
 * </ol></div>
 *
 * <div>
 * Here's an exmple of use to sort items aphabetically, ignoring the case.
 * <pre>
 * List list = new ArrayList();
 * list.add("apple");
 * list.add("Apple Pie");
 * list.add("Dog");
 * list.add("Bob");
 * list.add("Bob's your uncle");
 * FancyComboBox cb = new FancyComboBox();
 * cb.setComparator(new IgnoreCaseToStringComparator());
 * ListComboBoxModel model = (ListComboBoxModel) cb.getModel();
 * model.addAll(list);
 * </pre>
 * <div>
 *
 *
 * @author brian
 * @version $Id: FancyComboBox.java 314 2006-07-10 02:38:46Z hohonuuli $
 */
public class FancyComboBox extends JComboBox {

    /**
     *
     */
    private static final long serialVersionUID = -7372167388980529692L;
    private static final Logger log = LoggerFactory.getLogger(FancyComboBox.class);
    private static final String downKey = "selectNextRow";
    private static final String upKey = "selectPreviousRow";
    private static KeyStroke up = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
    private static KeyStroke cmdUp = KeyStroke.getKeyStroke(KeyEvent.VK_UP,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    private static KeyStroke kpUp = KeyStroke.getKeyStroke(KeyEvent.VK_KP_UP, 0);
    private static KeyStroke kpDown = KeyStroke.getKeyStroke(KeyEvent.VK_KP_DOWN, 0);
    private static KeyStroke down = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
    private static KeyStroke cmdDown = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
    private static KeyStroke cmdW = KeyStroke.getKeyStroke(KeyEvent.VK_W,
        Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());

    /**
     * Default constructor
     */
    public FancyComboBox() {
        super();

        /*
         * Allow user input. The editor works with the model to ensure only existing
         * values are allowed
         */
        setEditable(true);

        // Install a Look Ahead editor
        setModel(new SortedComboBoxModel(null));
        setEditor(new SearchAheadEditor());
    }

    /**
     * Constructor
     * @param comparator Set the Comparator used to order items.
     */
    public FancyComboBox(Comparator comparator) {
        this();
        setComparator(comparator);
    }

    /**
     * Add an ActionListener to the editor controlling the text input for this
     * <code>ConceptChangeListener</code>. The editor fires ActionEvents when the
     * user presses ENTER, whereas the JComboBox fires ActionEvents whenever the
     * list selection changes, so this method allows reacting to ActionEvents
     * fired by the editor component.
     *
     * @param  listener   The ActionListener to add.
     */
    public void addEditorActionListener(ActionListener listener) {
        editor.addActionListener(listener);
    }

    /**
     * Adds a FocusListener to the editor controlling the text input for
     * this <code>ConceptChangeListener</code>.
     *
     * @param  listener   The ActionListener to add.
     */
    public void addEditorFocusListener(FocusListener listener) {
        editor.getEditorComponent().addFocusListener(listener);
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("FancyComoBox Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComboBox cb = new FancyComboBox(new IgnoreCaseToStringComparator());
        SortedComboBoxModel model = (SortedComboBoxModel) cb.getModel();
        Collection data = new ArrayList();
        data.add("auto");
        data.add("autocrat");
        data.add("automobile");
        data.add("graduation");
        model.addAll(data);
        BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS);
        frame.getContentPane().setLayout(layout);
        frame.getContentPane().add(new javax.swing.JLabel("Text Field: "));
        frame.getContentPane().add(cb);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     *
     * @param comparator Sets the Comparator used to order items. The items in the
     * combobox are re-sorted when this method is called.
     */
    public void setComparator(Comparator comparator) {
        ((SortedComboBoxModel) getModel()).setComparator(comparator);
    }

    /**
     * @param editor
     */
    @Override
    public void setEditor(ComboBoxEditor editor) {
        if (!(editor instanceof SearchAheadEditor)) {
            log.debug("The ComboBoxEditor must be an instance of SearchAheadEditor");
        }

        super.setEditor(editor);
    }

    /**
     * @param model
     */
    @Override
    public void setModel(ComboBoxModel model) {
        if (!(model instanceof SortedComboBoxModel)) {
            log.debug("The model is now a " + model.getClass().getName() + ". It should be a SortedComboBoxModel");
        }

        super.setModel(model);
    }

    /**
     * The ComboBoxEditor used. It delegates calls to a custom JTextField.
     */
    private class SearchAheadEditor implements ComboBoxEditor {

        private final JTextField editor = new SearchAheadTextField();

        /**
         *
         * @param l
         */
        public void addActionListener(ActionListener l) {
            editor.addActionListener(l);
        }

        /**
         *
         * @return
         */
        public Component getEditorComponent() {
            return editor;
        }

        /**
         *
         * @return
         */
        public Object getItem() {
            return editor.getText();
        }

        /**
         *
         * @param l
         */
        public void removeActionListener(ActionListener l) {
            editor.removeActionListener(l);
        }

        public void selectAll() {
            editor.selectAll();
        }

        public void setItem(Object obj) {
            if (obj != null) {
                editor.setText(obj.toString());
            }
        }
    }


    /**
     * <p>Editor component for restricting input into a text field to only values
     *     currently in the associated model. Special handling is as follows:
     *      <ul>
     *        <li>
     *          User input is matched against all possible model values. Matches are
     *          pre-filled, with the auto-filled portion of the model selection
     *          highlighted.
     *        </li>
     *        <li>
     *          BackSpace moves the insertion point and highlighting left.
     *        </li>
     *        <li>
     *          Space moves the insertion point and highlighting right.
     *        </li>
     *        <li>
     *          Delete is silently ignored.
     *        </li>
     *        <li>
     *          Keystrokes that generate invalid input are silently ignored.
     *        </li>
     *      </ul></p>
     */
    private class SearchAheadTextField extends JTextField {

        /**
         *
         */
        private static final long serialVersionUID = -8851277261871110367L;



        /**
         * Constructs ...
         *
         */
        SearchAheadTextField() {
            setOpaque(true);
            InputMap inputMap = getInputMap();

            /*
             * Map BackSpace to LeftArrow. Apparently there is a bug in pre-1.5
             * JDK's so that the VK_BACK_SPACE is not mapped correctly. So,
             * for JRE 1.4 we also have to map '\b'. If we don't have both
             * mappings then values that are not in the knowledgebase can
             * be entered into the annotations.
             *
             * So 1st line works in 1.5; without it 1.5 does not work correctly
             * 2nd line works in 1.4; without it 1.4 does not work correctly
             *
             * A side effect is that the backspace key can jump back to spaces
             * not a big deal but surprising.
             */
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
                     inputMap.get(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)));

            /*
             * Map Space to RightArrow
             */
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),
                     inputMap.get(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0)));

            /*
             * Map Delete to none
             */
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "none");
            inputMap.put(cmdUp, "none");
            inputMap.put(cmdDown, "none");

            ActionMap actionMap = getActionMap();

             // Actions to bind up and down arrows to
            Action upAction = new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    FancyComboBox fc = FancyComboBox.this;
                    fc.setPopupVisible(true);
                    int idx = fc.getSelectedIndex();
                    if (idx >= 0) {
                        if (idx == 0 && fc.getModel().getSize() > 0) {
                            idx = fc.getModel().getSize() - 1;
                        }
                        else {
                            idx = idx - 1;
                        }
                        fc.setSelectedIndex(idx);
                        fc.repaint();
                    }
                }
            };

            Action downAction = new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    FancyComboBox fc = FancyComboBox.this;
                    fc.setPopupVisible(true);
                    int idx = fc.getSelectedIndex();
                    if (idx >= 0) {
                        if (idx < fc.getModel().getSize() - 1) {
                            idx = idx + 1;
                        }
                        else {
                            idx = 0; // Wrap around
                        }
                        fc.setSelectedIndex(idx);
                        fc.repaint();
                    }
                }
            };

            inputMap.put(up, upKey);

            inputMap.put(kpUp, upKey);
            inputMap.put(down, downKey);
            inputMap.put(kpDown, downKey);
            actionMap.put(downKey, downAction);
            actionMap.put(upKey, upAction);

            /*
             * TODO 20040505 wcpr: There should be a better way of acheiving
             * mouse click highlighting. Register an annonymous mouse listener
             * to perform highlighted from the insertion point to the end of
             * the String on mouse click.
             */
            addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent event) {
                    if (getSelectionEnd() != getText().length()) {
                        highlightText();
                    }
                }
            });

            /*
             * Highlight text after each key release if appropriate
             */
            addKeyListener(new KeyAdapter() {

                @Override
                public void keyReleased(KeyEvent event) {
                    if (getCaretPosition() < getText().length()) {
                        highlightText();
                    }
                }
            });

            /*
             * Un-highlight any text when user presses ENTER
             */
            addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    setCaretPosition(getText().length());
                }
            });
        }

        /**
         * Highlights from caret position to end of text.
         */
        private void highlightText() {
            int holdPosition = getCaretPosition();
            setCaretPosition(getText().length());
            moveCaretPosition(holdPosition);
        }

        /**
         * Replace the currently selected text with the specified content. The
         * resulting String is validated before allow the change to actually take
         * place.
         *
         * @param  content    The new content to replace the currently selected text.
         */
        @Override
        public void replaceSelection(String content) {
            final SortedComboBoxModel model = (SortedComboBoxModel) getModel();
            final Comparator comparator = model.getComparator();

            /*
             * Create a String as if the content is replacing selection
             */
            StringBuffer buf = new StringBuffer(getText().substring(0, getSelectionStart()));
            buf.append(content);
            buf.append(getText().substring(getSelectionEnd()));
            String lookFor = buf.toString();

            /*
             * Look for the String using the backing model.
             */
            int itemIndex = model.getItemIndex(lookFor);

            /*
             * A negative item index is interpreted as -(insertIndex) - 1,
             * that is, having come from a failed binary search. If the
             * insertIndex holds a selection starting with the lookFor String,
             * set the text to that selection and highlight to the end of the
             * String.
             */
            if (itemIndex < 0) {
                itemIndex = (-1 * itemIndex) - 1;
            }

            if (itemIndex < model.getSize()) {
                String selection = model.getElementAt(itemIndex).toString();
                boolean match = false;
                if (comparator != null) {

                    /*
                     * If a comparator is being used, chop off a substring of the
                     * appropriate length and use the comparator to check for
                     * equality.
                     */
                    int endIdx = (selection.length() > lookFor.length()) ? lookFor.length() : selection.length();
                    String select = selection.substring(0, endIdx);
                    match = comparator.compare(select, lookFor) == 0;
                }
                else {
                    match = selection.startsWith(lookFor);
                }

                if (match) {
                    model.setSelectedItem(selection);
                    setCaretPosition(selection.length());
                    moveCaretPosition(lookFor.length());
                }
            }
        }
    }
}
