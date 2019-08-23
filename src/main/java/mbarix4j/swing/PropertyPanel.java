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

import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import mbarix4j.awt.event.ActionAdapter;

/**
 * <p>Simple panel for displaying name-value pairs.</p>
 *
 * @author     <a href="http://www.mbari.org">MBARI</a>
 * @version    $Id: PropertyPanel.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class PropertyPanel extends JPanel {

    private static ImageIcon editIcon = null;
    private static ImageIcon icon = null;

    /**
     *
     */
    private static final long serialVersionUID = -5075241110598362410L;
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");

    /**
     *     @uml.property  name="action"
     *     @uml.associationEnd
     */
    private ActionAdapter action;

    /**
     *     @uml.property  name="editButton"
     *     @uml.associationEnd
     */
    private JButton editButton;

    /**
     *     @uml.property  name="editable"
     */
    private boolean editable = false;

    /**
     *     @uml.property  name="iconLabel"
     *     @uml.associationEnd
     */
    private JLabel iconLabel = null;

    /**
     *     @uml.property  name="nameLabel"
     *     @uml.associationEnd
     */
    private javax.swing.JLabel nameLabel = null;

    /**
     *     @uml.property  name="valueField"
     *     @uml.associationEnd
     */
    private javax.swing.JTextField valueField = null;

    /**
     * This is the default constructor
     */
    public PropertyPanel() {
        super();
        initialize();
    }

    /**
     * Convience constructor to set the name and value of the panel.
     *
     * @param  name   The name of the parameter
     * @param  value  The value of the parameter
     */
    public PropertyPanel(Object name, Object value) {
        this();
        setProperty(name, value);
    }

    /**
     *     Gets the edit button for this panel.
     *     @return     The editButton value
     *     @uml.property  name="editButton"
     */
    public JButton getEditButton() {
        if ((editButton == null) && (action != null)) {
            editButton = new JFancyButton();
            editButton.setAction(action);
            editButton.setText("edit");
            editButton.setRolloverEnabled(true);
        }

        return editButton;
    }

    /**
     * @return    The icon that indicates this panel can be edited
     */
    private ImageIcon getEditIcon() {
        if (editIcon == null) {
            editIcon = new ImageIcon(getClass().getResource("/mbarix4j/images/arrow_right_green.png"));
        }

        return editIcon;
    }

    /**
     * @return The icon that indicates this panel can not be edited
     */
    private ImageIcon getIcon() {
        if (icon == null) {
            icon = new ImageIcon(getClass().getResource("/mbarix4j/images/arrow_right_red.png"));
        }

        return icon;
    }

    /**
     *     <p><!-- Method description --></p>
     *     @return
     *     @uml.property  name="iconLabel"
     */
    private JLabel getIconLabel() {
        if (iconLabel == null) {
            iconLabel = new JLabel();
            iconLabel.setText("");
            iconLabel.setIcon(getIcon());
        }

        return iconLabel;
    }

    /**
     *     This method initializes NameLabel
     *     @return     javax.swing.JLabel
     *     @uml.property  name="nameLabel"
     */
    private javax.swing.JLabel getNameLabel() {
        if (nameLabel == null) {
            nameLabel = new javax.swing.JLabel();
            nameLabel.setText("name");

            // nameLabel.setBackground(Color.WHITE);
            // nameLabel.setForeground(Color.BLACK);
            nameLabel.setHorizontalTextPosition(SwingConstants.LEADING);
            nameLabel.setPreferredSize(new Dimension(180, 20));
            nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        }

        return nameLabel;
    }

    /**
     *     Returns the textField used to display the value of the property.
     *     @return     javax.swing.JTextField
     *     @uml.property  name="valueField"
     */
    public javax.swing.JTextField getValueField() {
        if (valueField == null) {
            valueField = new javax.swing.JTextField();
            valueField.setPreferredSize(new Dimension(180, 20));
            valueField.setEditable(false);
        }

        return valueField;
    }

    /**
     * This method initializes this
     *
     *
     */
    private void initialize() {
        this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
        this.add(Box.createHorizontalStrut(10));
        this.add(getNameLabel(), null);
        this.add(Box.createHorizontalGlue());
        this.add(getIconLabel(), null);
        this.add(Box.createHorizontalStrut(10));
        this.add(getValueField(), null);
    }

    /**
     * @return If the proprety displayed can be edited.
     */
    public boolean isEditable() {
        return editable;
    }

    /**
     *  Sets the action to be performed when the edit button is pressed.
     *
     * @param  action  The new editAction value
     */
    public void setEditAction(ActionAdapter action) {
        this.action = action;
        this.add(Box.createHorizontalStrut(10));
        add(getEditButton());
        this.add(Box.createHorizontalStrut(10));
        getValueField().setEditable(true);
        getValueField().setEnabled(false);
        setEditable(true);
    }

    /**
     *     @param  b
     *     @uml.property  name="editable"
     */
    public void setEditable(boolean b) {
        editable = b;
        getValueField().setEditable(b);

        if (b) {
            getIconLabel().setIcon(getEditIcon());
        }
        else {
            getIconLabel().setIcon(getIcon());
        }
    }

    /**
     *  Sets the property attribute of the PropertyPanel object. The values are
     * displayed using the toString methods of the name and value objects.
     *
     * @param  name   The name of the property
     * @param  value  The value of the property
     */
    public void setProperty(Object name, Object value) {
        getNameLabel().setText(name.toString());
        String s = value.toString();
        if (value instanceof Date) {
            s = dateFormat.format(value);
        }

        getValueField().setText(s);
    }
}
