package mbarix4j.concurrent;

import java.util.concurrent.Executor;

/**
 * Executor that executes immediately (synchronously)
 * @author Brian Schlining
 * @since 2013-09-26
 */
public class DirectExecutor implements Executor {
    public void execute(Runnable command) {
        command.run();
    }
}
