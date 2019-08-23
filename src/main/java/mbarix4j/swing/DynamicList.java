package mbarix4j.swing;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * This is a JList of Strings that can be quickly edited. To add an element, press enter and type the contents.
 * To remove and element select it and press backspace.
 */
public class DynamicList extends JList<String> {
    
    private String tempString;
    private final String defaultString;
    
    KeyListener keyListener = new KeyAdapter() {
        public void keyTyped(KeyEvent e) {
            ListListModel model = getContent();
            char key = e.getKeyChar();
            if (key == '\b') {
                if (model.getSize() > 0) {
                    int i = getSelectedIndex();
                    if (i < 0) {
                        i = model.getSize() - 1;
                    }
                    model.remove(i);
                    tempString = "";
                    setSelectedIndex(model.getSize() - 1);
                }
            }
            else if (key == '\n') {
                if (model.getSize() == 0 || !((String) model.getElementAt(model.getSize() - 1)).isEmpty()) {
                    tempString = defaultString;
                    model.add(tempString);
                    setSelectedIndex(model.getSize() - 1);
                }
            }
            else {
                tempString = tempString + key;
                model.setElementAt(model.getSize() - 1, tempString);
            }
        }
        
    };
    
    public DynamicList() {
        this("");
    }

    public DynamicList(String defaultString) {
        super(new ListListModel(new ArrayList<String>()));
        addKeyListener(keyListener);
        this.defaultString = defaultString;
    }

    /**
     *
     * @return
     */
    public ListListModel getContent() {
        return (ListListModel) getModel();
    }

    @Override
    public void setModel(ListModel<String> model) {
        throw new UnsupportedOperationException("DynamicList does not allow you to set the model. Use getContent to " +
                "retrieve the existing model instead");
    }
}