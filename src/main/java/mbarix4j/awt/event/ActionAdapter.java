/*
 * @(#)ActionAdapter.java   2011.12.10 at 08:50:23 PST
 *
 * Copyright 2009 MBARI
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



/*
 * The Monterey Bay Aquarium Research Institute (MBARI) provides this
 * documentation and code 'as is', with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from MBARI
 */
package mbarix4j.awt.event;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import java.awt.event.ActionEvent;

/**
 * <p>Custom implementation of AbstractAction. This class is primarily meant to be
 * called from ui components. For instance you might Bascially all you need to do
 * to use this class is subclass it an implement the doAction method. For example</p>
 * <pre>
 * IAction hw = new AbstractAction() {
 *     public void doAction() {
 *         System.out.println("Hello World");
 *     }
 * };
 *
 * hw.doAction();
 * </pre>
 *
 * <p>or you can associate it with a JButton or JMenu. For Example:</p>
 * <pre>
 * someJButton.setAction(hw);
 * </pre>
 *
 * <h2><u>UML</u></h2>
 * <pre>
 * [IAction]
 *    ^
 *    |
 *    |
 * [ActionAdapter]-->[AbsractAction]
 * </pre>
 *
 *@author     <a href="http://www.mbari.org">MBARI</a>
 *@version    $Id: ActionAdapter.java 3 2005-10-27 16:20:12Z hohonuuli $
 *@see        javax.swing.AbstractAction
 *@see        mbarix4j.awt.event.IAction
 */
public abstract class ActionAdapter extends AbstractAction implements IAction {

    /**
     * Default Constructor
     */
    public ActionAdapter() {
        super();
    }

    /**
     * @param  name The name of the action. This is what will appear in menus or on buttons
     *             that use this action.
     */
    public ActionAdapter(String name) {
        super(name);
    }

    /**
     * @param  name The name of the action. This is what will appear in menus or on buttons
     *             that use this action.
     * @param  icon The icon tha represents this action. This will appear in menus or buttons
     *             that use this action.
     */
    public ActionAdapter(String name, Icon icon) {
        super(name, icon);
    }

    /**
     * This method has been modified to call the doAction() method
     *
     * @param  e  Description of the Parameter
     * @see       java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public final void actionPerformed(ActionEvent e) {
        doAction();
    }

    /**
     * Code to perform a particular action should be placed here. This class should be overridden in subclasses
     *
     * @see    mbarix4j.awt.event.IAction#doAction()
     */
    public abstract void doAction();


}
