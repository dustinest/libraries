package com.foxjunior.utils.columnpredictor


import spock.lang.Specification

class NumberPatternMatcherSpec extends Specification {

	def testInteger() {
		expect:
			NumberPatternMatcher.INSTANCE.matches("1")
			NumberPatternMatcher.INSTANCE.matches("-1")
			!NumberPatternMatcher.INSTANCE.matches("+1")
	}

	def testDotNumber() {
		expect:
			NumberPatternMatcher.INSTANCE.matches("1.1")
			NumberPatternMatcher.INSTANCE.matches("-9.9")
			!NumberPatternMatcher.INSTANCE.matches("+1.1")
			!NumberPatternMatcher.INSTANCE.matches("1.")
			!NumberPatternMatcher.INSTANCE.matches(".")
	}

	def testDoubleDotNumber() {
		expect:
			!NumberPatternMatcher.INSTANCE.matches("1.1.1")
			!NumberPatternMatcher.INSTANCE.matches("1..1")
	}

	def testCommaNumber() {
		expect:
			NumberPatternMatcher.INSTANCE.matches("1,1")
			!NumberPatternMatcher.INSTANCE.matches("1,")
			!NumberPatternMatcher.INSTANCE.matches(",")
	}

	def testDoubleCommaNumber() {
		expect:
			!NumberPatternMatcher.INSTANCE.matches("1,1,1")
			!NumberPatternMatcher.INSTANCE.matches("1,,1")
	}

	def testMixed() {
		expect:
			!NumberPatternMatcher.INSTANCE.matches("1,1.1")
			!NumberPatternMatcher.INSTANCE.matches("1,.1")
			!NumberPatternMatcher.INSTANCE.matches("1.,1")
	}

	def testDoubleCast() {
		expect:
			NumberPatternMatcher.getAsDouble("1") == 1d
			NumberPatternMatcher.getAsDouble("1.1") == 1.1d
			NumberPatternMatcher.getAsDouble("1,1") == 1.1d
	}

	def testFloatCast() {
		expect:
			NumberPatternMatcher.getAsFloat("1") == 1f
			NumberPatternMatcher.getAsFloat("1.1") == 1.1f
			NumberPatternMatcher.getAsFloat("1,1") == 1.1f
	}

	def testIntegerCast() {
		expect:
			NumberPatternMatcher.getAsInteger("1") == 1
			NumberPatternMatcher.getAsInteger("1.1") == 1
			NumberPatternMatcher.getAsInteger("1,1") == 1
	}

}
