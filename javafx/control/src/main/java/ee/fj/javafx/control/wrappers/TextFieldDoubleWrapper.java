package ee.fj.javafx.control.wrappers;

import ee.fj.javafx.validator.ValueValidator;
import javafx.scene.control.TextInputControl;

public abstract class TextFieldDoubleWrapper extends TextFieldNumberWrapper<Double> {

	public TextFieldDoubleWrapper(String propertyName, TextInputControl field, boolean allowEmpty, ValueValidator<Double> validator) {
		super(propertyName, field, allowEmpty, validator);
	}

	@Override
	protected Double cast(String value) {
		return Double.parseDouble(value);
	}
}
