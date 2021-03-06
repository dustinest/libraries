package com.foxjunior.javafx.control.wrappers;

import com.foxjunior.javafx.validator.ValueValidator;
import javafx.scene.control.TextInputControl;

public abstract class TextFieldLongWrapper extends TextFieldNumberWrapper<Long> {

	public TextFieldLongWrapper(String propertyName, TextInputControl field, boolean allowEmpty, ValueValidator<Long> validator) {
		super(propertyName, field, allowEmpty, validator);
	}

	@Override
	protected Long cast(String value) {
		return Long.parseLong(value);
	}
}
