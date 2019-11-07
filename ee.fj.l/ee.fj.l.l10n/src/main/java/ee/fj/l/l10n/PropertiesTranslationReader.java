package ee.fj.l.l10n;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Read key value file from input stream into key value map.
 * Uses {@link Properties#load(InputStream)} as a function
 * @author margus
 */
public class PropertiesTranslationReader implements TranslationReader {

	/**
	 * Reads properties file. Default encoding=utf8
	 */
	@Override
	public Map<String, String> read(InputStream in) throws IOException {
		return read(new InputStreamReader(in, StandardCharsets.UTF_8));
	}

	/**
	 * The purpose of this method is to give user ability to extend the reader.
	 */
	protected Map<String, String> read(Reader reader) throws IOException {
		Properties props = new Properties();
		props.load(reader);
		Map<String, String> rv = new HashMap<>();
		props.forEach((key, value) -> rv.put(key.toString(), value.toString()));
		return rv;
	}
}
