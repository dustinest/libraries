package com.foxjunior.javafx.validator.number;

import java.text.ParseException;

import com.foxjunior.javafx.validator.SimpleValidProperty;
import com.foxjunior.javafx.validator.ValueValidator;
import com.foxjunior.javafx.validator.property.DoubleRangeProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

public class DoubleRangeValidator extends DoubleRangeProperty implements ValueValidator<Double> {
	private final SimpleValidProperty validProperty;
	private final DoubleProperty value;

	public DoubleRangeValidator(Object bean, String beanName,
			double lowerValue, double upperValue, double defaultValue) {
		super(bean, beanName, lowerValue, upperValue);
		value = new SimpleDoubleProperty(bean, beanName, defaultValue);
		validProperty = new SimpleValidProperty(bean, beanName + "Range", isBetween(defaultValue));
		value.addListener((ob, oldValue, newValue) -> validProperty.setValid(newValue != null && isBetween(newValue.doubleValue())) );
	}

	@Override
	public void rangeChanged(Double lowerValue, Double upperValue) {
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
	public Double getValue() {
		return value.getValue();
	}

	@Override
	public void setValue(Double value) {
		this.value.setValue(value);
	}

	@Override
	public void addValueListener(ChangeListener<Double> listener) {
		this.value.addListener((ob, oldValue, newValue) -> listener.changed(null, oldValue.doubleValue(), newValue.doubleValue()) );
	}

	@Override
	public String format(Double value) {
		return NumberFormatter.format(value).orElse(null);
	}

	@Override
	public Double parse(String value) throws ParseException {
		return NumberFormatter.parse(value).map(Number::doubleValue).orElse(null);
	}
}
