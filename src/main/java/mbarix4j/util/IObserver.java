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



/**
 * <p>A class that wants to be notifed by an IObervable needs to implement this
 * interface.</p>
 *
 * <p><font size="-1" color="#336699"><a href="http://www.mbari.org">
 * The Monterey Bay Aquarium Research Institute (MBARI)</a> provides this
 * documentation and code &quot;as is&quot;, with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from
 * MBARI.</font></p><br>
 *
 * <font size="-1" color="#336699">Copyright 2003 MBARI.<br>
 * MBARI Proprietary Information. All rights reserved.</font><br>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: IObserver.java 3 2005-10-27 16:20:12Z hohonuuli $
 * @see mbarix4j.util.IObservable
 * @deprecated
 */
public interface IObserver {

    /**
     *
     * @param obj A reference to the observedObject. NOTE:
     *          This may not be the object that you are observing but some
     *          object contained with it. Refer to the documentation
     *          of the class that you're observing to see exactly
     *          what type of object is returned
     * @param changeCode Generally a string describing what's changed. Included
     *      for those who might need it.
     */
    void update(Object obj, Object changeCode);
}