package com.foxjunior.l1xn.l10n;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface TranslationReader {
	/**
	 * Read Inputstream into translatable key and value set
	 * @return map of your translatables
	 */
	Map<String, String> read(InputStream in) throws IOException;
}
