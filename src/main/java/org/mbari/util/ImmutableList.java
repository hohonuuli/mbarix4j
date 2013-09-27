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


package org.mbari.util;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

//~--- classes ----------------------------------------------------------------

/**
 * @author brian
 * @version $Id: ImmutableList.java 3 2005-10-27 16:20:12Z hohonuuli $
 */
@Deprecated
public class ImmutableList extends ImmutableCollection implements List {

    /**
     *
     *
     * @param delegate
     */
    public ImmutableList(List delegate) {
        super(delegate);
    }

    //~--- methods ------------------------------------------------------------

    /*
     *  (non-Javadoc)
     * @see java.util.List#add(int, java.lang.Object)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param arg0
     * @param arg1
     */
    public void add(int arg0, Object arg1) {
        throw new UnsupportedOperationException(
                "Adding to an immutable collection is not allowed");
    }

    /*
     *  (non-Javadoc)
     * @see java.util.List#addAll(int, java.util.Collection)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param arg0
     * @param arg1
     *
     * @return
     */
    public boolean addAll(int arg0, Collection arg1) {
        throw new UnsupportedOperationException(
                "Adding to an immutable collection is not allowed");
    }

    //~--- get methods --------------------------------------------------------

    /*
     *  (non-Javadoc)
     * @see java.util.List#get(int)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param arg0
     *
     * @return
     */
    public Object get(int arg0) {
        return ((List) delegate).get(arg0);
    }

    //~--- methods ------------------------------------------------------------

    /*
     *  (non-Javadoc)
     * @see java.util.List#indexOf(java.lang.Object)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param arg0
     *
     * @return
     */
    public int indexOf(Object arg0) {
        return ((List) delegate).indexOf(arg0);
    }

    /*
     *  (non-Javadoc)
     * @see java.util.List#lastIndexOf(java.lang.Object)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param arg0
     *
     * @return
     */
    public int lastIndexOf(Object arg0) {
        return ((List) delegate).lastIndexOf(arg0);
    }

    /*
     *  (non-Javadoc)
     * @see java.util.List#listIterator()
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public ListIterator listIterator() {
        return ((List) delegate).listIterator();
    }

    /*
     *  (non-Javadoc)
     * @see java.util.List#listIterator(int)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param arg0
     *
     * @return
     */
    public ListIterator listIterator(int arg0) {
        return ((List) delegate).listIterator(arg0);
    }

    /*
     *  (non-Javadoc)
     * @see java.util.List#remove(int)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param arg0
     *
     * @return
     */
    public Object remove(int arg0) {
        throw new UnsupportedOperationException(
                "Removing from an immutable collection is not allowed");
    }

    //~--- set methods --------------------------------------------------------

    /*
     *  (non-Javadoc)
     * @see java.util.List#set(int, java.lang.Object)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param arg0
     * @param arg1
     *
     * @return
     */
    public Object set(int arg0, Object arg1) {
        throw new UnsupportedOperationException(
                "Modifying an immutable collection is not allowed");
    }

    //~--- methods ------------------------------------------------------------

    /*
     *  (non-Javadoc)
     * @see java.util.List#subList(int, int)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param arg0
     * @param arg1
     *
     * @return
     */
    public List subList(int arg0, int arg1) {
        return ((List) delegate).subList(arg0, arg1);
    }
}
