package com.foxjunior.javafx.validator;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;

public class SimpleValidProperty implements Validatable {
	private final BooleanProperty validProperty;

	public SimpleValidProperty(Object bean, String beanName, boolean isValid) {
		validProperty = new SimpleBooleanProperty(bean, beanName + "Valid", isValid);
	}

	public void setValid(boolean isValid) {
		validProperty.set(isValid);
	}

	@Override
	public boolean isValid() {
		return validProperty.get();
	}

	@Override
	public void addValidatableListener(ChangeListener<? super Boolean> listener) {
		validProperty.addListener(listener);
		listener.changed(validProperty, isValid(), isValid());
	}

}
