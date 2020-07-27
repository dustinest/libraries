package com.foxjunior.javafx.validator.number;

import java.text.ParseException;

import com.foxjunior.javafx.validator.SimpleValidProperty;
import com.foxjunior.javafx.validator.ValueValidator;
import com.foxjunior.javafx.validator.property.FloatRangeProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;

public class FloatRangeValidator extends FloatRangeProperty implements ValueValidator<Float> {
	private final SimpleValidProperty validProperty;
	private final FloatProperty value;

	public FloatRangeValidator(Object bean, String beanName,
			float lowerValue, float upperValue, float defaultValue) {
		super(bean, beanName, lowerValue, upperValue);
		value = new SimpleFloatProperty(bean, beanName, defaultValue);
		validProperty = new SimpleValidProperty(bean, beanName + "Range", isBetween(defaultValue));
		value.addListener((ob, oldValue, newValue) -> validProperty.setValid(newValue != null && isBetween(newValue.floatValue())) );
	}

	@Override
	public void rangeChanged(Float lowerValue, Float upperValue) {
		validProperty.setValid(isBetween(value.get(), lowerValue, upperValue));
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
	public Float getValue() {
		return value.getValue();
	}

	@Override
	public void setValue(Float value) {
		this.value.setValue(value);
	}

	@Override
	public void addValueListener(ChangeListener<Float> listener) {
		this.value.addListener((ob, oldValue, newValue) -> listener.changed(null, oldValue.floatValue(), newValue.floatValue()) );
	}

	@Override
	public String format(Float value) {
		return NumberFormatter.format(value).orElse(null);
	}

	@Override
	public Float parse(String value) throws ParseException {
		return NumberFormatter.parse(value).map(Number::floatValue).orElse(null);
	}
}
