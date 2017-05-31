package ee.fj.l.l10n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface TranslationReader {
	/**
	 * Read Inputstream into translatable key and value set
	 * @param in
	 * @return map of your translatables
	 * @throws IOException
	 */
	public Map<String, String> read(InputStream in) throws IOException;
}
