/*
 * @(#)JImageCanvas.java   2010.03.19 at 10:55:23 PDT
 *
 * Copyright 2009 MBARI
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package mbarix4j.swing;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.*;

/**
 * <p>
 * Displays an image and resizes as it's container resizes. The image is always
 * displayed at the correct aspect ratio even if the size of this component does
 * not have the same aspect ratio. Use as:</p>
 *
 * <pre>
 * JComponent imageCanvas = new JImageCanvas(someImage);
 * someJPanel.add(imageCanvas);
 * </pre>
 *
 *@author     <a href="http://www.mbari.org">MBARI</a>
 */
public class JImageCanvas extends JComponent {

    /**  */
    public static final String PROP_IMAGE = "image";
    private static final long serialVersionUID = -6750506357985486937L;
    private Image image;

    /**
     * Constructs ...
     */
    public JImageCanvas() {
        this(null);
    }

    /**
     * Constructs ...
     *
     * @param image
     */
    public JImageCanvas(Image image) {
        setBackground(Color.BLACK);
        setImage(image);
    }

    /**
     * Convert from a {@link Point} in the image (i.e. pixel coordinates) to
     * the corresponding locating in the {@link JImageCanvas}. Allows positioning
     * in a scaled component based on the pixel index in image coordinates.
     *
     * @param imagePoint A {@link Point} containing pixel coordinates in the image
     * @return A {@link Point} representing the corresponding location in the
     * ImgaeCanvas containing the image. If the image property is null this will
     * return null.
     */
    public Point2D convertToComponent(Point2D imagePoint) {
        Point2D point = null;

        if (image != null) {
            int imageWidth = image.getWidth(this);
            int imageHeight = image.getHeight(this);

            if ((imagePoint.getX() >= 0) && (imagePoint.getX() <= imageWidth) && (imagePoint.getY() >= 0) &&
                    (imagePoint.getY() <= imageHeight)) {

                Rectangle2D r = getImageRectangle();

                // The following 3 lines all return the same value
                //double widthRatio = image.getWidth(this) / r.getWidth();
                //double heightRatio = image.getHeight(this) / r.getHeight();
                double scale = image.getWidth(this) / r.getWidth();

                // Scale
                double sx = imagePoint.getX() / scale;
                double sy = imagePoint.getY() / scale;

                // Offset
                double x = r.getX() + sx;
                double y = r.getY() + sy;

                point = new Point2D.Double(x, y);

            }
        }

        return point;
    }

    /**
     * Convert a component point on the ImageCanvas to a Point on the image at
     * it's orignal size. Useful for identifiying the pixel coordinates in the
     * image even if the image is rescaled.
     *
     * @param componentPoint
     * @return A point that corresponds to the location inside the full size image.
     * <b>null</b> is returned if the componentPoint is outside the bounds of the image
     * or if the image property is null
     */
    public Point2D convertToImage(Point2D componentPoint) {

        Point2D point = null;
        if (image != null && this.getBounds().contains(componentPoint)) {

            Rectangle2D r = getImageRectangle();

            // The following 3 lines all return the same value
            //double widthRatio = r.getWidth() / image.getWidth(this);
            //double heightRatio = r.getHeight() / image.getHeight(this);
            double scale = r.getWidth() / image.getWidth(this);

            // Offset
            double x = componentPoint.getX() - r.getX();
            double y = componentPoint.getY() - r.getY();

            // Scale
            double sx = x / scale;
            double sy = y / scale;

            point = new Point2D.Double(sx, sy);

        }

        return point;
    }

    /**
     * Returns the scaling of the displayed image. Values &gt; 1 mean the image
     * is displayed larger than it's actual size. Values &lt; 1 mean the image is
     * displayed smaller than it's actual size.
     * 
     * @return The scaling (i.e. magnification) applied to the displayed image. If
     *      the image property is null this will return a value of 1 (parity)
     */
    public double getImageScale() {
        return (image == null) ? 1D : getImageRectangle().getWidth() / image.getWidth(this);
    }

    /**
     * @return
     */
    public Image getImage() {
        return image;
    }

    /**
     * @return    The height of the image. 0 is returned if the image property is
     *          null
     */
    public int getImageHeight() {
        return (image == null) ? 0 : image.getHeight(this);
    }

    /**
     * Represents location of image within this canvas.
     * @return The rectangle in the component where the image is located. If the
     *  image property is null this will return null
     */
    public Rectangle2D getImageRectangle() {

        if (image == null) {
            return null;
        }

        double componentWidth = this.getWidth();
        double componentHeight = this.getHeight();
        double imageWidth = image.getWidth(this);
        double imageHeight = image.getHeight(this);
        double wImageWidth = imageWidth;
        double wImageHeight = imageHeight;
        double hImageWidth = 0;
        double hImageHeight = 0;
        double newImageWidth = 0;
        double newImageHeight = 0;

        // check if image was loaded successfully
        if ((imageWidth > 0) && (imageHeight > 0)) {

            if (imageWidth == componentWidth && imageHeight == componentHeight) {
                wImageWidth = imageWidth;
                wImageHeight = imageHeight;
            }

            // Scale image to fit the canvas
            // calc new dimensions based on width
            if (imageWidth != componentWidth) {
                wImageWidth = componentWidth;
                wImageHeight = imageHeight * componentWidth / imageWidth;
            }

            // calc new dimensions based on height
            if (imageHeight != componentHeight) {
                hImageHeight = componentHeight;
                hImageWidth = imageWidth * componentHeight / imageHeight;
            }

            // assign one set of new dimensions or the other
            if (wImageHeight > componentHeight) {
                // the new height based on width is too large
                newImageHeight = hImageHeight;
                newImageWidth = hImageWidth;
            }
            else {
                // assume new width based on height is too large
                newImageHeight = wImageHeight;
                newImageWidth = wImageWidth;
            }
        }

        double x = (componentWidth - newImageWidth) / 2.0;
        double y = (componentHeight - newImageHeight) / 2.0;

        return new Rectangle2D.Double(x, y, newImageWidth, newImageHeight);

    }

    /**
     * @return    The width of the image. 0 is returned if the image property
     *      is null
     */
    public int getImageWidth() {
        return (image == null) ? 0 : image.getWidth(this);
    }

    /**
     * @return    Returns true if the image was not loaded.
     */
    public boolean isAborted() {
        int infoFlags = this.checkImage(image, this);
        if ((infoFlags & ABORT) != 0) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Fill background with black
        g.setColor(getBackground());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        // Center the image
        if (image != null) {
            Rectangle r = getImageRectangle().getBounds();
            g.drawImage(image, r.x, r.y, r.width, r.height, this);
        }

        super.paintComponent(g);
    }


    /**
     *
     * @param image
     */
    public void setImage(Image image) {
        Image oldImage = this.image;
        this.image = image;
        if (image != null) {
            setPreferredSize(new Dimension(image.getWidth(this), image.getHeight(this)));
        }
        firePropertyChange(PROP_IMAGE, oldImage, image);
        repaint();
    }

}
