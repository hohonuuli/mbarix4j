package mbarix4j.net;

import java.io.File;
import java.net.URL;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 * URLUtilTest.java
 *
 * Created on March 8, 2007, 1:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author brian
 */
public class URLUtilTest {
    
    private static final Logger log = LoggerFactory.getLogger(URLUtilTest.class);
    /** Creates a new instance of URLUtilTest */
    public URLUtilTest() {
    }
    
    @Test()
    public void testUrlToTempFile() {
        String[] s = {"http://nanomia.shore.mbari.org/EITS_testing/RESULTS/09272006_120017/09272006_120017/09272006_120017.mov",
        "http://nanomia.shore.mbari.org/EITS_testing/RESULTS/09272006_120017/09272006_120017/09272006_120017.events.xml"
        };
        
        for (String u: s){
            try {
                URL url = new URL(u);
                File file = URLUtilities.urlToTempFile(url);
                file.deleteOnExit();
            } catch (Exception ex) {
                log.error("Failed", ex);
                Assert.fail();
            }
        }
    }
    
}
