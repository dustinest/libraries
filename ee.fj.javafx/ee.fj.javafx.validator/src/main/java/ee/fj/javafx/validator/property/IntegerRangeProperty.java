package ee.fj.javafx.validator.property;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class IntegerRangeProperty implements RangeProperty<Integer>{
	private final IntegerProperty lower;
	private final IntegerProperty upper;

	public IntegerRangeProperty(Object bean, String beanName,
			int lowerValue, int upperValue) {
		this.lower = new SimpleIntegerProperty(bean, beanName+"LowerValue", lowerValue);
		this.upper = new SimpleIntegerProperty(bean, beanName+"UpperValue", upperValue);
		this.upper.addListener((ob, oldV, newV) -> rangeChanged(lower.get(),newV.intValue()));
		this.lower.addListener((ob, oldV, newV) -> rangeChanged(newV.intValue(), upper.get()));
	}
	@Override
	public void setLowerValue(Integer lower) {
		this.lower.set(lower);
	}

	@Override
	public void setUpperValue(Integer upper) {
		this.upper.set(upper);
	}

	protected boolean isBetween(int value, int lower, int upper) {
		return lower <= value && upper >= value;
	}
	
	@Override
	public boolean isBetween(Integer value) {
		return value != null && isBetween(value, lower.get(), upper.get());
	}


	@Override
	public Integer getLowerValue() {
		return lower.get();
	}

	@Override
	public Integer getUpperValue() {
		return upper.get();
	}
}
