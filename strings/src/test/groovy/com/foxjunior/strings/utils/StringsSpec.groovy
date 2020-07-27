package com.foxjunior.strings.utils


import spock.lang.Specification
import spock.lang.Unroll

class StringsSpec extends Specification {
	@Unroll
	def "Null string trim(null) == null && Strings.trimIfHasText(null).isEmpty()"() {
		expect:
        Strings.trimIfHasText(null).isEmpty()
			Strings.trim(null) == null
			Strings.removeWhitespace(null) == null
			Strings.removeWhitespaceIfHasText(null).isEmpty()
	}

	@Unroll
	def "Whitespace Strings.trimIfHasText(#text).isEmpty() && Strings.trim(#text) == ''"() {
		expect:
			Strings.trim(text) == ""
			Strings.trimIfHasText(text).isEmpty()
			Strings.removeWhitespace(text) == ""
			Strings.removeWhitespaceIfHasText(text).isEmpty()
		where:
			text << ["", "\r\n\t "]
	}

	@Unroll
	def "trim(#text) == #value && Strings.trimIfHasText(#text).get() == #value"() {
		expect:
			Strings.trim(text) == result
			Strings.trimIfHasText(text).get() == result
			Strings.removeWhitespace(text) == trimresult
			Strings.removeWhitespaceIfHasText(text).get() == trimresult
		where:
			text			| result		| trimresult
			"testäõ"		| "testäõ"		| "testäõ"
			"Õ"				| "Õ"			| "Õ"
			"a"				| "a"			| "a"
			" t e	s t "	| "t e	s t"	| "test"
			" 👍 "			| "👍"			| "👍"
	}
}
