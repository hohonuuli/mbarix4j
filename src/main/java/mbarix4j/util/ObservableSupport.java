package mbarix4j.util;

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


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * <p>
 * This component can be included inside a class to provide observable
 * capabilities. The class containing the <i>ObservableSupport</i> should
 * implement the <i>IObservable</i> interface. Once implemented this class
 * can notify other classes of a change in state of the Observed object.
 * </p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: ObservableSupport.java 332 2006-08-01 18:38:46Z hohonuuli $
 * @see mbarix4j.util.IObservable
 * @see mbarix4j.util.IObserver
 * @deprecated
 */
public class ObservableSupport {

    private List observers = Collections.synchronizedList(new ArrayList());


    /**
     *     Add a class that implements the IObserver interface. Classes added will
     *     be notified when the <code>notify()</code> method is called
     *
     *     @param observer
     *                The class to be notified
     *     @since Apr 29, 2003
     */
    public void add(IObserver observer) {
        observers.add(observer);
    }

    /**
     *     Clears the list of observers that need to be notified.
     *
     *     @since Apr 29, 2003
     */
    public void clear() {
        observers.clear();
    }

    /**
     *     Notify ll observers that have been added that a change has occured.
     *
     *     @param observed
     *                The object whos state has changed
     *     @param stateChange
     *                A flag indicating the chagne of state. This is not usually
     *                needed since the notified object will be passes a reference
     *                to the observed object
     *     @since Apr 29, 2003
     */
    public void notify(Object observed, Object stateChange) {
        IObserver[] obs = new IObserver[observers.size()] ;
        observers.toArray(obs);
        for (int i = 0; i < obs.length; i++) {
            try {
                obs[i].update(observed, stateChange);
            } catch (Exception e) {
                System.out.println("Exception notifying observer");
                e.printStackTrace();
            }
        }
    }

    /**
     *     Remove an observer from the notification list.
     *
     *     @param observer
     *                The object to be removed.
     *     @since Apr 29, 2003
     */
    public void remove(IObserver observer) {
        observers.remove(observer);
    }
}