package mbarix4j.swing;

import javax.swing.DefaultListModel;

/**
 * @author Brian Schlining
 * @since 2014-11-19T16:18:00
 */
public class DefaultMutableListModel extends DefaultListModel implements MutableListModel {
    public boolean isCellEditable(int index){
        return true;
    }

    public void setValueAt(Object value, int index){
        super.setElementAt(value, index);
    }
}