package com.foxjunior.classloader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassLoaderFactory {
	private static final List<ClassLoaderImpl> classLoaders = new ArrayList<>();

	public static boolean addClassLoader(ClassLoader classloader, String... paths) {
		ClassLoaderImpl add = new ClassLoaderImpl(classloader);
		synchronized (classLoaders) {
			for (ClassLoaderImpl c : classLoaders) {
				if (c.is(add)) {
					c.addPaths(paths);
					return false;
				}
			}
			classLoaders.add(add);
			return true;
		}
	}

	public static boolean addCurrentClassLoader(String... paths) {
		return addClassLoader(Thread.currentThread().getContextClassLoader(), paths);
	}

	public static void start() throws IOException {
		synchronized (classLoaders) {
			// If there is no classloaders we just add current one
			if (classLoaders.size() == 0) {
				classLoaders.add(new ClassLoaderImpl(Thread.currentThread().getContextClassLoader()));
			}
			Collection<ClassMonitor> monitors = new ArrayList<>();
			for (ClassLoaderImpl cl : classLoaders) {
				ClassMonitor monitor = new ClassMonitor();
				monitor.runClassLoader(cl);
				monitors.add(monitor);
			}
			for (ClassMonitor monitor : monitors) {
				monitor.run();
			}
		}
	}
}
