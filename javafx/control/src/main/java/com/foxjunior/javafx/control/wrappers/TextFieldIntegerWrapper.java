package com.foxjunior.javafx.control.wrappers;

import com.foxjunior.javafx.validator.ValueValidator;
import javafx.scene.control.TextInputControl;

public abstract class TextFieldIntegerWrapper extends TextFieldNumberWrapper<Integer> {

	public TextFieldIntegerWrapper(String propertyName, TextInputControl field, boolean allowEmpty, ValueValidator<Integer> validator) {
		super(propertyName, field, allowEmpty, validator);
	}

	@Override
	protected Integer cast(String value) {
		return Integer.parseInt(value);
	}
}
