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
			if (result.get(key.toString()).equals(value)) {
				return;
			}
			throw new IllegalArgumentException("The class key " + key + " is already defined for " + result.get(key) + " found new value " + value + "!");
		}
		result.put(key.toString(), value);
	}

	private static <T> void addObject(final Map<String, T> result, Object key, T value) {
		if (value == null) return;
		if (key instanceof Enum) {
			addObject(result, ((Enum<?>) key).name(), value);
			return;
		}
		if (key instanceof String) {
			checkAndAdd(result, key, value);
			return;
		}
		if (key instanceof String[]) {
			for (String k : (String[])key) {
				addObject(result, k, value);
			}
			return;
		}
		if (key instanceof Iterable) {
			//noinspection rawtypes
			for (Object k : (Iterable)key) {
				addObject(result, k, value);
			}
			return;
		}
		throw new IllegalArgumentException("The key type " + value.getClass().getName() + " is not supported!");
	}
	@SafeVarargs
	private static <T> Map<String, T> getArray(Function<T, Object> lookup, T... values) {
		Objects.requireNonNull(lookup, "Lookup must not be null!");
		final Map<String, T> result = new HashMap<>();
		for (T v : values) {
			Object keys = lookup.apply(v);
			addObject(result, keys, v);
		}
		return Collections.unmodifiableMap(result);
	}

	@SafeVarargs
	public static <T> Function<String, Optional<T>> build(Function<T, Object> lookup, T... values) {
		Objects.requireNonNull(lookup, "Lookup must not be null!");
		final Map<String, T> result = getArray(lookup, values);
		return s -> s == null ? Optional.empty() : Optional.ofNullable(result.get(s));
	}
}
