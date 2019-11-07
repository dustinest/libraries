package ee.fj.javafx.validator.number;

import java.text.ParseException;

import ee.fj.javafx.validator.SimpleValidProperty;
import ee.fj.javafx.validator.ValueValidator;
import ee.fj.javafx.validator.property.IntegerRangeProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;

public class IntegerRangeValidator extends IntegerRangeProperty implements ValueValidator<Integer> {
	private final SimpleValidProperty validProperty;
	private final IntegerProperty value;
	
	public IntegerRangeValidator(Object bean, String beanName,
			int lowerValue, int upperValue, int defaultValue) {
		super(bean, beanName, lowerValue, upperValue);
		value = new SimpleIntegerProperty(bean, beanName, defaultValue);
		validProperty = new SimpleValidProperty(bean, beanName + "Range", isBetween(defaultValue));
		value.addListener((ob, oldValue, newValue) -> validProperty.setValid(newValue != null && isBetween(newValue.intValue())) );
	}

	@Override
	public void rangeChanged(Integer lowerValue, Integer upperValue) {
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
	public Integer getValue() {
		return value.getValue();
	}

	@Override
	public void setValue(Integer value) {
		this.value.setValue(value);
	}

	@Override
	public void addValueListener(ChangeListener<Integer> listener) {
		this.value.addListener((ob, oldValue, newValue) -> listener.changed(null, oldValue.intValue(), newValue.intValue()) );
	}

	@Override
	public String format(Integer value) {
		return NumberFormatter.format(value).orElse(null);
	}

	@Override
	public Integer parse(String value) throws ParseException {
		return NumberFormatter.parse(value).map(Number::intValue).orElse(null);
	}
}
