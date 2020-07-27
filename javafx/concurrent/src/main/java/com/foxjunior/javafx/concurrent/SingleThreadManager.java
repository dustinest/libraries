package com.foxjunior.javafx.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SingleThreadManager {
	private static final ScheduledExecutorService _EXECUTOR;
	public static final Scheduler DAYS;
	public static final Scheduler HOURS;
	public static final Scheduler MINUTES;
	public static final Scheduler SECONDS;
	public static final Scheduler MILLISECONDS;
	public static final Scheduler MICROSECONDS;
	public static final Scheduler NANOSECONDS;

	public static final Executor EXECUTOR;
	static {
		_EXECUTOR = Executors.newSingleThreadScheduledExecutor();
		NANOSECONDS = new Scheduler(_EXECUTOR, TimeUnit.NANOSECONDS);
		MICROSECONDS = new Scheduler(_EXECUTOR, TimeUnit.MICROSECONDS);
		MILLISECONDS = new Scheduler(_EXECUTOR, TimeUnit.MILLISECONDS);
		MINUTES = new Scheduler(_EXECUTOR, TimeUnit.MINUTES);
		DAYS = new Scheduler(_EXECUTOR, TimeUnit.DAYS);
		HOURS = new Scheduler(_EXECUTOR, TimeUnit.HOURS);
		SECONDS = new Scheduler(_EXECUTOR, TimeUnit.SECONDS);

		EXECUTOR = new Executor(_EXECUTOR);
	}


	/**
	 * Tries to shut down the executor and waits for the termination
     * @param timeout the maximum time to wait
     * @param timeunit the time unit of the timeout argument
     * @return {@code true} if this executor terminated and
     *         {@code false} if the timeout elapsed before termination
     * @throws InterruptedException if interrupted while waiting
     * @see ExecutorService#shutdown()
     * @see ExecutorService#awaitTermination(long, TimeUnit)
	 */
	public static boolean shutdownAndWait(long timeout, TimeUnit timeunit) throws InterruptedException {
		_EXECUTOR.shutdown();
		return _EXECUTOR.awaitTermination(timeout, timeunit);
	}

	/**
	 * Wait for seconds
	 * calls {@link #shutdownAndWait(long, TimeUnit)} with TimeUnit.SECONDS as second argument
	 */
	public static boolean shutdownAndWait(long timeout) throws InterruptedException {
		return shutdownAndWait(timeout, TimeUnit.SECONDS);
	}
}
