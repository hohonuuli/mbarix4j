package org.mbari.concurrent;

import java.util.concurrent.Executor;

/**
 * This executor spawns a new thread for each task.
 *
 * @author Brian Schlining
 * @since 2013-09-26
 */
public class ThreadPerTaskExecutor implements Executor {
    public void execute(Runnable command) {
        new Thread(command).start();
    }
}
