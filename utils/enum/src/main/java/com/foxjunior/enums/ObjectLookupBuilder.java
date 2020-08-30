package com.foxjunior.enums;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ObjectLookupBuilder {
	private static <T> void checkAndAdd(Map<String, T> result, Object key, T value) {
		Objects.requireNonNull(key, "The key must not be null for " + value + "!");
		if (!(key instanceof String)) {
			throw new IllegalArgumentException("The key must be a String " + key.getClass() + " found instead!");
		}
		if (result.containsKey(key.toString())) {
			throw new IllegalArgumentException("The class key " + key + " is already defined for " + result.get(key) + " found new value " + value + "!");
		}
		result.put(key.toString(), value);
	}

	@SafeVarargs
	private static <T> Map<String, T> buildMap(Class<T> classType, Function<T, Object> lookup, T... values) {
		Objects.requireNonNull(classType, "Class type must be set!");
		Objects.requireNonNull(lookup, "Lookup must not be null!");
		final Map<String, T> result = new HashMap<>();
		for (T v : values) {
			Object keys = lookup.apply(v);
			if (keys == null) continue;
			if (keys instanceof String) {
				checkAndAdd(result, keys, v);
				continue;
			} else if (keys instanceof String[]) {
				for (String k : (String[])keys) {
					checkAndAdd(result, k, v);
				}
				continue;
			} else if (keys instanceof Iterable) {
				//noinspection rawtypes
				for (Object k : (Iterable)keys) {
					checkAndAdd(result, k, v);
				}
				continue;
			}
			throw new IllegalArgumentException("The key type " + keys.getClass().getName() + " is not supported!");
		}
		return Collections.unmodifiableMap(result);
	}

	@SafeVarargs
	public static <T> Function<String, Optional<T>> build(Class<T> classType, Function<T, Object> lookup, T... values) {
		Objects.requireNonNull(lookup, "Lookup must not be null!");
		final Map<String, T> result = buildMap(classType, lookup, values);
		return s -> s == null ? Optional.empty() : Optional.ofNullable(result.get(s));
	}
}
