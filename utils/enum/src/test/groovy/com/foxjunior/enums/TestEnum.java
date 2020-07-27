package com.foxjunior.enums;

import java.util.Optional;
import java.util.function.Function;

public enum TestEnum {
    VALUE1("VALUE_1", 1, "CODE1"),
    VALUE2("VALUE_2", 2, "CODE2"),
    VALUE3("VALUE_3", 3, "CODE3");

    final int valueInt;
    final String valueString1;
    final String valueString2;
    TestEnum(String valueString1, int valueInt, String valueString2) {
        this.valueInt = valueInt;
        this.valueString1 = valueString1;
        this.valueString2 = valueString2;
    }
    private static final Function<String, Optional<TestEnum>> ADVANCED_LOOKUP = EnumLookupBuilder.build(
            TestEnum.class,
            testEnum -> new String[]{
                    testEnum.valueString1,
                    testEnum.valueString2,
                    testEnum.valueInt + "",
                    testEnum.name()
            });
    static Optional<TestEnum> advancedLookup(String key) {
        return ADVANCED_LOOKUP.apply(key);
    }

    static final Function<String, Optional<TestEnum>> BASIC_LOOKUP = EnumLookupBuilder.build(TestEnum.class);
    static Optional<TestEnum> basicLookup(String key) {
        return BASIC_LOOKUP.apply(key);
    }
}
