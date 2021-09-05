package mbarix4j3.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionUtils {

  public static <T> Collection<List<T>> grouped(List<T> xs, int groupSize) {
    var counter = new AtomicInteger();
    return xs.stream()
        .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / groupSize))
        .values();
  }

  /**
   * This is used to find the most popular searches. You can grab the stream and return the top
   * three using `limit(3)` on the stream.
   * 
   * @param map
   * @param <K>
   * @param <V>
   * @return
   */
  public static <K, V extends Comparable<? super V>> Stream<Map.Entry<K, V>> sortByValueInReverseOrder(
      Map<K, V> map) {
    return map.entrySet()
        .stream()
        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
  }

}
