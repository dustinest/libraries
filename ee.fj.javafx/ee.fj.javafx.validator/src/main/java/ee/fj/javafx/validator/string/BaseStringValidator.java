package ee.fj.javafx.validator.string;

import ee.fj.javafx.validator.SimpleValidProperty;
import ee.fj.javafx.validator.ValueValidator;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;

public abstract class BaseStringValidator implements ValueValidator<String> {
	private final SimpleValidProperty validProperty;
	private final StringProperty property;
	private final BooleanProperty allowNullProperty;
	private final BooleanProperty allowEmptyProperty;

	public BaseStringValidator(Object bean, String beanName, String defaultValue, boolean allowNull, boolean allowEmpty) {
		this.allowNullProperty = new SimpleBooleanProperty(allowNull);
		this.allowEmptyProperty = new SimpleBooleanProperty(allowEmpty);
		this.property = new SimpleStringProperty(defaultValue);
		this.validProperty = new SimpleValidProperty(bean, beanName, false);
		this.property.addListener((ob, oldValue, newValue) -> reValidate(newValue, allowNullProperty.get(), allowEmptyProperty.get()));
		this.allowNullProperty.addListener((ob, oldValue, newValue) -> reValidate(property.get(), newValue, allowEmptyProperty.get()));
		this.allowEmptyProperty.addListener((ob, oldValue, newValue) -> reValidate(property.get(), allowNullProperty.get(), newValue));
		Platform.runLater(this::reValidate);
	}

	/**
	 * Set true if null is also valid
	 */
	public void setAllowNull(boolean isAllowNull) {
		this.allowNullProperty.set(isAllowNull);
	}

	/**
	 * Set true if empty string is also valid value
	 */
	public void setAllowEmpty(boolean isallowEmpty) {
		this.allowEmptyProperty.set(isallowEmpty);
	}

	public boolean isAllowNull() {
		return this.allowNullProperty.get();
	}

	public boolean isAllowEmpty() {
		return this.allowEmptyProperty.get();
	}
	
	public final void reValidate() {
		reValidate(property.get(), allowNullProperty.get(), allowEmptyProperty.get());
	}

	public final void reValidate(boolean parentValidator) {
		validProperty.setValid(parentValidator == isValid());
	}

	private void reValidate(String value, boolean allowNull, boolean allowEmpty) {
		validProperty.setValid(isValid(value, allowNull, allowEmpty));
	}
	
	private boolean isValid(String value, boolean allowNull, boolean allowEmpty) {
		if (value == null) {
			return allowNull;
		}
		if (value.length() == 0) {
			return allowEmpty;
		}
		return isValid(value);
	}
	
	protected abstract boolean isValid(String value);
	
	@Override
	public String getValue() {
		return this.property.getValue();
	}

	@Override
	public void setValue(String value) {
		this.property.setValue(value);
	}

	@Override
	public void addValueListener(ChangeListener<String> listener) {
		this.property.addListener(listener);
	}

	@Override
	public boolean isValid() {
		return validProperty.isValid();
	}

	@Override
	public void addValidatableListener(ChangeListener<? super Boolean> listener) {
		validProperty.addValidatableListener(listener);
	}

	@Override
	public String format(String value) throws IllegalArgumentException {
		return value;
	}

	@Override
	public String parse(String value) {
		return value;
	}

}
