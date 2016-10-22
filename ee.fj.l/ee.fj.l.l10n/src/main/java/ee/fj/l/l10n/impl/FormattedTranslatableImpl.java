package ee.fj.l.l10n.impl;

import ee.fj.l.l10n.Translatable;
import ee.fj.l.l10n.TranslationCallback;

public class FormattedTranslatableImpl implements Translatable, TranslationCallback {
	private final TranslationCallback callback;
	private final String[] keys;
	private final Object[] values;
	private String previousTranslation = null;
	private TranslatableField[] fields;

	public FormattedTranslatableImpl(TranslationCallback callback, String defaultValue, String... others) {
		this.callback = callback;
		keys = new String[others.length];
		values = new Object[keys.length];
		for (int i = 0; i < others.length; i++) {
			keys[i] = others[i];
		}
		fields = TranslatableField.getTranslatables(defaultValue);
	}

	
	@Override
	public void translated(String value) {
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
		this.previousTranslation = sv.toString();
		callback.translated(previousTranslation);
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
