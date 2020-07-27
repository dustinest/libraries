package com.foxjunior.javafx.control;

import java.util.regex.Pattern;

import com.foxjunior.javafx.validator.ValueValidator;

public class NumberTextField<T extends Number> extends ValidatableTextField<T> {
	Pattern REGEXP = Pattern.compile("[0-9]*.?[0-9]*");
	Pattern NEGATIVE_REGEXP = Pattern.compile("[-[0-9]]*.?[0-9]*");

	public NumberTextField(boolean required, ValueValidator<T> valueValidator) {
		super(required, valueValidator);
	}
	public NumberTextField(String text, boolean required, ValueValidator<T> valueValidator) {
		super(text, required, valueValidator);
	}
	public NumberTextField(String value, ValueValidator<T> valueValidator) {
		super(value, valueValidator);
	}
	public NumberTextField(ValueValidator<T> valueValidator) {
		super(valueValidator);
	}

}
