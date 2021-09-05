package mbarix4j3.swing;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URL;

/**
 * @author Brian Schlining
 * @since 2019-09-06T08:36:00
 */
public class ReadResourceTest {

    private final String src = "/mbarix4j/images/view.png";

    @Test
    public void readResourceUsingClass() {
        URL resource = getClass().getResource(src);
        assertNotNull(resource);
    }

    @Ignore("This should fail")
    @Test
    public void readResourceUsingClasspath() {
        URL resource = getClass().getClassLoader().getResource(src);
        assertNotNull(resource);
    }

    @Test
    public void readResourceUsingModule() throws Exception{
        InputStream stream = getClass().getModule().getResourceAsStream(src);
        assertNotNull(stream);
        stream.close();
    }
}
