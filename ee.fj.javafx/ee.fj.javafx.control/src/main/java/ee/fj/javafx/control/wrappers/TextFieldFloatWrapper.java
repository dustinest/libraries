package ee.fj.javafx.control.wrappers;

import ee.fj.javafx.validator.ValueValidator;
import javafx.scene.control.TextInputControl;

public abstract class TextFieldFloatWrapper extends TextFieldNumberWrapper<Float> {

	public TextFieldFloatWrapper(String propertyName, TextInputControl field, boolean allowEmpty, ValueValidator<Float> validator) {
		super(propertyName, field, allowEmpty, validator);
	}

	@Override
	protected Float cast(String value) {
		return Float.parseFloat(value);
	}
}
