/*
 * @(#)SearchableTreePanel.java   2009.11.05 at 04:52:15 PST
 *
 * Copyright 2009 MBARI
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package mbarix4j.swing;

import java.awt.BorderLayout;
import java.util.Enumeration;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * <p>Provides case-insensitive search capabilites on a JTree. This component can be easily
 * embedded in applications.</p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: SearchableTreePanel.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class SearchableTreePanel extends JPanel {

    private static boolean resetEnum = false;
    private static Enumeration enm;
    private javax.swing.JScrollPane jScrollPane = null;
    private javax.swing.JTree jTree = null;
    private javax.swing.JButton searchBtn = null;
    private javax.swing.JPanel searchButtonPanel = null;
    private javax.swing.JPanel searchPanel = null;
    private javax.swing.JTextField searchTextField = null;
    private char[] wordSeparators = { ' ', '\t', '-', '_' };
    private JCheckBox globCheckBox;

    /**
     *
     */
    public enum SearchType { LEADING, GLOB, EXACT }

    /**
     * This is the default constructor
     */
    public SearchableTreePanel() {
        super();
        initialize();
    }

    private JCheckBox getGlobCheckBox() {
        if (globCheckBox == null) {
            globCheckBox = new JCheckBox();
            globCheckBox.setText("Glob");
            globCheckBox.setMargin(new java.awt.Insets(0, 4, 0, 4));
        }

        return globCheckBox;
    }

    private javax.swing.JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new javax.swing.JScrollPane();
            jScrollPane.setViewportView(getJTree());
        }

        return jScrollPane;
    }

    /**
     * This method returns the JTree being used. By default it calls <code>new JTree()</code> if no JTree has been set.
     * @return     javax.swing.JTree
     */
    public javax.swing.JTree getJTree() {
        if (jTree == null) {
            jTree = new javax.swing.JTree();
        }

        return jTree;
    }

    /**
     * This class is provided to be overridden by subclasses. The <tt>String</tt> returned
     * should be the <tt>String</tt> to use in checking if the user's search string matches
     * the a specific node.<br/>
     * <em>NOTE</em>: The default implementation of this method returns <tt>node.toString</tt>
     * it is strongly recommended that this method be overrided when used. Perhaps with an
     * anonymous subclass like so:<br/>
     * <pre>
     * SearchableTreePanel searchTree = new SearchableTreePanel(){
     *  public String getNodeTextToSearch(TreeNode node){
     *          MyWonderfullClass mwc = (MyWonderfulClass)node.getUserObject();
     *          return mwc.getDisplayName();
     *  }
     * };
     * </pre>
     *
     *
     * @param  node  The node to return a <tt>String</tt> for
     * @return       The <tt>String</tt> representation of this node as will be searched against
     * the user's search string.
     */
    public String getNodeTextToSearch(DefaultMutableTreeNode node) {
        return node.toString();
    }

    protected javax.swing.JButton getSearchBtn() {
        if (searchBtn == null) {
            searchBtn = new JFancyButton();
            searchBtn.setName("searchBtn");

            try {
                // Running into JPMS issue with reading images when subclasses
                // outside of mbarix4j module
                searchBtn.setIcon(new ImageIcon(getClass().getResource("/mbarix4j/images/view.png")));
                searchBtn.setPressedIcon(new ImageIcon(getClass().getResource("/mbarix4j/images/view_next.png")));
            }
            catch (Exception e) {
                searchBtn.setText("Search");
            }


            //searchBtn.setPreferredSize(new java.awt.Dimension(icon.getIconWidth(), icon.getIconWidth()));
            searchBtn.setToolTipText("Search for words in the tree, use this button or press enter in search field");
            searchBtn.addActionListener(e -> {
                if (!goToMatchingNode(getSearchTextField().getText().toLowerCase())) {
                    SwingUtils.flashJComponent(getSearchTextField(), 5);
                }
            });
            searchBtn.setEnabled(false);
        }

        return searchBtn;
    }

    private javax.swing.JPanel getSearchButtonPanel() {
        if (searchButtonPanel == null) {
            searchButtonPanel = new javax.swing.JPanel();
            searchButtonPanel.setLayout(new java.awt.BorderLayout());
            searchButtonPanel.add(getSearchBtn(), java.awt.BorderLayout.CENTER);
            searchButtonPanel.add(getGlobCheckBox(), BorderLayout.WEST);
        }

        return searchButtonPanel;
    }

    private javax.swing.JPanel getSearchPanel() {
        if (searchPanel == null) {
            searchPanel = new javax.swing.JPanel();
            searchPanel.setLayout(new java.awt.BorderLayout());
            searchPanel.setBorder(BorderFactory.createTitledBorder(null, "Search",
                    javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                    javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            searchPanel.add(getSearchButtonPanel(), java.awt.BorderLayout.EAST);
            searchPanel.add(getSearchTextField(), java.awt.BorderLayout.CENTER);
            searchPanel.setName("search");
            searchPanel.setToolTipText("Search for next matching item in tree");
        }

        return searchPanel;
    }

    protected javax.swing.JTextField getSearchTextField() {
        if (searchTextField == null) {
            searchTextField = new javax.swing.JTextField();
            searchTextField.setColumns(15);
            searchTextField.setText("");
            searchTextField.setToolTipText("Search for words in tree, press enter to search, or press search button");

            // The next 2 lines add a prompt to the text field on Macs
            searchTextField.putClientProperty( "JTextField.variant", "search");
            searchTextField.putClientProperty( "JTextField.Search.Prompt", "Search");
            searchTextField.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (!goToMatchingNode(getSearchTextField().getText().toLowerCase())) {
                        SwingUtils.flashJComponent(getSearchTextField(), 4);
                    }
                }

            });
            searchTextField.getDocument().addDocumentListener(new DocumentListener() {

                public void changedUpdate(DocumentEvent e) {
                    update();
                }
                public void insertUpdate(DocumentEvent e) {
                    update();
                }
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                private void update() {
                    String text = searchTextField.getText();
                    getSearchBtn().setEnabled((text != null) && (text.length() > 0) && !text.matches("\\A\\s+"));
                }

            });
        }

        return searchTextField;
    }

    public char[] getWordSeparators() {
        return wordSeparators;
    }

    /**
     * Goes to the next node in the tree which matches the text in the
     * <tt>searchTextField</tt>.
     *
     * @param  text           The String to search for.
     * @param  useGlobSearch  true=glob search false= prefix mtaching
     * @return                Description of the Return Value
     */
    public boolean goToMatchingNode(String text, boolean useGlobSearch) {
        SearchType searchType = useGlobSearch ? SearchType.GLOB : SearchType.LEADING;

        return goToMatchingNode(text, searchType);
    }

    /**
     * Goes to the next node in the tree which matches the text in the
     * <tt>searchTextField</tt>.
     *
     * @param  text           The String to search for.
     * @param  searchType  GLOB = glob search, LEADING = prefix mtaching, EXACT= exact match
     * @return                Description of the Return Value
     */
    public boolean goToMatchingNode(String text, SearchType searchType) {
        text = text.toLowerCase();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getJTree().getModel().getRoot();
        if (enm == null) {
            enm = root.preorderEnumeration();
            resetEnum = true;
        }
        else {
            resetEnum = false;
        }

        DefaultMutableTreeNode nodeFound = null;
        OUTER:
        {
            while (enm.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) enm.nextElement();

                // Searches are case insensitive
                final String nodeName = getNodeTextToSearch(node).toLowerCase();
                switch (searchType) {
                case EXACT:
                    if (nodeName.equals(text)) {
                        nodeFound = node;
                    }

                    break;

                case GLOB:
                    if (nodeName.contains(text)) {
                        nodeFound = node;

                        break OUTER;
                    }

                    break;

                case LEADING:
                    String[] parts = nodeName.split(" ");
                    for (String p : parts) {
                        if (p.startsWith(text)) {
                            nodeFound = node;

                            break OUTER;
                        }
                    }
                default:
                    // Do nada
                }

            }

        }

        // if no node was found, but the entire enum wasn't searched, go again
        // otherwise return false
        if (nodeFound == null) {
            if (!resetEnum) {
                enm = null;

                return goToMatchingNode(text, searchType);
            }
            else {
                return false;
            }
        }

        TreePath path = new TreePath(nodeFound.getPath());
        getJTree().setSelectionPath(path);
        getJTree().scrollPathToVisible(path);

        return true;
    }

    /**
     * Convience method for internal class use.
     *
     * @param  text
     * @return
     */
    boolean goToMatchingNode(String text) {
        return goToMatchingNode(text, isGlobSearch());
    }

    private void initialize() {
        this.setLayout(new java.awt.BorderLayout());
        this.add(getSearchPanel(), java.awt.BorderLayout.NORTH);
        this.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
        this.setSize(261, 329);
    }

    /**
     * Is the search mechanism a glob search or a leading search. A glob search
     * will match characters anywhere in the word, a leading search will only match
     * from the beginning of the word onwards.
     *
     *
     * @return    Whether or not this is a glob search
     */
    public boolean isGlobSearch() {
        return getGlobCheckBox().isSelected();
    }

    /**
     *  A demonstration method.
     *
     * @param  args  The command line arguments will be ignored.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Searchable Tree");
        frame.getContentPane().add(new SearchableTreePanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(300, 300, 300, 300);
        frame.setVisible(true);
    }

    /**
     * Set the search mechanism to a glob search or a leading search. A glob search
     * will match characters anywhere in the word, a leading search will only match
     * from the beginning of the word onwards.
     *
     * @param  b
     */
    public void setGlobSearch(boolean b) {
        getGlobCheckBox().setSelected(b);
    }

    /**
     * This method sets the <tt>JTree</tt> which is to be displayed and searched on. The nodes in this tree must be of type <tt>DefaultMutableTreeNode</tt> because the <tt>DefaultMutableTreeNode.preorderEnumeration</tt> method is needed for searching the tree.
     * @param  tree
     */
    public void setJTree(javax.swing.JTree tree) {
        this.remove(getJTree());
        jTree = tree;
        getJScrollPane().setViewportView(jTree);
    }

    /**
     * Set the <tt>char</tt> array of wordseparators used to check for whitespace which defines word boundaries
     * @param cs   The array of whitespace separators
     */
    public void setWordSeparators(char[] cs) {
        wordSeparators = cs;
    }
}
