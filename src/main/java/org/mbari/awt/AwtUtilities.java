/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mbari.awt;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.awt.geom.Point2D;

/**
 *
 * @author brian
 */
public class AwtUtilities {

    /**
     * Convert a {@link Point} to a {@link Point2D}. Decimals are rounded.
     * @param p2 The Point2D object to convert
     * @return the Point object closes to the Point2D object
     */
    public static Point toPoint(Point2D p2) {
        return new Point((int) Math.round(p2.getX()), (int) Math.round(p2.getY()));
    }

    /**
     * Convert a {@link Point2D} to a {@link Point}.
     * @param p The Point object to convert
     * @return the Point2D.Double object containing the infromation from the
     *      point you supplied.
     */
    public static Point2D toPoint2D(Point p) {
        return new Point2D.Double(p.x, p.y);
    }

    /**
     * Returns the Window that contains the given component. If it's not contained
     * in window then null is returned.
     *
     * @param component The component whose containing window we want to find.
     * @return The containing window or null if not in a containing window
     */
    public static Window getWindow(Component component) {
        while (true) {
            Component parent = component.getParent();
            if (parent == null) {
                return (component instanceof Window) ? (Window) component : null;
            }
            component = parent;
        }
    }

    /**
     * Returns the Frame that contains the given component. If it's not contained
     * in a Frame (for example if it's in a Dialog, which is a Window but not a Frame)
     * then null is returned
     */
    public static Frame getFrame(Component component) {
        Window window = getWindow(component);
        return (window instanceof Frame) ? (Frame) window : null;
    }

}
