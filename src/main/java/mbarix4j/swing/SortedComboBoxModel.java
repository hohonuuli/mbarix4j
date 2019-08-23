/*
 * @(#)SortedComboBoxModel.java   2009.12.16 at 09:14:20 PST
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.MutableComboBoxModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MutableComboBoxModel implementation with model contents maintained in sorted
 * order. Use as:
 *
 * <pre>
 *  // The model must be passed in a comparator to use for sorting objects
 *  // Here we use one that compares objects using case insensitive strings.
 * SortedComboBoxModel model = new SortedComboBoxModel(IgnoreCaseToStringComparator());
 * </pre>
 *
 * @author  : $Author: hohonuuli $
 * @created  March 25, 2005
 * @version  : $Revision: 332 $
 */
public class SortedComboBoxModel<T> implements MutableComboBoxModel {

    private static final Logger log = LoggerFactory.getLogger(SortedComboBoxModel.class);

    /**
     * Used for sorting the objects
     */
    private Comparator<T> comparator;

    /**
     * The backing list.
     */
    private List<T> itemList;

    /**
     * A list of listeners interested in receiving list data change notification.
     */
    protected EventListenerList listenerList = new EventListenerList();;

    /**
     * The currently selected item.
     */
    private T selectedItem;


    /**
     * Constructs an empty model.
     *
     * @param comparator
     */
    public SortedComboBoxModel(Comparator<T> comparator) {
        this.itemList = new ArrayList<T>();
        this.comparator = comparator;
    }

    /**
     * Constructs model containing the specified array of items. Assumes the
     * items in the array are not in sorted order.
     *
     * @param  items   The items to initial the model.
     * @param comparator
     */
    public SortedComboBoxModel(Collection<T> items, Comparator<T> comparator) {
        this.itemList = new ArrayList<T>(items);
        this.comparator = comparator;

        if (0 < getSize()) {
            Collections.sort(itemList, comparator);
            selectedItem = (T) getElementAt(0);
        }
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param items
     */
    public void addAll(Collection<T> items) {
        itemList.addAll(items);
        Collections.sort(itemList, comparator);
    }

    /**
     *  Adds a feature to the Element attribute of the SortedComboBoxModel object
     *
     * @param  item The feature to be added to the Element attribute
     */
    public void addElement(Object item) {
        T sItem = (T) item;
        int index = Collections.binarySearch(itemList, sItem, comparator);
        if (index < 0) {
            index = -index - 1;
            itemList.add(index, sItem);
        }
        else {
            if (log.isInfoEnabled()) {
                log.info("Item, " + item + " already exists in the internal list. It will not be added.");
            }
        }

        fireIntervalAdded(this, index, index);
    }

    /**
     *  Adds a feature to the ListDataListener attribute of the SortedComboBoxModel object
     *
     * @param  listener The feature to be added to the ListDataListener attribute
     */
    public void addListDataListener(ListDataListener listener) {
        listenerList.add(ListDataListener.class, listener);
    }

    /**
     * Checks whether the specified item is at the specified index.
     *
     * @param  item Description of the Parameter
     * @param  index Description of the Parameter
     * @return  Description of the Return Value
     */
    private boolean checkIndex(T item, int index) {
        boolean out = false;
        if (index < itemList.size()) {
            T obj = itemList.get(index);
            if ((item == null) && (obj == null)) {
                out = true;
            }

            if (comparator != null) {
                out = (comparator.compare(obj, item) == 0);
            }
            else {
                if (obj.equals(item)) {
                    out = true;
                }
            }
        }

        return out;
    }

    /**
     *  Description of the Method
     */
    public void clear() {
        int size = itemList.size();
        itemList.clear();
        fireIntervalRemoved(this, 0, size);
    }

    /**
     *
     * @param item
     *
     * @return
     */
    public boolean contains(T item) {
        return (getItemIndex(item) > 0);
    }

    /**
     * Call <em>after</em> changing items in the model.
     *
     * @param  source Description of the Parameter
     * @param  index0 Description of the Parameter
     * @param  index1 Description of the Parameter
     */
    protected void fireContentsChanged(Object source, int index0, int index1) {
        fireEvent(new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, index0, index1));
    }

    /**
     * Fire a ListDataEvent to all listeners.
     *
     * @param  event Description of the Parameter
     */
    private void fireEvent(ListDataEvent event) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; 0 <= i; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                ((ListDataListener) listeners[i + 1]).contentsChanged(event);
            }
        }
    }

    /**
     * Call <em>after</em> adding items to the model.
     *
     * @param  source Description of the Parameter
     * @param  index0 Description of the Parameter
     * @param  index1 Description of the Parameter
     */
    protected void fireIntervalAdded(Object source, int index0, int index1) {
        fireEvent(new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, index0, index1));
    }

    /**
     * Call <em>after</em> adding items to the model.
     *
     * @param  source Description of the Parameter
     * @param  index0 Description of the Parameter
     * @param  index1 Description of the Parameter
     */
    protected void fireIntervalRemoved(Object source, int index0, int index1) {
        fireEvent(new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index0, index1));
    }

    /**
     * @return
     */
    public synchronized Comparator<T> getComparator() {
        return comparator;
    }


    /**
     *  Gets the elementAt attribute of the SortedComboBoxModel object
     *
     * @param  index Description of the Parameter
     * @return  The elementAt value
     */
    public T getElementAt(int index) {
        T result = null;
        if ((0 <= index) && (index < itemList.size())) {
            result = itemList.get(index);
        }

        return result;
    }

    /**
     * Gets the index of the specified item using the start index as a hint.
     *
     * @param  item   The object to search for.
     * @return    The index of the specified item, or < 0 if the item is not found. Note if
     *  an item is not found it's insertion index (-(insertion point) - 1) is returned.
     */
    public int getItemIndex(T item) {
        int itemIndex = -1;
        if (item != null) {
            itemIndex = Collections.binarySearch(itemList, item, comparator);
        }

        return itemIndex;
    }

    /**
     * Gets the index of the specified item using the start index as a hint.
     *
     * @param  item   The object to search for.
     * @param  startIndex   The index to use as a hint for the location of the object.
     * @return    The index of the specified item, or -1 if the item is not found.
     */
    public int getItemIndex(T item, int startIndex) {

        // Convenience method for getting an item index under the assumption that
        // the item may be at the specified index or the next one after.
        int result = -1;

        // Check the starting index and possibly the next item. If a match is
        // found, use that index. O/W, ignore the specified starting index.
        if (checkIndex(item, startIndex)) {
            result = startIndex;
        }
        else if (checkIndex(item, startIndex + 1)) {
            result = startIndex + 1;
        }
        else {
            result = getItemIndex(item);
        }

        return result;
    }


    /**
     * Gets the selectedItem attribute of the SortedComboBoxModel object
     * @return   The selectedItem value
     */
    public Object getSelectedItem() {
        return selectedItem;
    }


    /**
     *  Gets the size attribute of the SortedComboBoxModel object
     *
     * @return  The size value
     */
    public int getSize() {
        return itemList.size();
    }


    /**
     *  Description of the Method
     *
     * @param  object Description of the Parameter
     * @param  index Description of the Parameter
     */
    public void insertElementAt(Object object, int index) {
        throw new UnsupportedOperationException("Specifying index not allowed. Model maintains sorted ordering.");
    }


    /**
     *  Description of the Method
     *
     * @param  object Description of the Parameter
     */
    public void removeElement(Object object) {
        int index = itemList.indexOf(object);
        if (index != -1) {
            removeElementAt(index);
        }
    }


    /**
     *  Description of the Method
     *
     * @param  index Description of the Parameter
     */
    public void removeElementAt(int index) {
        itemList.remove(index);
        fireIntervalRemoved(this, index, index);
    }


    /**
     *  Description of the Method
     *
     * @param  listener Description of the Parameter
     */
    public void removeListDataListener(ListDataListener listener) {
        listenerList.remove(ListDataListener.class, listener);
    }

    /**
     * Sets the comparator used to order the elements.
     * @param  comparator
     */
    public synchronized void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Sets the items in the model. All previous item references are discarded.
     * Assumes the items in the list are not in sorted order.
     *
     * @param  list   The items for the model.
     */
    public void setItems(List<T> list) {
        itemList.clear();
        itemList.addAll(list);

        while (itemList.contains(null)) {
            itemList.remove(null);
        }

        Collections.sort(itemList, comparator);

        if (0 < getSize()) {
            fireIntervalAdded(this, 0, getSize() - 1);
        }
    }

    /**
     * Sets the selectedItem attribute of the SortedComboBoxModel object
     * @param item  The new selectedItem value
     */
    public void setSelectedItem(Object item) {
        if (((selectedItem != null) && !selectedItem.equals(item)) || ((selectedItem == null) && (item != null))) {
            selectedItem = (T) item;
            fireContentsChanged(this, -1, -1);
        }
    }
}
