package ee.fj.l.l10n.impl;

import java.util.Map;

import ee.fj.l.l10n.TranslationListCallback;

public class TranslationContainer {
	private final TranslationListCallback callback;
	private final String[] key;
	private final String[] defaultValue;

	public TranslationContainer(String[] key, String[] defaultValue, TranslationListCallback callback) {
		this.key = key;
		this.defaultValue = defaultValue == null ? key : defaultValue;
		this.callback = callback;
	}

	public void callback(Map<String, String> defaultValues, Map<String, String> translatedValues) {
		for (int i = 0; i < key.length; i++) {
			if (translatedValues.containsKey(key[i]))
				callback.accept(key[i], i, translatedValues.get(key[i]));
			else if (defaultValues.containsKey(key[i]))
				callback.accept(key[i], i, defaultValues.get(key[i]));
			else
				callback.accept(key[i], i, defaultValue[i]);
		}
	}
}
