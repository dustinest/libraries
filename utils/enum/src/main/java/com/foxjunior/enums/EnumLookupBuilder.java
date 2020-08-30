package com.foxjunior.enums;

import java.util.Arrays;
import java.util.HashSet;
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

    public static <T extends Enum<?>> Function<String, Optional<T>> build(Class<T> enumType, Function<T, Object> lookup) {
        return ObjectLookupBuilder.build(enumType, lookup, enumType.getEnumConstants());
    }

    public static <T extends Enum<?>> Function<String, Optional<T>> build(Class<T> enumType, SearchType buildType) {
        if (buildType == null) {
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
