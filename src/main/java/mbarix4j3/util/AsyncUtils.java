package mbarix4j3.util;

import java.time.Duration;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Logger;

public class AsyncUtils {

    private static final Logger log = Logger.getLogger(AsyncUtils.class.getName());

    public static <T> Optional<T> await(CompletableFuture<T> f, Duration timeout) {
        Optional<T> r;
        try {
            r = Optional.ofNullable(f.get(timeout.toMillis(), TimeUnit.MILLISECONDS));
        }
        catch (Exception e) {
            log.info("An exception was thrown when waiting for a future to complete: "  + log.getClass().getName());
            r = Optional.empty();
        }
        return r;
    }

    /**
     * Apply a function that converts an item in a collection to a future. Return
     * a future that completes when all futures on the items complete.
     * @param items The items to process
     * @param fn The function to apply to each item
     * @param <T> The type of the items
     * @return A future that completes when all items futures have completed.
     */
    public static <T> CompletableFuture<Void> completeAll(Collection<T> items,
                                                          Function<T, CompletableFuture> fn) {
        CompletableFuture[] futures = items.stream()
                .map(fn)
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures);
    }

    /**
     * Apply a function that converts an item in a collection to a future that
     * returns a value when completed. This method will collect all the
     * results and return them as a collection when all futures are completed
     * @param items
     * @param fn
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> CompletableFuture<Collection<R>> collectAll(Collection<T> items,
                                                                     Function<T, CompletableFuture<R>> fn) {
        CopyOnWriteArrayList<R> returnValues = new CopyOnWriteArrayList<>();

        CompletableFuture[] futures = items.stream()
                .map(fn)
                .map(r -> r.thenAccept(returnValues::add))
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures)
                .thenApply(v -> returnValues);

    }
}
