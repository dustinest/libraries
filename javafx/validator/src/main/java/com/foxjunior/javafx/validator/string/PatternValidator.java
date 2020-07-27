package com.foxjunior.javafx.validator.string;

import java.util.regex.Pattern;

public class PatternValidator extends BaseStringValidator {
	private Pattern pattern;

	public PatternValidator(Object bean, String beanName, Pattern pattern, boolean allowNull, boolean allowEmpty, String defaultValue) {
		super(bean, beanName, defaultValue, allowNull, allowEmpty);
		this.pattern = pattern;
	}

	public PatternValidator(Object bean, String beanName, String pattern, boolean allowNull, boolean allowEmpty, String defaultValue) {
		this(bean, beanName, Pattern.compile(pattern), allowNull, allowEmpty, defaultValue);
	}

	public void setPattern(String pattern) {
		setPattern(Pattern.compile(pattern));
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
		reValidate();
	}

	@Override
	protected boolean isValid(String value) {
		return value != null && pattern.matcher(value).matches();
	}

}
