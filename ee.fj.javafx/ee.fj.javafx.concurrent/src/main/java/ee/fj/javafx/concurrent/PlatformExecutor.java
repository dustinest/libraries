package ee.fj.javafx.concurrent;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import javafx.application.Platform;

public class PlatformExecutor {
	private final ScheduledExecutorService executor;
	public PlatformExecutor(ScheduledExecutorService executor) {
		this.executor = executor;
	}

	/**
	 * @see ScheduledExecutorService#submit(Runnable)
	 */
	public Future<?> submit(Runnable task) {
		return executor.submit(() -> Platform.runLater(task));
	}

	/**
	 * @see ScheduledExecutorService#execute(Runnable)
	 */
	public void execute(Runnable command) {
		executor.execute(() -> Platform.runLater(command));
	}
}
