package com.foxjunior.arrays.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ArrayUtils {
	public static String[] removeAllFromArray(String[] array, String value) {
		if (array == null || array.length == 0) return new String[0];
		return Stream.of(array).filter(item -> !Objects.equals(item, value)).toArray(String[]::new);
	}

	public static <T> Set<T> getNullSafeSetFromCollection(Collection<T> other) {
		if (other == null || other.size() == 0) return Collections.emptySet();
		return new HashSet<>(other);
	}

	public static <T> void withNullSafeSetFromCollection(Collection<T> other, Consumer<Set<T>> callable) {
		callable.accept(getNullSafeSetFromCollection(other));
	}

	public static <T> Set<T> getNullSafeSetFromArray(T[] other) {
		if (other == null || other.length == 0) return Collections.emptySet();
		return new HashSet<>(Arrays.asList(other));
	}

	public static <T> void withNullSafeCopyOfArrayToSet(T[] other, Consumer<Set<T>> callable) {
		callable.accept(getNullSafeSetFromArray(other));
	}
}
