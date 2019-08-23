/*
 * @(#)IAction.java   2011.12.10 at 08:52:04 PST
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
 * <p>Interface for custom actions. It consists of a single method doAction()</p>
 *
 *@author     <a href="http://www.mbari.org">MBARI</a>
 *@created    October 3, 2004
 *@version    $Id: IAction.java 3 2005-10-27 16:20:12Z hohonuuli $
 *@see        ActionAdapter
 */
public interface IAction {

    /**
     *
     */
    void doAction();
}
