package ee.fj.l.l10n;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import ee.fj.l.l10n.impl.FormattedTranslatableImpl;
import ee.fj.l.l10n.impl.TranslationContainer;

public class LocalizationFactory {
	private static final Logger LOGGER = Logger.getLogger(LocalizationFactory.class.getName());

	private static final Object mutex = new Object();
	private static TranslationFileReader fileReader = new CommonTranslationFileReader();
	private static TranslationReader translationReader = new PropertiesTranslationReader();
	private static Locale currentLocale = null;
	private static final Map<String, String> defaultValues = new HashMap<>();
	private static final Map<String,String> localeTRanslations = new HashMap<>();
	private static final List<TranslationContainer> callbacks = new ArrayList<>();

	private static boolean defaultsRead = false;
	private static void readDefaults() {
		if (defaultsRead)
			return;
		defaultsRead = true;
		try (InputStream in = fileReader.getDefault()) {
			fillMap(in, defaultValues);
		} catch (NullPointerException e) {
			LOGGER.log(Level.WARNING, "The default file was not found!", e);
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Cannot read the default file!", e);
		}
	}

	private static boolean readLocale(Locale locale) throws IOException {
		if (currentLocale != null && currentLocale.equals(locale))
			return false;
		currentLocale = locale;
		try (InputStream in = fileReader.getFile(locale)) {
			fillMap(in, localeTRanslations);
		} catch (NullPointerException e) {
			localeTRanslations.clear();
			LOGGER.log(Level.WARNING, "The file for locale " + locale.toString() + " was not found!", e);
		} 
		return true;
	}

	private static void fillMap(InputStream in, Map<String, String> target) throws IOException {
		Map<String, String> source = translationReader.read(in);
		target.clear();
		target.putAll(source);
	}

	/**
	 * Set your own translation file reader
	 * @see CommonTranslationFileReader how it is implemented
	 */
	public static void set(TranslationFileReader reader) {
		synchronized (mutex) {
			defaultsRead = false;
			fileReader = reader;
		}
	}

	/**
	 * Set your own translation reader
	 * @see PropertiesTranslationReader how the proeprties reader is implemented
	 */
	public static void set(TranslationReader reader) {
		synchronized (mutex) {
			defaultsRead = false;
			translationReader = reader;
		}
	}

	/**
	 * Set your locale for translation
	 */
	public static void setLocale(Locale locale) throws IOException {
		synchronized (mutex) {
			readDefaults();
			if (!readLocale(locale)) {
				return;
			}
			for (TranslationContainer t : callbacks) {
				t.callback(defaultValues, localeTRanslations);
			}
		}
	}


	/**
	 * Translate whole array in once.
	 */
	public static void translate(String[] keyArray, String[] defaultsArray, TranslationListCallback t) {
		TranslationContainer sv = new TranslationContainer(keyArray, defaultsArray, t);
		synchronized (mutex) {
			readDefaults();
			callbacks.add(sv);
			sv.callback(defaultValues, localeTRanslations);
		}
	}

	/**
	 * Translate whole array in once.
	 */
	public static void translate(Collection<String> keyArray, Collection<String> defaultsArray, TranslationListCallback t) {
		translate(keyArray.toArray(new String[keyArray.size()]), defaultsArray.toArray(new String[defaultsArray.size()]), t);
	}

	/**
	 * Formatted translation. The value must contain ${key} to get translated
	 */
	public static Translatable translate(String key, String defaultValue, Consumer<String> t, String... labels) {
		FormattedTranslatableImpl rv = new FormattedTranslatableImpl(t, defaultValue, labels);
		translate(new String[]{key}, new String[]{defaultValue}, (_key, _index, _value) -> rv.accept(_value) );
		return rv;
	}
	
	/**
	 * Formatted translation. The value must contain ${key} to get translated
	 */
	public static Translatable translate(String key, Consumer<String> t, String... labels) {
		return translate(key, key, t, labels);
	}
}
