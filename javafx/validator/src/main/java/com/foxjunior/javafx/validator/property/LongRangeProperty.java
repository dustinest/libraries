package com.foxjunior.javafx.validator.property;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public abstract class LongRangeProperty implements RangeProperty<Long>{
	private final LongProperty lower;
	private final LongProperty upper;

	public LongRangeProperty(Object bean, String beanName,
			long lowerValue, long upperValue) {
		this.lower = new SimpleLongProperty(bean, beanName+"LowerValue", lowerValue);
		this.upper = new SimpleLongProperty(bean, beanName+"UpperValue", upperValue);
		this.upper.addListener((ob, oldV, newV) -> rangeChanged(lower.get(),newV.longValue()));
		this.lower.addListener((ob, oldV, newV) -> rangeChanged(newV.longValue(), upper.get()));
	}

	@Override
	public void setLowerValue(Long lower) {
		this.lower.set(lower);
	}

	@Override
	public void setUpperValue(Long upper) {
		this.upper.set(upper);
	}

	protected boolean isBetween(long value, long lower, long upper) {
		return lower <= value && upper >= value;
	}

	@Override
	public boolean isBetween(Long value) {
		return value != null && isBetween(value, lower.get(), upper.get());
	}


	@Override
	public Long getLowerValue() {
		return lower.get();
	}

	@Override
	public Long getUpperValue() {
		return upper.get();
	}
}
