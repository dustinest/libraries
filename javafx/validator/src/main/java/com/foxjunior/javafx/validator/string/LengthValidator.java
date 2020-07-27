package com.foxjunior.javafx.validator.string;

import com.foxjunior.javafx.validator.property.IntegerRangeProperty;

public class LengthValidator extends BaseStringValidator {
	private final IntegerRangeProperty rangeProperty;

	public LengthValidator(Object bean, String beanName, int minLength, int maxLength, String defaultValue, boolean allowNull, boolean allowEmpty) {
		super(bean, beanName, defaultValue, allowNull, allowEmpty);
		rangeProperty = new IntegerRangeProperty(bean, beanName, minLength, maxLength) {
			@Override
			public void rangeChanged(Integer lowerValue, Integer upperValue) {
				reValidate(isValid(lowerValue, upperValue));
			}
		};

	}

	private boolean isValid(int lower, int upper) {
		return isValid(getValue(), lower, upper);
	}

	private boolean isValid(String value, int lower, int upper) {
		int length = value != null ? value.length() : 0;
		return length >= rangeProperty.getLowerValue() && value.length() <= length;
	}

	@Override
	protected boolean isValid(String value) {
		return isValid(value, rangeProperty.getLowerValue(), rangeProperty.getUpperValue());
	}
}
