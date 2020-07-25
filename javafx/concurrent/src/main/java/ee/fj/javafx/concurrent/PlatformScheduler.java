package ee.fj.javafx.concurrent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;

public class PlatformScheduler {
	private final TimeUnit timeunit;
	private final ScheduledExecutorService executor;

	public PlatformScheduler(ScheduledExecutorService executor, TimeUnit timeunit) {
		this.timeunit = timeunit;
		this.executor = executor;
	}

	/**
	 * @see ScheduledExecutorService#schedule(Runnable, long, TimeUnit)
	 */
	public ScheduledFuture<?> schedule(Runnable command, long delay) {
		return executor.schedule(() -> Platform.runLater(command), delay, timeunit);
	}

	/**
	 * @see ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period) {
		return executor.scheduleAtFixedRate(() -> Platform.runLater(command), initialDelay, period, timeunit);
	}

	/**
	 * @see ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long period) {
		return scheduleAtFixedRate(command, period, period);
	}

	/**
	 * @see ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay) {
		return executor.scheduleWithFixedDelay(() -> Platform.runLater(command), initialDelay, delay, timeunit);
	}

	/**
	 * @see ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long delay) {
		return scheduleWithFixedDelay(command, delay, delay);
	}

}
