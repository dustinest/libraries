package com.foxjunior.utils.columnpredictor;

import java.util.Objects;
import java.util.regex.Pattern;

public class DecimalPatternMatcher extends ColumnPatternMatcher {
	public static final String DEFAULT_NAME = "decimal";
	private static final Pattern PATTERN = Pattern.compile("^[-]?\\d+([.,]\\d+)$");
	public static final DecimalPatternMatcher INSTANCE = new DecimalPatternMatcher(DEFAULT_NAME);

	public DecimalPatternMatcher(String id) {
		super(id, PATTERN);
	}

	public static double getAsDouble(Object value) {
		Objects.requireNonNull(value, "The argument must be not null!");
		if (!INSTANCE.matches(value)) {
			throw new IllegalArgumentException( value + " is not double!");
		}
		return Double.parseDouble(value.toString().replaceAll(",", "."));
	}

	public static float getAsFloat(Object value) {
		Double rv = getAsDouble(value);
		if (rv < Float.MIN_VALUE || rv > Float.MAX_VALUE) {
			throw new IllegalArgumentException(rv + " is out of float!");
		}
		return rv.floatValue();
	}
}
