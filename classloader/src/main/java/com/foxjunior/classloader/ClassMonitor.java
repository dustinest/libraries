package com.foxjunior.classloader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.foxjunior.classloader.annotation.Inject;
import com.foxjunior.classloader.annotation.OnLoad;

class ClassMonitor {
	private final List<InitializationHolder> onLoad = new ArrayList<>();

	private void parseMethods(Class<?> clazz) {
		final ArrayList<Method> onLoadResut = new ArrayList<>();
		final ArrayList<Method> injectResult = new ArrayList<>();
		Object instance = null;
		for (Method method : clazz.getDeclaredMethods()) {
			final OnLoad onLoadAnnotation = method.getAnnotation(OnLoad.class);
			final Inject injectAnnotation = method.getAnnotation(Inject.class);
			if (onLoadAnnotation == null && injectAnnotation == null) continue;
			if (Modifier.isStatic(method.getModifiers())) {
				if (!method.canAccess(null))
					method.setAccessible(true);
			} else {
				try {
					instance = clazz.getDeclaredConstructor().newInstance();
					if (!method.canAccess(instance))
						method.setAccessible(true);
				} catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
								 IllegalAccessException e) {
					throw new IllegalArgumentException(e.getMessage(), e);
				}
			}
			if (onLoadAnnotation != null) onLoadResut.add(method);
			// TODO: do implement injection
			if (injectAnnotation != null) injectResult.add(method);
		}
		if (onLoadResut.size() > 0) {
			this.onLoad.add(new InitializationHolder(instance, onLoadResut.toArray(new Method[0])));
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
