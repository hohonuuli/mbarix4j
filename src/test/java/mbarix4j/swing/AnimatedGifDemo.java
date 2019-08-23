package mbarix4j.swing;



import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author brian
 * @version $Id: $
 * @since Nov 17, 2006 3:40:40 PM PST
 */
public class AnimatedGifDemo {

    private static final Logger log = LoggerFactory.getLogger(AnimatedGifDemo.class);

    public static void main(String[] args){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel(new ImageIcon(frame.getClass().getResource("/mbarix4j/images/wait18trans.gif"))));
        frame.pack();
        frame.setVisible(true);
    }

}