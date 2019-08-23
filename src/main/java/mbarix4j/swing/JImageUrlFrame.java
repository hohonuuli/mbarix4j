/*
 * @(#)JImageUrlFrame.java   2010.05.03 at 03:53:42 PDT
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
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ProgressMonitor;
import mbarix4j.awt.event.ActionAdapter;
import mbarix4j.awt.event.ActionRunnable;
import mbarix4j.swing.actions.SaveFramegrabsAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a class for displaying an image in it's own frame. Use as:
 * <code>
 * ImageFrame f = new ImageFrame();
 * f.show();
 * f.setImageUrl(new URL("http://foobar.com/foo/bar/image1.png"));
 * // You can keep reusing the frame later on to display other images.
 * f.setImageUrl(new URL("http://foobar.com/foo/bar/image2.png"));
 * </code>
 *
 * @author brian
 * @version $Id: ImageFrame.java 332 2006-08-01 18:38:46Z hohonuuli $
 */
public class JImageUrlFrame extends JImageFrame {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private URL imageUrl;
    private JMenuBar theMenuBar;

    /**
     *
     * @throws HeadlessException
     */
    public JImageUrlFrame() throws HeadlessException {
        super();
        initialize();
    }

    public JImageUrlFrame(URL url) throws HeadlessException {
        this();
        setImageUrl(url);
    }

    /**
     * @return  the imageUrl
     */
    public URL getImageUrl() {
        return imageUrl;
    }

    private JMenuBar getTheMenuBar() {
        if (theMenuBar == null) {
            theMenuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
            theMenuBar.add(fileMenu);
            JMenuItem saveItem = new JMenuItem(new SaveImageAction());
            fileMenu.add(saveItem);
        }

        return theMenuBar;
    }

    protected void initialize() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setJMenuBar(getTheMenuBar());
    }

    /**
     *
     * @param newImageUrl
     */
    public void setImageUrl(final URL newImageUrl) {
        (new ImageLoader(newImageUrl)).execute();
    }

    public static void main(String[] args) throws Exception {
        final URL url1 = args.getClass().getResource("/images/BrianSchlining.jpg");
        final URL url2 = args.getClass().getResource("/images/no_image.jpg");
        final JImageUrlFrame imageFrame = new JImageUrlFrame();
        imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imageFrame.setVisible(true);
        JImageCanvas imageCanvas = (JImageCanvas) imageFrame.getImageDisplayComponent();
        imageFrame.setImageUrl(url1);

//        Thread swapThread = new Thread(new Runnable() {
//
//            public void run() {
//
//                while (imageFrame.isVisible()) {
//                    try {
//                        Thread.sleep(3000);
//                        SwingUtilities.invokeLater(new Runnable() {
//                            public void run() {
//                                imageFrame.setImageUrl(url2);
//                            }
//                        });
//
//                        Thread.sleep(3000);
//                        SwingUtilities.invokeLater(new Runnable() {
//                            public void run() {
//                                imageFrame.setImageUrl(url1);
//                            }
//                        });
//                    }
//                    catch (InterruptedException e) {
//                        System.exit(0);
//                    }
//                }
//            }
//        });
//        swapThread.setDaemon(true);
//        swapThread.start();

    }

    /**
     * @return
     */
    private class ImageLoader extends javax.swing.SwingWorker<BufferedImage, Object> {

        final URL url;

        /**
         * Constructs ...
         *
         * @param url
         */
        public ImageLoader(final URL url) {
            this.url = url;
        }

        protected BufferedImage doInBackground() throws Exception {
            log.debug("Reading image from " + url);
            return ImageIO.read(url);
        }

        @Override
        protected void done() {
            try {
                final Image newImage = get();
                log.debug("Image " + url + " [" + newImage.getWidth(JImageUrlFrame.this) + " x " +
                          newImage.getHeight(JImageUrlFrame.this) + " pixels] has been loaded");
                JImageUrlFrame.this.imageUrl = url;
                setImage(newImage);
            }
            catch (Exception e) {
                // TODO Draw an image with error text on it
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


    /**
     * Class for saving all the images in a search to the desktop. The class
     * SaveFramegrabsAction does all the work however we wrap it in this action
     * to a) get the images to save from the resultPanel and b) Pop up a dialog
     * to select the save location.
     */
    class SaveImageAction extends ActionAdapter {

        private final SaveFramegrabsAction action = new SaveFramegrabsAction();
        private JFileChooser chooser;

        /**
         * Constructs ...
         */
        public SaveImageAction() {
            super("Save Image");
        }

        /**
         */
        public void doAction() {

            // Show dialog for selecting a directory
            int option = getChooser().showOpenDialog(JImageUrlFrame.this);
            if (option == JFileChooser.APPROVE_OPTION) {
                action.setSaveLocation(getChooser().getSelectedFile());
                URL[] urls = new URL[] { imageUrl };
                action.setUrls(urls);
                action.setProgressMonitor(new ProgressMonitor(JImageUrlFrame.this, "Downloading images", "", 0,
                        urls.length));
                ActionRunnable ar = new ActionRunnable(action);
                ar.start();
            }
        }

        private JFileChooser getChooser() {
            if (chooser == null) {
                chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            }

            return chooser;
        }
    }
}
