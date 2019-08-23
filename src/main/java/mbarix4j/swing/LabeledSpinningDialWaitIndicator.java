/*
 * @(#)LabeledSpinningDialWaitIndicator.java   2010.01.21 at 10:07:56 PST
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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author brian
 */
public class LabeledSpinningDialWaitIndicator extends SpinningDialWaitIndicator {

    private Font font;
    private String label;

    /**
     * Constructs ...
     *
     * @param target
     */
    public LabeledSpinningDialWaitIndicator(JComponent target) {
        this(target, null, null);
    }

    /**
     * Constructs ...
     *
     * @param frame
     */
    public LabeledSpinningDialWaitIndicator(JFrame frame) {
        this(frame, null, null);
    }

    /**
     * Constructs ...
     *
     * @param target
     * @param label
     */
    public LabeledSpinningDialWaitIndicator(JComponent target, String label) {
        this(target, label, null);
    }

    /**
     * Constructs ...
     *
     * @param frame
     * @param label
     */
    public LabeledSpinningDialWaitIndicator(JFrame frame, String label) {
        this(frame, label, null);
    }

    /**
     * Constructs ...
     *
     * @param target
     * @param label
     * @param font
     */
    public LabeledSpinningDialWaitIndicator(JComponent target, String label, Font font) {
        super(target);
        this.label = label;
        this.font = font;
    }

    /**
     * Constructs ...
     *
     * @param frame
     * @param label
     * @param font
     */
    public LabeledSpinningDialWaitIndicator(JFrame frame, String label, Font font) {
        super(frame);
        this.label = label;
        this.font = font;
    }

    /**
     * @return
     */
    public Font getFont() {
        return font;
    }

    /**
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    JFrame f = new JFrame("Labeled dial test");
                    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    f.setLayout(new BorderLayout());
                    URL url = f.getClass().getResource("/mbarix4j/images/test.jpg");
                    BufferedImage image = ImageIO.read(url);
                    JLabel label = new JLabel(new ImageIcon(image));
                    f.add(label, BorderLayout.CENTER);
                    f.pack();
                    f.setVisible(true);
                    final LabeledSpinningDialWaitIndicator waitIndicator = new LabeledSpinningDialWaitIndicator(f,
                        "This is just a test");

                    Timer timer = new Timer(2000, new ActionListener() {

                        int i = 0;

                        String[] strings = new String[] {
                            "Changing to new text", "more text", "This class is " + getClass().getSimpleName(), "OK",
                            "We're done now", "bye!!"
                        };


                        public void actionPerformed(ActionEvent e) {
                            waitIndicator.setLabel(strings[i]);
                            i++;

                            if (i == strings.length) {
                                i = 0;
                            }
                        }
                    });

                    timer.setRepeats(true);
                    timer.start();


                    Timer timer1 = new Timer(750, new ActionListener() {

                        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();


                        public void actionPerformed(ActionEvent e) {
                            int i = (int) Math.round(Math.random() * (fonts.length - 1));
                            int size = (int) Math.round(Math.random() * 24) + 8;
                            Font font = new Font(fonts[i].getName(), Font.PLAIN, size);
                            waitIndicator.setFont(font);
                        }
                    });

                    timer1.setRepeats(true);
                    timer1.start();

                }
                catch (IOException ex) {
                    System.out.println("An error occurred");
                    ex.printStackTrace();
                    System.exit(2);
                }
            }
        });

    }

    /**
     *
     * @param graphics
     */
    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);

        if (label != null) {
            if (fade < FADE_THRESHOLD) {
                return;
            }

            Graphics2D g2 = (Graphics2D) graphics;
            Rectangle r = getComponent().getVisibleRect();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg = getComponent().getForeground();
            g2.setPaint(bg);

            if (font != null) {
                g2.setFont(font);
            }


            FontRenderContext frc = g2.getFontRenderContext();
            Rectangle2D bounds = g2.getFont().getStringBounds(label, frc);

            int x = (r.width - (int) bounds.getWidth()) / 2;
            int y = (r.height + dial.getIconHeight() + (int) bounds.getHeight()) / 2 + 4;
            g2.drawString(label, x, y);
        }

    }

    /**
     *
     * @param font
     */
    public void setFont(Font font) {
        this.font = font;
        repaint();
    }

    /**
     *
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
        repaint();
    }
}
