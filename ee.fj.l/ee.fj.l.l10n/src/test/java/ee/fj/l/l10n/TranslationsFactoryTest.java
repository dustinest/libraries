package ee.fj.l.l10n;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TranslationsFactoryTest {
	@Before
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
				Assert.assertEquals("First execution" + which[0], "Fox Junior", t);
			} else {
				Assert.assertEquals("Second execution " + which[0], "Fox Senior", t);
			}
		});
		LocalizationFactory.setLocale(Locale.forLanguageTag("en"));
		Assert.assertEquals("We must call it out twice!", 2, which[0]);
	}

	@Test
	public void testLabeledTranslation() throws IOException {
		AtomicInteger index = new AtomicInteger(0);
		Translatable translatable = LocalizationFactory.translate("profile.description", t -> {
			switch (index.incrementAndGet()) {
			case 1:
			case 2:
				Assert.assertEquals("Execution " + index.get(), "profile.description", t);
				break;
			case 3:
				Assert.assertEquals("Execution " + index.get(), "My name is name and I am a male", t);
				break;
			case 4:
				Assert.assertEquals("Execution " + index.get(), "My name is name and I am a female", t);
				break;
			case 5:
				Assert.assertEquals("Execution " + index.get(), "My name is Lora Muffin and I am a female", t);
				break;
			case 6:
				Assert.assertEquals("Execution " + index.get(), "My name is Lora Muffin and I am a gender", t);
				break;
			case 7:
				Assert.assertEquals("Execution " + index.get(), "My name is  and I am a ", t);
				break;
			case 8:
				Assert.assertEquals("Execution " + index.get(), "My name is name and I am a gender", t);
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
		Assert.assertEquals("The amount of calls!", 8, index.get());
	}

	@Test
	public void testDefaultTranslation() throws IOException {
		AtomicInteger index = new AtomicInteger(0);
		Translatable translatable = LocalizationFactory.translate("profile.description", "My name is not ${name} and I am not a ${gender}", 
				t -> {
			switch (index.incrementAndGet()) {
			case 1:
				Assert.assertEquals("Execution " + index.get(), "My name is not name and I am not a gender", t);
				break;
			case 2:
				Assert.assertEquals("Execution " + index.get(), "My name is not name and I am not a male", t);
				break;
			case 3:
				Assert.assertEquals("Execution " + index.get(), "My name is name and I am a male", t);
				break;
			case 4:
				Assert.assertEquals("Execution " + index.get(), "My name is name and I am a female", t);
				break;
			case 5:
				Assert.assertEquals("Execution " + index.get(), "My name is Lora Muffin and I am a female", t);
				break;
			case 6:
				Assert.assertEquals("Execution " + index.get(), "My name is Lora Muffin and I am a gender", t);
				break;
			case 7:
				//Estonian translation. We reverse the tags. Before it was name and gender now the transaltion has reversed keys, first gender and then name
				Assert.assertEquals("Execution " + index.get(), "Ma olen gender ja mu nimi on Lora Muffin", t);
				break;
			case 8:
				// In case of empty values no tags or labels are visible
				Assert.assertEquals("Execution " + index.get(), "Ma olen  ja mu nimi on ", t);
				break;
			case 9:
				// In case the values are null default tags are used
				Assert.assertEquals("Execution " + index.get(), "Ma olen gender ja mu nimi on name", t);
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
		Assert.assertEquals("The amount of calls!", 9, index.get());
	}

	@Test
	public void testDefaultTranslationSingleArgs() {
		AtomicInteger index = new AtomicInteger(0);
		Translatable translatable = LocalizationFactory.translate("profile.description", "${name} is my name", 
				t -> {
			switch (index.incrementAndGet()) {
			case 1:
				Assert.assertEquals("Execution " + index.get(), "name is my name", t);
				break;
			case 2:
				Assert.assertEquals("Execution " + index.get(), "Susan is my name", t);
				break;
			default:
				throw new UnsupportedOperationException("This call is over amount!" + index.get());
			}
		}, "name");
		translatable.translate("Susan");
		Assert.assertEquals("The amount of calls!", 2, index.get());
	}

}
