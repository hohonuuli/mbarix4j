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


/*
 * Created on Mar 18, 2005
 */
package mbarix4j.swing;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//~--- classes ----------------------------------------------------------------

/**
 * <p>This is a dialog for displaying messages. Messages are wrapped as HTML; so
 * they can containg HTML markup (but not html, head, or body tags)</p>
 *
 * @author brian
 * @version $Id: FancyDialog.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class FancyDialog extends JDialog {


    //~--- fields -------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 5263246330599377064L;
    /**
	 * @uml.property  name="jContentPane"
	 * @uml.associationEnd  
	 */
    private javax.swing.JPanel jContentPane = null;
    /**
	 * @uml.property  name="jPanel"
	 * @uml.associationEnd  
	 */
    private JPanel jPanel = null;
    /**
	 * @uml.property  name="jButton"
	 * @uml.associationEnd  
	 */
    private JButton jButton = null;
    /**
	 * @uml.property  name="jScrollPane"
	 * @uml.associationEnd  
	 */
    private JScrollPane jScrollPane = null;
    /**
	 * @uml.property  name="jTextArea"
	 * @uml.associationEnd  
	 */
    private JTextArea jTextArea = null;

    //~--- constructors -------------------------------------------------------

    /**
     *
     * @throws HeadlessException
     */
    public FancyDialog() throws HeadlessException {
        super();

        // TODO Auto-generated constructor stub
        initialize();
    }

    /**
     * @param owner
     *
     * @throws HeadlessException
     */
    public FancyDialog(Dialog owner) throws HeadlessException {
        super(owner);

        // TODO Auto-generated constructor stub
        initialize();
    }

    /**
     * @param owner
     *
     * @throws HeadlessException
     */
    public FancyDialog(Frame owner) throws HeadlessException {
        super(owner);

        // TODO Auto-generated constructor stub
        initialize();
    }

    /**
     * @param owner
     * @param modal
     *
     * @throws HeadlessException
     */
    public FancyDialog(Dialog owner, boolean modal) throws HeadlessException {
        super(owner, modal);

        // TODO Auto-generated constructor stub
        initialize();
    }

    /**
     * @param owner
     * @param title
     *
     * @throws HeadlessException
     */
    public FancyDialog(Dialog owner, String title) throws HeadlessException {
        super(owner, title);

        // TODO Auto-generated constructor stub
        initialize();
    }

    /**
     * @param owner
     * @param modal
     *
     * @throws HeadlessException
     */
    public FancyDialog(Frame owner, boolean modal) throws HeadlessException {
        super(owner, modal);

        // TODO Auto-generated constructor stub
        initialize();
    }

    /**
     * @param owner
     * @param title
     *
     * @throws HeadlessException
     */
    public FancyDialog(Frame owner, String title) throws HeadlessException {
        super(owner, title);

        // TODO Auto-generated constructor stub
        initialize();
    }

    /**
     * @param owner
     * @param title
     * @param modal
     *
     * @throws HeadlessException
     */
    public FancyDialog(Dialog owner, String title, boolean modal)
            throws HeadlessException {
        super(owner, title, modal);

        // TODO Auto-generated constructor stub
        initialize();
    }

    /**
     * @param owner
     * @param title
     * @param modal
     *
     * @throws HeadlessException
     */
    public FancyDialog(Frame owner, String title, boolean modal)
            throws HeadlessException {
        super(owner, title, modal);

        // TODO Auto-generated constructor stub
        initialize();
    }

    /**
     * @param owner
     * @param title
     * @param modal
     * @param gc
     *
     * @throws HeadlessException
     */
    public FancyDialog(Dialog owner, String title, boolean modal,
            GraphicsConfiguration gc)
            throws HeadlessException {
        super(owner, title, modal, gc);

        // TODO Auto-generated constructor stub
        initialize();
    }

    /**
     * @param owner
     * @param title
     * @param modal
     * @param gc
     */
    public FancyDialog(Frame owner, String title, boolean modal,
            GraphicsConfiguration gc) {
        super(owner, title, modal, gc);

        // TODO Auto-generated constructor stub
        initialize();
    }

    //~--- get methods --------------------------------------------------------

    /**
	 * This method initializes jButton
	 * @return  javax.swing.JButton
	 * @uml.property  name="jButton"
	 */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("OK");
            jButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    JDialog thisDialog = FancyDialog.this;
                    int i = thisDialog.getDefaultCloseOperation();
                    switch (i) {
                    case JDialog.DISPOSE_ON_CLOSE:
                        thisDialog.dispose();
                        break;
                    case JDialog.DO_NOTHING_ON_CLOSE:
                        break;
                    case JDialog.EXIT_ON_CLOSE:
                        System.exit(0);
                        break;
                    case JDialog.HIDE_ON_CLOSE:
                        thisDialog.setVisible(false);
                        break;
                    default:
                        break;
                    }
                }

            });
        }

        return jButton;
    }

    /**
	 * This method initializes jContentPane
	 * @return  javax.swing.JPanel
	 * @uml.property  name="jContentPane"
	 */
    private javax.swing.JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new javax.swing.JPanel();
            jContentPane.setLayout(new java.awt.BorderLayout());
            jContentPane.add(getJPanel(), java.awt.BorderLayout.SOUTH);
            jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
        }

        return jContentPane;
    }

    /**
	 * This method initializes jPanel
	 * @return  javax.swing.JPanel
	 * @uml.property  name="jPanel"
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.X_AXIS));
            jPanel.add(Box.createHorizontalGlue());
            jPanel.add(getJButton(), null);
        }

        return jPanel;
    }

    /**
	 * This method initializes jScrollPane
	 * @return  javax.swing.JScrollPane
	 * @uml.property  name="jScrollPane"
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJTextArea());
        }

        return jScrollPane;
    }

    /**
	 * This method initializes jTextArea
	 * @return  javax.swing.JTextArea
	 * @uml.property  name="jTextArea"
	 */
    private JTextArea getJTextArea() {
        if (jTextArea == null) {
            jTextArea = new JTextArea();
            jTextArea.setEditable(false);
            jTextArea.setLineWrap(true);
        }

        return jTextArea;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * This method initializes this
     *
     */
    private void initialize() {
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
    }

    //~--- set methods --------------------------------------------------------

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param message
     */
    public void setMessage(String message) {
        StringBuffer sb = new StringBuffer();

        // sb.append(HEAD);
        sb.append(message);
        // sb.append(FOOT);
        getJTextArea().setText(sb.toString());
    }

}    // @jve:decl-index=0:visual-constraint="29,30"

