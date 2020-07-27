package com.foxjunior.javafx.validator.property;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public abstract class FloatRangeProperty implements RangeProperty<Float>{
	private final FloatProperty lower;
	private final FloatProperty upper;

	public FloatRangeProperty(Object bean, String beanName,
			float lowerValue, float upperValue) {
		this.lower = new SimpleFloatProperty(bean, beanName+"LowerValue", lowerValue);
		this.upper = new SimpleFloatProperty(bean, beanName+"UpperValue", upperValue);
		this.upper.addListener((ob, oldV, newV) -> rangeChanged(lower.get(),newV.floatValue()));
		this.lower.addListener((ob, oldV, newV) -> rangeChanged(newV.floatValue(), upper.get()));
	}
	@Override
	public void setLowerValue(Float lower) {
		this.lower.set(lower);
	}

	@Override
	public void setUpperValue(Float upper) {
		this.upper.set(upper);
	}

	protected boolean isBetween(float value, float lower, float upper) {
		return lower <= value && upper >= value;
	}

	@Override
	public boolean isBetween(Float value) {
		return value != null && isBetween(value, lower.get(), upper.get());
	}


	@Override
	public Float getLowerValue() {
		return lower.get();
	}

	@Override
	public Float getUpperValue() {
		return upper.get();
	}
}
