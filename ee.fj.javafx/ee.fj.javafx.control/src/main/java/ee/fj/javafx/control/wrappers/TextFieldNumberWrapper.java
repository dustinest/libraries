package ee.fj.javafx.control.wrappers;

import ee.fj.javafx.validator.ValueValidator;
import javafx.scene.control.TextInputControl;

public abstract class TextFieldNumberWrapper<T extends Number> extends TextFieldValueWrapper<T> {
	public TextFieldNumberWrapper(String propertyName, TextInputControl field, boolean allowEmpty, ValueValidator<T> validator) {
		super(propertyName, field, allowEmpty, validator);
	}
}
