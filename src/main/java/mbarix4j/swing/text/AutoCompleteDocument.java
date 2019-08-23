/*
 * @(#)AutoCompleteDocument.java   2010.03.31 at 04:25:46 PDT
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



package mbarix4j.swing.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import mbarix4j.text.IgnoreCaseToStringComparator;

/**
 * <p>
 * Autocompleting document that can be easily installed into any JTextField.
 * Typical usage is:
 * <pre>
 * JTextField textField = new JTextField();
 *
 * // The dictionary can be a String[] or a Collection. It contains all the terms
 * // that are valid for auto-completion
 * String[] dictionary = new String[]{'auto', 'automobile', 'autocrat',
 *         'graduation'};
 *
 * AutoCompleteDocument doc = new AutoCompleteDocument( textField,
 *         dictionary, new IgnoreCaseToStringComparator() );
 * textField.setDocument(doc);
 * </pre>
 * </p>
 *
 * <p>Inspiration for this class came from
 * http://jroller.com/comments/charliehubbard?anchor=auto_completing_fields_for_swing</p>
 *
 * @author brian
 *
 */
public class AutoCompleteDocument extends PlainDocument {

    /**
     *
     */
    private static final long serialVersionUID = 984417310787419236L;

    private List dictionary = new ArrayList();


    private Comparator comparator;

    private JTextComponent component;

    /**
     *
     * @param field
     * @param aDictionary A Collection of valid terms. A copy is made internally.
     * @param comparator The comparator used to order and search for terms.
     */
    public AutoCompleteDocument(JTextComponent field, Collection aDictionary, Comparator comparator) {
        component = field;
        dictionary.addAll(aDictionary);
        this.comparator = comparator;
        Collections.sort(dictionary, comparator);
    }

    /**
     * Constructs ...
     *
     *
     * @param field
     * @param aDictionary
     * @param comparator
     */
    public AutoCompleteDocument(JTextComponent field, String[] aDictionary, Comparator comparator) {
        this(field, Arrays.asList(aDictionary), comparator);
    }

    /**
     *
     * @param item
     */
    public void addDictionaryEntry(String item) {
        dictionary.add(item);
    }

    /**
     *
     * @param text
     * @return
     */
    public String autoComplete(String text) {
        int idx = Collections.binarySearch(dictionary, text, comparator);
        if (idx < -1) {

            /*
             * A negative item index is interpreted as -(insertIndex) - 1, that is,
             * having come from a failed binary search. If the insertIndex holds a
             * selection starting with the lookFor String, set the text to that
             * selection and highlight to the end of the String.
             */
            if (idx < 0) {
                idx = (-1 * idx) - 1;
            }

            if (idx >= dictionary.size()) {
                idx = dictionary.size() - 1;
            }
        }

        String word = (String) dictionary.get(idx);
        return word.substring(text.length());
    }

    /**
     * Creates a auto completing JTextField.
     *
     * @param dictionary an array of words to use when trying auto completion.
     * @return a JTextField that is initialized as using an auto
     * completing textfield.
     */
    public static JTextField createAutoCompleteTextField(String[] dictionary) {
        JTextField field = new JTextField();
        AutoCompleteDocument doc = new AutoCompleteDocument(field, dictionary, new IgnoreCaseToStringComparator());
        field.setDocument(doc);
        return field;
    }

    /**
     *
     * @param offs
     * @param str
     * @param a
     *
     * @throws BadLocationException
     */
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        super.insertString(offs, str, a);
        String word = autoComplete(getText(0, getLength()));
        if (word != null) {
            super.insertString(offs + str.length(), word, a);
            component.setCaretPosition(offs + str.length());
            component.moveCaretPosition(getLength());
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        javax.swing.JFrame frame = new javax.swing.JFrame("Auto complete demo");
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        String[] dict = { "auto", "automobile", "autocrat", "graduation" };
        JTextField field = AutoCompleteDocument.createAutoCompleteTextField(dict);
        BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS);
        frame.getContentPane().setLayout(layout);
        frame.getContentPane().add(new javax.swing.JLabel("Text Field: "));
        frame.getContentPane().add(field);
        frame.pack();
        frame.setVisible(true);
    }
}
