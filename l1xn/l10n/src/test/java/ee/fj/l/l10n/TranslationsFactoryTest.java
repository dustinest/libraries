package ee.fj.l.l10n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TranslationsFactoryTest {
	@BeforeEach
	public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = LocalizationFactory.class.getDeclaredField("callbacks");
		f.setAccessible(true);
		List.class.cast(f.get(null)).clear();

		f = LocalizationFactory.class.getDeclaredField("currentLocale");
		f.setAccessible(true);
		f.set(null, null);

		f = LocalizationFactory.class.getDeclaredField("defaultValues");
		f.setAccessible(true);
		Map.class.cast(f.get(null)).clear();

		f = LocalizationFactory.class.getDeclaredField("localeTRanslations");
		f.setAccessible(true);
		Map.class.cast(f.get(null)).clear();

		f = LocalizationFactory.class.getDeclaredField("defaultsRead");
		f.setAccessible(true);
		f.set(null, false);

	}

	@Test
	public void testDefaultTranslations() throws IOException {
		final int[] which = new int[]{0};
		LocalizationFactory.translate("my.name", t -> {
			which[0] = which[0] + 1;
			if (which[0] == 1) {
				Assertions.assertEquals("Fox Junior", t, "First execution" + which[0]);
			} else {
				Assertions.assertEquals("Fox Senior", t, "Second execution " + which[0]);
			}
		});
		LocalizationFactory.setLocale(Locale.forLanguageTag("en"));
		Assertions.assertEquals(2, which[0], "We must call it out twice!");
	}

	@Test
	public void testLabeledTranslation() throws IOException {
		AtomicInteger index = new AtomicInteger(0);
		Translatable translatable = LocalizationFactory.translate("profile.description", t -> {
			switch (index.incrementAndGet()) {
			case 1:
			case 2:
				Assertions.assertEquals("profile.description", t, "Execution " + index.get());
				break;
			case 3:
				Assertions.assertEquals("My name is name and I am a male", t, "Execution " + index.get());
				break;
			case 4:
				Assertions.assertEquals("My name is name and I am a female", t, "Execution " + index.get());
				break;
			case 5:
				Assertions.assertEquals("My name is Lora Muffin and I am a female", t, "Execution " + index.get());
				break;
			case 6:
				Assertions.assertEquals("My name is Lora Muffin and I am a gender", t, "Execution " + index.get());
				break;
			case 7:
				Assertions.assertEquals("My name is  and I am a ", t, "Execution " + index.get());
				break;
			case 8:
				Assertions.assertEquals("My name is name and I am a gender", t, "Execution " + index.get());
				break;
			default:
				throw new UnsupportedOperationException("This call is over amount!" + index.get());
			}
		}, "name", "gender");
		translatable.translate(null, "male");
		LocalizationFactory.setLocale(Locale.forLanguageTag("en"));
		translatable.translate(null, "female");
		translatable.translate("Lora Muffin", "female");
		translatable.translate("Lora Muffin");
		translatable.translate("", "");
		translatable.translate(null, null);
		Assertions.assertEquals(8, index.get(), "The amount of calls!");
	}

	@Test
	public void testDefaultTranslation() throws IOException {
		AtomicInteger index = new AtomicInteger(0);
		Translatable translatable = LocalizationFactory.translate("profile.description", "My name is not ${name} and I am not a ${gender}",
				t -> {
			switch (index.incrementAndGet()) {
			case 1:
				Assertions.assertEquals("My name is not name and I am not a gender", t, "Execution " + index.get());
				break;
			case 2:
				Assertions.assertEquals("My name is not name and I am not a male", t, "Execution " + index.get());
				break;
			case 3:
				Assertions.assertEquals("My name is name and I am a male", t, "Execution " + index.get());
				break;
			case 4:
				Assertions.assertEquals("My name is name and I am a female", t, "Execution " + index.get());
				break;
			case 5:
				Assertions.assertEquals("My name is Lora Muffin and I am a female", t, "Execution " + index.get());
				break;
			case 6:
				Assertions.assertEquals("My name is Lora Muffin and I am a gender", t, "Execution " + index.get());
				break;
			case 7:
				//Estonian translation. We reverse the tags. Before it was name and gender now the transaltion has reversed keys, first gender and then name
				Assertions.assertEquals("Ma olen gender ja mu nimi on Lora Muffin", t, "Execution " + index.get());
				break;
			case 8:
				// In case of empty values no tags or labels are visible
				Assertions.assertEquals("Ma olen  ja mu nimi on ", t, "Execution " + index.get());
				break;
			case 9:
				// In case the values are null default tags are used
				Assertions.assertEquals("Ma olen gender ja mu nimi on name", t, "Execution " + index.get());
				break;
			default:
				throw new UnsupportedOperationException("This call is over amount!" + index.get() + " with result " + t);
			}
		}, "name", "gender");
		translatable.translate(null, "male");
		LocalizationFactory.setLocale(Locale.forLanguageTag("en"));
		translatable.translate(null, "female");
		translatable.translate("Lora Muffin", "female");
		translatable.translate("Lora Muffin");
		// Estonian translation. We reverse the tags. Before it was name and gender now the transaltion has reversed keys, first gender and then name
		LocalizationFactory.setLocale(Locale.forLanguageTag("et"));
		translatable.translate("", "");
		translatable.translate(null, null);
		Assertions.assertEquals(9, index.get(), "The amount of calls!");
	}

	@Test
	public void testDefaultTranslationSingleArgs() {
		AtomicInteger index = new AtomicInteger(0);
		Translatable translatable = LocalizationFactory.translate("profile.description", "${name} is my name",
				t -> {
			switch (index.incrementAndGet()) {
			case 1:
				Assertions.assertEquals("name is my name", t, "Execution " + index.get());
				break;
			case 2:
				Assertions.assertEquals("Susan is my name", t, "Execution " + index.get());
				break;
			default:
				throw new UnsupportedOperationException("This call is over amount!" + index.get());
			}
		}, "name");
		translatable.translate("Susan");
		Assertions.assertEquals(2, index.get(), "The amount of calls!");
	}

}
