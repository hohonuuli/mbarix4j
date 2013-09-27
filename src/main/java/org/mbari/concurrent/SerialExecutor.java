package org.mbari.concurrent;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

/**
 * The executor below serializes the submission of tasks to a second executor, illustrating a
 * composite executor.
 *
 * @author Brian Schlining
 * @since 2013-09-26
 */
public class SerialExecutor implements Executor {
    final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
    final Executor executor;
    Runnable active;

    SerialExecutor(Executor executor) {
        this.executor = executor;
    }

    public synchronized void execute(final Runnable r) {
        tasks.offer(new Runnable() {
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });
        if (active == null) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            executor.execute(active);
        }
    }
}
