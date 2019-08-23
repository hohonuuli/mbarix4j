package mbarix4j.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Brian Schlining
 * @since 2018-01-22T14:41:00
 */
public class ReflectUtilities {

    /**
     * Get the default no arg Constructor of a class. None if one isn't found.
     * @param clazz
     * @return
     */
    public static Optional<Constructor<?>> findDefaultConstructor(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.getParameterCount() == 0)
                .findFirst();
    }
}
