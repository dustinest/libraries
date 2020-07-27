package com.foxjunior.l1xn.l10n;

/**
 *
 * Translatable helps to translate the labels based on the label. For instance if the label is:
 * My name is ${name} and I love ${something} then you would like to call translate("John", "bread");
 *
 */
public interface Translatable {
	@SuppressWarnings("UnusedReturnValue")
	Translatable translate(Object... values);
}
