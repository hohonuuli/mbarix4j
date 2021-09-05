/*
 * Created on Feb 22, 2005
 * 
 * The Monterey Bay Aquarium Research Institute (MBARI) provides this
 * documentation and code 'as is', with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from MBARI
 */
package mbarix4j3.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import mbarix4j3.text.IgnoreCaseToStringComparator;

/**
 * @author brian
 * @version $Id: FancyComboBoxDemo.java 3 2005-10-27 16:20:12Z hohonuuli $
 */
public class FancyComboBoxDemo {

    /**
     * 
     */
    public FancyComboBoxDemo() {
        super();
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        List list = new ArrayList();
        list.add("apple");
        list.add("apple pie");
        list.add("Apple Pie");
        list.add("Dog");
        list.add("Bob");
        list.add("bob");
        list.add("Bob's your uncle");
        
        FancyComboBox cb = new FancyComboBox();
        cb.setComparator(new IgnoreCaseToStringComparator());
        SortedComboBoxModel model = (SortedComboBoxModel) cb.getModel();
        model.setItems(list);
        f.getContentPane().add(cb);
        f.pack();
        f.setVisible(true);
    }
}
