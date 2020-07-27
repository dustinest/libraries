package com.foxjunior.javafx.control.wrappers;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.foxjunior.javafx.validator.ValueValidator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextInputControl;

public abstract class TextFieldValueWrapper<T> {
	private static final Logger LOGGER = Logger.getLogger(TextFieldValueWrapper.class.getName());

	private final ValueValidator<T> validator;
	@SuppressWarnings("FieldCanBeLocal")
	private final BooleanProperty requiredProperty;

	public TextFieldValueWrapper(String propertyName, TextInputControl field, boolean allowEmpty, ValueValidator<T> validator) {
		this.validator = validator;
		valueChanged("", field.getText());
		requiredProperty = new SimpleBooleanProperty(field, propertyName + "Required");
		field.textProperty().addListener((ob, oldValue, newValue) -> valueChanged(oldValue, newValue) );
	}

	protected void valueChanged(String oldValue, String newValue) {
		T _value = null;
		try {
			_value = cast(newValue);
		} catch (NullPointerException e) {
			LOGGER.log(Level.FINE, "Value must be null", e);
			_value = null;
		} catch (IllegalArgumentException e) {
			LOGGER.log(Level.FINE, "Got exception while parsing new value", e);
			try {
				_value = cast(oldValue);
			} catch (NullPointerException | IllegalArgumentException e1) {
				LOGGER.log(Level.WARNING, "Was not able to cast the old value", e1);
			}
		}
		try {
			validator.setValue(_value);
		} catch (NullPointerException e) {
			LOGGER.log(Level.WARNING, "Error while changing the validator", e);
		}
	}

	/**
	 * Cast the value into appropriate form.
	 * @param value the value to cast. Might be null
	 * @return casted value
	 */
	protected abstract T cast(String value);

	/**
	 * Set value
	 */
	public abstract void setValue(T newValue);

}
