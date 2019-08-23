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
 * The Monterey Bay Aquarium Research Institute (MBARI) provides this
 * documentation and code 'as is', with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from MBARI
 */
package mbarix4j.awt.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

//~--- classes ----------------------------------------------------------------

/**
 * <p>VerticalFlowLayout is similar to FlowLayout except it lays out components
 * vertically. Extends FlowLayout because it mimics much of the
 * behavior of the FlowLayout class, except vertically. An additional
 * feature is that you can specify a fill to edge flag, which causes
 * the VerticalFlowLayout manager to resize all components to expand to the
 * column width Warning: This causes problems when the main panel
 * has less space that it needs and it seems to prohibit multi-column
 * output. Additionally there is a vertical fill flag, which fills the last
 * component to the remaining height of the container.</p>
 *
 *@author     <a href="http://www.mbari.org">MBARI</a>
 *@created    October 3, 2004
 *@version    $Id: VerticalFlowLayout.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class VerticalFlowLayout extends FlowLayout
        implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -8826147318144148303L;

    /** <!-- Field description --> */
    public final static int TOP = 0;

    /** <!-- Field description --> */
    public final static int MIDDLE = 1;

    /** <!-- Field description --> */
    public final static int BOTTOM = 2;

    //~--- fields -------------------------------------------------------------

    /**
	 * @uml.property  name="hfill"
	 */
    boolean hfill;

    // int align;
    /**
	 * @uml.property  name="hgap"
	 */
    int hgap;
    /**
	 * @uml.property  name="vfill"
	 */
    boolean vfill;
    /**
	 * @uml.property  name="vgap"
	 */
    int vgap;

    //~--- constructors -------------------------------------------------------

    /**
     * Construct a new VerticalFlowLayout with a middle alignemnt, and the fill to edge flag set.
     */
    public VerticalFlowLayout() {
        this(TOP, 5, 5, true, false);
    }

    /**
     * Construct a new VerticalFlowLayout with a middle alignemnt.
     *
     * @param  align  the alignment value
     */
    public VerticalFlowLayout(int align) {
        this(align, 5, 5, true, false);
    }

    /**
     * Construct a new VerticalFlowLayout with a middle alignemnt.
     *
     * @param  hfill  Description of the Parameter
     * @param  vfill  Description of the Parameter
     */
    public VerticalFlowLayout(boolean hfill, boolean vfill) {
        this(TOP, 5, 5, hfill, vfill);
    }

    /**
     * Construct a new VerticalFlowLayout.
     *
     * @param  align  the alignment value
     * @param  hfill  Description of the Parameter
     * @param  vfill  Description of the Parameter
     */
    public VerticalFlowLayout(int align, boolean hfill, boolean vfill) {
        this(align, 5, 5, hfill, vfill);
    }

    /**
     * Construct a new VerticalFlowLayout.
     *
     * @param  align  the alignment value
     * @param  hgap   the horizontal gap variable
     * @param  vgap   the vertical gap variable
     * @param  hfill  Description of the Parameter
     * @param  vfill  Description of the Parameter
     */
    public VerticalFlowLayout(int align, int hgap, int vgap, boolean hfill,
            boolean vfill) {
        setAlignment(align);
        this.hgap = hgap;
        this.vgap = vgap;
        this.hfill = hfill;
        this.vfill = vfill;
    }

    //~--- get methods --------------------------------------------------------

    /**
	 * Gets the horizontal gap between components.
	 * @return     the horizontal gap between components.
	 * @see java.awt.FlowLayout#setHgap
	 * @since      JDK1.1
	 * @uml.property  name="hgap"
	 */
    public int getHgap() {
        return hgap;
    }

    /**
     *  Gets the horizontalFill attribute of the VerticalFlowLayout object
     *
     * @return    The horizontalFill value
     */
    public boolean getHorizontalFill() {
        return hfill;
    }

    /**
     *  Gets the verticalFill attribute of the VerticalFlowLayout object
     *
     * @return    The verticalFill value
     */
    public boolean getVerticalFill() {
        return vfill;
    }

    /**
	 * Gets the vertical gap between components.
	 * @return     The vgap value
	 * @uml.property  name="vgap"
	 */
    public int getVgap() {
        return vgap;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Lays out the container.
     *
     * @param  target  the container to lay out.
     */
    public void layoutContainer(Container target) {
        Insets insets = target.getInsets();
        int maxheight = target.getSize().height -
            (insets.top + insets.bottom + vgap * 2);
        int maxwidth = target.getSize().width -
            (insets.left + insets.right + hgap * 2);
        int numcomp = target.getComponentCount();
        int x = insets.left + hgap;
        int y = 0;
        int colw = 0;
        int start = 0;
        for (int i = 0; i < numcomp; i++) {
            Component m = target.getComponent(i);
            if (m.isVisible()) {
                Dimension d = m.getPreferredSize();

                // fit last component to remaining height
                if ((this.vfill) && (i == (numcomp - 1))) {
                    d.height = Math.max((maxheight - y),
                            m.getPreferredSize().height);
                }

                // fit componenent size to container width
                if (this.hfill) {
                    m.setSize(maxwidth, d.height);
                    d.width = maxwidth;
                } else {
                    m.setSize(d.width, d.height);
                }

                if (y + d.height > maxheight) {
                    placethem(
                            target,
                            x,
                            insets.top + vgap,
                            colw,
                            maxheight - y,
                            start,
                            i);
                    y = d.height;
                    x += hgap + colw;
                    colw = d.width;
                    start = i;
                } else {
                    if (y > 0) {
                        y += vgap;
                    }

                    y += d.height;
                    colw = Math.max(colw, d.width);
                }
            }
        }

        placethem(
                target,
                x,
                insets.top + vgap,
                colw,
                maxheight - y,
                start,
                numcomp);
    }

    /**
     * Returns the minimum size needed to layout the target container
     *
     * @param  target  the component to lay out
     * @return         Description of the Return Value
     */
    public Dimension minimumLayoutSize(Container target) {
        Dimension tarsiz = new Dimension(0, 0);
        for (int i = 0; i < target.getComponentCount(); i++) {
            Component m = target.getComponent(i);
            if (m.isVisible()) {
                Dimension d = m.getMinimumSize();
                tarsiz.width = Math.max(tarsiz.width, d.width);

                if (i > 0) {
                    tarsiz.height += vgap;
                }

                tarsiz.height += d.height;
            }
        }

        Insets insets = target.getInsets();
        tarsiz.width += insets.left + insets.right + hgap * 2;
        tarsiz.height += insets.top + insets.bottom + vgap * 2;
        return tarsiz;
    }

    /**
     * places the components defined by first to last within the target container using the bounds box defined
     *
     * @param  target  the container
     * @param  x       the x coordinate of the area
     * @param  y       the y coordinate of the area
     * @param  width   the width of the area
     * @param  height  the height of the area
     * @param  first   the first component of the container to place
     * @param  last    the last component of the container to place
     */
    private void placethem(Container target, int x, int y, int width,
            int height, int first, int last) {
        int align = getAlignment();

        // if ( align == this.TOP )
        // y = 0;
        // Insets insets = target.getInsets();
        if (align == VerticalFlowLayout.MIDDLE) {
            y += height / 2;
        }

        if (align == VerticalFlowLayout.BOTTOM) {
            y += height;
        }

        for (int i = first; i < last; i++) {
            Component m = target.getComponent(i);
            Dimension md = m.getSize();
            if (m.isVisible()) {
                int px = x + (width - md.width) / 2;
                m.setLocation(px, y);
                y += vgap + md.height;
            }
        }
    }

    /**
     * Returns the preferred dimensions given the components in the target container.
     *
     * @param  target  the component to lay out
     * @return         Description of the Return Value
     */
    public Dimension preferredLayoutSize(Container target) {
        Dimension tarsiz = new Dimension(0, 0);
        for (int i = 0; i < target.getComponentCount(); i++) {
            Component m = target.getComponent(i);
            if (m.isVisible()) {
                Dimension d = m.getPreferredSize();
                tarsiz.width = Math.max(tarsiz.width, d.width);

                if (i > 0) {
                    tarsiz.height += vgap;
                }

                tarsiz.height += d.height;
            }
        }

        Insets insets = target.getInsets();
        tarsiz.width += insets.left + insets.right + hgap * 2;
        tarsiz.height += insets.top + insets.bottom + vgap * 2;
        return tarsiz;
    }

    //~--- set methods --------------------------------------------------------

    /**
	 * Sets the horizontal gap between components.
	 * @param hgap   The new hgap value
	 * @uml.property  name="hgap"
	 */
    public void setHgap(int hgap) {
        super.setHgap(hgap);
        this.hgap = hgap;
    }

    /**
     *  Sets the horizontalFill attribute of the VerticalFlowLayout object
     *
     * @param  hfill  The new horizontalFill value
     */
    public void setHorizontalFill(boolean hfill) {
        this.hfill = hfill;
    }

    /**
     *  Sets the verticalFill attribute of the VerticalFlowLayout object
     *
     * @param  vfill  The new verticalFill value
     */
    public void setVerticalFill(boolean vfill) {
        this.vfill = vfill;
    }

    /**
	 * Sets the vertical gap between components.
	 * @param vgap   The new vgap value
	 * @uml.property  name="vgap"
	 */
    public void setVgap(int vgap) {
        super.setVgap(vgap);
        this.vgap = vgap;
    }
}
