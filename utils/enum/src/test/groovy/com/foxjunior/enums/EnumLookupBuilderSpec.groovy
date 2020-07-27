package com.foxjunior.enums

import spock.lang.Specification
import spock.lang.Unroll

class EnumLookupBuilderSpec extends Specification {
	@Unroll
	def "Test enum registry for #value"() {
		when:
			Optional<TestEnum> valueInt = TestEnum.advancedLookup(value.valueInt + "")
			Optional<TestEnum> valueString1 = TestEnum.advancedLookup(value.valueString1)
			Optional<TestEnum> valueString2 = TestEnum.advancedLookup(value.valueString2)
			Optional<TestEnum> name = TestEnum.advancedLookup(value.name())
		then:
			valueInt.isPresent() && valueString1.isPresent() && valueString2.isPresent() && name.isPresent()
			valueInt.get() == value
			valueString1.get() == value
			valueString2.get() == value
			name.get() == value

			TestEnum.basicLookup(value.valueInt + "").isEmpty()
			TestEnum.basicLookup(value.valueString1 + "").isEmpty()
			TestEnum.basicLookup(value.valueString2 + "").isEmpty()
			TestEnum.advancedLookup(value.name() + "").get() == value
		where:
			value << TestEnum.values()
	}

	@Unroll
	def "Test simple enum #value"() {
		expect:
			ToUpperAndLowerCaseEnum.CASE_INSENSITIVE.apply(value.name()).get() == value
			ToUpperAndLowerCaseEnum.CASE_INSENSITIVE.apply(value.name().toLowerCase()).get() == value
			ToUpperAndLowerCaseEnum.CASE_INSENSITIVE.apply(value.name().toUpperCase()).get() == value
		where:
			value << ToUpperAndLowerCaseEnum.values()
	}
}
