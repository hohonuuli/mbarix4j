/*
 * ReverseSortComparator.java
 * 
 * Created on Aug 24, 2007, 9:41:23 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mbarix4j.text;

import java.util.Comparator;

/**
 * Sorts Comparable objects in referse order based on thier <b>compareTo</b> methods
 * @author brian
 */
public class ReverseSortComparator implements Comparator {

    public ReverseSortComparator() {
        super();
    }

    @SuppressWarnings("unchecked")
    public int compare(Object o1, Object o2) {
        if (!(o1 instanceof Comparable) || !(o2 instanceof Comparable)) {
            throw new IllegalArgumentException("Attempted to compare 2 objects that do not both implement " +
                    "the Comparable interface...bad, bad, bad! ");
        }
        
        Comparable a = (Comparable) o1;
        Comparable b = (Comparable) o2;
        
        return b.compareTo(a);
    }

}
