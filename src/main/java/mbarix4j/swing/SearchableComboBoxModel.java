/*
 * @(#)SearchableComboBoxModel.java   2009.12.16 at 09:13:58 PST
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

import java.util.Collection;
import java.util.Comparator;
import mbarix4j.text.DefaultToStringConverter;
import mbarix4j.text.IgnoreCaseToStringComparator;
import mbarix4j.text.ObjectToStringConverter;

/**
 * <h2><u>Description</u></h2>
 * <p>A ComboBox model that is meant to store Strings in alphabetacal order.
 * It also provides searching facilities</p>
 *
 * <h2><u>UML</u></h2>
 * <pre>
 *    [JComboBox]
 *         |
 *         |
 *         V 1                1
 * [SearchableComboBoxModel]-->[SortedArrayList]
 *                                   |
 *                                   |
 *                                   V *
 *                                [Object]
 * </pre>
 *
 * @since May 23, 2003 2:53:28 PM
 */
public class SearchableComboBoxModel<T> extends SortedComboBoxModel<T> {

    private final ObjectToStringConverter converter;

    /**
     * Determines which String matching method to use for model content search.
     */
    private boolean useStartsWith;

    /**
     * Allows for searching of objects based on their toString() method.
     */
    public SearchableComboBoxModel() {
        this(new IgnoreCaseToStringComparator());
    }

    /**
     * Constructs ...
     *
     * @param comparator
     */
    public SearchableComboBoxModel(Comparator comparator) {
        this(comparator, new DefaultToStringConverter());
    }

    /**
     * @param objects
     * @param comparator
     */
    public SearchableComboBoxModel(Collection objects, Comparator comparator) {
        this(objects, comparator, new DefaultToStringConverter());
    }

    /**
     * Constructs ...
     *
     * @param comparator
     * @param converter
     */
    public SearchableComboBoxModel(Comparator comparator, ObjectToStringConverter converter) {
        super(comparator);
        this.converter = converter;
    }

    /**
     * Constructs ...
     *
     * @param objects
     * @param comparator
     * @param converter
     */
    public SearchableComboBoxModel(Collection objects, Comparator comparator, ObjectToStringConverter converter) {
        super(objects, comparator);
        this.converter = converter;
    }

    /**
     * Gets a default starting index of one greater than the current index.
     *
     * @return
     */
    private int getStartIndexUsingCurrent() {

        T item = (T) getSelectedItem();
        int startIndex = 0;

        if (item != null) {
            startIndex = getItemIndex(item) + 1;
        }

        if (startIndex == getSize()) {
            startIndex = 0;
        }

        return startIndex;
    }

    /**
     * Performs the actual String match using the current String matching
     * method.
     *
     * @param value
     * @param lookFor
     *
     * @return
     */
    private boolean performStringMatch(String value, String lookFor) {
        if (useStartsWith) {
            return value.startsWith(lookFor);
        }
        else {
            return (-1 < value.indexOf(lookFor));
        }
    }

    /**
     * Searches through each String stored in the model and checks to see
     * if the string matchs the provided substring using the currently
     * specified String matching method.
     * @param s The substring to search for.
     * @param startIndex The starting index of the search.
     * @param wrap Whether to wrap during the search. If wrapping is allowed,
     * the search will continue at 0 after the last index.
     * @return The index of the first string in the Model after the specified
     * startIndex that contains s; -1 if  no matches are found.
     * @throw ArrayIndexOutOfBoundsException If the specified startIndex is
     * out-of-bounds for this model.
     */
    private int searchForItem(String s, int startIndex, boolean wrap) {
        int size = getSize();
        int lastIndex = size - 1;
        if (startIndex > lastIndex) {
            if (wrap) {
                startIndex = 0;
            }
            else {
                startIndex = lastIndex;
            }
        }

        if (startIndex < 0) {
            startIndex = 0;
        }

        int index = -1;

        // Search from start index to end
        for (int j = startIndex; j < size; ++j) {
            if (performStringMatch(converter.convert(getElementAt(j)), s)) {
                index = j;

                break;
            }
        }

        // If no match and wrapping, search from 0 up to but not including
        // startIndex since startIndex already search in above for loop
        if ((index == -1) && wrap) {
            for (int j = 0; j < startIndex; ++j) {
                if (performStringMatch(converter.convert(getElementAt(j)), s)) {
                    index = j;

                    break;
                }
            }
        }

        return index;
    }

    /**
     * Searches through each String stored in the model and checks to see
     * if the string contains the provided substring.
     * @param s The substring to search for
     * @return The index of the first string in the Model that contains s; -1
     * if no match found.
     */
    public int searchForItemContaining(String s) {
        return searchForItemContaining(s, 0);
    }

    /**
     * Searches through each String stored in the model and checks to see
     * if the string contains the provided substring. The search starts at
     * the currently selected item index.
     * @param s The substring to search for.
     * @param wrap Whether to wrap during the search. If wrapping is allowed,
     * the search will continue at 0 after the last index.
     * @returnThe index of the first string in the Model after the specified
     * startIndex that contains s; -1 if  no matches are found.
     *
     * @return
     */
    public int searchForItemContaining(String s, boolean wrap) {
        return searchForItemContaining(s, getStartIndexUsingCurrent(), wrap);
    }

    /**
     * Searches through each String stored in the model and checks to see
     * if the string contains the provided substring.
     * @param s The substring to search for.
     * @param startIndex The starting index of the search.
     * @return The index of the first string in the Model after the specified
     * startIndex that contains s; -1 if  no matches are found.
     * @throw ArrayIndexOutOfBoundsException If the specified startIndex is
     * out-of-bounds for this model.
     */
    public int searchForItemContaining(String s, int startIndex) {
        return searchForItemContaining(s, startIndex, false);
    }

    /**
     * Searches through each String stored in the model and checks to see
     * if the string contains the provided substring.
     * @param s The substring to search for.
     * @param startIndex The starting index of the search.
     * @param wrap Whether to wrap during the search. If wrapping is allowed,
     * the search will continue at 0 after the last index.
     * @return The index of the first string in the Model after the specified
     * startIndex that contains s; -1 if  no matches are found.
     * @throw ArrayIndexOutOfBoundsException If the specified startIndex is
     * out-of-bounds for this model.
     */
    public int searchForItemContaining(String s, int startIndex, boolean wrap) {
        useStartsWith = false;

        return searchForItem(s, startIndex, wrap);
    }

    /**
     * Searches through each String stored in the model and checks to see
     * if the string starts with the provided substring.
     * @param s The substring to search for
     * @return The index of the first string in the Model that contains s; -1
     * if no match found.
     */
    public int searchForItemStartingWith(String s) {
        return searchForItemStartingWith(s, 0);
    }

    /**
     * Searches through each String stored in the model and checks to see
     * if the string starts with the provided substring. The search starts at
     * the currently selected item index.
     * @param s The substring to search for.
     * @param wrap Whether to wrap during the search. If wrapping is allowed,
     * the search will continue at 0 after the last index.
     * @returnThe index of the first string in the Model after the specified
     * startIndex that contains s; -1 if  no matches are found.
     *
     * @return
     */
    public int searchForItemStartingWith(String s, boolean wrap) {
        return searchForItemStartingWith(s, getStartIndexUsingCurrent(), wrap);
    }

    /**
     * Searches through each String stored in the model and checks to see
     * if the string starts with the provided substring.
     * @param s The substring to search for.
     * @param startIndex The starting index of the search.
     * @return The index of the first string in the Model after the specified
     * startIndex that contains s; -1 if  no matches are found.
     * @throw ArrayIndexOutOfBoundsException If the specified startIndex is
     * out-of-bounds for this model.
     */
    public int searchForItemStartingWith(String s, int startIndex) {
        return searchForItemStartingWith(s, startIndex, false);
    }

    /**
     * Searches through each String stored in the model and checks to see
     * if the string starts with the provided substring.
     * @param s The substring to search for.
     * @param startIndex The starting index of the search.
     * @param wrap Whether to wrap during the search. If wrapping is allowed,
     * the search will continue at 0 after the last index.
     * @return The index of the first string in the Model after the specified
     * startIndex that contains s; -1 if  no matches are found.
     * @throw ArrayIndexOutOfBoundsException If the specified startIndex is
     * out-of-bounds for this model.
     */
    public int searchForItemStartingWith(String s, int startIndex, boolean wrap) {
        useStartsWith = true;

        return searchForItem(s, startIndex, wrap);
    }
}
