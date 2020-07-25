package ee.fj.classloader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

class ClassLoaderImpl {
	private final Logger LOGGER = Logger.getLogger(ClassLoaderImpl.class.getName());

	private final ClassLoader classLoader;
	private final List<String> paths = new ArrayList<>();

	ClassLoaderImpl(java.lang.ClassLoader classLoader, String... paths) {
		this.classLoader = classLoader;
		addPaths(paths);
	}

	void addPaths(String... paths) {
		for (String p : paths) {
			if (!this.paths.contains(p)) {
				this.paths.add(p);
			}
		}
	}

	boolean is(Object other) {
		if (!(other instanceof ClassLoaderImpl)) {
			return false;
		}
		ClassLoaderImpl _other = (ClassLoaderImpl) other;
		return _other.classLoader.equals(classLoader);
	}

	void walk(Consumer<Class<?>> consumer) throws IOException {
		Enumeration<URL> classloaderUrls = classLoader.getResources("");
		while(classloaderUrls.hasMoreElements()) {
			URL url = classloaderUrls.nextElement();
			try {
				System.out.println(url);
				Path path = Paths.get(url.toURI());
				if (Files.isDirectory(path)) {
					walkPath(consumer, path);
				} else if (path.toString().toLowerCase().endsWith(".jar")) {
					walkJar(consumer, path);
				}
			} catch (FileSystemNotFoundException e) {
				LOGGER.log(Level.WARNING, "File system was not found for " + url, e);
			} catch (URISyntaxException e1) {
				throw new IOException(e1);
			}
		}
	}

	private void walkPath(Consumer<Class<?>> consumer, Path path) throws IOException {
		Files.walkFileTree(path, new SimpleFileVisitor<>() {
			private final List<String> paths = new ArrayList<>();

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				paths.add(dir.getFileName().toString());
				return super.preVisitDirectory(dir, attrs);
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				paths.remove(paths.size() - 1);
				return super.postVisitDirectory(dir, exc);
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attr) throws IOException {
				if (file.getFileName().toString().endsWith(".class")) {
					StringBuilder path = new StringBuilder();
					for (int i = 1; i < paths.size(); i++) {
						path.append(paths.get(i));
						path.append('.');
					}
					path.append(file.getFileName().toString());
					try {
						consumeClassIfValid(consumer, path.toString());
					} catch (ClassNotFoundException e) {
						throw new IOException(e);
					}
				}
				return super.visitFile(file, attr);
			}
		});
	}

	private void walkJar(Consumer<Class<?>> consumer, Path path) throws IOException {
		try (JarInputStream in = new JarInputStream(Files.newInputStream(path))) {
			for (JarEntry e = in.getNextJarEntry(); e != null; e = in.getNextJarEntry()) {
				if (e.isDirectory())
					continue;
				try {
					consumeClassIfValid(consumer, e.getName());
				} catch (NoClassDefFoundError | ClassNotFoundException ex) {
					LOGGER.log(Level.INFO, "Seems like " + path + " is not set in classpath and " + e.getName()
							+ " could not be loaded. That's cool! No need to!", ex);
				}
			}
		}
	}

	@SuppressWarnings("UnusedReturnValue")
	private boolean consumeClassIfValid(Consumer<Class<?>> consumer, String classpath) throws ClassNotFoundException {
		if (!classpath.endsWith(".class")) {
			return false;
		}
		classpath = classpath.substring(0, classpath.length() - ".class".length());
		classpath = classpath.replace('/', '.');
		Class<?> c = classLoader.loadClass(classpath);
		if (c.isInterface()) {
			return false;
		}
		consumer.accept(c);
		return true;
	}
}
