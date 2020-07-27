package com.foxjunior.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class EnumLookupBuilder {
    public enum SearchType {
        Name,
        UpperCase,
        LowerCase,
        CaseInsensitive,
        UpperCaseWithName,
        LowerCaseWithName
    }

    private static <T extends Enum<?>> void checkAndAdd(Map<String, T> result, Object key, T value) {
        Objects.requireNonNull(key, "The key must not be null for " + value + "!");
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("The key must be a String " + key.getClass() + " found instead!");
        }
        if (result.containsKey(key.toString())) {
            throw new IllegalArgumentException("The enum key " + key + " is already defined for " + result.get(key) + " found new value " + value + "!");
        }
        result.put(key.toString(), value);
    }

    private static <T extends Enum<?>> Map<String, T> buildMap(Class<T> enumType, Function<T, Object> lookup) {
        Objects.requireNonNull(enumType, "Enum type must be set!");
        Objects.requireNonNull(lookup, "Lookup must not be null!");
        final Map<String, T> result = new HashMap<>();
        for (T v : enumType.getEnumConstants()) {
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
                for (Object k : (Iterable)keys) {
                    checkAndAdd(result, k, v);
                }
                continue;
            }
            throw new IllegalArgumentException("The key type " + keys.getClass().getName() + " is not supported!");
        }
        return Collections.unmodifiableMap(result);
    }

    public static <T extends Enum<?>> Function<String, Optional<T>> build(Class<T> enumType, Function<T, Object> lookup) {
        final Map<String, T> result = buildMap(enumType, lookup != null ? lookup : Enum::name);
        return s -> s == null ? Optional.empty() : Optional.ofNullable(result.get(s));
    }

    public static <T extends Enum<?>> Function<String, Optional<T>> build(Class<T> enumType, SearchType buildType) {
        if (buildType == null || buildType == SearchType.Name) {
            return build(enumType, Enum::name);
        }
        switch (buildType) {
            case Name:
                return build(enumType, Enum::name);
            case CaseInsensitive:
                return build(enumType, e -> new HashSet<>(Arrays.asList(e.name(), e.name().toLowerCase(), e.name().toUpperCase())));
            case LowerCase:
                return build(enumType, e -> e.name().toLowerCase());
            case LowerCaseWithName:
                return build(enumType, e -> new HashSet<>(Arrays.asList(e.name(), e.name().toLowerCase())));
            case UpperCase:
                return build(enumType, e -> e.name().toUpperCase());
            case UpperCaseWithName:
                return build(enumType, e -> new HashSet<>(Arrays.asList(e.name(), e.name().toUpperCase())));
        }
        throw new UnsupportedOperationException("Build type " + buildType + " is not implemented!");
    }

    public static <T extends Enum<?>>  Function<String, Optional<T>> buildCaseInsensitive(Class<T> enumType) {
        return build(enumType, SearchType.CaseInsensitive);
    }

    public static <T extends Enum<?>>  Function<String, Optional<T>> build(Class<T> enumType) {
        return build(enumType, SearchType.Name);
    }
}
