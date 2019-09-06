/*
 * @(#)ImageCanvas.java   2010.01.25 at 11:35:56 PST
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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Loads the images from a given URL and resizes as it's container resizes.
 * Use as:</p>
 *
 * <pre>
 * JComponent imageCanvas = new ImageCanvas(new URL("http://foo.com/bar.png"));
 * someJPanel.add(imageCanvas);
 * </pre>
 *
 *@author     <a href="http://www.mbari.org">MBARI</a>
 */
public class JImageUrlCanvas extends JImageCanvas {

    private static final long serialVersionUID = -6750506357985486937L;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private URL url;
    public static final String PROP_URL = "url";
    private volatile BufferedImage noImage;


    /**
     * Constructs ...
     */
    public JImageUrlCanvas() {
        super();
    }

    /**
     * Constructs ...
     *
     * @param image
     */
    public JImageUrlCanvas(Image image) {
        super(image);
    }

    /**
     * Constructor for the ImageCanvas object
     *
     * @param  strImageURL                String URL to retrieve the image from
     * @exception  MalformedURLException  If the URL is bogus this is thrown.
     */
    public JImageUrlCanvas(String strImageURL) throws MalformedURLException {
        this(new URL(strImageURL));
    }

    /**
     * Constructor for the ImageCanvas object
     *
     * @param  url  URL to retrieve the image from.
     */
    public JImageUrlCanvas(URL url) {
        setUrl(url);
    }

    /**
     * @return     Returns the url of the image being displayed
     */
    public URL getUrl() {
        return url;
    }

    private BufferedImage getNoImage() {
        if (noImage == null) {
            noImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = (Graphics2D) noImage.getGraphics();
            g.setPaint(getBackground());
            g.fillRect(0, 0, noImage.getWidth(), noImage.getHeight());
            g.setPaint(getForeground());
            g.drawString("NO IMAGE " + url , 1, 100);
            g.dispose();
        }
        return noImage;
    }

    public void setUrl(URL newUrl) {
        if (newUrl != null && newUrl.equals(url)) {
            // Do nothing. It's the same URL
        }
        else if (newUrl == null) {
            url = null;
            setImage(getNoImage());
        }
        else {
            url = newUrl;
            (new ImageLoader()).execute();
        }
    }


    /**
     *
     * @param image
     */
    @Override
    public void setImage(Image image) {
        if (image == null) {
            super.setImage(getNoImage());
        }
        else {
            super.setImage(image);
        }
    }


    /**
     * @return
     */
    private class ImageLoader extends javax.swing.SwingWorker<BufferedImage, Object> {

        protected BufferedImage doInBackground() throws Exception {
            BufferedImage bufferedImage = null;
            if (url != null) {
                log.debug("Reading image from " + url);
                bufferedImage = ImageIO.read(url);
            }
            return bufferedImage;
        }

        @Override
        protected void done() {
            try {
                final Image newImage = get();
                log.debug("Image " + url + " [" + newImage.getWidth(JImageUrlCanvas.this) + " x " +
                          newImage.getHeight(JImageUrlCanvas.this) + " pixels] has been loaded");
                setImage(newImage);
            }
            catch (Exception e) {
                // Draw an image with error text on it
                BufferedImage image = new BufferedImage(800, 200, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = (Graphics2D) image.getGraphics();
                g.setPaint(getBackground());
                g.fillRect(0, 0, image.getWidth(), image.getHeight());
                g.setPaint(getForeground());
                g.drawString("Failed to read: " + url , 1, 100);
                g.dispose();
                setImage(image);
                log.debug("Failed to read image", e);
            }
        }
    }
}
