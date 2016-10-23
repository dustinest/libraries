# Single threaded scheduler to improve JAVAFX timed functions

Anlthrough most of processes can be done with the Timeline I wanted to have more control over my flow and make animations or timeouts where necessary. Also, in most cases I have no need of more than
One thread.

For that I created simple single threaded manager for easy access to execute processes in and out of platform.

Simple usage:

	SingleThreadManager.SECONDS.PLATFORM.schedule(() -> updateSomething(), 1);

This will be executed in one second. Same time, if you want to do something out of JavaFX scope

	SingleThreadManager.SECONDS.schedule(() -> updateSomething(), 1);

The timeouts available:

- SingleThreadManager.DAYS.schedule(() -> updateSomething(), 1);
- SingleThreadManager.DAYS.PLATFORM.schedule(() -> updateSomething(), 1);
- SingleThreadManager.HOURS.schedule(() -> updateSomething(), 1);
- SingleThreadManager.HOURS.PLATFORM.schedule(() -> updateSomething(), 1);
- SingleThreadManager.MINUTES.schedule(() -> updateSomething(), 1);
- SingleThreadManager.MINUTES.PLATFORM.schedule(() -> updateSomething(), 1);
- SingleThreadManager.SECONDS.schedule(() -> updateSomething(), 1);
- SingleThreadManager.SECONDS.PLATFORM.schedule(() -> updateSomething(), 1);
- SingleThreadManager.MILLISECONDS.schedule(() -> updateSomething(), 1);
- SingleThreadManager.MILLISECONDS.PLATFORM.schedule(() -> updateSomething(), 1);
- SingleThreadManager.MICROSECONDS.schedule(() -> updateSomething(), 1);
- SingleThreadManager.MICROSECONDS.PLATFORM.schedule(() -> updateSomething(), 1);
- SingleThreadManager.NANOSECONDS.schedule(() -> updateSomething(), 1);
- SingleThreadManager.NANOSECONDS.PLATFORM.schedule(() -> updateSomething(), 1);

Also simple executor is availbale:

- SingleThreadManager.EXECUTOR.submit(() -> updateSomething(), 1);
- SingleThreadManager.EXECUTOR.PLATFORM.submit(() -> updateSomething(), 1);
