package ee.fj.l.l10n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class CommonTranslationFileReader implements TranslationFileReader {

	@Override
	public InputStream getFile(Locale locale) throws IOException {
		try {
			return getLocale(locale.toString()); // et_EE || et
		} catch (IOException e) {
			try {
				return getLocale(locale.toLanguageTag()); // et-EE
			} catch (IOException e1) {
				try {
					return getLocale(locale.getLanguage()); // et
				} catch (IOException e3) {
					throw new IOException("Could not find localization for " + locale.toString() + ", " + locale.toLanguageTag() + ", " + locale.getLanguage());
				}
			}
		}
	}

	@Override
	public InputStream getDefault() throws IOException {
		InputStream in = CommonTranslationFileReader.class.getResourceAsStream(solveFilePath("translation.properties"));
		if (in == null) {
			throw new NullPointerException("File translation.properties not found!");
		}
		return in;
	}

	private InputStream getLocale(String var) throws IOException {
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
