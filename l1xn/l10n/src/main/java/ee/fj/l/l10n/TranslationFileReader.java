package ee.fj.l.l10n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public interface TranslationFileReader {
	/**
	 * @return input stream to read for current locale
	 */
	InputStream getFile(Locale locale) throws IOException;

	/**
	 * @return input stream for default locale
	 */
	InputStream getDefault() throws IOException;
}
