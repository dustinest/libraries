package com.foxjunior.utils.columnpredictor


import spock.lang.Specification

class IntegerPatternMatcherSpec extends Specification {

	def testInteger() {
		expect:
			IntegerPatternMatcher.INSTANCE.matches("1")
			IntegerPatternMatcher.INSTANCE.matches("-1")
			!IntegerPatternMatcher.INSTANCE.matches("+1")
	}

	def testDotNumber() {
		expect:
			!IntegerPatternMatcher.INSTANCE.matches("1.1")
			!IntegerPatternMatcher.INSTANCE.matches("-9.9")
			!IntegerPatternMatcher.INSTANCE.matches("+1.1")
			!IntegerPatternMatcher.INSTANCE.matches("1.")
			!IntegerPatternMatcher.INSTANCE.matches(".")
	}


	def testDoubleDotNumber() {
		expect:
			!IntegerPatternMatcher.INSTANCE.matches("1.1.1")
			!IntegerPatternMatcher.INSTANCE.matches("1..1")
	}


	def testCommaNumber() {
		expect:
			!IntegerPatternMatcher.INSTANCE.matches("1,1")
			!IntegerPatternMatcher.INSTANCE.matches("1,")
			!IntegerPatternMatcher.INSTANCE.matches(",")
	}


	def testDoubleCommaNumber() {
		expect:
			!IntegerPatternMatcher.INSTANCE.matches("1,1,1")
			!IntegerPatternMatcher.INSTANCE.matches("1,,1")
	}


	def testMixed() {
		expect:
			!IntegerPatternMatcher.INSTANCE.matches("1,1.1")
			!IntegerPatternMatcher.INSTANCE.matches("1,.1")
			!IntegerPatternMatcher.INSTANCE.matches("1.,1")
	}


	def testIntegerCast() {
		expect:
			IntegerPatternMatcher.getAsInteger("1")
		when:
			IntegerPatternMatcher.getAsInteger("1.1")
		then:
			Exception error = thrown(IllegalArgumentException)
			error.message == "1.1 is not long!"
	}

}
