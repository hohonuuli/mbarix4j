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

import javax.swing.JPopupMenu;

//~--- interfaces -------------------------------------------------------------

/**
 * <p>Interface that gaurenntees that a swing component will hava JPopupMenu associated with it.</p> <h2><u>License</u></h2> <p><font size="-1" color="#336699"><a href="http://www.mbari.org"> The Monterey Bay Aquarium Research Institute (MBARI)</a> provides this documentation and code &quot;as is&quot;, with no warranty, express or implied, of its quality or consistency. It is provided without support and without obligation on the part of MBARI to assist in its use, correction, modification, or enhancement. This information should not be published or distributed to third parties without specific written permission from MBARI.</font></p> <p><font size="-1" color="#336699">Copyright 2003 MBARI. MBARI Proprietary Information. All rights reserved.</font></p>
 * @author      <a href="http://www.mbari.org">MBARI</a>
 * @version     $Id: IPopup.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public interface IPopup {

    /**
	 * <p><!-- Method description --></p>
	 * @return
	 * @uml.property  name="popupMenu"
	 * @uml.associationEnd  
	 */
    JPopupMenu getPopupMenu();
}
