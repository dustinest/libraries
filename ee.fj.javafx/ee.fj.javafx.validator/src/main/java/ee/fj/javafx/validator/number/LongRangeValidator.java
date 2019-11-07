package ee.fj.javafx.validator.number;

import java.text.ParseException;

import ee.fj.javafx.validator.SimpleValidProperty;
import ee.fj.javafx.validator.ValueValidator;
import ee.fj.javafx.validator.property.LongRangeProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ChangeListener;

public class LongRangeValidator extends LongRangeProperty implements ValueValidator<Long> {
	private final SimpleValidProperty validProperty;
	private final LongProperty value;
	
	public LongRangeValidator(Object bean, String beanName,
			long lowerValue, long upperValue, long defaultValue) {
		super(bean, beanName, lowerValue, upperValue);
		value = new SimpleLongProperty(bean, beanName, defaultValue);
		validProperty = new SimpleValidProperty(bean, beanName + "Range", isBetween(defaultValue));
		value.addListener((ob, oldValue, newValue) -> validProperty.setValid(newValue != null && isBetween(newValue.longValue())) );
	}

	@Override
	public void rangeChanged(Long lowerValue, Long upperValue) {
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
	public Long getValue() {
		return value.getValue();
	}

	@Override
	public void setValue(Long value) {
		this.value.setValue(value);
	}

	@Override
	public void addValueListener(ChangeListener<Long> listener) {
		this.value.addListener((ob, oldValue, newValue) -> listener.changed(null, oldValue.longValue(), newValue.longValue()) );
	}

	@Override
	public String format(Long value) {
		return NumberFormatter.format(value).orElse(null);
	}

	@Override
	public Long parse(String value) throws ParseException {
		return NumberFormatter.parse(value).map(Number::longValue).orElse(null);
	}
}
