package com.foxjunior.enums.speed_comparison;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Attempt to speed up the search but failed
 */
public class ObjectLookupArrayBuilder {
	private static class IndexedArray<T> {
		protected final T value;
		protected final int hash;
		protected IndexedArray(int hash, T value) {
			this.hash = hash;
			this.value = value;
		}
	}
	private static <T> void checkAndAdd(final Map<String, IndexedArray<T>> result, final String key, final T value) {
		Objects.requireNonNull(key, "The key must not be null for " + value + "!");
		if (result.containsKey(key)) {
			if (result.get(key).value.equals(value)) {
				return;
			}
			throw new IllegalArgumentException("The class key " + key + " is already defined for " + result.get(key).value + " found new value " + value + "!");
		}
		result.put(key, new IndexedArray<>(key.hashCode(), value));
	}

	private static <T> void addObject(final Map<String, IndexedArray<T>> result, final Object key, final T value) {
		if (value == null) return;
		if (key instanceof String) {
			checkAndAdd(result, key.toString(), value);
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
	@SuppressWarnings("unchecked")
	private static <T> IndexedArray<T>[] getArray(Function<T, Object> lookup, T... values) {
		Objects.requireNonNull(lookup, "Lookup must not be null!");
		final Map<String, IndexedArray<T>> result = new HashMap<>();
		for (T v : values) {
			Object keys = lookup.apply(v);
			addObject(result, keys, v);
		}
		//noinspection unchecked
		return result.values().stream().sorted(Comparator.comparing(e -> e.hash)).toArray(IndexedArray[]::new);
	}

	@SafeVarargs
	public static <T> Function<String, Optional<T>> build(Function<T, Object> lookup,  T... values) {
		Objects.requireNonNull(lookup, "Lookup must not be null!");
		final IndexedArray<T>[] result = getArray(lookup, values);
		if (result.length == 0) return (s) -> Optional.empty();
		return (find) -> {
			final int hash = find.hashCode();
			int left = 0;
			int right = result.length;
			while(left < right) {
				if (result[left].hash == hash) {
					return Optional.of(result[left].value);
				}
				final int s = left + ((right - left) / 2);
				if (result[s].hash == hash) {
					return Optional.of(result[s].value);
				} else if (hash > s) {
					left = s + 1;
				} else if (hash < s) {
					right = s;
				} else {
					left++;
				}
			}
			return Optional.empty();
		};
	}
}
