package ee.fj.javafx.control;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ee.fj.javafx.validator.ValueValidator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextField;

public class ValidatableTextField<T> extends TextField implements ValueValidator<T> {
	private static final Logger LOGGER = Logger.getLogger(ValidatableTextField.class.getName());
	/**
	 * If this is required
	 */
	private final BooleanProperty isRequiredProperty;
	/**
	 * The value validator
	 */
	private final ValueValidator<T> valueValidatorProperty;

	/**
	 * If required is triggered
	 */
	private final BooleanProperty isRequired;

	/**
	 * If valid is triggered
	 */
	private final BooleanProperty isValid;

	private final StringProperty INVALID_CLASS = new SimpleStringProperty("invalid");
	private final StringProperty REQUIRED_CLASS = new SimpleStringProperty("required");

	public ValidatableTextField(ValueValidator<T> valueValidator) {
		this("", valueValidator);
	}

	public ValidatableTextField(String value, ValueValidator<T> valueValidator) {
		this(value, false, valueValidator);
	}

	public ValidatableTextField(boolean required, ValueValidator<T> valueValidator) {
		this("", required, valueValidator);
	}

	public ValidatableTextField(String text, boolean required, ValueValidator<T> valueValidator) {
		super(text);
		this.valueValidatorProperty = valueValidator;
		setValueValidatorValue(text);
		this.isRequiredProperty = new SimpleBooleanProperty(this, "required", required);
		this.isRequired = new SimpleBooleanProperty(this, "_isValueRequired", required && (text == null || text.trim().length() == 0));
		this.isValid = new SimpleBooleanProperty(this, "_isValueValid", !isRequired.get() && valueValidator.isValid());

		this.valueValidatorProperty.addValidatableListener((ob, oldValue, newValue) -> isValid.set(!isRequiredProperty.get() && valueValidator.isValid()));

		textProperty().addListener((observable, oldValue, newValue) -> setValueValidatorValue(newValue));
		setValueValidatorValue(text);
	}

	private void setValueValidatorValue(String text) {
		this.isRequired.set(isRequiredProperty.get() && (text == null || text.trim().length() == 0));
		try {
			T _parsed = valueValidatorProperty.parse(text);
			valueValidatorProperty.setValue(_parsed);
			this.isValid.setValue(valueValidatorProperty.isValid() && this.isRequired.get());
		} catch (ParseException e) {
			LOGGER.log(Level.WARNING, "Error while parsing " + text, e);
			valueValidatorProperty.setValue(null);
			this.isValid.setValue(false);
		}
		if (this.isRequired.get()) {
			removeClass(INVALID_CLASS.get());
			addClass(REQUIRED_CLASS.get());
		} else if (this.isValid.get()) {
			addClass(INVALID_CLASS.get());
			removeClass(REQUIRED_CLASS.get());
		} else {
			removeClass(REQUIRED_CLASS.get());
			removeClass(INVALID_CLASS.get());
		}
	}
	
	private void addClass(String className) { if (!getStyleClass().contains(className)) { getStyleClass().add(className); } }
	private void removeClass(String className) {
		getStyleClass().remove(className);
	}

	/**
	 * @return true if value is valid
	 */
	@Override
	public boolean isValid() {
		return isValid.get();
	}
	
	@Override
	public void addValidatableListener(ChangeListener<? super Boolean> listener) {
		isValid.addListener(listener);
	}

	@Override
	public T getValue() {
		return valueValidatorProperty.getValue();
	}

	@Override
	public void setValue(T value) {
		setText(valueValidatorProperty.format(value));
	}

	@Override
	public void addValueListener(ChangeListener<T> listener) {
		valueValidatorProperty.addValueListener(listener);
	}

	@Override
	public String format(T value) throws IllegalArgumentException {
		return valueValidatorProperty.format(value);
	}

	@Override
	public T parse(String value) throws ParseException {
		return valueValidatorProperty.parse(value);
	}
}
