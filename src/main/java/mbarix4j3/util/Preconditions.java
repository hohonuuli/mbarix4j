package mbarix4j3.util;

import java.util.function.Supplier;

public class Preconditions {

    public static void checkArgument(boolean ok) {
        checkArgument(ok, "Argument failed validation check");
    }

    public static final void checkArgument(boolean ok, String msg) {
        if (!ok) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void require(boolean b, Supplier<String> fn) {
        if (!b) {
            throw new IllegalArgumentException(fn.get());
        }
    }
}
