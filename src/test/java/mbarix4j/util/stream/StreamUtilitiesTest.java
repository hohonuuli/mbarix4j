package mbarix4j.util.stream;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Brian Schlining
 * @since 2015-10-06T15:03:00
 */
public class StreamUtilitiesTest {

    @Test
    public void testIteratorToStream() {
        Iterator<String> sourceIterator = Arrays.asList("A", "B", "C").iterator();
        List<String> aPrefixedStrings = StreamUtilities.toStream(sourceIterator)
                .filter(t -> t.startsWith("A"))
                .collect(Collectors.toList());

        assertEquals("Size did not match", 1, aPrefixedStrings.size());
    }
}
