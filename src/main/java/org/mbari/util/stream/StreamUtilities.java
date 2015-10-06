package org.mbari.util.stream;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtilities {

    public static <T> Stream<T> toStream(Iterator<T> sourceIterator) {
        return toStream(sourceIterator, false);
    }

    public static <T> Stream<T> toStream(Iterator<T> sourceIterator, boolean parallel) {
        Iterable<T> iterable = () -> sourceIterator;
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }
}