package com.foxjunior.utils.columnpredictor


import spock.lang.Specification

class DecimalPatternMatcherSpec extends Specification {
	def testInteger() {
		expect:
			!DecimalPatternMatcher.INSTANCE.matches("1")
			!DecimalPatternMatcher.INSTANCE.matches("-1")
			!DecimalPatternMatcher.INSTANCE.matches("+1")
	}

	def "test dot number"() {
		expect:
			DecimalPatternMatcher.INSTANCE.matches("1.1")
			DecimalPatternMatcher.INSTANCE.matches("1.1")
			DecimalPatternMatcher.INSTANCE.matches("-9.9")
			!DecimalPatternMatcher.INSTANCE.matches("+1.1")
			!DecimalPatternMatcher.INSTANCE.matches("1.")
			!DecimalPatternMatcher.INSTANCE.matches(".")
	}

	def "test double dot number"() {
		expect:
			!DecimalPatternMatcher.INSTANCE.matches("1.1.1")
			!DecimalPatternMatcher.INSTANCE.matches("1..1")
	}

	def "test comma number"() {
		expect:
			DecimalPatternMatcher.INSTANCE.matches("1,1")
			!DecimalPatternMatcher.INSTANCE.matches("1,")
			!DecimalPatternMatcher.INSTANCE.matches(",")
	}

	def "test double comma number"() {
		expect:
			!DecimalPatternMatcher.INSTANCE.matches("1,1,1")
			!DecimalPatternMatcher.INSTANCE.matches("1,,1")
	}

	def "test mixed"() {
		expect:
			!DecimalPatternMatcher.INSTANCE.matches("1,1.1")
			!DecimalPatternMatcher.INSTANCE.matches("1,.1")
			!DecimalPatternMatcher.INSTANCE.matches("1.,1")
	}

	def "test double cast"() {
		expect:
			DecimalPatternMatcher.getAsDouble("1.1") == 1.1d
			DecimalPatternMatcher.getAsDouble("1,1") == 1.1d
	}
	def "double cast fails"() {
		when:
			DecimalPatternMatcher.getAsDouble("1")
		then:
			Exception error = thrown(IllegalArgumentException)
			error.message == "1 is not double!"
	}

	def "test float cast"() {
		expect:
			DecimalPatternMatcher.getAsFloat("1.1") == 1.1f
			DecimalPatternMatcher.getAsFloat("1,1") == 1.1f
	}

	def "flast cast fails"() {
		when:
			DecimalPatternMatcher.getAsFloat("1")
		then:
			Exception error = thrown(IllegalArgumentException)
			error.message == "1 is not double!"
	}
}
