package com.foxjunior.javafx.validator.property;

public interface RangeProperty<T extends Number> {
	void setLowerValue(T lower);
	void setUpperValue(T upper);

	T getLowerValue();
	T getUpperValue();

	void rangeChanged(T lowerValue, T upperValue);

	/**
	 * Check is value is between or equals lower and upper
	 */
	boolean isBetween(T value);
}
