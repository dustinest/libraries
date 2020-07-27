package com.foxjunior.l1xn.l10n;

import java.io.InputStream;
import java.util.Locale;

public class CommonTranslationFileReader implements TranslationFileReader {

	@Override
	public InputStream getFile(Locale locale) {
		try {
			return getLocale(locale.toString()); // et_EE || et
		} catch (Exception e) {
			try {
				return getLocale(locale.toLanguageTag()); // et-EE
			} catch (Exception e1) {
				return getLocale(locale.getLanguage()); // et
			}
		}
	}

	@Override
	public InputStream getDefault() {
		InputStream in = CommonTranslationFileReader.class.getResourceAsStream(solveFilePath("translation.properties"));
		if (in == null) {
			throw new NullPointerException("File translation.properties not found!");
		}
		return in;
	}

	private InputStream getLocale(String var) {
		InputStream in =LocalizationFactory.class.getResourceAsStream(solveFilePath("translation." + var + ".properties"));
		if (in == null) {
			throw new NullPointerException("translation." + var + ".properties");
		}
		return in;
	}

	protected String solveFilePath(String file) {
		return "/" + file;
	}
}
