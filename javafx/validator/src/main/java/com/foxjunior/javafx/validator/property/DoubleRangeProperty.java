package com.foxjunior.javafx.validator.property;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public abstract class DoubleRangeProperty implements RangeProperty<Double>{
	private final DoubleProperty lower;
	private final DoubleProperty upper;

	public DoubleRangeProperty(Object bean, String beanName,
			double lowerValue, double upperValue) {
		this.lower = new SimpleDoubleProperty(bean, beanName+"LowerValue", lowerValue);
		this.upper = new SimpleDoubleProperty(bean, beanName+"UpperValue", upperValue);
		this.upper.addListener((ob, oldV, newV) -> rangeChanged(lower.get(),newV.doubleValue()));
		this.lower.addListener((ob, oldV, newV) -> rangeChanged(newV.doubleValue(), upper.get()));
	}

	@Override
	public void setLowerValue(Double lower) {
		this.lower.set(lower);
	}

	@Override
	public void setUpperValue(Double upper) {
		this.upper.set(upper);
	}

	protected boolean isBetween(double value, double lower, double upper) {
		return lower <= value && upper >= value;
	}

	@Override
	public boolean isBetween(Double value) {
		return value!= null && isBetween(value, lower.get(), upper.get());
	}

	@Override
	public Double getLowerValue() {
		return lower.get();
	}

	@Override
	public Double getUpperValue() {
		return upper.get();
	}
}
