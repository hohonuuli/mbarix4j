/*
 * @(#)ActionRunnable.java   2011.12.10 at 08:50:47 PST
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



package mbarix4j.awt.event;

/**
 * This Runnable allows actions to be run in a separate thread. For example,
 *
 * <pre>
 * IAction action = new ActionAdapter() {
 *     public void doAction() {
 *         System.out.println("Hello World");
 *     }
 * }
 *
 * ActionRunnable ar = new ActionRunnable(action);
 * ar.start();
 * </pre>
 *
 * @author brian
 * @version $Id: ActionRunnable.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class ActionRunnable implements Runnable {

    private final IAction action;

    /**
     *
     *
     * @param action
     */
    public ActionRunnable(IAction action) {
        super();
        this.action = action;
    }



    /**
     * Runnable method that calls doAction on the contained action
     */
    public void run() {
        action.doAction();
    }

    /**
     * Runs the action in it's own thread
     */
    public void start() {
        Thread thread = new Thread(this);
        thread.start();
    }
}
