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
import java.util.Iterator;

//~--- classes ----------------------------------------------------------------

/**
 * This provides an immutable wrapper around an existing collection. Useful for when
 * you want to provide a Collection but don't want the user to generally modify it.
 *
 * <pre>
 * Collection a = new ArrayList();
 * a.add("cat");a.add("dog"); a.add("boy");
 *
 * Collection b = new ImmutableCollection(a);
 * b.add("girl"); // Throws an exception
 *
 * a.add("girl"); // Can add "girl" to a
 * boolean ok = b.contains("girl"); // Can now get girl from b
 * </pre>
 *
 * @author brian
 * @version $Id: ImmutableCollection.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
@Deprecated
public class ImmutableCollection implements Collection {

    /**
	 * @uml.property  name="delegate" multiplicity="(0 -1)"
	 */
    protected Collection delegate;

    //~--- constructors -------------------------------------------------------

    /**
     *
     *
     * @param delegate
     */
    public ImmutableCollection(Collection delegate) {
        super();
        this.delegate = delegate;
    }

    //~--- methods ------------------------------------------------------------

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#add(java.lang.Object)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param o
     *
     * @return
     */
    public boolean add(Object o) {
        throw new UnsupportedOperationException(
                "Adding to an immutable collection is not allowed");
    }

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#addAll(java.util.Collection)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param c
     *
     * @return
     */
    public boolean addAll(Collection c) {
        throw new UnsupportedOperationException(
                "Adding to an immutable collection is not allowed");
    }

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#clear()
     */

    /**
     * <p><!-- Method description --></p>
     *
     */
    public void clear() {
        throw new UnsupportedOperationException(
                "Removing from an immutable collection is not allowed");
    }

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#contains(java.lang.Object)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param o
     *
     * @return
     */
    public boolean contains(Object o) {
        return delegate.contains(o);
    }

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#containsAll(java.util.Collection)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param c
     *
     * @return
     */
    public boolean containsAll(Collection c) {
        return delegate.containsAll(c);
    }

    //~--- get methods --------------------------------------------------------

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#isEmpty()
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    //~--- methods ------------------------------------------------------------

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#iterator()
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public Iterator iterator() {
        return delegate.iterator();
    }

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#remove(java.lang.Object)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param o
     *
     * @return
     */
    public boolean remove(Object o) {
        throw new UnsupportedOperationException(
                "Removing from an immutable collection is not allowed");
    }

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#removeAll(java.util.Collection)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param c
     *
     * @return
     */
    public boolean removeAll(Collection c) {
        throw new UnsupportedOperationException(
                "Removing from an immutable collection is not allowed");
    }

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#retainAll(java.util.Collection)
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param c
     *
     * @return
     */
    public boolean retainAll(Collection c) {
        throw new UnsupportedOperationException(
                "Modifying an immutable collection is not allowed");
    }

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#size()
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public int size() {
        return delegate.size();
    }

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#toArray()
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public Object[] toArray() {
        return delegate.toArray();
    }

    /*
     *  (non-Javadoc)
     * @see java.util.Collection#toArray(java.lang.Object[])
     */

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param a
     *
     * @return
     */
    public Object[] toArray(Object[] a) {
        return delegate.toArray(a);
    }

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @return
     */
    public String toString() {
        return delegate.toString();
    }
}
