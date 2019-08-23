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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.MutableComboBoxModel;

//~--- classes ----------------------------------------------------------------

/**
 * <p>Allows a <code>JComboBox</code> to use a <code>java.util.List</code> (and subclasses
 * of List too) as the underlying data storage. In conjuction with <code>ListListModel</code>,
 * this allows signifigant reuse of <code>List</code> components in <code>swing</code>
 * interfaces. It also avoid the overhead associated with maintaining seperate
 * copies of data stores for each swing component, which normally contains a
 * unique <code>Vector</code> for each data model. Vectors are also notoriously
 * slow because the contain synchronized code blocks.</p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: ListComboBoxModel.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class ListComboBoxModel extends ListListModel
        implements MutableComboBoxModel, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3499591360371335117L;
    /**
	 * @uml.property  name="selectedObject"
	 */
    Object selectedObject;

    //~--- constructors -------------------------------------------------------

    /** Default Constructor. Uses an Array list for storage. */
    public ListComboBoxModel() {
        this(Collections.synchronizedList(new ArrayList()));
    }

    /**
     * Constructs a DefaultComboBoxModel object initialized with an array of objects.
     *
     * @param list
     */
    public ListComboBoxModel(java.util.List list) {
        super(list);
        // selectedObject = get(0);
    }

    //~--- methods ------------------------------------------------------------

    /**
     * implements javax.swing.MutableComboBoxModel
     *
     * @param anObject
     */
    public void addElement(Object anObject) {
        add(anObject);
    }

    //~--- get methods --------------------------------------------------------

    /**
     * implements javax.swing.ComboBoxModel
     *
     * @return
     */
    public Object getSelectedItem() {
        return selectedObject;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * implements javax.swing.MutableComboBoxModel
     *
     * @param anObject
     * @param index
     */
    public void insertElementAt(Object anObject, int index) {
        add(index, anObject);
    }

    /** Empties the list. */
    public void removeAllElements() {
        clear();
    }

    /**
     * implements javax.swing.MutableComboBoxModel
     *
     * @param anObject
     */
    public void removeElement(Object anObject) {
        remove(anObject);
    }

    /**
     * implements javax.swing.MutableComboBoxModel
     *
     * @param index
     */
    public void removeElementAt(int index) {
        remove(index);
    }

    //~--- set methods --------------------------------------------------------

    /**
     * implements javax.swing.ComboBoxModel
     *
     * @param anObject
     */
    public void setSelectedItem(Object anObject) {
        if ((anObject != null) &&!anObject.equals(selectedObject)) {
            selectedObject = anObject;
            fireContentsChanged(this, -1, -1);
        }
    }
}
