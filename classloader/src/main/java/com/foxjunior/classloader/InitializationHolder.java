package com.foxjunior.classloader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class InitializationHolder {
	private final Object o;
	private final Method[] methods;

	InitializationHolder(Object o, Method[] methods) {
		this.o = o;
		this.methods = methods;
	}

	void runMethods() throws IOException {
		if (methods == null)
			return;
		for (Method m : methods) {
			try {
				if (Modifier.isStatic(m.getModifiers())) {
					m.invoke(null);
				} else {
					m.invoke(o);
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new IOException(e);
			}
		}
	}
}
