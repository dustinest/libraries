package com.foxjunior.javafx.validator.number;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Optional;

public class NumberFormatter {
	public static <T extends Number> Optional<String> format(T value) throws IllegalArgumentException {
		if (value == null) {
			return Optional.empty();
		}
		return Optional.of(DecimalFormat.getInstance().format(value));
	}

	public static Optional<Number> parse(String value) throws ParseException {
		if (value == null || value.trim().length() == 0) {
			return Optional.empty();
		}
		return Optional.of(DecimalFormat.getInstance().parse(value));
	}
}
