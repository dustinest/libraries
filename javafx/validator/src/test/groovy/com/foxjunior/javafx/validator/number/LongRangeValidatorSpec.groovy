package com.foxjunior.javafx.validator.number


import spock.lang.Specification
import spock.lang.Unroll

class LongRangeValidatorSpec extends Specification {
	@Unroll
	def "On #value result is #result"() {
		given:
			LongRangeValidator validator = new LongRangeValidator(null, "test", 1, 5, 20)
		expect:
			validator.parse(value) == result
		where:
			value    | result
			"1.1123" | 1L
			null     | null
			""       | null
	}
}
