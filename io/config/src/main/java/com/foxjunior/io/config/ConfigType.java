package com.foxjunior.io.config;

public interface ConfigType<T> {
	T getValue(byte[] data);
	boolean is(Class<?> clazz);
	<S> ConfigType<S> as(Class<S> clazz);
}
