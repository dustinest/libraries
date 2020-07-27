package com.foxjunior.numbers


import spock.lang.Specification
import spock.lang.Unroll

class NumbersSpec extends Specification {
	@Unroll
	def "Finding number from null results always null"() {
		expect:
			Numbers.getFirstNumberOrNull(null) == null
			Numbers.getFirstNumber(null).isEmpty()
			Numbers.getLastNumberOrNull(null) == null
			Numbers.getLastNumber(null).isEmpty()
	}

	@Unroll
	def "If number in #text is not found then result is null"() {
		expect:
			Numbers.getFirstNumberOrNull(text) == null
			Numbers.getFirstNumber(text).isEmpty()
			Numbers.getLastNumberOrNull(text) == null
			Numbers.getLastNumber(text).isEmpty()
		where:
			text << ["", "\r\n\t ", "testäõ", "o", " 👍 ", "1.2.3", "a 1.2.3 bvc"]
	}

	@Unroll
	def "getFirstNumber(#text) == #value && Strings.getFirstNumberIfHasValue(#text).get() == #value"() {
		expect:
			Numbers.getFirstNumberOrNull(text) == result
			Numbers.getFirstNumber(text).get() == result
			Numbers.getLastNumberOrNull(text) == result
			Numbers.getLastNumber(text).get() == result
		where:
			text					| result
			"0"						| 0
			"10"					| 10
			"abc.def 1.2"			| 1.2
			"abc.1.2"				| 1.2
			"1.2"					| 1.2
			"5123123123"			| 5123123123
			"51231.23123"			| 51231.23123
			"test 51231.23123.abc"	| 51231.23123
			"test 1.2"				| 1.2
			"1.2test"				| 1.2
	}

	@Unroll
	def "#text first is #first and last is #last"() {
		expect:
			Numbers.getFirstNumberOrNull(text) == first
			Numbers.getFirstNumber(text).get() == first
			Numbers.getLastNumberOrNull(text) == last
			Numbers.getLastNumber(text).get() == last
		where:
			text									| first			| last
			"0 1"									| 0				| 1
			"10 01"									| 10			| 1
			"abc.def 1.2 2.1 abc.def"				| 1.2			| 2.1
			"abc.1.2 cda.2.1"						| 1.2			| 2.1
			"1.2 2.1"								| 1.2			| 2.1
			"5123123123 123"						| 5123123123	| 123
			"51231.23123 1.23123"					| 51231.23123	| 1.23123
			"test 51231.23123.abc test.51231.1.abc"	| 51231.23123	| 51231.1
			"1.2test2.1"							| 1.2			| 2.1
	}
}
