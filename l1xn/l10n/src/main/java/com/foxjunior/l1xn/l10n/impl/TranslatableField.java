package com.foxjunior.l1xn.l10n.impl;

import java.util.ArrayList;
import java.util.List;

public abstract class TranslatableField {
	private final String value;
	protected TranslatableField(String value) {
		this.value = value;
	}

	static class ReplacableValue extends TranslatableField {
		ReplacableValue(String value) {
			super(value);
		}

		@Override
		public Object getValue(String[] keys, Object[] values) {
			Object rv = super.getValue(keys, values);
			if (keys == null || values == null || keys.length == 0 || values.length == 0)
				return rv;
			for (int i = 0; i < keys.length; i++) {
				if (i >= values.length)
					return rv;
				if (keys[i].equals(rv)) {
					return values[i] == null ? keys[i] : values[i];
				}
			}
			return rv;
		}

	}
	public Object getValue(String[] keys, Object[] values) {
		return value;
	}

	public static TranslatableField[] getTranslatables(String value) {
		List<TranslatableField> rv = new ArrayList<>();
		char[] chars = value.toCharArray();
		boolean isStarted = false;
		StringBuilder key = new StringBuilder();
		StringBuilder val = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] == '$' && (i == 0 || chars[i-1] != '\\') && i < chars.length -1 && chars[i+1] == '{') {
				if (val.length() > 0)
					rv.add(new TranslatableField(val.toString()){});
				i++;
				isStarted = true;
				key.setLength(0);
				val.setLength(0);
			} else if (isStarted && chars[i] == '}') {
				rv.add(new ReplacableValue(key.toString()));
				isStarted = false;
			} else if (isStarted) {
				key.append(chars[i]);
			} else {
				val.append(chars[i]);
			}
		}
		if (val.length() > 0)
			rv.add(new TranslatableField(val.toString()){});
		if (isStarted) {
			if (key.length() > 0) {
				rv.add(new TranslatableField("${" + key.toString()){});
			} else {
				rv.add(new TranslatableField("${"){});
			}
		}
		return rv.toArray(new TranslatableField[0]);
	}
}
