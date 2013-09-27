/*
 * TimecodeTest.java
 * JUnit based test
 *
 * Created on February 22, 2007, 4:15 PM
 */

package org.mbari.movie;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestCase;
import junit.textui.TestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Assert;
import org.junit.Test;
import org.mbari.util.IObserver;

/**
 *
 * @author brian
 */
public class TimecodeTest {
    
    private static final Logger log = LoggerFactory.getLogger(TimecodeTest.class);

    /**
     * Method description
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        TestRunner.run(suite());
    }

 
    @Test()
    public void testSetFrames() {
        double frameRate = 30.0;
        final Timecode timecode = new Timecode(0, frameRate);
        timecode.addObserver(new IObserver() {
            public void update(Object obj, Object changeCode) {
                log.debug("Timecode: " + timecode);
            }
        });
        double frames = timecode.getFrames();
        Assert.assertEquals(0, frames, 0.001);
        
        for (int i = 0; i < frameRate; i++) {
            timecode.setFrames(i);
            Assert.assertEquals(i, timecode.getFrames(), 0.001);
            Assert.assertEquals(i, timecode.getFrame());
            
        }
        
        for (int i = 0; i <= frameRate; i++) {
            for (int j = 0; j < frameRate; j++) {
                frames = frameRate * i + j;
                timecode.setFrames(frames);
                Assert.assertEquals(j, timecode.getFrame(), 0.001);
                Assert.assertEquals(i, timecode.getSecond());
                Assert.assertEquals(0, timecode.getMinute());
                Assert.assertEquals(0, timecode.getHour());
                Assert.assertEquals(frames, timecode.getFrames(), 0.001);
            }
        }
    }
    
    /**
     * Quick test to verify that setting the framerate doesn't break the hour,
     * minute, second, frame.
     */
    @Test
    public void testFrameRate() {
        String time = "09:44:10:00";
        Timecode timecode = new Timecode(time);
        for (int i = 12; i < 60 ; i += 3) {
            timecode.setFrameRate(i);
            log.debug("Timecode = " + timecode + "; frameRate = " + i + "; frames = " + timecode.getFrames());
            Assert.assertTrue(timecode.toString().equals(time));
        }
    }

    /**
     * Method description
     *
     *
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TimecodeTest.class);
    }
    


}
