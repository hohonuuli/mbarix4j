/*
 * @(#)ImageWithCrossHairPanel.java   2009.12.29 at 01:36:34 PST
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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author brian
 */
public class ImageWithCrossHairPanel extends JPanel {

    private Shape crossHair;
    private BufferedImage image;

    /**
     * Constructs ...
     */
    public ImageWithCrossHairPanel() {
        initialize();
    }

    /**
     * @return
     */
    public BufferedImage getImage() {
        return image;
    }

    protected void initialize() {
        addMouseMotionListener(new CrossHairMouseMotionListener());
        setBackground(Color.BLACK);
    }

    /**
     *
     * @param args
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        URL url = f.getClass().getResource("/images/BrianSchlining.jpg");
        BufferedImage image = ImageIO.read(url);
        ImageWithCrossHairPanel p = new ImageWithCrossHairPanel();
        p.setImage(image);

        f.setLayout(new BorderLayout());
        f.add(p, BorderLayout.CENTER);
        f.setSize(image.getWidth(), image.getHeight());
        f.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        if (image != null) {
            g2.drawImage(image, null, this);
        }
        else {
            g2.setPaint(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.setXORMode(Color.WHITE);

        if (crossHair != null) {
            g2.draw(crossHair);
        }

        g2.setPaintMode();

    }

    /**
     *
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }

    /**
     * Creates a cross hair based on the location of the mouse.
     */
    private class CrossHairMouseMotionListener implements MouseMotionListener {

        /**
         *
         * @param e
         */
        public void mouseDragged(MouseEvent e) {

            // Do nothing
        }

        /**
         *
         * @param e
         */
        public void mouseMoved(MouseEvent e) {

            if (image != null) {
                int x = e.getX();
                int y = e.getY();
                int w = image.getWidth();
                int h = image.getHeight();

                /*
                 * Create new crossHair. Only draw portions that are over the image!
                 */
                GeneralPath gp = new GeneralPath();
                if (y <= h) {

                    //if (x <= w) {
                    gp.moveTo(0, y);
                    gp.lineTo(w, y);
                }

                if (x <= w) {
                    gp.moveTo(x, 0);
                    gp.lineTo(x, h);
                }

                crossHair = gp;

                /*
                 * Redraw as little as possible!
                 */
                repaint(x, 0, 3, h);
                repaint(0, y, w, 3);
            }

        }
    }
}
