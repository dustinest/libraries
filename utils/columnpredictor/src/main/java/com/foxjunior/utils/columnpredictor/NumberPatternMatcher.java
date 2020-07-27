package com.foxjunior.utils.columnpredictor;

import java.util.Objects;
import java.util.regex.Pattern;

public class NumberPatternMatcher extends ColumnPatternMatcher {
	public static final String DEFAULT_NAME = "number";
	private static final Pattern PATTERN = Pattern.compile("^[-]?\\d+([.,]\\d+)?$");
	public static final NumberPatternMatcher INSTANCE = new NumberPatternMatcher(DEFAULT_NAME);

	public NumberPatternMatcher(String id) {
		super(id, PATTERN);
	}

	public static double getAsDouble(Object value) {
		Objects.requireNonNull(value, "The argument must be not null!");
		if (!INSTANCE.matches(value)) {
			throw new IllegalArgumentException( value .toString() + " is not long!");
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

	public static long getAsLong(Object value) {
		Double rv = getAsDouble(value);
		if (rv < Long.MIN_VALUE || rv > Long.MAX_VALUE) {
			throw new IllegalArgumentException(rv + " is out of long!");
		}
		return rv.longValue();
	}

	public static int getAsInteger(Object value) {
		Double rv = getAsDouble(value);
		if (rv < Integer.MIN_VALUE || rv > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(rv + " is out of integer!");
		}
		return rv.intValue();
	}
}
