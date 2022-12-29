/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mbarix4j.geometry;

import java.util.Comparator;

/**
 * Compares the Z values in a Point3D
 * 
 * @author brian
 */
public class ZComparator implements Comparator<Point3D<? extends Number>> {

    public int compare(Point3D<? extends Number> o1, Point3D<? extends Number> o2) {
        Double n1 = o1.getZ().doubleValue();
        Double n2 = o2.getZ().doubleValue();
        return n1.compareTo(n2);
    }

}
