package mbarix4j3.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Functional utils.
 * @author Brian Schlining
 * @since 2017-09-26T15:30:00
 */
public class FnUtils {

    /**
     * <pre>
     * 	public class Person {
     * 	    private int age;
     * 	    private String name;
     * 	    private String email;
     * 	    // standard getters and setters
     *  }
     *
     * List<Person> personListFiltered = personList.stream()
     * 	  .filter(distinctByKey(p -> p.getName()))
     * 	  .collect(Collectors.toList());
     * </pre>
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctBy(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
