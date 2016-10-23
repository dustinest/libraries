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
	 * @see {@link ScheduledExecutorService#schedule(Runnable, long, TimeUnit)}
	 */
	public ScheduledFuture<?> schedule(Runnable command, long delay) {
		return executor.schedule(() -> Platform.runLater(() -> command.run()), delay, timeunit);
	}

	/**
	 * @see {@link ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)}
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period) {
		return executor.scheduleAtFixedRate(() -> Platform.runLater(() -> command.run()), initialDelay, period, timeunit);
	}

	/**
	 * @see {@link ScheduledExecutorService#scheduleAtFixedRate(Runnable, long, long, TimeUnit)}
	 */
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long period) {
		return scheduleAtFixedRate(command, period, period);
	}

	/**
	 * @see {@link ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay) {
		return executor.scheduleWithFixedDelay(() -> Platform.runLater(() -> command.run()), initialDelay, delay, timeunit);
	}

	/**
	 * @see {@link ScheduledExecutorService#scheduleWithFixedDelay(Runnable, long, long, TimeUnit)}
	 */
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long delay) {
		return scheduleWithFixedDelay(command, delay, delay);
	}

}
