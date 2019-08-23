package mbarix4j.util;

/**
 *   
 * @author Brian Schlining
 * @since 2011-11-01
 */
public class ExceptionUtilities {

    /**
     * Throw a checked Exception w/o having to declare it in your method signature.
     * (Without having to wrap it in {@link RuntimeException}
     * From:
     *   http://aragozin.blogspot.com/2011/10/java-how-to-throw-undeclared-checked.html
     *   http://www.eishay.com/2011/11/throw-undeclared-checked-exception-in.html
     *
     * Use as:
     * <pre>
     * try {
     *     doSomethingThatThrows.exception();
     * } catch (Exception e) {
     *     throw ExceptionUtilities.unchecked(e);
     * }
     * </pre>
     * @param e
     * @return
     */
    public static RuntimeException unchecked(Throwable e) {
        ExceptionUtilities.<RuntimeException>throwAny(e);
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwAny(Throwable e) throws E {
        throw (E) e;
    }
}
