package com.foxjunior.l1xn.l10n.impl;

import java.util.function.Consumer;

import com.foxjunior.l1xn.l10n.Translatable;

public class FormattedTranslatableImpl implements Translatable, Consumer<String> {
	private final Consumer<String> callback;
	private final String[] keys;
	private final Object[] values;
	private TranslatableField[] fields;

	public FormattedTranslatableImpl(Consumer<String> callback, String defaultValue, String... others) {
		this.callback = callback;
		keys = new String[others.length];
		values = new Object[keys.length];
		System.arraycopy(others, 0, keys, 0, others.length);
		fields = TranslatableField.getTranslatables(defaultValue);
	}


	@Override
	public void accept(String value) {
		synchronized (this.values) {
			fields = TranslatableField.getTranslatables(value);
			setPreviousTranslations();
		}
	}

	private void setPreviousTranslations() {
		StringBuilder sv = new StringBuilder();
		for (TranslatableField t : fields) {
			sv.append(t.getValue(keys, values));
		}
		callback.accept(sv.toString());
	}

	@Override
	public Translatable translate(Object... values) {
		synchronized (this.values) {
			for (int i = 0; i < this.values.length; i++) {
				if (values.length <= i) {
					this.values[i] = null;
				} else {
					this.values[i] = values[i];
				}
			}
			setPreviousTranslations();
		}
		return this;
	}
}
