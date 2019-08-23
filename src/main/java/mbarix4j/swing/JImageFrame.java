/*
 * @(#)JImageFrame.java   2010.05.28 at 03:19:48 PDT
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * Resizable frame for displaying an image. Frame can be resized and image will
 * resize along with it keeping it's aspect ratio preserved
 * @author brian
 */
public class JImageFrame extends JFrame {

    /**  */
    private final JImageCanvas imageCanvas = new JImageCanvas();
    private boolean autosized = true;

    /**
     * Create the frame
     */
    public JImageFrame() {
        super();
        initialize();
    }

    /**
     * Constructs ...
     *
     * @param image
     */
    public JImageFrame(Image image) {
        this();
        setImage(image);
    }

    /**
     * The image is stored internally as a {@code BufferedImage}. This
     * call returns the underlying image
     * @return The image fetched in
     */
    public Image getImage() {
        return imageCanvas.getImage();
    }

    /**
     *
     * @return retrieves the underlying component used to render the image
     */
    public JComponent getImageDisplayComponent() {
        return imageCanvas;
    }

    /**
     * Returns the scaling of image. To get the 0.1 means the image is displayed
     * at 1/10th it's actual size, 2 means the image is displayed at twice it's
     * actual size.
     * @return
     */
    public double getImageScale() {
        return imageCanvas.getImageScale();
    }

    private void initialize() {
        setLayout(new BorderLayout(0, 0));
        add(imageCanvas, BorderLayout.CENTER);

        // Add c-W keystroke to close window
        getRootPane().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()), "close");
        getRootPane().getActionMap().put("close", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JImageFrame.this.dispose();
            }

        });

    }

    /**
     * @return
     */
    public boolean isAutosized() {
        return autosized;
    }

    private void layoutFrame() {

        // Needs to be visible in order to get locationOnScreen
        if (autosized && isVisible()) {
            int imageWidth = imageCanvas.getImageWidth();
            int imageHeight = imageCanvas.getImageHeight();
            Dimension screenSize = getToolkit().getScreenSize();
            Point locationOnScreen = getLocationOnScreen();
            Insets insets = getInsets();

            int preferredWidth = screenSize.width - (locationOnScreen.x + insets.left + insets.right);
            int preferredHeight = screenSize.height - (locationOnScreen.y + insets.top + insets.bottom);
            int actualFrameWidth = imageWidth + insets.left + insets.right;
            int actualFrameHeight = imageHeight + insets.top + insets.bottom;

            // If image fits on screen at the current frame position, just
            // resize the frame to the size of the image
            Dimension size = null;

            if ((actualFrameWidth <= preferredWidth) && (actualFrameHeight <= preferredHeight)) {
                size = new Dimension(actualFrameWidth, actualFrameHeight);
            }
            else {
                double scaleX = (double) preferredWidth / (double) imageWidth;
                double scaleY = (double) preferredHeight / (double) imageWidth;

                if (scaleX <= scaleY) {
                    size = new Dimension(preferredWidth, (int) Math.round(imageHeight * scaleX));
                }
                else {
                    size = new Dimension((int) Math.round(imageWidth * scaleY), preferredHeight);
                }
            }

            setSize(size);
            validate();    // This call elimates a subtle flicker
        }

        repaint();
    }

    /**
     *
     * @param args
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        final Image image1 = ImageIO.read(args.getClass().getResource("/images/BrianSchlining.jpg"));
        final Image image2 = ImageIO.read(args.getClass().getResource("/images/no_image.jpg"));
        final JImageFrame imageFrame = new JImageFrame(image1);

        imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imageFrame.setVisible(true);

        Thread swapThread = new Thread(new Runnable() {

            public void run() {

                while (imageFrame.isVisible()) {
                    try {
                        Thread.sleep(3000);
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                imageFrame.setImage(image2);
                            }

                        });

                        Thread.sleep(3000);
                        SwingUtilities.invokeLater(new Runnable() {

                            public void run() {
                                imageFrame.setImage(image1);
                            }

                        });
                    }
                    catch (InterruptedException e) {
                        System.exit(0);
                    }
                }
            }
        });

        swapThread.setDaemon(true);
        swapThread.start();

    }

    /**
     *
     * @param autosize
     */
    public void setAutosized(boolean autosize) {
        this.autosized = autosize;
    }

    /**
     *
     * @param image
     */
    public void setImage(Image image) {
        imageCanvas.setImage(image);
        layoutFrame();
    }

    /**
     *
     * @param b
     */
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);    //To change body of overridden methods use File | Settings | File Templates.
        layoutFrame();
    }
}
