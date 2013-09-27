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
 * Created on Mar 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.mbari.util;

import java.util.Set;

//~--- classes ----------------------------------------------------------------

/**
 * @author brian
 */
@Deprecated
public class ImmutableSet extends ImmutableCollection implements Set {

    /**
     * @param delegate
     */
    public ImmutableSet(Set delegate) {
        super(delegate);
        // TODO Auto-generated constructor stub
    }
}
