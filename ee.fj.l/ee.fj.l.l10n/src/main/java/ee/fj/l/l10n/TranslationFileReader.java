package ee.fj.l.l10n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public interface TranslationFileReader {
	/**
	 * @param locale
	 * @return input stream to read for current locale
	 * @throws IOException
	 */
	public abstract InputStream getFile(Locale locale) throws IOException;

	/**
	 * @return input stream for default locale
	 * @throws IOException
	 */
	public abstract InputStream getDefault() throws IOException;
}
