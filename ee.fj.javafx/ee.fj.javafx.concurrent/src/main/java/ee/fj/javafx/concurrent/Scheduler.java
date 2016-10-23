package ee.fj.javafx.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Scheduler {
	private final TimeUnit timeunit;
	private final ScheduledExecutorService executor;

	public final PlatformScheduler PLATFORM;
	
	public Scheduler(ScheduledExecutorService executor, TimeUnit timeunit) {
		this.timeunit = timeunit;
		this.executor = executor;
		PLATFORM = new PlatformScheduler(executor, timeunit);
	}

	/**
	 * @see {@link ScheduledExecutorService#invokeAll(Collection, long, TimeUnit)}
	 */
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout)
			throws InterruptedException {
		return executor.invokeAll(tasks, timeout, timeunit);
	}

	/**
	 * @see {@link ScheduledExecutorService#invokeAny(Collection, long, TimeUnit)}
	 */
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout)
			throws InterruptedException, ExecutionException, TimeoutException {
		return executor.invokeAny(tasks, timeout, timeunit);
	}

	/**
	 * @see {@link ScheduledExecutorService#schedule(Runnable, long, TimeUnit)}
	 */
	public ScheduledFuture<?> schedule(Runnable command, long delay) {
		return executor.schedule(command, delay, timeunit);
	}

	/**
	 * @see {@link ScheduledExecutorService#schedule(Callable, long, long, TimeUnit)}
	 */
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay) {
		return executor.schedule(callable, delay, timeunit);
	}

	/**
	 * @see {@link ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)}
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period) {
		return executor.scheduleAtFixedRate(command, initialDelay, period, timeunit);
	}

	/**
	 * @see {@link ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)}
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long period) {
		return executor.scheduleAtFixedRate(command, period, period, timeunit);
	}

	/**
	 * @see {@link ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay) {
		return executor.scheduleWithFixedDelay(command, initialDelay, delay, timeunit);
	}

	/**
	 * @see {@link ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long delay) {
		return executor.scheduleWithFixedDelay(command, delay, delay, timeunit);
	}
}
