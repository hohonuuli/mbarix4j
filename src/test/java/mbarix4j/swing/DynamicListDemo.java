package mbarix4j.swing;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * @author Brian Schlining
 * @since 2014-11-20T09:20:00
 */
public class DynamicListDemo {

    public static void main(String[] args) {
        JFrame frame = new JFrame("DynamicList Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JList list = new DynamicList("0.");
        JScrollPane scrollPane = new JScrollPane(list);

        scrollPane.setBorder(new TitledBorder("Type to edit, enter adds, backspace removes"));
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
