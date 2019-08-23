package mbarix4j.swing;

import javax.swing.*;
import java.awt.*;

public interface ListCellEditor extends CellEditor {
    Component getListCellEditorComponent(JList list, Object value,
                                          boolean isSelected, 
                                          int index); 
} 