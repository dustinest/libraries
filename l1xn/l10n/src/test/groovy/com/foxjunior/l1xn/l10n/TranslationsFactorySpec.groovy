package com.foxjunior.l1xn.l10n


import spock.lang.Specification

import java.lang.reflect.Field
import java.util.concurrent.atomic.AtomicInteger

class TranslationsFactorySpec extends Specification {
	def setup() {
		Field field = LocalizationFactory.class.getDeclaredField("callbacks")
		field.setAccessible(true)
		List.class.cast(field.get(null)).clear()

		field = LocalizationFactory.class.getDeclaredField("currentLocale")
		field.setAccessible(true)
		field.set(null, null)

		field = LocalizationFactory.class.getDeclaredField("defaultValues")
		field.setAccessible(true)
		Map.class.cast(field.get(null)).clear()

		field = LocalizationFactory.class.getDeclaredField("localeTRanslations")
		field.setAccessible(true)
		Map.class.cast(field.get(null)).clear()

		field = LocalizationFactory.class.getDeclaredField("defaultsRead")
		field.setAccessible(true)
		field.set(null, false)
	}

	def "test default translations"() {
		given:
			final int[] which = new int[]{0}
		when:
			LocalizationFactory.translate("my.name", t -> {
				which[0] = which[0] + 1
				if (which[0] == 1) {
					assert t == "Fox Junior"
				} else {
					assert t == "Fox Senior"
				}
			})
			LocalizationFactory.setLocale(Locale.forLanguageTag("en"))
		then:
			which[0] == 2
	}

	def "labeled translation"() {
		given:
			AtomicInteger index = new AtomicInteger(0)
		when:
			Translatable translatable = LocalizationFactory.translate("profile.description", t -> {
				//noinspection GroovyFallthrough
				switch (index.incrementAndGet()) {
					case 1:
					case 2:
						assert t == "profile.description"
						break
					case 3:
						t == "My name is name and I am a male"
						break
					case 4:
						assert t == "My name is name and I am a female"
						break
					case 5:
						assert t == "My name is Lora Muffin and I am a female"
						break
					case 6:
						assert t == "My name is Lora Muffin and I am a gender"
						break
					case 7:
						assert t == "My name is  and I am a "
						break
					case 8:
						assert t == "My name is name and I am a gender"
						break
					default:
						throw new UnsupportedOperationException("This call is over amount!" + index.get())
				}
			}, "name", "gender")
			translatable.translate(null, "male")
			LocalizationFactory.setLocale(Locale.forLanguageTag("en"))
			translatable.translate(null, "female")
			translatable.translate("Lora Muffin", "female")
			translatable.translate("Lora Muffin")
			translatable.translate("", "")
			translatable.translate(null, null)
		then:
			index.get() == 8
	}

	def "test default translation"() {
		given:
			AtomicInteger index = new AtomicInteger(0)
		when:
			Translatable translatable = LocalizationFactory.translate("profile.description", "My name is not \${name} and I am not a \${gender}",
			t -> {
				switch (index.incrementAndGet()) {
					case 1:
						assert t == "My name is not name and I am not a gender"
						break
					case 2:
						assert t == "My name is not name and I am not a male"
						break
					case 3:
						assert t == "My name is name and I am a male"
						break
					case 4:
						assert t == "My name is name and I am a female"
						break
					case 5:
						assert t == "My name is Lora Muffin and I am a female"
						break
					case 6:
						assert t == "My name is Lora Muffin and I am a gender"
						break
					case 7:
						//Estonian translation. We reverse the tags. Before it was name and gender now the translation has reversed keys, first gender and then name
						assert t == "Ma olen gender ja mu nimi on Lora Muffin"
						break
					case 8:
						// In case of empty values no tags or labels are visible
						assert t == "Ma olen  ja mu nimi on "
						break
					case 9:
						// In case the values are null default tags are used
						assert t == "Ma olen gender ja mu nimi on name"
						break
					default:
						throw new UnsupportedOperationException("This call is over amount!" + index.get() + " with result " + t)
				}
			}, "name", "gender")
			translatable.translate(null, "male")
			LocalizationFactory.setLocale(Locale.forLanguageTag("en"))
			translatable.translate(null, "female")
			translatable.translate("Lora Muffin", "female")
			translatable.translate("Lora Muffin")
			// Estonian translation. We reverse the tags. Before it was name and gender now the transaltion has reversed keys, first gender and then name
			LocalizationFactory.setLocale(Locale.forLanguageTag("et"))
			translatable.translate("", "")
			translatable.translate(null, null)
		then:
			index.get() == 9
	}

	def "test default translation single args"() {
		given:
			AtomicInteger index = new AtomicInteger(0)
		when:
			Translatable translatable = LocalizationFactory.translate("profile.description", "\${name} is my name",
				t -> {
					switch (index.incrementAndGet()) {
						case 1:
							assert t == "name is my name"
							break
						case 2:
							assert t == "Susan is my name"
							break
						default:
							throw new UnsupportedOperationException("This call is over amount!" + index.get())
					}
				}, "name")
			translatable.translate("Susan")
		then:
			index.get() == 2
	}
}
