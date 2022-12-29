/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mbarix4j.geometry;

import java.util.Comparator;

/**
 *
 * @author brian
 */
public class YComparator implements Comparator<Point2D<? extends Number>> {

    public int compare(Point2D<? extends Number> o1, Point2D<? extends Number> o2) {
        Double n1 = o1.getY().doubleValue();
        Double n2 = o2.getY().doubleValue();
        return n1.compareTo(n2);
    }

}
