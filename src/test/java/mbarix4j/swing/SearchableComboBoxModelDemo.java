/*
 * Created on Feb 18, 2005
 * 
 * The Monterey Bay Aquarium Research Institute (MBARI) provides this
 * documentation and code 'as is', with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from MBARI
 */
package mbarix4j.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFrame;

/**
 * @author brian
 * @version $Id: SearchableComboBoxModelDemo.java 3 2005-10-27 16:20:12Z hohonuuli $
 */
public class SearchableComboBoxModelDemo {



    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        List list = new ArrayList();
        list.add("apple");
        list.add("Dog");
        list.add("Bob");
        list.add("bob");
        
        SearchableComboBoxModel model = new SearchableComboBoxModel();
        model.addAll(list);
        JComboBox box = new JComboBox();
        box.setModel(model);
        f.getContentPane().add(box);
        f.pack();
        f.setVisible(true);
    }
}
