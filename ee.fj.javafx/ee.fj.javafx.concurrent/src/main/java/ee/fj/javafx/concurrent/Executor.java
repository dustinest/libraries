package ee.fj.javafx.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Executor {
	private final ScheduledExecutorService executor;

	public final PlatformExecutor PLATFORM;
	
	public Executor(ScheduledExecutorService executor) {
		this.executor = executor;
		PLATFORM = new PlatformExecutor(executor);
	}

	/**
	 * @see {@link ScheduledExecutorService#submit(Callable)}
	 */
	public <T> Future<T> submit(Callable<T> task) {
		return executor.submit(task);
	}

	/**
	 * @see {@link ScheduledExecutorService#submit(Runnable, T)}
	 */
	public <T> Future<T> submit(Runnable task, T result) {
		return executor.submit(task, result);
	}

	/**
	 * @see {@link ScheduledExecutorService#submit(Runnable)}
	 */
	public Future<?> submit(Runnable task) {
		return executor.submit(task);
	}

	/**
	 * @see {@link ScheduledExecutorService#invokeAll(Collection)}
	 */
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return executor.invokeAll(tasks);
	}

	/**
	 * @see {@link ScheduledExecutorService#invokeAny(Collection)}
	 */
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return executor.invokeAny(tasks);
	}

	/**
	 * @see {@link ScheduledExecutorService#execute(Runnable)}
	 */
	public void execute(Runnable command) {
		executor.execute(command);
	}
}
