package ee.fj.classloader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import ee.fj.classloader.annotation.OnLoad;

class ClassMonitor {
	private final List<InitializationHolder> onLoad = new ArrayList<>();

	private void parseMethods(Class<?> clazz) {
		ArrayList<Method> onLoad = new ArrayList<>();
		Object instance = null;
		for (Method m : clazz.getDeclaredMethods()) {
			OnLoad annotation = m.getAnnotation(OnLoad.class);
			if (annotation != null) {
				if (!m.isAccessible())
					m.setAccessible(true);
				onLoad.add(m);
				if (!Modifier.isStatic(m.getModifiers())) {
					try {
						instance = clazz.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new IllegalArgumentException(e.getMessage(), e);
					}
				}
			}
		}
		InitializationHolder holder = new InitializationHolder(instance, onLoad.toArray(new Method[0]));
		if (onLoad.size() > 0) {
			this.onLoad.add(holder);
		}
	}

	void runClassLoader(ClassLoaderImpl classLoader) throws IOException {
		classLoader.walk(this::parseMethods);
	}

	void run() throws IOException {
		for (InitializationHolder h : onLoad) {
			h.runMethods();
		}
	}
}
